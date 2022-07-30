package HeadLibs.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_USE)
public @interface IntRange {
    boolean minimum_equally() default true;
    int minimum() default Integer.MIN_VALUE;
    int maximum() default Integer.MAX_VALUE;
    boolean maximum_equally() default true;
}
