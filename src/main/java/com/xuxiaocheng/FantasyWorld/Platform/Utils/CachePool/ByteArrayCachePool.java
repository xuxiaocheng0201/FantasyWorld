package com.xuxiaocheng.FantasyWorld.Platform.Utils.CachePool;

import com.xuxiaocheng.HeadLibs.Annotations.Range.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ByteArrayCachePool implements ICachePool<byte[]> {
    protected final BlockingQueue<byte[]> queue;
    protected final int arrayLength;

    public ByteArrayCachePool(@IntRange(minimum = 0) final int arrayLength, @IntRange(minimum = 0) final int capacity) {
        super();
        this.arrayLength = arrayLength;
        this.queue = new LinkedBlockingDeque<>(capacity);
    }

    public ByteArrayCachePool(@IntRange(minimum = 0) final int arrayLength, final @NotNull BlockingQueue<byte[]> queue) {
        super();
        this.arrayLength = arrayLength;
        this.queue = queue;
    }

    @IntRange(minimum = 0)
    public int getArrayLength() {
        return this.arrayLength;
    }

    @Override
    public byte @NotNull [] allocate() {
        final byte[] bytes = this.queue.poll();
        if (bytes != null)
            return bytes;
        return new byte[this.arrayLength];
    }

    @Override
    public void recycle(final byte[] e) {
        if (e == null || e.length != this.arrayLength)
            return;
        //noinspection ResultOfMethodCallIgnored
        this.queue.offer(e);
    }

    @Override
    public @NotNull String toString() {
        return "ByteArrayCachePool{" + this.hashCode() + '}';
    }
}
