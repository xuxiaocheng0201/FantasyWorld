package HeadLibs.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_USE)
public @interface ByteRange {
    boolean minimum_equally() default true;
    byte minimum() default Byte.MIN_VALUE;
    byte maximum() default Byte.MAX_VALUE;
    boolean maximum_equally() default true;
}
