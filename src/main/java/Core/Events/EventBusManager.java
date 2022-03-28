package Core.Events;

import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class EventBusManager {
    private static final EventBus DEFAULT_EVENT_BUS = EventBus.getDefault();
    private static final List<EventBus> ALL_EVENT_BUS = new ArrayList<>();
    static {
        ALL_EVENT_BUS.add(DEFAULT_EVENT_BUS);
    }

    public static EventBus getDefaultEventBus() {
        return DEFAULT_EVENT_BUS;
    }

    public static EventBus getEventBusByName(String name) {
        //TODO: Add more event_bus
        return DEFAULT_EVENT_BUS;
    }

    public static String getNameByEventBus(EventBus eventBus) {
        if (eventBus.equals(DEFAULT_EVENT_BUS))
            return "default";
        return "undefined";
    }

    public static List<EventBus> getAllEventBus() {
        return ALL_EVENT_BUS;
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
