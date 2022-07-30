package HeadLibs.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_USE)
public @interface DoubleRange {
    boolean minimum_equally() default true;
    double minimum() default Double.MIN_VALUE;
    double maximum() default Double.MAX_VALUE;
    boolean maximum_equally() default true;
}
