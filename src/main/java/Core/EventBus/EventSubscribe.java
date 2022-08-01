package Core.EventBus;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Auto register the instance of a class announced {@code EventSubscribe}.
 * @see EventBusManager#getEventBusByName(String) 
 * @see HeadLibs.Helper.HClassHelper#getInstance(Class)
 * @author xuxiaocheng
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribe {
    /**
     * The EventBus name to register.
     */
    @NotNull String eventBus() default "default";

    /**
     * Register to eventbus before mod initialization.
     * @see EventBusManager#register(Class)
     */
    boolean autoRegister() default true;
}
