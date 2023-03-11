package com.xuxiaocheng.EventBus.Events;

import com.xuxiaocheng.EventBus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public record SubscriberExceptionEvent(@NotNull EventBus eventBus, @NotNull Throwable throwable,
                                       @Nullable Object causingEvent, @NotNull Method causingMethod, @NotNull Object causingSubscriber) {
    @Override
    public @NotNull String toString() {
        return "SubscriberExceptionEvent{" +
                "eventBus=" + this.eventBus +
                ", throwable=" + this.throwable +
                ", causingEvent=" + this.causingEvent +
                ", causingMethod=" + this.causingMethod +
                ", causingSubscriber=" + this.causingSubscriber +
                '}';
    }
}
