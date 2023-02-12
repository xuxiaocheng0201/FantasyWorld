package com.xuxiaocheng.FantasyWorld.Platform.Utils.CachePool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICachePool<T> {
    @NotNull T allocate();
    void recycle(final @Nullable T e);
}
