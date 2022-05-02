package Core.EventBus;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auto register the instance of a class announced {@code EventSubscribe}.
 * @see EventBusManager#getEventBusByName(String) 
 * @see HeadLibs.Helper.HClassHelper#getInstance(Class)
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribe {
    /**
     * The EventBus name to register.
     */
    @NotNull String eventBus() default "default";
}
