package Core.Addition.Element;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Announce at new mods element util classes.
 * @author xuxiaocheng
 * @see ElementUtil
 */
@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewElementUtilCore {
    @NotNull String modName() default "Craftworld";
    @NotNull String elementName();
}
