package com.xuxiaocheng.FantasyWorld.Platform.Network;

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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketAddress;

public class ClientNetwork extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    protected final @NotNull HLog logger = HLog.getInstance("DefaultLogger");
    protected final @NotNull SocketAddress address;
    protected final @NotNull PacketOutputStream readBuffer = new PacketOutputStream();
    protected final @NotNull EventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    protected final @NotNull Channel channel;

    public ClientNetwork(@NotNull final SocketAddress address) throws IOException, InterruptedException {
        super();
        this.address = address;
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(this.group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(ClientNetwork.this);
                    }
                });
        this.channel = bootstrap.connect(address).sync().channel();
    }

    @Override
    public void close() throws InterruptedException {
        this.channel.close().sync();
        this.group.shutdownGracefully().sync();
    }

    public @NotNull SocketAddress getAddress() {
        return this.address;
    }

    public @NotNull ChannelFuture send(final byte @NotNull [] bytes) throws IOException {
        final ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(bytes.length);
        buf.writeBytes(bytes);
        return this.channel.writeAndFlush(buf);
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final @NotNull ByteBuf msg) {
        final byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        this.logger.log(HLogLevel.DEBUG, "Client: channel read. length: ", bytes.length);
        // TODO: handle bytes.
    }

    @Override
    public void exceptionCaught(final @NotNull ChannelHandlerContext ctx, final Throwable cause) {
        this.logger.log(HLogLevel.ERROR, "Client: channel throws.", cause);
        ctx.close();
    }

    @Override
    public @NotNull String toString() {
        return "ClientNetwork{" +
                "address=" + this.address +
                "}";
    }
}
