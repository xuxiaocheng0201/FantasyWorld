package com.xuxiaocheng.FantasyWorld.Platform.Network.Events;

import io.netty.channel.ChannelId;
import org.jetbrains.annotations.NotNull;

public record ClientActiveEvent(@NotNull ChannelId id, boolean active) {
}
