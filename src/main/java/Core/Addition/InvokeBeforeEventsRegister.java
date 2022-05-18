package Core.Addition;

import java.lang.annotation.*;

/**
 * Only be announced on static methods. It will be invoked before eventbus registered.
 * Must be a 'run' type method. (no parameter and no return)
 * {@code
 *  package Core.Addition;
 *  @InvokeBeforeEventsRegister
 *  public class Test {
 *      @InvokeBeforeEventsRegister
 *      public static void beforeEventRegister() {
 *          // Do something.
 *      }
 *  }
 * }
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokeBeforeEventsRegister {
}
