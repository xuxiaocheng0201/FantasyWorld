package Core.EventBus;

import Core.Addition.ModManager;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HDoubleMapRegisterer;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HSetRegisterer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Event bus manager.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class EventBusManager {
    private static final HDoubleMapRegisterer<String, EventBus> ALL_EVENT_BUS = new HDoubleMapRegisterer<>();
    private static final HSetRegisterer<Object> REGISTER_ALL_EVENTBUS_CLASS = new HSetRegisterer<>(false);

    /**
     * Register a new event bus.
     * @param name event bus name
     * @param eventBus event bus
     * @throws HElementRegisteredException Registered event bus or illegal name.
     */
    public static void addEventBus(@Nullable String name, EventBus eventBus) throws HElementRegisteredException {
        if (eventBus == null)
            throw new HElementRegisteredException("Null eventbus.");
        String s = HStringHelper.notNullStrip(name);
        if ("*".equals(s))
            throw new HElementRegisteredException("Illegal eventbus name with '*'.");
        if (s.isEmpty())
            throw new HElementRegisteredException("Empty eventbus name.");
        ALL_EVENT_BUS.register(s, eventBus);
        for (Object instance: REGISTER_ALL_EVENTBUS_CLASS)
            eventBus.register(instance);
    }

    private static final EventBus DEFAULT_EVENT_BUS = loggerEventBusBuilder(new HLog("DefaultEventBus", Thread.currentThread().getName()))
            .throwSubscriberException(false).logSubscriberExceptions(true).sendSubscriberExceptionEvent(true)
            .logNoSubscriberMessages(false).sendNoSubscriberEvent(true).build();
    private static final EventBus GL_EVENT_BUS = loggerEventBusBuilder(new HLog("GLEventBus", Thread.currentThread().getName()))
            .throwSubscriberException(false).logSubscriberExceptions(true).sendSubscriberExceptionEvent(true)
            .logNoSubscriberMessages(false).sendNoSubscriberEvent(false).build();
    public static EventBus getDefaultEventBus() {
        return DEFAULT_EVENT_BUS;
    }
    public static EventBus getGLEventBus() {
        return GL_EVENT_BUS;
    }
    static {
        try {
            addEventBus("default", DEFAULT_EVENT_BUS);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
        try {
            addEventBus("gl", GL_EVENT_BUS);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    public static EventBus getEventBusByName(String name) {
        try {
            return ALL_EVENT_BUS.getValue(HStringHelper.notNullStrip(name));
        } catch (HElementNotRegisteredException exception) {
            return DEFAULT_EVENT_BUS;
        }
    }

    public static String getNameByEventBus(EventBus eventBus) {
        try {
            return ALL_EVENT_BUS.getKey(eventBus);
        } catch (HElementNotRegisteredException exception) {
            return "undefined";
        }
    }

    public static @NotNull Collection<EventBus> getAllEventBus() {
        return ALL_EVENT_BUS.values();
    }

    private static final Collection<Class<?>> registeredClassesFlag = new HashSet<>();

    /**
     * Register the class to eventbus by {@link EventSubscribe} annotation.
     * @param aClass the class to register
     * @throws NoSuchMethodException Failed to get instance.
     */
    public static void register(@NotNull Class<?> aClass) throws NoSuchMethodException {
        EventSubscribe subscribe = aClass.getAnnotation(EventSubscribe.class);
        if (subscribe == null || !HClassHelper.isClass(aClass))
            return;
        boolean noSubscriber = true;
        for (Method method: aClass.getMethods())
            if (method.getAnnotation(Subscribe.class) != null) {
                noSubscriber = false;
                break;
            }
        if (noSubscriber)
            return;
        Object instance = HClassHelper.getInstance(aClass, true);
        if (instance == null)
            throw new NoSuchMethodException("Get instance failed. Can't register class to event bus '" + subscribe.eventBus() + "'."
                    + ModManager.crashClassInformation(aClass));
        if (registeredClassesFlag.contains(aClass))
            return;
        registeredClassesFlag.add(aClass);
        if ("*".equals(HStringHelper.notNullStrip(subscribe.eventBus()))) {
            for (EventBus eventBus: getAllEventBus())
                eventBus.register(instance);
            try {
                REGISTER_ALL_EVENTBUS_CLASS.register(instance);
            } catch (HElementRegisteredException ignore) {
            }
            return;
        }
        Collection<String> buses = Arrays.asList(HStringHelper.notEmptyStrip(subscribe.eventBus().split(";")))
                .stream().distinct().collect(Collectors.toList());
        for (String name: buses)
            getEventBusByName(name).register(instance);
    }

    /**
     * Get {@link EventBusBuilder} with HLog.
     * @return {@code EventBusBuilder}
     */
    public static @NotNull EventBusBuilder defaultLoggerEventBusBuilder() {
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                HLog.logger(HLogLevel.mapFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                HLog.logger(HLogLevel.mapFromLevel(level), msg, th);
            }
        });
    }

    /**
     * Get {@link EventBusBuilder} with param logger.
     * @param logger event logger
     * @return {@code EventBusBuilder}
     */
    public static @NotNull EventBusBuilder loggerEventBusBuilder(@Nullable HLog logger) {
        if (logger == null)
            return defaultLoggerEventBusBuilder();
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                logger.log(HLogLevel.mapFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                logger.log(HLogLevel.mapFromLevel(level), msg, th);
            }
        });
    }
}
