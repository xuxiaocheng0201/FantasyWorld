package com.xuxiaocheng.FantasyWorld.Platform.Additions;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.xuxiaocheng.FantasyWorld.Platform.FantasyWorldPlatform;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>{@code AdditionsLoader/Exceptions}
 *   Post {@link com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.IllegalAdditionException}</p>
 *
 * <p>{@code AdditionInitialization-0}
 *   Post {@link com.xuxiaocheng.FantasyWorld.Platform.Events.AdditionInitializationEvent} before addition load.</p>
 * <p>{@code %AdditionId%}
 *   Post {@link com.xuxiaocheng.FantasyWorld.Platform.Events.AdditionInitializationEvent} when this addition loads.</p>
 * <p>{@code AdditionInitialization-1}
 *   Post {@link com.xuxiaocheng.FantasyWorld.Platform.Events.AdditionInitializationEvent} after addition load.</p>
 */
public final class EventBusManager {
    private EventBusManager() {
        super();
    }

    private static final @NotNull Map<String, EventBus> AdditionsEventBuses = new HashMap<>();

    private static final @NotNull EventBus GlobalEventBus = new EventBus("global");
    static {
        EventBusManager.AdditionsEventBuses.put("global", EventBusManager.GlobalEventBus);
    }

    public static @NotNull EventBus get(@Nullable final String id) {
        if (id == null)
            return EventBusManager.GlobalEventBus;
        synchronized (EventBusManager.AdditionsEventBuses) {
            if (EventBusManager.AdditionsEventBuses.containsKey(id))
                return EventBusManager.AdditionsEventBuses.get(id);
            final EventBus eventBus = new EventBus(id);
            EventBusManager.AdditionsEventBuses.put(id, eventBus);
            if (FantasyWorldPlatform.DebugMode)
                eventBus.register(new DebugEventLogger(id));
            return eventBus;
        }
    }

    public static @Nullable EventBus getExist(@Nullable final String id) {
        synchronized (EventBusManager.AdditionsEventBuses) {
            return EventBusManager.AdditionsEventBuses.get(id);
        }
    }

    private record DebugEventLogger(String name) {
        @Subscribe
        public void log(final Object o) {
            HLog.DefaultLogger.log(HLogLevel.INFO, "EventBus: ", this.name, ", Event: ", o);
        }
    }
}
