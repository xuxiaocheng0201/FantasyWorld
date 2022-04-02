package Core.Mod.New;

import Core.Craftworld;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewMod {
    String name();
    String version() default "1.0";
    String require() default "";
    String availableCraftworldVersion() default Craftworld.CURRENT_VERSION;
}
