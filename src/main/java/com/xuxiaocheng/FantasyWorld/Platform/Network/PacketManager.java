package com.xuxiaocheng.FantasyWorld.Platform.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.ByteBufIOUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public final class PacketManager {
    private PacketManager() {
        super();
    }

    public static final int InitPacketBufferSize = 1 << 20; // 1 MB
    public static final int MaxPacketBufferSize = 1 << 26; // 64 MB
    public static @NotNull ByteBuf allocateReadBuffer() {
        return ByteBufAllocator.DEFAULT.buffer(PacketManager.InitPacketBufferSize, PacketManager.MaxPacketBufferSize);
    }
    public static @NotNull ByteBuf allocateWriteBuffer() {
        return ByteBufAllocator.DEFAULT.buffer(PacketManager.InitPacketBufferSize, PacketManager.MaxPacketBufferSize);
    }

    private static final @NotNull Map<@NotNull Class<?>, @NotNull String> classMap = new ConcurrentHashMap<>();
    private static final @NotNull Map<@NotNull String, @NotNull PacketNode<?>> packetMap = new ConcurrentHashMap<>();

    public static <T> void registerPacket(final @NotNull String id, final @NotNull Class<T> type, final @NotNull PacketFromBytes<T> fromMethod, final @NotNull PacketToBytes<T> toMethod) {
        PacketManager.classMap.putIfAbsent(type, id);
        PacketManager.packetMap.putIfAbsent(id, new PacketNode<>(fromMethod, toMethod));
    }

    public static boolean isPacketRegistered(final @Nullable String id) {
        return PacketManager.packetMap.containsKey(id);
    }

    public static boolean isPacketRegistered(final @Nullable Class<?> type) {
        return PacketManager.packetMap.containsKey(PacketManager.classMap.get(type));
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull T fromBytes(final @NotNull ByteBuf buffer) throws IOException {
        final String id = ByteBufIOUtil.readUTF(buffer);
        final PacketNode<T> node = (PacketNode<T>) PacketManager.packetMap.get(id);
        if (node == null)
            throw new NoSuchElementException("Packet id(" + id + ") is not registered.");
        return node.fromMethod.fromBytes(buffer);
    }

    @SuppressWarnings("unchecked")
    public static <T> void toBytes(final @NotNull ByteBuf buffer, final @NotNull T packet) throws IOException {
        final String id = PacketManager.classMap.get(packet.getClass());
        if (id == null)
            throw new NoSuchElementException("Packet type(" + packet.getClass() + ") is not registered.");
        ByteBufIOUtil.writeUTF(buffer, id);
        final PacketNode<T> node = (PacketNode<T>) PacketManager.packetMap.get(id);
        node.toMethod.toBytes(buffer, packet);
    }

    @FunctionalInterface
    public interface PacketFromBytes<T> {
        T fromBytes(final @NotNull ByteBuf buffer) throws IOException;
    }

    @FunctionalInterface
    public interface PacketToBytes<T> {
        void toBytes(final @NotNull ByteBuf buffer, final @NotNull T packet) throws IOException;
    }

    protected record PacketNode<T>(@NotNull PacketFromBytes<T> fromMethod, @NotNull PacketToBytes<T> toMethod) {
    }
}
