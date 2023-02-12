package com.xuxiaocheng.FantasyWorld.Platform.Utils.Network;

import com.xuxiaocheng.HeadLibs.Annotations.Range.IntRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class PacketInputStream extends InputStream {
    protected final @NotNull ByteArrayInputStream byteArrayInputStream;

    public PacketInputStream(final byte @NotNull [] b) {
        super();
        this.byteArrayInputStream = new ByteArrayInputStream(b);
    }

    public PacketInputStream(final byte @NotNull [] b, @IntRange(minimum = 0) final int off, @IntRange(minimum = 0) final int len) {
        super();
        this.byteArrayInputStream = new ByteArrayInputStream(b, off, len);
    }

    @Override
    public void close() throws IOException {
        this.byteArrayInputStream.close();
    }

    @Override
    @IntRange(minimum = Byte.MIN_VALUE, maximum = Byte.MAX_VALUE)
    public int read() {
        return this.byteArrayInputStream.read();
    }

    @Override
    @IntRange(minimum = 0)
    public int read(final byte @NotNull [] b, @IntRange(minimum = 0) final int off, @IntRange(minimum = 0) final int len) {
        return this.byteArrayInputStream.read(b, off, len);
    }

    public byte readByte() {
        return (byte) this.read();
    }

    @IntRange(minimum = 0)
    public int readByteArray(final byte @NotNull [] b) throws IOException {
        return this.read(b);
    }

    public boolean readBoolean() throws IOException {
        final byte b = this.readByte();
        if (b == 1)
            return true;
        if (b == 0)
            return false;
        throw new IOException("Boolean in stream is invalid.");
    }

    public short readShort() {
        return (short) ((this.readByte() & 0x0FF) | (this.readByte() << 8));
    }

    public short readVariableLenShort() throws IOException {
        return (short) this.readVariableLenInt();
    }

    public int readInt() {
        return ((this.readByte() & 0x0FF)
                | ((this.readByte() << 8) & 0x0FF00)
                | ((this.readByte() << 16) & 0x0FF0000)
                | (this.readByte() << 24));
    }

    public int readVariableLenInt() throws IOException {
        int value = 0;
        int position = 0;
        while (true) {
            final byte current = this.readByte();
            value |= (current & 0x7F) << position;
            if ((current & 0x80) == 0)
                break;
            position += 7;
            if (position >= 32)
                throw new IOException("Int in stream is too big.");
        }
        return value;
    }

    public long readLong() {
        return ((this.readInt() & 0x0FFFFFFFFL) | ((long) this.readInt() << 32));
    }

    public long readVariableLenLong() throws IOException {
        long value = 0;
        int position = 0;
        while (true) {
            final byte current = this.readByte();
            value |= (long) (current & 0x7F) << position;
            if ((current & 0x80) == 0)
                break;
            position += 7;
            if (position >= 64)
                throw new IOException("Long in stream is too big.");
        }
        return value;
    }

    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    public float readVariableLenFloat() throws IOException {
        return Float.intBitsToFloat(this.readVariableLenInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }

    public double readVariableLenDouble() throws IOException {
        return Double.longBitsToDouble(this.readVariableLenLong());
    }

    public @Nullable String readUTF() throws IOException {
        final int length = this.readVariableLenInt();
        if (length < 0)
            return null;
        final byte[] bytes = new byte[length];
        if (this.readByteArray(bytes) != length)
            throw new IOException("String in stream has different length.");
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T extends Serializable> T readSerializable() throws IOException {
        try (final ObjectInputStream objectInputStream = new ObjectInputStream(this.byteArrayInputStream)) {
            return (T) objectInputStream.readObject();
        } catch (final ClassNotFoundException | ClassCastException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    public @NotNull String toString() {
        return "PacketInputStream{" + this.byteArrayInputStream + '}';
    }
}
