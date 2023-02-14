package com.xuxiaocheng.FantasyWorld.Platform.Network;

import com.xuxiaocheng.FantasyWorld.Platform.FantasyWorldPlatform;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.ClientActiveEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkReceiveEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Network.Events.NetworkSendEvent;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.EventBus.EventBusManager;
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
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ServerNetwork extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    protected static @NotNull HLog logger = HLog.createInstance("NetworkLogger",
            FantasyWorldPlatform.DebugMode ? Integer.MIN_VALUE : HLogLevel.DEBUG.getPriority() + 1,
            true, HLog.DefaultLogger.getWriter());

    protected final @NotNull SocketAddress address;
    protected final @NotNull String id;
    protected final @NotNull EventBus eventBus;
    protected final @NotNull Map<@NotNull ChannelId, @NotNull ByteBuf> readBuffer = new ConcurrentHashMap<>();

    protected final @NotNull EventLoopGroup bossGroup;
    protected final @NotNull EventLoopGroup workerGroup;
    protected final @NotNull ChannelFuture channelFuture;
    protected final @NotNull ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ServerNetwork(final @NotNull SocketAddress address, final @NotNull String id) {
        super(true);
        this.address = address;
        this.id = id;
        this.eventBus = EventBusManager.getInstance(id);
        this.bossGroup = new NioEventLoopGroup(Math.max(1, Runtime.getRuntime().availableProcessors() >>> 2), new DefaultThreadFactory("Server/" + id + "(Boss)"));
        this.workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1, new DefaultThreadFactory("Server/" + id + "(Worker)"));
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
        // Confirm this.readBuffer is always empty here.
        for (final ByteBuf buf: this.readBuffer.values())
            buf.release();
        this.readBuffer.clear();
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull SocketAddress getAddress() {
        return this.address;
    }

    public @NotNull ChannelGroupFuture send(final @NotNull ByteBuf buffer) {
        final int len = buffer.readableBytes();
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(4 + len, 4 + len);
        buf.writeInt(len).writeBytes(buffer);
        return this.channelGroup.writeAndFlush(buf);
    }

    @FunctionalInterface
    public interface ChannelIdMatcher {
        boolean matches(ChannelId id);
    }

    public @NotNull ChannelGroupFuture send(final @NotNull ByteBuf buffer, final @NotNull ChannelIdMatcher matcher) {
        final int len = buffer.readableBytes();
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(4 + len, 4 + len);
        buf.writeInt(len).writeBytes(buffer);
        return this.channelGroup.writeAndFlush(buf, channel -> matcher.matches(channel.id()));
    }

    public @NotNull ChannelFuture send(final @NotNull ByteBuf buffer, final @NotNull ChannelId id) {
        final int len = buffer.readableBytes();
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(4 + len, 4 + len);
        buf.writeInt(len).writeBytes(buffer);
        return this.channelGroup.find(id).writeAndFlush(buf);
    }

    @Subscribe
    public void onSendEvent(final @NotNull NetworkSendEvent<?> event) throws IOException, InterruptedException {
        final ByteBuf buf = PacketManager.allocateWriteBuffer();
        buf.writeInt(0);
        PacketManager.toBytes(buf, event.packet());
        buf.setInt(0, buf.readableBytes() - 4);
        if (event.matcher() == null)
            this.channelGroup.writeAndFlush(buf).sync();
        else
            this.channelGroup.writeAndFlush(buf, channel -> event.matcher().matches(channel.id())).sync();
    }

    @Override
    public void channelActive(final @NotNull ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        this.channelGroup.add(channel);
        ServerNetwork.logger.log(HLogLevel.FINE, "Server \"", this.id, "\": channel active: ", channel.remoteAddress(), ", id: ", channel.id());
        this.eventBus.post(new ClientActiveEvent(channel.id(), true));
        if (this.readBuffer.put(channel.id(), PacketManager.allocateReadBuffer()) != null)
            throw new IllegalStateException("Server \"" + this.id + "\": Existed channel. id: " + channel.id());
    }

    @Override
    public void channelInactive(final @NotNull ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        this.channelGroup.remove(channel);
        ServerNetwork.logger.log(HLogLevel.FINE, "Server \"", this.id, "\": channel inactive: ", channel.remoteAddress(), ", id: ", channel.id());
        this.eventBus.post(new ClientActiveEvent(channel.id(), false));
        final ByteBuf buf = this.readBuffer.remove(channel.id());
        if (buf == null)
            throw new IllegalStateException("Server \"" + this.id + "\": Channel not existed. id: " + channel.id());
        buf.release();
    }

    @Override
    protected void channelRead0(final @NotNull ChannelHandlerContext ctx, final @NotNull ByteBuf msg) throws IOException {
        final Channel channel = ctx.channel();
        ServerNetwork.logger.log(HLogLevel.DEBUG, "Server \"", this.id, "\": channel read: ", channel.remoteAddress(), ", id: ", channel.id(), ", length: ", msg.readableBytes());
        ServerNetwork.logger.log(HLogLevel.VERBOSE, (Supplier<byte[]>) () -> {
            final byte[] received = new byte[msg.readableBytes()];
            msg.markReaderIndex().readBytes(received).resetReaderIndex();
            return received;
        });
        final ByteBuf buffer = this.readBuffer.get(channel.id());
        if (buffer == null)
            throw new IllegalStateException("Server \"" + this.id + "\": Channel not existed. id: " + channel.id());
        buffer.writeBytes(msg);
        this.handleReadBuf(buffer, channel.id());
    }

    protected void handleReadBuf(final @NotNull ByteBuf buffer, final @NotNull ChannelId id) throws IOException {
        if (buffer.readableBytes() >= 4) {
            final int length = buffer.markReaderIndex().readInt();
            if (buffer.readableBytes() >= length) {
                this.eventBus.post(new NetworkReceiveEvent<>(PacketManager.fromBytes(buffer), id));
                this.handleReadBuf(buffer, id);
                return;
            }
            buffer.resetReaderIndex();
        }
        buffer.discardReadBytes();
    }

    @Override
    public void exceptionCaught(final @NotNull ChannelHandlerContext ctx, final Throwable cause) {
        final Channel channel = ctx.channel();
        ServerNetwork.logger.log(HLogLevel.ERROR, "Server \"", this.id, "\": channel throws: ", channel.remoteAddress(), ", id: ", channel.id(), cause);
        ctx.close();
    }

    @Override
    public @NotNull String toString() {
        return "ServerNetwork{" +
                "id=" + this.id +
                ", address=" + this.address +
                '}';
    }
}
