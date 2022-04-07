package HeadLibs.Registerer;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when getting an unregistered element.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HElementNotRegisteredException extends Exception {
    @Serial
    private static final long serialVersionUID = -4342199817525193050L;

    private static final String DEFAULT_MESSAGE = "No registered element!";
    private static @NotNull String getMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public HElementNotRegisteredException() {
        super(DEFAULT_MESSAGE);
    }

    public HElementNotRegisteredException(@Nullable String message) {
        super(getMessage(message));
    }

    public HElementNotRegisteredException(@Nullable String message, @NotNull Object key) {
        super(getMessage(message) + " key=" + key);
    }
}
