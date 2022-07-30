package HeadLibs.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_USE)
public @interface FloatRange {
    boolean minimum_equally() default true;
    float minimum() default Float.MIN_VALUE;
    float maximum() default Float.MAX_VALUE;
    boolean maximum_equally() default true;
}
