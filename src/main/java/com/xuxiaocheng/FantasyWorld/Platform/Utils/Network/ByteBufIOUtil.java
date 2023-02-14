package com.xuxiaocheng.FantasyWorld.Platform.Utils.Network;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public final class ByteBufIOUtil {

    public static final byte[] EmptyByteArray = new byte[0];

    private ByteBufIOUtil() {
        super();
    }

    public static byte readByte(final @NotNull ByteBuf buffer) throws IOException {
        try {
            return buffer.readByte();
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static byte @NotNull [] readByteArray(final @NotNull ByteBuf buffer) throws IOException {
        final int length = ByteBufIOUtil.readVariableLenInt(buffer);
        if (length <= 0)
            return ByteBufIOUtil.EmptyByteArray;
        final byte[] bytes = new byte[length];
        try {
            buffer.readBytes(bytes);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
        return bytes;
    }

    public static boolean readBoolean(final @NotNull ByteBuf buffer) throws IOException {
        try {
            return buffer.readBoolean();
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static short readShort(final @NotNull ByteBuf buffer) throws IOException {
        try {
            return buffer.readShort();
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static short readVariableLenShort(final @NotNull ByteBuf buffer) throws IOException {
        try {
            return (short) ByteBufIOUtil.readVariableLenInt(buffer);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static int readInt(final @NotNull ByteBuf buffer) throws IOException {
        try {
            return buffer.readInt();
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static int readVariableLenInt(final @NotNull ByteBuf buffer) throws IOException {
        int value = 0;
        int position = 0;
        while (true) {
            final byte current = ByteBufIOUtil.readByte(buffer);
            value |= (current & 0x7F) << position;
            if ((current & 0x80) == 0)
                break;
            position += 7;
            if (position >= 32)
                throw new IOException("Int in stream is too big.");
        }
        return value;
    }

    public static long readLong(final @NotNull ByteBuf buffer) throws IOException {
        try {
            return buffer.readLong();
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static long readVariableLenLong(final @NotNull ByteBuf buffer) throws IOException {
        long value = 0;
        int position = 0;
        while (true) {
            final byte current = ByteBufIOUtil.readByte(buffer);
            value |= (long) (current & 0x7F) << position;
            if ((current & 0x80) == 0)
                break;
            position += 7;
            if (position >= 64)
                throw new IOException("Long in stream is too big.");
        }
        return value;
    }

    public static float readFloat(final @NotNull ByteBuf buffer) throws IOException {
        return Float.intBitsToFloat(ByteBufIOUtil.readInt(buffer));
    }

    public static float readVariableLenFloat(final @NotNull ByteBuf buffer) throws IOException {
        return Float.intBitsToFloat(ByteBufIOUtil.readVariableLenInt(buffer));
    }

    public static double readDouble(final @NotNull ByteBuf buffer) throws IOException {
        return Double.longBitsToDouble(ByteBufIOUtil.readLong(buffer));
    }

    public static double readVariableLenDouble(final @NotNull ByteBuf buffer) throws IOException {
        return Double.longBitsToDouble(ByteBufIOUtil.readVariableLenLong(buffer));
    }

    public static @NotNull String readUTF(final @NotNull ByteBuf buffer) throws IOException {
        return new String(ByteBufIOUtil.readByteArray(buffer), StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    public static @Nullable <T extends Serializable> T readSerializable(final @NotNull ByteBuf buffer) throws IOException {
        final byte[] bytes = ByteBufIOUtil.readByteArray(buffer);
        if (bytes.length == 0)
            return null;
        try (final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) objectInputStream.readObject();
        } catch (final ClassNotFoundException | ClassCastException exception) {
            throw new IOException(exception);
        }
    }


    public static void writeByte(final @NotNull ByteBuf buffer, final byte b) throws IOException {
        try {
            buffer.writeByte(b);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static void writeByteArray(final @NotNull ByteBuf buffer, final byte @NotNull [] b) throws IOException {
        try {
            ByteBufIOUtil.writeVariableLenInt(buffer, b.length);
            buffer.writeBytes(b);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static void writeBoolean(final @NotNull ByteBuf buffer, final boolean f) throws IOException {
        try {
            buffer.writeBoolean(f);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static void writeShort(final @NotNull ByteBuf buffer, final short s) throws IOException {
        try {
            buffer.writeShort(s);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static void writeVariableLenShort(final @NotNull ByteBuf buffer, final short s) throws IOException {
        ByteBufIOUtil.writeVariableLenInt(buffer, s);
    }

    public static void writeInt(final @NotNull ByteBuf buffer, final int i) throws IOException {
        try {
            buffer.writeInt(i);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static void writeVariableLenInt(final @NotNull ByteBuf buffer, final int i) throws IOException {
        int value = i;
        while (true) {
            if ((value & ~0x7F) == 0) {
                ByteBufIOUtil.writeByte(buffer, (byte) value);
                return;
            }
            ByteBufIOUtil.writeByte(buffer, (byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
    }

    public static void writeLong(final @NotNull ByteBuf buffer, final long l) throws IOException {
        try {
            buffer.writeLong(l);
        } catch (final IndexOutOfBoundsException exception) {
            throw new IOException(exception);
        }
    }

    public static void writeVariableLenLong(final @NotNull ByteBuf buffer, final long l) throws IOException {
        long value = l;
        while (true) {
            if ((value & ~0x7FL) == 0) {
                ByteBufIOUtil.writeByte(buffer, (byte) value);
                return;
            }
            ByteBufIOUtil.writeByte(buffer, (byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
    }

    public static void writeFloat(final @NotNull ByteBuf buffer, final float f) throws IOException {
        ByteBufIOUtil.writeInt(buffer, Float.floatToIntBits(f));
    }

    public static void writeVariableLenFloat(final @NotNull ByteBuf buffer, final float f) throws IOException {
        ByteBufIOUtil.writeVariableLenInt(buffer, Float.floatToIntBits(f));
    }

    public static void writeDouble(final @NotNull ByteBuf buffer, final double d) throws IOException {
        ByteBufIOUtil.writeLong(buffer, Double.doubleToLongBits(d));
    }

    public static void writeVariableLenDouble(final @NotNull ByteBuf buffer, final double d) throws IOException {
        ByteBufIOUtil.writeVariableLenLong(buffer, Double.doubleToLongBits(d));
    }

    public static void writeUTF(final @NotNull ByteBuf buffer, final @NotNull String s) throws IOException {
        ByteBufIOUtil.writeByteArray(buffer, s.getBytes(StandardCharsets.UTF_8));
    }

    public static void writeSerializable(final @NotNull ByteBuf buffer, final @Nullable Serializable serializable) throws IOException {
        if (serializable == null) {
            ByteBufIOUtil.writeVariableLenInt(buffer, -1);
            return;
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final ObjectOutput objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(outputStream))) {
            objectOutputStream.writeObject(serializable);
        }
        ByteBufIOUtil.writeByteArray(buffer, outputStream.toByteArray());
    }
}
