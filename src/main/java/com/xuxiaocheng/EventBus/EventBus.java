package com.xuxiaocheng.EventBus;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SortedSetMultimap;
import com.xuxiaocheng.EventBus.Events.NoSubscriberEvent;
import com.xuxiaocheng.EventBus.Events.SubscriberExceptionEvent;
import com.xuxiaocheng.HeadLibs.DataStructures.Pair;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;

public class EventBus {
    public static EventBusBuilder builder() {
        return new EventBusBuilder();
    }

    protected static final EventBus DefaultInstance = EventBus.builder().build();
    public static EventBus getDefaultInstance() {
        return EventBus.DefaultInstance;
    }

    protected static final @NotNull LoadingCache<@NotNull Class<?>, @NotNull Set<@NotNull SubscribedMethod>> methodBySubscriberCache = CacheBuilder.newBuilder().maximumSize(1024)
            .build(CacheLoader.asyncReloading(new CacheLoader<>() {
                @Override
                public @NotNull Set<@NotNull SubscribedMethod> load(final @NotNull Class<?> key) {
                    final Method[] methods = key.getDeclaredMethods();
                    final Set<SubscribedMethod> set = new HashSet<>();
                    for (final Method method: methods) {
                        final Subscribe subscribe = method.getAnnotation(Subscribe.class);
                        if (subscribe == null)
                            continue;
                        final Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length != 1)
                            continue;
                        method.setAccessible(false);
                        set.add(new SubscribedMethod(method, parameterTypes[0], subscribe.threadMode(), subscribe.sticky(), subscribe.priority()));
                    }
                    return set;
                }
            }, EventBusBuilder.DefaultExecutorService));
    protected static final @NotNull LoadingCache<@NotNull Class<?>, @NotNull Set<@NotNull Class<?>>> eventTypeInheritanceCache = CacheBuilder.newBuilder().maximumSize(1024)
            .build(CacheLoader.asyncReloading(new CacheLoader<>() {
                @Override
                public @NotNull Set<@NotNull Class<?>> load(final @NotNull Class<?> key) {
                    final Set<Class<?>> set = new HashSet<>();
                    set.add(Object.class);
                    for (Class<?> superclass = key; superclass != Object.class; superclass = superclass.getSuperclass()) {
                        set.add(superclass);
                        set.addAll(List.of(superclass.getInterfaces()));
                    }
                    return set;
                }
            }, EventBusBuilder.DefaultExecutorService));

    protected final boolean logSubscriberExceptions;
    protected final boolean logNoSubscriberMessages;
    protected final boolean sendSubscriberExceptionEvent;
    protected final boolean sendNoSubscriberEvent;
    protected final boolean eventInheritance;
    protected final @NotNull ExecutorService executorService;
    protected final @Nullable HLog logger;

    protected final @NotNull Multimap<@NotNull SubscribedMethod, @NotNull Object> methodSubscriberMap = MultimapBuilder.hashKeys().linkedListValues().build();
    protected final @NotNull SortedSetMultimap<@NotNull Class<?>, @NotNull SubscribedMethod> eventMethodMap = MultimapBuilder.hashKeys()
                    .treeSetValues((a, b) -> {
                        if (a == b)
                            return 0;
                        assert a != null;
                        assert b != null;
                        return (((SubscribedMethod) a).priority() < ((SubscribedMethod) b).priority()) ? 1 : -1;
                    }).build();
    protected final @NotNull Multimap<@NotNull Class<?>, @NotNull Object> stickyEventsMap = MultimapBuilder.hashKeys().linkedListValues().build();

    public EventBus(final @NotNull EventBusBuilder builder) {
        super();
        this.logSubscriberExceptions = builder.logSubscriberExceptions;
        this.logNoSubscriberMessages = builder.logNoSubscriberMessages;
        this.sendSubscriberExceptionEvent = builder.sendSubscriberExceptionEvent;
        this.sendNoSubscriberEvent = builder.sendNoSubscriberEvent;
        this.eventInheritance = builder.eventInheritance;
        this.executorService = builder.executorService;
        this.logger = builder.logger;
    }

    public void register(final @NotNull Object subscriber) {
        final Class<?> aClass = subscriber.getClass();
        final Set<SubscribedMethod> methods = EventBus.methodBySubscriberCache.getUnchecked(aClass);
        if (methods.isEmpty()) {
            if (this.logger != null)
                this.logger.log(HLogLevel.MISTAKE, "No subscribed method for class: ", aClass);
            return;
        }
        final Multimap<SubscribedMethod, Object> ms = MultimapBuilder.hashKeys().linkedListValues().build();
        final Multimap<Class<?>, SubscribedMethod> em = MultimapBuilder.hashKeys().hashSetValues().build();
        for (final SubscribedMethod method: methods) {
            ms.put(method, subscriber);
            em.put(method.eventType(), method);
        }
        em.keySet().forEach(c -> this.stickyEventsMap.get(c).forEach(this::post));
        synchronized (this) {
            this.methodSubscriberMap.putAll(ms);
            this.eventMethodMap.putAll(em);
        }
    }

    public void unregister(final @NotNull Object subscriber) {
        final Class<?> aClass = subscriber.getClass();
        final Collection<SubscribedMethod> methods = EventBus.methodBySubscriberCache.getUnchecked(aClass);
        synchronized (this) {
            for (final SubscribedMethod method: methods) {
                final Collection<Object> subscribers = this.methodSubscriberMap.get(method);
                subscribers.remove(subscriber);
                if (subscribers.isEmpty())
                    this.eventMethodMap.remove(method.eventType(), method);
            }
        }
    }

    public void post(final @NotNull Object event) {
        final Class<?> aClass = event.getClass();
        final Collection<Pair<Object, SubscribedMethod>> invokers = new TreeSet<>((a, b) -> a.getSecond() == b.getSecond() ? 0 : a.getSecond().priority() < b.getSecond().priority() ? 1 : -1);
        synchronized (this) {
            final Collection<SubscribedMethod> inheritedMethods = new HashSet<>(this.eventMethodMap.get(aClass));
            if (this.eventInheritance)
                for (final SubscribedMethod method: inheritedMethods)
                    //noinspection AccessToStaticFieldLockedOnInstance
                    EventBus.eventTypeInheritanceCache.getUnchecked(method.eventType()).forEach(c -> inheritedMethods.addAll(this.eventMethodMap.get(c)));
            inheritedMethods.forEach(m -> {
                for (final Object subscriber: this.methodSubscriberMap.get(m))
                    invokers.add(Pair.makePair(subscriber, m));
            });
        }
        if (!invokers.isEmpty())
            invokers.forEach(p -> this.invokeMethod(p.getFirst(), p.getSecond(), event));
        else if (!(event instanceof NoSubscriberEvent))
            this.handleNoSubscriberException(new NoSubscriberEvent(this, event));
    }

    public void postSticky(final @NotNull Object event) {
        this.post(event);
        this.stickyEventsMap.put(event.getClass(), event);
    }

    public boolean removeSticky(final @NotNull Object event) {
        return this.stickyEventsMap.remove(event.getClass(), event);
    }

    protected void invokeMethod(final @NotNull Object subscriber, final @NotNull SubscribedMethod method, final @NotNull Object event) {
        switch (method.threadMode()) {
            case SYNC -> {
                try {
                    method.method().invoke(subscriber, event);
                } catch (final IllegalAccessException | InvocationTargetException exception) {
                    this.handleInvokeException(new SubscriberExceptionEvent(this, exception, event, method.method(), subscriber));
                }
            }
            case ASYNC -> this.executorService.submit(() -> {
                try {
                    method.method().invoke(subscriber, event);
                } catch (final IllegalAccessException | InvocationTargetException exception) {
                    this.handleInvokeException(new SubscriberExceptionEvent(this, exception, event, method.method(), subscriber));
                }
            });
        }
    }

    protected void handleNoSubscriberException(final @NotNull NoSubscriberEvent exception) {
        if (this.logNoSubscriberMessages && this.logger != null)
            this.logger.log(HLogLevel.INFO, "No subscriber for event. ", exception);
        if (this.sendNoSubscriberEvent)
            this.post(exception);
    }

    protected void handleInvokeException(final @NotNull SubscriberExceptionEvent exception) {
        if (exception.causingEvent() instanceof SubscriberExceptionEvent)
            return;
        if (this.logSubscriberExceptions && this.logger != null)
            this.logger.log(HLogLevel.ERROR, "An exception was thrown when posting event.", exception, exception.throwable());
        if (this.sendSubscriberExceptionEvent)
            this.post(exception);
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof EventBus eventBus)) return false;
        return this.logSubscriberExceptions == eventBus.logSubscriberExceptions && this.logNoSubscriberMessages == eventBus.logNoSubscriberMessages && this.sendSubscriberExceptionEvent == eventBus.sendSubscriberExceptionEvent && this.sendNoSubscriberEvent == eventBus.sendNoSubscriberEvent && this.eventInheritance == eventBus.eventInheritance && this.executorService.equals(eventBus.executorService) && Objects.equals(this.logger, eventBus.logger) && this.methodSubscriberMap.equals(eventBus.methodSubscriberMap) && this.eventMethodMap.equals(eventBus.eventMethodMap) && this.stickyEventsMap.equals(eventBus.stickyEventsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.executorService, this.logger, this.methodSubscriberMap, this.eventMethodMap, this.stickyEventsMap);
    }

    @Override
    public String toString() {
        return "EventBus{" +
                "executorService=" + this.executorService +
                '}';
    }
}
