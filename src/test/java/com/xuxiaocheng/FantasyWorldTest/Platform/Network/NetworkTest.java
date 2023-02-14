package com.xuxiaocheng.FantasyWorldTest.Platform.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Network.ClientNetwork;
import com.xuxiaocheng.FantasyWorld.Platform.Network.ServerNetwork;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketOutputStream;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.TestUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NetworkTest {
    @Test
    public void server() throws InterruptedException {
        HLog.setDebugMode(false);
        final ServerNetwork serverNetwork = new ServerNetwork(new InetSocketAddress(25564), "Server");
        final Thread client = new Thread(() -> {
            try (final ClientNetwork clientNetwork = new ClientNetwork(new InetSocketAddress(25564), "Client")) {
                try (final PacketOutputStream outputStream = new PacketOutputStream()) {
                    outputStream.writeUTF("Hello from client.");
                    clientNetwork.send(outputStream.toBytes()).sync();
                }
        try (final PacketOutputStream outputStream = new PacketOutputStream()) {
            outputStream.writeUTF("Hello from server.");
            serverNetwork.send(outputStream.toBytes(), (c) -> true).sync();
        }
            } catch (final IOException | InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }, "Client");
        client.start();
        client.join();
        serverNetwork.close();
    }
}
