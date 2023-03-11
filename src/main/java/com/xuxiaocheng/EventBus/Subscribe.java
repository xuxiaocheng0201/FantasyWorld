package com.xuxiaocheng.EventBus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.SYNC;
    boolean sticky() default false;
    int priority() default 0;

    enum ThreadMode {
        SYNC, ASYNC
    }
}
