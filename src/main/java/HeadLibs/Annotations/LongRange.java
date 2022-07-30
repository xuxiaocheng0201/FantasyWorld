package HeadLibs.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_USE)
public @interface LongRange {
    boolean minimum_equally() default true;
    long minimum() default Long.MIN_VALUE;
    long maximum() default Long.MAX_VALUE;
    boolean maximum_equally() default true;
}
