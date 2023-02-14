package com.xuxiaocheng.FantasyWorldTest.Platform.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Network.ClientNetwork;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkSendEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Network.PacketManager;
import com.xuxiaocheng.FantasyWorld.Platform.Network.ServerNetwork;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus.EventBusManager;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.ByteBufIOUtil;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NetworkTest {
    @Test
    public void server() throws InterruptedException {
        HLog.setDebugMode(false);
        PacketManager.registerPacket("UTF", StringPacket.class, StringPacket::from, StringPacket::to);
        final ServerNetwork serverNetwork = new ServerNetwork(new InetSocketAddress(25564), "Server");
        final Thread client = new Thread(() -> {
            try (final ClientNetwork clientNetwork = new ClientNetwork(new InetSocketAddress(25564), "Client")) {
                EventBusManager.getInstance("Client").post(new NetworkSendEvent<>(new StringPacket("Hello from client!"), null));
        EventBusManager.getInstance("Server").post(new NetworkSendEvent<>(new StringPacket("Hello from server!"), null));
            } catch (final InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }, "Client");
        client.start();
        client.join();
        serverNetwork.close();
    }

    public static class StringPacket {
        protected final @NotNull String string;

        public StringPacket(final @NotNull String string) {
            super();
            this.string = string;
        }

        @Override
        public @NotNull String toString() {
            return "StringPacket{" +
                    "string='" + this.string + '\'' +
                    '}';
        }

        public static @NotNull StringPacket from(final @NotNull ByteBuf buffer) throws IOException {
            return new StringPacket(ByteBufIOUtil.readUTF(buffer));
        }

        public static void to(final @NotNull ByteBuf buffer, final @NotNull StringPacket packet) throws IOException {
            ByteBufIOUtil.writeUTF(buffer, packet.string);
        }
    }
}
