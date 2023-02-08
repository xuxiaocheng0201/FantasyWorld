package com.xuxiaocheng.FantasyWorld.Platform.Network.Events;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketOutputStream;
import io.netty.channel.group.ChannelMatcher;

public record NetworkSendEvent(PacketOutputStream stream, ChannelMatcher toMatcher) {
}
