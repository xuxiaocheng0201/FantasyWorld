package org.greenrobot.eventbus;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class EventBusEnhance extends EventBus {
    protected static final @NotNull LoadingCache<Class<?>, Set<@NotNull SubscriberMethod>> methodCache = CacheBuilder.newBuilder()
            .weakKeys().maximumSize(1000).build(new CacheLoader<>() {
                @Override
                public @NotNull Set<@NotNull SubscriberMethod> load(final @NotNull Class<?> key) {
                    final Set<SubscriberMethod> methods = new HashSet<>();
                    for (final Method method: key.getDeclaredMethods()) {
                        final Subscribe subscribe = method.getAnnotation(Subscribe.class);
                        if (subscribe == null)
                            continue;
                        final Class<?>[] parameters = method.getParameterTypes();
                        if (parameters.length != 1)
                            continue;
                        methods.add(new SubscriberMethod(method, parameters[0], subscribe.threadMode(), subscribe.priority(), subscribe.sticky()));
                    }
                    if (methods.isEmpty()) {
                        throw new EventBusException("Subscriber " + key
                                + " and its super classes have no public methods with the @Subscribe annotation");
                    }
                    return methods;
                }
            });

    protected static Method subscribe;
    static {
        try {
            EventBusEnhance.subscribe = EventBus.class.getDeclaredMethod("subscribe", Object.class, SubscriberMethod.class);
            //noinspection NonFinalStaticVariableUsedInClassInitialization
            EventBusEnhance.subscribe.setAccessible(true);
        } catch (final NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    public EventBusEnhance() {
        super();
    }

    public EventBusEnhance(final @NotNull EventBusBuilder builder) {
        super(builder);
    }

    @Override
    public void register(final @NotNull Object subscriber) {
        final Class<?> c = subscriber.getClass();
        final Set<SubscriberMethod> methods;
        try {
            methods = EventBusEnhance.methodCache.get(c);
        } catch (final ExecutionException exception) {
            throw new RuntimeException(exception);
        }
        //noinspection SynchronizeOnNonFinalField
        synchronized (EventBusEnhance.subscribe) {
            for (final SubscriberMethod method: methods) {
                try {
                    EventBusEnhance.subscribe.invoke(this, subscriber, method);
                } catch (final IllegalAccessException | InvocationTargetException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
    }
}
