package HeadLibs.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_USE)
public @interface CharRange {
    boolean minimum_equally() default true;
    char minimum() default Character.MIN_VALUE;
    char maximum() default Character.MAX_VALUE;
    boolean maximum_equally() default true;
}
