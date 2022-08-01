package Core.Addition.Element;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Announce at new mods element implement classes.
 * @author xuxiaocheng
 * @see ElementImplement
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewElementImplementCore {
    @NotNull String modName() default "Craftworld";
    @NotNull String elementName();
}
