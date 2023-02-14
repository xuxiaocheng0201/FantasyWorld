package com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus;

import com.xuxiaocheng.FantasyWorld.Platform.Additions.Events.AdditionInitializationEvent;
import com.xuxiaocheng.FantasyWorld.Platform.FantasyWorldPlatform;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.EventBusEnhance;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>{@code AdditionsLoader/Exceptions}
 *   Post {@link com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions.IllegalAdditionException} when something went wrong during addition loading.</p>
 * <p>{@code AdditionsLoader/%AdditionId%}
 *   Post {@link AdditionInitializationEvent} when this addition loads.</p>
 */
public final class EventBusManager {
    private EventBusManager() {
        super();
    }

    private static final @NotNull Map<String, EventBus> AdditionsEventBuses = new ConcurrentHashMap<>();

    private static final @NotNull EventBus DefaultEventBus = new EventBusEnhance(EventBus.builder().executorService(FantasyWorldPlatform.DefaultThreadPool));
    static {
        EventBusManager.AdditionsEventBuses.put("default", EventBusManager.DefaultEventBus);
        if (FantasyWorldPlatform.DebugMode)
            EventBusManager.DefaultEventBus.register(new DebugEventLogger("default"));
    }

    public static @Nullable EventBus getExist(final @Nullable String id) {
        return EventBusManager.AdditionsEventBuses.get(id);
    }

    public static @NotNull EventBus createInstance(final @Nullable String id, final @Nullable EventBusBuilder builder) {
        if (id == null)
            return EventBusManager.DefaultEventBus;
        final EventBus eventBus = EventBusManager.getExist(id);
        if (eventBus != null)
            return eventBus;
        final EventBus build;
        build = new EventBusEnhance(Objects.requireNonNullElseGet(builder, EventBus::builder).executorService(FantasyWorldPlatform.DefaultThreadPool));
        if (FantasyWorldPlatform.DebugMode)
            build.register(new DebugEventLogger(id));
        EventBusManager.AdditionsEventBuses.put(id, build);
        return build;
    }

    public static @NotNull EventBus getInstance(final @Nullable String id) {
        return EventBusManager.createInstance(id, null);
    }

    private record DebugEventLogger(@NotNull String name) {
        @Subscribe(priority = Integer.MAX_VALUE)
        public void log(final Object event) {
            if (event instanceof SubscriberExceptionEvent)
                return;
            HLog.DefaultLogger.log(HLogLevel.INFO, "DebugEventLogger event log: At EventBus id: ", this.name, ", Event: ", event);
        }

        @Subscribe(priority = Integer.MAX_VALUE)
        public void exception(final @NotNull SubscriberExceptionEvent event) {
            HLog.DefaultLogger.log(HLogLevel.ERROR, "DebugEventLogger: At EventBus id: ", this.name, ", CausingEvent: ", event.causingEvent, ", Caused: ", event.throwable);
        }
    }
}
