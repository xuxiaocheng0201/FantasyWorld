package Core.Addition.Mod;

import Core.Craftworld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Announce at new mods main classes.
 * @author xuxiaocheng
 * @see ModImplement
 */
@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewMod {
    @Nullable String name();
    @NotNull String version() default "1.0";
    @NotNull String availableCraftworldVersion() default Craftworld.CURRENT_VERSION_STRING;
    @NotNull String requirements() default "";
}
