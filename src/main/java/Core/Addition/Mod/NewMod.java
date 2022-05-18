package Core.Addition.Mod;

import Core.Craftworld;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Announce at new mods main classes.
 * @author xuxiaocheng
 * @see ModImplement
 */
@SuppressWarnings("unused")
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewMod {
    @NotNull String name();
    @NotNull String version() default "1.0";
    @NotNull String availableCraftworldVersion() default Craftworld.CURRENT_VERSION_STRING;
    @NotNull String requirements() default "";
}
