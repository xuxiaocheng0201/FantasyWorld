package com.xuxiaocheng.EventBus.Events;

import com.xuxiaocheng.EventBus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record NoSubscriberEvent(@NotNull EventBus eventBus, @Nullable Object originalEvent) {
}
