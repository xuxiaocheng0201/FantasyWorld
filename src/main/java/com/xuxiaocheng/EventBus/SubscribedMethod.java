package com.xuxiaocheng.EventBus;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public record SubscribedMethod(@NotNull Method method, @NotNull Class<?> eventType,
                               @NotNull Subscribe.ThreadMode threadMode, boolean sticky, int priority) {
}
