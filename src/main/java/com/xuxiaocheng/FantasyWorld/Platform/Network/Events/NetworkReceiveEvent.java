package com.xuxiaocheng.FantasyWorld.Platform.Network.Events;

import io.netty.channel.ChannelId;
import org.jetbrains.annotations.NotNull;

public record NetworkReceiveEvent<T>(@NotNull T packet, @NotNull ChannelId from) {
}
