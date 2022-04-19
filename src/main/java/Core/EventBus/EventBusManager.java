package Core.EventBus;

import Core.Addition.ModManager;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Level;

public class EventBusManager {
    private static final HMapRegisterer<String, EventBus> ALL_EVENT_BUS = new HMapRegisterer<>(false);
    private static final HMapRegisterer<EventBus, String> ALL_EVENT_BUS_REVERSE = new HMapRegisterer<>(false);

    public static void addEventBus(String name, EventBus eventBus) throws HElementRegisteredException {
        String s = HStringHelper.notNullStrip(name);
        if ("*".equals(s))
            throw new HElementRegisteredException("Illegal eventbus name with '*'.");
        ALL_EVENT_BUS.register(s, eventBus);
        ALL_EVENT_BUS_REVERSE.register(eventBus, s);
    }

    private static final EventBus DEFAULT_EVENT_BUS = defaultLoggerEventBusBuilder()
            .throwSubscriberException(false).logSubscriberExceptions(true).sendSubscriberExceptionEvent(true)
            .logNoSubscriberMessages(false).sendNoSubscriberEvent(true).build();
    public static EventBus getDefaultEventBus() {
        return DEFAULT_EVENT_BUS;
    }
    static {
        try {
            addEventBus("default", DEFAULT_EVENT_BUS);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    public static EventBus getEventBusByName(String name) {
        try {
            return ALL_EVENT_BUS.getElement(HStringHelper.notNullStrip(name));
        } catch (HElementNotRegisteredException exception) {
            return DEFAULT_EVENT_BUS;
        }
    }

    public static String getNameByEventBus(EventBus eventBus) {
        try {
            return ALL_EVENT_BUS_REVERSE.getElement(eventBus);
        } catch (HElementNotRegisteredException exception) {
            return "undefined";
        }
    }

    public static @NotNull Set<EventBus> getAllEventBus() {
        return ALL_EVENT_BUS_REVERSE.getMap().keySet();
    }

    public static void register(@NotNull Class<?> aClass) throws NoSuchMethodException {
        EventSubscribe subscribe = aClass.getAnnotation(EventSubscribe.class);
        if (subscribe == null)
            return;
        boolean noSubscriber = true;
        for (Method method: aClass.getMethods())
            if (method.getAnnotation(Subscribe.class) != null) {
                noSubscriber = false;
                break;
            }
        if (noSubscriber)
            return;
        Object instance = HClassHelper.getInstance(aClass);
        if (instance == null)
            throw new NoSuchMethodException("Get instance failed. Can't register class to event bus '" + subscribe.eventBus() + "'."
                    + ModManager.crashClassInformation(aClass));
        if ("*".equals(HStringHelper.notNullStrip(subscribe.eventBus()))) {
            for (EventBus eventBus: EventBusManager.getAllEventBus())
                eventBus.register(instance);
            return;
        }
        EventBus eventBus = getEventBusByName(subscribe.eventBus());
        eventBus.register(instance);
    }

    public static EventBusBuilder defaultLoggerEventBusBuilder() {
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                HLog.logger(HLogLevel.mapFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                HLog.logger(HLogLevel.mapFromLevel(level), msg, " Cause message: ", th.getMessage());
                HLog.logger(HLogLevel.mapFromLevel(level), th);
            }
        });
    }

    public static EventBusBuilder loggerEventBusBuilder(HLog logger) {
        return EventBus.builder().logger(new Logger() {
            @Override
            public void log(Level level, String msg) {
                logger.log(HLogLevel.mapFromLevel(level), msg);
            }
            @Override
            public void log(Level level, String msg, Throwable th) {
                logger.log(HLogLevel.mapFromLevel(level), msg, " Cause message: ", th);
                logger.log(HLogLevel.mapFromLevel(level), th);
            }
        });
    }
}
