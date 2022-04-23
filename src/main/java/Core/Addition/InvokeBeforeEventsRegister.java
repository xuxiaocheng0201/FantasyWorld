package Core.Addition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Only be announced on static methods. It will be invoked before eventbus registered.
 * Must be a 'run' type method. (no parameter and no return)
 * {@code
 *  public static void beforeEventRegister() {
 *      // Do something.
 *  }
 * }
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeBeforeEventsRegister {
}
