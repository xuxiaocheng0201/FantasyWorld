package com.xuxiaocheng.FantasyWorld.Platform.Additions;

import com.google.common.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class EventBusManager {
    private EventBusManager() {
        super();
    }

    private static final @NotNull EventBus GlobalEventBus = new EventBus("global");
    private static final @NotNull Map<String, EventBus> AdditionsEventBuses = new HashMap<>();

    public static @NotNull EventBus get(@Nullable final String id) {
        if (id == null)
            return EventBusManager.GlobalEventBus;
        synchronized (EventBusManager.AdditionsEventBuses) {
            if (EventBusManager.AdditionsEventBuses.containsKey(id))
                return EventBusManager.AdditionsEventBuses.get(id);
            final EventBus eventBus = new EventBus(id);
            EventBusManager.AdditionsEventBuses.put(id, eventBus);
            return eventBus;
        }
    }

    public static @Nullable EventBus getExist(@Nullable final String id) {
        return EventBusManager.AdditionsEventBuses.get(id);
    }
}
