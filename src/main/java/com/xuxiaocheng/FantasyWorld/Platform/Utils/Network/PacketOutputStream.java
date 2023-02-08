package com.xuxiaocheng.FantasyWorld.Platform.Utils.Network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class PacketOutputStream extends OutputStream {
    protected final @NotNull ByteArrayOutputStream byteArrayOutputStream;
    protected final @NotNull BufferedOutputStream bufferedOutputStream;

    public PacketOutputStream() {
        super();
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.bufferedOutputStream = new BufferedOutputStream(this.byteArrayOutputStream);
    }

    public PacketOutputStream(final int size) {
        super();
        this.byteArrayOutputStream = new ByteArrayOutputStream(size);
        this.bufferedOutputStream = new BufferedOutputStream(this.byteArrayOutputStream);
    }

    @Override
    public void close() throws IOException {
        this.bufferedOutputStream.close();
    }

    @Override
    public void write(final int b) throws IOException {
        this.bufferedOutputStream.write(b);
    }

    @Override
    public void write(final byte @NotNull [] b, final int off, final int len) throws IOException {
        this.bufferedOutputStream.write(b, off, len);
    }

    public void writeByte(final byte b) throws IOException {
        this.write(b);
    }

    public void writeByteArray(final byte[] b) throws IOException {
        this.write(b);
    }

    public void writeBoolean(final boolean f) throws IOException {
        this.write(f ? 1 : 0);
    }

    public void writeShort(final short s) throws IOException {
        this.writeByte((byte) s);
        this.writeByte((byte) (s >>> 8));
    }

    public void writeVariableLenShort(final short s) throws IOException {
        this.writeVariableLenInt(s);
    }

    public void writeInt(final int i) throws IOException {
        this.writeByte((byte) i);
        this.writeByte((byte) (i >>> 8));
        this.writeByte((byte) (i >>> 16));
        this.writeByte((byte) (i >>> 24));
    }

    public void writeVariableLenInt(final int i) throws IOException {
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

    public void writeLong(final long l) throws IOException {
        this.writeInt((int) l);
        this.writeInt((int) (l >>> 32));
    }

    public void writeVariableLenLong(final long l) throws IOException {
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

    public void writeFloat(final float f) throws IOException {
        this.writeInt(Float.floatToIntBits(f));
    }

    public void writeDouble(final double d) throws IOException {
        this.writeLong(Double.doubleToLongBits(d));
    }

    public void writeUTF(final @Nullable String s) throws IOException {
        if (s == null) {
            this.writeVariableLenInt(-1);
            return;
        }
        final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        this.writeVariableLenInt(bytes.length);
        this.writeByteArray(bytes);
    }

    public void writeSerializable(final @Nullable Serializable serializable) throws IOException {
        try (final ObjectOutput objectOutputStream = new ObjectOutputStream(this.bufferedOutputStream)) {
            objectOutputStream.writeObject(serializable);
        }
    }

    public byte[] toBytes() throws IOException {
        this.bufferedOutputStream.flush();
        return this.byteArrayOutputStream.toByteArray();
    }

    public void clear() throws IOException {
        this.bufferedOutputStream.flush();
        this.byteArrayOutputStream.reset();
    }

    @Override
    public @NotNull String toString() {
        return "PacketOutputStream{" + this.bufferedOutputStream + '}';
    }
}
