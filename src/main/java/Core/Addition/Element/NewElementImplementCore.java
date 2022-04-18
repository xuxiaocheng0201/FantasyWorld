package Core.Addition.Element;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Announce at new mods element implement classes.
 * @author xuxiaocheng
 * @see ElementImplement
 */
@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewElementImplementCore {
    @NotNull String modName() default "Craftworld";
    @NotNull String elementName();
    @NotNull String parentElements() default "";
}
