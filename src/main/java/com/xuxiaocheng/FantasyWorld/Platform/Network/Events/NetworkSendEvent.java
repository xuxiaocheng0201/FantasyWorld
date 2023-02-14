package com.xuxiaocheng.FantasyWorld.Platform.Network.Events;

import com.xuxiaocheng.FantasyWorld.Platform.Network.ServerNetwork;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketOutputStream;

public record NetworkSendEvent(PacketOutputStream stream, ServerNetwork.ChannelIdMatcher matcher) {
}
