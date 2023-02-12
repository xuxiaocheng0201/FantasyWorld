package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.CachePool;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.CachePool.ByteArrayCachePool;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;

public class ByteArrayCachePoolTest {
    @Test
    public void allocate() {
        final ByteArrayCachePool pool = new ByteArrayCachePool(10, new LinkedBlockingQueue<>());
        final byte[] b1 = pool.allocate();
        final byte[] b2 = pool.allocate();
        //noinspection ArrayEquality
        assert b1 != b2;
    }

    @Test
    public void recycle() {
        final ByteArrayCachePool pool = new ByteArrayCachePool(10, new LinkedBlockingQueue<>());
        final byte[] b1 = pool.allocate();
        pool.recycle(b1);
        final byte[] b2 = pool.allocate();
        //noinspection ArrayEquality
        assert b1 == b2;
    }
}
