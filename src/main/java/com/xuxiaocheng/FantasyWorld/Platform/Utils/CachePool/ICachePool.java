package com.xuxiaocheng.FantasyWorld.Platform.Utils.CachePool;

public interface ICachePool<T> {
    T allocate();
    void recycle(final T e);
}
