package com.xuxiaocheng.FantasyWorld.Platform.Network;

import com.xuxiaocheng.FantasyWorld.Platform.FantasyWorldPlatform;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkReceiveEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkSendEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus.EventBusManager;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketInputStream;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketOutputStream;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketAddress;

public class ClientNetwork extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    protected static @NotNull HLog logger = HLog.createInstance("NetworkLogger",
            FantasyWorldPlatform.DebugMode ? Integer.MIN_VALUE : HLogLevel.DEBUG.getPriority() + 1,
            true, HLog.DefaultLogger.getWriter());

    protected final @NotNull SocketAddress address;
    protected final @NotNull String id;
    protected final @NotNull EventBus eventBus;
    protected final @NotNull PacketOutputStream readBuffer = new PacketOutputStream();

    protected final @NotNull EventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    protected final @NotNull Channel channel;

    public ClientNetwork(final @NotNull SocketAddress address, final @NotNull String id) {
        super(true);
        this.address = address;
        this.id = id;
        this.eventBus = EventBusManager.getInstance(id);
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(this.group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(ClientNetwork.this);
                    }
                });
        this.channel = bootstrap.connect(address).syncUninterruptibly().channel();
        this.eventBus.register(this);
    }

    @Override
    public void close() throws InterruptedException {
        this.channel.close().sync();
        this.group.shutdownGracefully().sync();
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull SocketAddress getAddress() {
        return this.address;
    }

    public @NotNull ChannelFuture send(final byte @NotNull [] bytes) {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(4 + bytes.length).writeInt(bytes.length).writeBytes(bytes);
        return this.channel.writeAndFlush(buf);
    }

    @Subscribe
    public void onSendEvent(final @NotNull NetworkSendEvent event) throws InterruptedException {
        this.send(event.stream().toBytes()).sync();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final @NotNull ByteBuf msg) throws IOException {
        final byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        ClientNetwork.logger.log(HLogLevel.DEBUG, "Client: channel read. length: ", bytes.length);
        ClientNetwork.logger.log(HLogLevel.VERBOSE, "Read bytes: ", bytes);
        this.readBuffer.writeByteArray(bytes);
        if (this.readBuffer.size() >= 4) {
            final ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(4, 4);
            final int length = buf
                    .writeByte(this.readBuffer.getByte(0)).writeByte(this.readBuffer.getByte(1))
                    .writeByte(this.readBuffer.getByte(2)).writeByte(this.readBuffer.getByte(3))
                    .readInt();
            buf.release();
            if (this.readBuffer.size() - 4 >= length) {
                this.eventBus.post(new NetworkReceiveEvent(PacketManager.handleInput(new PacketInputStream(this.readBuffer.toBytes(4, length))), this.channel.id()));
                this.readBuffer.clear(length + 4);
            }
        }
    }

    @Override
    public void exceptionCaught(final @NotNull ChannelHandlerContext ctx, final Throwable cause) {
        ClientNetwork.logger.log(HLogLevel.ERROR, "Client: channel throws.", cause);
        ctx.close();
    }

    @Override
    public @NotNull String toString() {
        return "ClientNetwork{" +
                "id=" + this.id +
                ", address=" + this.address +
                "}";
    }
}
