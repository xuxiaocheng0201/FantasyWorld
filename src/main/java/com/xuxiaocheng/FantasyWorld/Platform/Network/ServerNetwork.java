package com.xuxiaocheng.FantasyWorld.Platform.Network;

import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketAddress;

public class ServerNetwork extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    protected final @NotNull HLog logger = HLog.getInstance("DefaultLogger");
    protected final @NotNull SocketAddress address;
    protected final @NotNull EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1);
    protected final @NotNull EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() >> 1);
    protected final @NotNull ChannelFuture channelFuture;
    protected final @NotNull ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ServerNetwork(@NotNull final SocketAddress address) {
        super();
        this.address = address;
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(ServerNetwork.this);
                    }
                });
        this.channelFuture = serverBootstrap.bind(address).syncUninterruptibly();
    }

    @Override
    public void close() throws InterruptedException {
        this.channelFuture.channel().close().sync();
        this.workerGroup.shutdownGracefully().sync();
        this.bossGroup.shutdownGracefully().sync();
    }

    public @NotNull SocketAddress getAddress() {
        return this.address;
    }

    public ChannelGroupFuture send(final byte @NotNull [] bytes, @NotNull final ChannelMatcher matcher) {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(bytes.length);
        buf.writeBytes(bytes);
        return this.channelGroup.writeAndFlush(buf, matcher);
    }

    @Override
    public void channelActive(@NotNull final ChannelHandlerContext ctx) throws IOException {
        final Channel channel = ctx.channel();
        this.channelGroup.add(channel);
        this.logger.log(HLogLevel.FINE, "Server: channel active: ", channel.remoteAddress(), ", id: ", channel.id());
    }

    @Override
    public void channelInactive(@NotNull final ChannelHandlerContext ctx) throws IOException {
        final Channel channel = ctx.channel();
        this.channelGroup.remove(channel);
        this.logger.log(HLogLevel.FINE, "Server: channel inactive: ", channel.remoteAddress(), ", id: ", channel.id());
    }

    @Override
    protected void channelRead0(@NotNull final ChannelHandlerContext ctx, final @NotNull ByteBuf msg) {
        final Channel channel = ctx.channel();
        final byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        this.logger.log(HLogLevel.DEBUG, "Server: channel read: ", channel.remoteAddress(), ", id: ", channel.id(), ", length: ", bytes.length);
        // TODO: handle bytes.
    }

    @Override
    public void exceptionCaught(final @NotNull ChannelHandlerContext ctx, final Throwable cause) {
        final Channel channel = ctx.channel();
        this.logger.log(HLogLevel.ERROR, "Server: channel throws: ", channel.remoteAddress(), ", id: ", channel.id(), cause);
        ctx.close();
    }

    @Override
    public @NotNull String toString() {
        return "ServerNetwork{" +
                "address=" + this.address +
                '}';
    }
}
