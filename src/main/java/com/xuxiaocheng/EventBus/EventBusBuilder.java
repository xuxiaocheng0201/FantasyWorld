package com.xuxiaocheng.EventBus;

import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventBusBuilder {
    public static final ExecutorService DefaultExecutorService = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors() << 2,
            30L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(128),
            new DefaultThreadFactory("EventBus/Default"), (r, executor) -> {
        HLog.DefaultLogger.log(HLogLevel.ERROR, "EventBusBuilder/DefaultExecutorService is full. Runnable class: ", r.getClass());
        r.run();
    });

    protected boolean logSubscriberExceptions = true;
    protected boolean logNoSubscriberMessages = true;
    protected boolean sendSubscriberExceptionEvent = true;
    protected boolean sendNoSubscriberEvent = true;
    protected boolean eventInheritance = true;
    protected @NotNull ExecutorService executorService = EventBusBuilder.DefaultExecutorService;
    protected @Nullable HLog logger;

    protected EventBusBuilder() {
        super();
    }

    public EventBusBuilder logSubscriberExceptions(final boolean logSubscriberExceptions) {
        this.logSubscriberExceptions = logSubscriberExceptions;
        return this;
    }

    public EventBusBuilder logNoSubscriberMessages(final boolean logNoSubscriberMessages) {
        this.logNoSubscriberMessages = logNoSubscriberMessages;
        return this;
    }

    public EventBusBuilder sendSubscriberExceptionEvent(final boolean sendSubscriberExceptionEvent) {
        this.sendSubscriberExceptionEvent = sendSubscriberExceptionEvent;
        return this;
    }

    public EventBusBuilder sendNoSubscriberEvent(final boolean sendNoSubscriberEvent) {
        this.sendNoSubscriberEvent = sendNoSubscriberEvent;
        return this;
    }

    public EventBusBuilder eventInheritance(final boolean eventInheritance) {
        this.eventInheritance = eventInheritance;
        return this;
    }

    public EventBusBuilder executorService(final @NotNull ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public EventBusBuilder logger(final @Nullable HLog logger) {
        this.logger = logger;
        return this;
    }

    public @NotNull EventBus build() {
        return new EventBus(this);
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof final EventBusBuilder that)) return false;
        return this.logSubscriberExceptions == that.logSubscriberExceptions && this.logNoSubscriberMessages == that.logNoSubscriberMessages && this.sendSubscriberExceptionEvent == that.sendSubscriberExceptionEvent && this.sendNoSubscriberEvent == that.sendNoSubscriberEvent && this.eventInheritance == that.eventInheritance && this.executorService.equals(that.executorService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.logSubscriberExceptions, this.logNoSubscriberMessages, this.sendSubscriberExceptionEvent, this.sendNoSubscriberEvent, this.eventInheritance, this.executorService);
    }

    @Override
    public @NotNull String toString() {
        return "EventBusBuilder{" +
                "logSubscriberExceptions=" + this.logSubscriberExceptions +
                ", logNoSubscriberMessages=" + this.logNoSubscriberMessages +
                ", sendSubscriberExceptionEvent=" + this.sendSubscriberExceptionEvent +
                ", sendNoSubscriberEvent=" + this.sendNoSubscriberEvent +
                ", eventInheritance=" + this.eventInheritance +
                ", executorService=" + this.executorService +
                '}';
    }
}
