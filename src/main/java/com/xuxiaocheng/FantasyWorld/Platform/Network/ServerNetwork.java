package com.xuxiaocheng.FantasyWorld.Platform.Network;

import com.xuxiaocheng.FantasyWorld.Platform.FantasyWorldPlatform;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.ClientActiveEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkReceiveEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkSendEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus.EventBusManager;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketInputStream;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketOutputStream;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServerNetwork extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    protected static @NotNull HLog logger = HLog.createInstance("NetworkLogger",
            FantasyWorldPlatform.DebugMode ? Integer.MIN_VALUE : HLogLevel.DEBUG.getPriority() + 1,
            true, HLog.DefaultLogger.getWriter());

    protected final @NotNull SocketAddress address;
    protected final @NotNull String id;
    protected final @NotNull EventBus eventBus;
    protected final @NotNull Map<@NotNull ChannelId, @NotNull PacketOutputStream> readBuffer = new HashMap<>();

    protected final @NotNull EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1);
    protected final @NotNull EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() >>> 1);
    protected final @NotNull ChannelFuture channelFuture;
    protected final @NotNull ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ServerNetwork(final @NotNull SocketAddress address, final @NotNull String id) {
        super(true);
        this.address = address;
        this.id = id;
        this.eventBus = EventBusManager.getInstance(id);
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(this.bossGroup, this.workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(ServerNetwork.this);
                    }
                });
        this.channelFuture = serverBootstrap.bind(address).syncUninterruptibly();
        this.eventBus.register(this);
    }

    @Override
    public void close() throws InterruptedException {
        this.channelFuture.channel().close().sync();
        this.workerGroup.shutdownGracefully().sync();
        this.bossGroup.shutdownGracefully().sync();
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull SocketAddress getAddress() {
        return this.address;
    }

    public @NotNull ChannelGroupFuture send(final byte @NotNull [] bytes, final @NotNull ChannelIdMatcher matcher) {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(4 + bytes.length).writeInt(bytes.length).writeBytes(bytes);
        return this.channelGroup.writeAndFlush(buf, channel -> matcher.matches(channel.id()));
    }

    public @NotNull ChannelFuture send(final byte @NotNull [] bytes, final @NotNull ChannelId id) {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(4 + bytes.length).writeInt(bytes.length).writeBytes(bytes);
        return this.channelGroup.find(id).writeAndFlush(buf);
    }

    @FunctionalInterface
    public interface ChannelIdMatcher {
        boolean matches(ChannelId id);
    }

    @Subscribe
    public void onSendEvent(final @NotNull NetworkSendEvent event) throws InterruptedException {
        this.send(event.stream().toBytes(), event.matcher()).sync();
    }

    @Override
    public void channelActive(final @NotNull ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        this.channelGroup.add(channel);
        ServerNetwork.logger.log(HLogLevel.FINE, "Server \"", this.id, "\": channel active: ", channel.remoteAddress(), ", id: ", channel.id());
        this.eventBus.post(new ClientActiveEvent(channel.id(), true));
    }

    @Override
    public void channelInactive(final @NotNull ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        this.channelGroup.remove(channel);
        ServerNetwork.logger.log(HLogLevel.FINE, "Server \"", this.id, "\": channel inactive: ", channel.remoteAddress(), ", id: ", channel.id());
        this.eventBus.post(new ClientActiveEvent(channel.id(), false));
    }

    @Override
    public void exceptionCaught(final @NotNull ChannelHandlerContext ctx, final Throwable cause) {
        final Channel channel = ctx.channel();
        ServerNetwork.logger.log(HLogLevel.ERROR, "Server \"", this.id, "\": channel throws: ", channel.remoteAddress(), ", id: ", channel.id(), cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(final @NotNull ChannelHandlerContext ctx, final @NotNull ByteBuf msg) throws IOException {
        final Channel channel = ctx.channel();
        final byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        ServerNetwork.logger.log(HLogLevel.DEBUG, "Server \"", this.id, "\": channel read: ", channel.remoteAddress(), ", id: ", channel.id(), ", length: ", bytes.length);
        ServerNetwork.logger.log(HLogLevel.VERBOSE, "Read bytes: ", bytes);
        PacketOutputStream stream;
        synchronized (this.readBuffer) {
            stream = this.readBuffer.get(channel.id());
            if (stream == null) {
                stream = new PacketOutputStream();
                this.readBuffer.put(channel.id(), stream);
            }
        }
        stream.writeByteArray(bytes);
        if (stream.size() >= 4) {
            final ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(4, 4);
            final int length = buf
                    .writeByte(stream.getByte(0)).writeByte(stream.getByte(1))
                    .writeByte(stream.getByte(2)).writeByte(stream.getByte(3))
                    .readInt();
            buf.release();
            if (stream.size() - 4 >= length) {
                this.eventBus.post(new NetworkReceiveEvent(PacketManager.handleInput(new PacketInputStream(stream.toBytes(4, length))), channel.id()));
                stream.clear(length + 4);
            }
        }
    }

    @Override
    public @NotNull String toString() {
        return "ServerNetwork{" +
                "id=" + this.id +
                ", address=" + this.address +
                '}';
    }
}
