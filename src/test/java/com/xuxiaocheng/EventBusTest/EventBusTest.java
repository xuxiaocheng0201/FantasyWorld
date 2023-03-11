package com.xuxiaocheng.EventBusTest;

import com.google.common.collect.Multimap;
import com.xuxiaocheng.EventBus.EventBus;
import com.xuxiaocheng.EventBus.Subscribe;
import com.xuxiaocheng.HeadLibs.Logger.HLog;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import com.xuxiaocheng.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

/*
 * WARNING: Should check by debugger.
 */
public class EventBusTest {
    @Test
    public void test() {
        final EventBus eventBus = EventBus.builder().build();
        eventBus.register(this);
        eventBus.post(this);
        eventBus.unregister(this);
        assert ((Multimap<?, ?>) TestUtil.getField(EventBus.class, "methodSubscriberMap", eventBus)).isEmpty();
        assert ((Multimap<?, ?>) TestUtil.getField(EventBus.class, "eventMethodMap", eventBus)).isEmpty();
    }

    @Test
    public void mulTest() {
        final EventBus eventBus = EventBus.builder().build();
        eventBus.register(this);
        eventBus.postSticky(this);
        eventBus.register(this);
        eventBus.unregister(this);
        eventBus.post(new Object());
        eventBus.unregister(this);
        assert ((Multimap<?, ?>) TestUtil.getField(EventBus.class, "methodSubscriberMap", eventBus)).isEmpty();
        assert ((Multimap<?, ?>) TestUtil.getField(EventBus.class, "eventMethodMap", eventBus)).isEmpty();
    }

    @Subscribe
    public void obj(final @NotNull Object o) {
        HLog.DefaultLogger.log(HLogLevel.FINE, o);
    }

    @Subscribe(priority = 1)
    public void ebt(final @NotNull EventBusTest o) {
        HLog.DefaultLogger.log(HLogLevel.FINE, o);
    }

    @Subscribe(threadMode = Subscribe.ThreadMode.ASYNC)
    public void exes(final @NotNull Object o) {
        HLog.DefaultLogger.log(HLogLevel.FINE, o);
    }
}
