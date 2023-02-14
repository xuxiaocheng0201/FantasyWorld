package com.xuxiaocheng.FantasyWorld.Platform.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketInputStream;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Network.PacketOutputStream;
import com.xuxiaocheng.HeadLibs.DataStructures.Pair;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public final class PacketManager {
    private PacketManager() {
        super();
    }

    private static final @NotNull Map<@NotNull Class<?>, Pair.@NotNull ImmutablePair<@NotNull PacketFromBytes<?>, @NotNull PacketToBytes>> map = new ConcurrentHashMap<>();

    public static <T> void registerPacket(final @NotNull Class<T> id, final @NotNull PacketFromBytes<T> fromMethod, final @NotNull PacketToBytes toMethod) {
        if (PacketManager.map.containsKey(id))
            return;
        PacketManager.map.put(id, new Pair.ImmutablePair<>(fromMethod, toMethod));
    }

    public static boolean isPacketRegistered(final @NotNull Class<?> id) {
        return PacketManager.map.containsKey(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull PacketFromBytes<T> getFromMethod(final @NotNull Class<T> id) {
        final Pair.ImmutablePair<PacketFromBytes<?>, PacketToBytes> pair = PacketManager.map.get(id);
        if (pair == null)
            throw new NoSuchElementException("Packet " + id + " is not registered.");
        return (PacketFromBytes<T>) pair.getFirst();
    }

    public static <T> @NotNull PacketToBytes getToMethod(final @NotNull Class<T> id) {
        final Pair.ImmutablePair<PacketFromBytes<?>, PacketToBytes> pair = PacketManager.map.get(id);
        if (pair == null)
            throw new NoSuchElementException("Packet " + id + " is not registered.");
        return pair.getSecond();
    }

    @FunctionalInterface
    public interface PacketFromBytes<T> {
        T fromBytes(final @NotNull PacketInputStream inputStream);
    }

    @FunctionalInterface
    public interface PacketToBytes {
        byte @NotNull [] toBytes(final @NotNull PacketOutputStream outputStream);
    }

    public static <T> @NotNull T handleInput(final @NotNull PacketInputStream inputStream) throws IOException {
        HLog.DefaultLogger.log("DEBUG", inputStream.readUTF());
//        inputStream.readSerializable();
        return (T) inputStream;
    }
}
