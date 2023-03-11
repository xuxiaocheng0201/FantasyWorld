package com.xuxiaocheng.FantasyWorld.Platform.Network;

import com.xuxiaocheng.EventBus.EventBus;
import com.xuxiaocheng.EventBus.Subscribe;
import com.xuxiaocheng.FantasyWorld.Platform.FantasyWorldPlatform;
import com.xuxiaocheng.FantasyWorld.Platform.LoggerOutputStream;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkReceiveEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkSendEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus.EventBusManager;
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
import io.netty.util.concurrent.DefaultThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.function.Supplier;

public class ClientNetwork extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    protected static @NotNull HLog logger = HLog.createInstance("NetworkLogger",
            FantasyWorldPlatform.DebugMode ? Integer.MIN_VALUE : HLogLevel.DEBUG.getPriority() + 1,
            true, new LoggerOutputStream(true, false));

    protected final @NotNull SocketAddress address;
    protected final @NotNull String id;
    protected final @NotNull EventBus eventBus;
    protected final @NotNull ByteBuf readBuffer = PacketManager.allocateReadBuffer();

    protected final @NotNull EventLoopGroup group;
    protected final @NotNull Channel channel;

    public ClientNetwork(final @NotNull SocketAddress address, final @NotNull String id) {
        super(true);
        this.address = address;
        this.id = id;
        this.eventBus = EventBusManager.getInstance(id);
        this.group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new DefaultThreadFactory("Client/" + id));
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
        this.eventBus.unregister(this);
        this.channel.close().sync();
        this.group.shutdownGracefully().sync();
        this.readBuffer.release();
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull SocketAddress getAddress() {
        return this.address;
    }

    public @NotNull ChannelFuture send(final @NotNull ByteBuf buffer) {
        final int len = buffer.readableBytes();
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(4 + len, 4 + len);
        buf.writeInt(len).writeBytes(buffer);
        return this.channel.writeAndFlush(buf);
    }

    @Subscribe
    public void onSendEvent(final @NotNull NetworkSendEvent<?> event) throws IOException, InterruptedException {
        final ByteBuf buf = PacketManager.allocateWriteBuffer();
        buf.writeInt(0);
        PacketManager.toBytes(buf, event.packet());
        buf.setInt(0, buf.readableBytes() - 4);
        this.channel.writeAndFlush(buf).sync();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final @NotNull ByteBuf msg) throws IOException {
        ClientNetwork.logger.log(HLogLevel.DEBUG, "Client: channel read. length: ", msg.readableBytes());
        ClientNetwork.logger.log(HLogLevel.VERBOSE, (Supplier<byte[]>) () -> {
            final byte[] received = new byte[msg.readableBytes()];
            msg.markReaderIndex().readBytes(received).resetReaderIndex();
            return received;
        });
        this.readBuffer.writeBytes(msg);
        this.handleReadBuf();
    }

    protected void handleReadBuf() throws IOException {
        if (this.readBuffer.readableBytes() >= 4) {
            final int length = this.readBuffer.markReaderIndex().readInt();
            if (this.readBuffer.readableBytes() >= length) {
                this.eventBus.post(new NetworkReceiveEvent<>(PacketManager.fromBytes(this.readBuffer), this.channel.id()));
                this.handleReadBuf();
                return;
            }
            this.readBuffer.resetReaderIndex();
        }
        this.readBuffer.discardReadBytes();
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
