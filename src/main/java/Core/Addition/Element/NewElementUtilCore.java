package Core.Addition.Element;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Announce at new mods element util classes.
 * @author xuxiaocheng
 * @see ElementUtil
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewElementUtilCore {
    @NotNull String modName() default "Craftworld";
    @NotNull String elementName();
}
