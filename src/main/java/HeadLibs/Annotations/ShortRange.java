package HeadLibs.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_USE)
public @interface ShortRange {
    boolean minimum_equally() default true;
    short minimum() default Short.MIN_VALUE;
    short maximum() default Short.MAX_VALUE;
    boolean maximum_equally() default true;
}
