package CraftWorld.Network;

import CraftWorld.ConstantStorage;
import CraftWorld.Network.Packet.PacketProcessor;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Set;

public class NetworkServer {
    private static final @NotNull HLog logger = new HLog("NetworkServer", Thread.currentThread().getName());

    protected final @NotNull SocketAddress socketAddress;
    protected @NotNull String address = "";
    protected int port;
    protected @NotNull Selector selector;
    protected final @NotNull ServerSocketChannel serverSocketChannel;
    protected final @NotNull ServerSocket serverSocket;

    public NetworkServer(@NotNull SocketAddress socketAddress) throws IOException {
        super();
        ProtocolFamily protocolFamily = null;
        //noinspection ChainOfInstanceofChecks
        if (socketAddress instanceof InetSocketAddress inetSocketAddress) {
            this.address = inetSocketAddress.getHostString();
            this.port = inetSocketAddress.getPort();
            protocolFamily = inetSocketAddress.getAddress().getAddress().length == 4 ? StandardProtocolFamily.INET : StandardProtocolFamily.INET6;
        }
        if (socketAddress instanceof UnixDomainSocketAddress unixDomainSocketAddress) {
            this.address = "unix://" + unixDomainSocketAddress.getPath();
            this.port = 0;
            protocolFamily = StandardProtocolFamily.UNIX;
        }
        if (protocolFamily == null)
            throw new IllegalArgumentException("Address must be an InetSocketAddress or a UnixDomainSocketAddress");
        this.socketAddress = socketAddress;
        this.serverSocketChannel = ServerSocketChannel.open(protocolFamily);
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocket = this.serverSocketChannel.socket();
        this.serverSocket.bind(socketAddress);
        this.selector = Selector.open();
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    public @NotNull String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public @NotNull SocketAddress getSocketAddress() {
        return this.socketAddress;
    }

    protected volatile boolean stop;
    protected final Thread networkServerListenThread = new Thread(() -> {
        Thread.currentThread().setUncaughtExceptionHandler((thread, error) -> {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, error);
            this.stop();
        });
        while (!this.stop)
            try {
                if (this.selector.select() == 0)
                    continue;
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    this.handleKey(selectionKey);
                }
            } catch (IOException exception) {
                logger.log(HLogLevel.WARN, "Network server selector failed to select.", exception);
            }
    });

    public void start() {
        this.stop = false;
        this.networkServerListenThread.start();
    }

    public void stop() {
        this.stop = true;
        try {
            this.serverSocketChannel.close();
        } catch (IOException exception) {
            logger.log(HLogLevel.WARN, "Failed to close server socket.", exception);
        }
        if (this.socketAddress instanceof UnixDomainSocketAddress unixDomainSocketAddress)
            try {
                Files.deleteIfExists(unixDomainSocketAddress.getPath());
            } catch (IOException exception) {
                logger.log(HLogLevel.WARN, "Failed to delete file.", exception);
            }
        this.selector.wakeup();
    }

    protected final @NotNull ByteBuffer sendBuffer = ByteBuffer.allocate(ConstantStorage.NETWORK_BYTEBUFFER_SIZE);
    protected final @NotNull ByteBuffer receiveBuffer = ByteBuffer.allocate(ConstantStorage.NETWORK_BYTEBUFFER_SIZE);

    protected void handleKey(@Nullable SelectionKey selectionKey) throws IOException {
        if (selectionKey == null)
            return;
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(this.selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            SocketChannel client = (SocketChannel) selectionKey.channel();
            this.receiveBuffer.clear();
            int count = client.read(this.receiveBuffer);
            if (count > 0) {
                //TODO handle receive buffer.
                //String receiveText = new String(this.receiveBuffer.array(), 0, count);
                client.register(this.selector, SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isWritable()) {
            SocketChannel client = (SocketChannel) selectionKey.channel();
            this.sendBuffer.clear();
            byte[] bytes = PacketProcessor.getSendBytes();
            if (bytes == null)
                return;
            this.sendBuffer.put(bytes);
            this.sendBuffer.flip();
            client.write(this.sendBuffer);
            client.register(this.selector, SelectionKey.OP_READ);
        }
    }
}
