package com.xuxiaocheng.FantasyWorld.Platform.Network.Events;

import com.xuxiaocheng.FantasyWorld.Platform.Network.ServerNetwork;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record NetworkSendEvent<T>(@NotNull T packet, @Nullable ServerNetwork.ChannelIdMatcher matcher) {
}
