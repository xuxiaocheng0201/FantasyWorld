package com.xuxiaocheng.FantasyWorld.Platform.Network.Events;

import io.netty.channel.ChannelId;

public record ClientActiveEvent(ChannelId id, boolean active) {
}
