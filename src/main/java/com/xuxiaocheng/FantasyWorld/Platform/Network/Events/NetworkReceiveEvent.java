package com.xuxiaocheng.FantasyWorld.Platform.Network.Events;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketInputStream;
import io.netty.channel.ChannelId;

public record NetworkReceiveEvent(PacketInputStream stream, ChannelId from) {
}
