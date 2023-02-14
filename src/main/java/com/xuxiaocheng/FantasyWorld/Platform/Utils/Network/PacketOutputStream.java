package com.xuxiaocheng.FantasyWorld.Platform.Utils.Network;

import com.xuxiaocheng.HeadLibs.Annotations.Range.IntRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("NonPrivateFieldAccessedInSynchronizedContext")
public class PacketOutputStream extends OutputStream {
    protected @NotNull BufferedByteArrayOutputStream bufferedByteArrayOutputStream;

    public PacketOutputStream() {
        super();
        this.bufferedByteArrayOutputStream = new BufferedByteArrayOutputStream();
    }

    @Override
    public void close() {
    }

    @Override
    public synchronized void write(@IntRange(minimum = Byte.MIN_VALUE, maximum = Byte.MAX_VALUE) final int b) {
        this.bufferedByteArrayOutputStream.write(b);
    }

    @Override
    public synchronized void write(final byte @NotNull [] b, @IntRange(minimum = 0) final int off, @IntRange(minimum = 0) final int len) {
        this.bufferedByteArrayOutputStream.write(b, off, len);
    }

    public void writeByte(final byte b) {
        this.write(b);
    }

    public void writeByteArray(final byte @NotNull [] b) throws IOException {
        this.write(b);
    }

    public void writeBoolean(final boolean f) {
        this.write(f ? 1 : 0);
    }

    public synchronized void writeShort(final short s) {
        this.writeByte((byte) s);
        this.writeByte((byte) (s >>> 8));
    }

    public void writeVariableLenShort(final short s) {
        this.writeVariableLenInt(s);
    }

    public synchronized void writeInt(final int i) {
        this.writeByte((byte) i);
        this.writeByte((byte) (i >>> 8));
        this.writeByte((byte) (i >>> 16));
        this.writeByte((byte) (i >>> 24));
    }

    public synchronized void writeVariableLenInt(final int i) {
        int value = i;
        while (true) {
            if ((value & ~0x7F) == 0) {
                this.writeByte((byte) value);
                return;
            }
            this.writeByte((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
    }

    public synchronized void writeLong(final long l) {
        this.writeInt((int) l);
        this.writeInt((int) (l >>> 32));
    }

    public synchronized void writeVariableLenLong(final long l) {
        long value = l;
        while (true) {
            if ((value & ~0x7FL) == 0) {
                this.writeByte((byte) value);
                return;
            }
            this.writeByte((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
    }

    public void writeFloat(final float f) {
        this.writeInt(Float.floatToIntBits(f));
    }

    public void writeVariableLenFloat(final float f) {
        this.writeVariableLenInt(Float.floatToIntBits(f));
    }

    public void writeDouble(final double d) {
        this.writeLong(Double.doubleToLongBits(d));
    }

    public void writeVariableLenDouble(final double d) {
        this.writeVariableLenLong(Double.doubleToLongBits(d));
    }

    public synchronized void writeUTF(final @Nullable String s) throws IOException {
        if (s == null) {
            this.writeVariableLenInt(-1);
            return;
        }
        final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        this.writeVariableLenInt(bytes.length);
        this.writeByteArray(bytes);
    }

    public synchronized void writeSerializable(final @Nullable Serializable serializable) throws IOException {
        try (final ObjectOutput objectOutputStream = new ObjectOutputStream(this.bufferedByteArrayOutputStream)) {
            objectOutputStream.writeObject(serializable);
        }
    }

    public synchronized byte @NotNull [] toBytes() {
        return this.bufferedByteArrayOutputStream.toByteArray();
    }

    public synchronized byte @NotNull [] toBytes(@IntRange(minimum = 0) final int off, @IntRange(minimum = 0) final int len) {
        return this.bufferedByteArrayOutputStream.toByteArray(off, len);
    }

    public synchronized int getByte(@IntRange(minimum = 0) final int index) {
        return this.bufferedByteArrayOutputStream.get(index);
    }

    public synchronized void clear(final int len) {
        final BufferedByteArrayOutputStream stream = new BufferedByteArrayOutputStream();
        try {
            this.bufferedByteArrayOutputStream.writeTo(stream, len, this.bufferedByteArrayOutputStream.size() - len);
        } catch (final IOException ignored) {
        }
        this.bufferedByteArrayOutputStream = stream;
    }

    @IntRange(minimum = 0)
    public synchronized int size() {
        return this.bufferedByteArrayOutputStream.size();
    }

    public synchronized void clear() {
        this.bufferedByteArrayOutputStream.reset();
    }

    @Override
    public synchronized @NotNull String toString() {
        return "PacketOutputStream{" + this.bufferedByteArrayOutputStream + '}';
    }
}
