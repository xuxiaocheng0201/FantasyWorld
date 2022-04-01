package Core.EventBus;

import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventBusManager {
    private static final Map<String, EventBus> ALL_EVENT_BUS = new HashMap<>();
    private static final Map<EventBus, String> ALL_EVENT_BUS_REVERSE = new HashMap<>();

    public static void addEventBus(String name, EventBus eventBus) {
        ALL_EVENT_BUS.put(name, eventBus);
        ALL_EVENT_BUS_REVERSE.put(eventBus, name);
    }

    private static final EventBus DEFAULT_EVENT_BUS = EventBusCreator.defaultLoggerEventBusBuilder()
            .throwSubscriberException(false).logSubscriberExceptions(true).sendSubscriberExceptionEvent(true)
            .logNoSubscriberMessages(false).sendNoSubscriberEvent(true).build();
    static {
        addEventBus("default", DEFAULT_EVENT_BUS);
    }

    public static EventBus getDefaultEventBus() {
        return DEFAULT_EVENT_BUS;
    }

    public static EventBus getEventBusByName(String name) {
        if (ALL_EVENT_BUS.containsKey(name))
            return ALL_EVENT_BUS.get(name);
        return DEFAULT_EVENT_BUS;
    }

    public static String getNameByEventBus(EventBus eventBus) {
        if (ALL_EVENT_BUS_REVERSE.containsKey(eventBus))
            return ALL_EVENT_BUS_REVERSE.get(eventBus);
        return "undefined";
    }

    public static Set<EventBus> getAllEventBus() {
        return ALL_EVENT_BUS_REVERSE.keySet();
    }

    public static void register(Class<?> aClass) throws NoSuchMethodException {
        EventSubscribe subscribe = aClass.getAnnotation(EventSubscribe.class);
        if (subscribe == null)
            return;
        if (subscribe.eventBus().equals("*")) {
            Object instance = HClassHelper.getInstance(aClass);
            if (instance == null)
                throw new NoSuchMethodException(HStringHelper.merge("Get instance failed. Can't register class '", aClass, "' to event bus '", subscribe.eventBus(), "'."));
            for (EventBus eventBus: EventBusManager.getAllEventBus())
                eventBus.register(instance);
            return;
        }
        EventBus eventBus = getEventBusByName(subscribe.eventBus());
        Object instance = HClassHelper.getInstance(aClass);
        if (instance == null)
            throw new NoSuchMethodException(HStringHelper.merge("Get instance failed. Can't register class '", aClass, "' to event bus '", subscribe.eventBus(), "'."));
        eventBus.register(instance);
    }
}
