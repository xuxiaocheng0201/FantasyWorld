package com.xuxiaocheng.FantasyWorld.Platform.Utils.Network;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.CachePool.ByteArrayCachePool;
import com.xuxiaocheng.HeadLibs.Annotations.Range.IntRange;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"NonPrivateFieldAccessedInSynchronizedContext", "AccessToStaticFieldLockedOnInstance"})
public class BufferedByteArrayOutputStream extends ByteArrayOutputStream {
    @IntRange(minimum = 0)
    public static final int MaxLengthPerByteArrayNBit = 2;
    private static final int MaxLengthPerByteArray = 1 << BufferedByteArrayOutputStream.MaxLengthPerByteArrayNBit;
    protected static final ByteArrayCachePool cachePool = new ByteArrayCachePool(BufferedByteArrayOutputStream.MaxLengthPerByteArray, 1024); // Max: 1M

    protected final List<byte[]> outputted = new ArrayList<>();

    public BufferedByteArrayOutputStream() {
        super(BufferedByteArrayOutputStream.MaxLengthPerByteArray);
    }

    @Override
    public synchronized void write(final int b) {
        this.buf[this.count] = (byte) b;
        this.count += 1;
        if (this.count >= BufferedByteArrayOutputStream.MaxLengthPerByteArray) {
            this.outputted.add(this.buf);
            this.buf = BufferedByteArrayOutputStream.cachePool.allocate();
            this.count = 0;
        }
    }

    @Override
    public synchronized void write(final byte[] b, final int off, final int len) {
        Objects.checkFromIndexSize(off, len, b.length);
        int copiedLen = Math.min(BufferedByteArrayOutputStream.MaxLengthPerByteArray - this.count, len);
        System.arraycopy(b, off, this.buf, this.count, copiedLen);
        this.count += copiedLen;
        int leftLen = len - copiedLen;
        while (leftLen > 0) {
            this.outputted.add(this.buf);
            this.buf = BufferedByteArrayOutputStream.cachePool.allocate();
            this.count = 0;
            final int copyLen = Math.min(BufferedByteArrayOutputStream.MaxLengthPerByteArray, leftLen);
            System.arraycopy(b, off + copiedLen, this.buf, 0, copyLen);
            this.count += copyLen;
            copiedLen += copyLen;
            leftLen -= copyLen;
        }
        if (this.count >= BufferedByteArrayOutputStream.MaxLengthPerByteArray) {
            this.outputted.add(this.buf);
            this.buf = BufferedByteArrayOutputStream.cachePool.allocate();
            this.count = 0;
        }
    }

    @Override
    public synchronized void writeTo(final OutputStream out) throws IOException {
        for (final byte[] bytes: this.outputted)
            out.write(bytes);
        out.write(this.buf, 0, this.count);
    }

    @Override
    public synchronized void reset() {
        for (final byte[] bytes: this.outputted)
            BufferedByteArrayOutputStream.cachePool.recycle(bytes);
        this.outputted.clear();
        this.count = 0;
    }

    @Override
    public synchronized byte @NotNull [] toByteArray() {
        final byte[] bytes = new byte[this.size()];
        int c = 0;
        for (final byte[] b: this.outputted) {
            System.arraycopy(b, 0, bytes, c, b.length);
            c += b.length;
        }
        System.arraycopy(this.buf, 0, bytes, c, this.count);
        return bytes;
    }

    public synchronized byte @NotNull [] toByteArray(final int off, final int len) {
        Objects.checkFromIndexSize(off, len, this.size());
        final byte[] bytes = new byte[len];
        int m = off >>> BufferedByteArrayOutputStream.MaxLengthPerByteArrayNBit;
        int n = off & (BufferedByteArrayOutputStream.MaxLengthPerByteArray - 1);
        int leftLen = len;
        while (leftLen > 0) {
            if (m == this.outputted.size()) {
                System.arraycopy(this.buf, n, bytes, 0, leftLen);
                return bytes;
            }
            final int copiedLen = Math.min(BufferedByteArrayOutputStream.MaxLengthPerByteArray - n, leftLen);
            System.arraycopy(this.outputted.get(m), n, bytes, len - leftLen, copiedLen);
            ++m;
            n = 0;
            leftLen -= copiedLen;
        }
        return bytes;
    }

    public synchronized byte get(final int index) {
        final int m = index >>> BufferedByteArrayOutputStream.MaxLengthPerByteArrayNBit;
        final int n = index & (BufferedByteArrayOutputStream.MaxLengthPerByteArray - 1);
        if (m < this.outputted.size())
            return this.outputted.get(m)[n];
        if (m == this.outputted.size() && n < this.count)
            return this.buf[n];
        throw new IndexOutOfBoundsException(index);
    }

    @Override
    public synchronized int size() {
        return (this.outputted.size() << BufferedByteArrayOutputStream.MaxLengthPerByteArrayNBit) + this.count;
    }

    @Override
    public synchronized @NotNull String toString() {
        return "BufferedByteArrayOutputStream{" +
                "size=" + this.size() +
                '}';
    }

    @Override
    @Deprecated
    public final synchronized String toString(final Charset charset) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final synchronized String toString(final String charsetName) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public final synchronized String toString(final int hiByte) {
        throw new UnsupportedOperationException();
    }
}
