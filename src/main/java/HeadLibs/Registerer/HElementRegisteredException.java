package HeadLibs.Registerer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when registering an element that has been registered.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HElementRegisteredException extends Exception{
    @Serial
    private static final long serialVersionUID = 5758033473623251615L;

    private static final String DEFAULT_MESSAGE = "Element has been registered!";
    private static @NotNull String getMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public HElementRegisteredException() {
        super(DEFAULT_MESSAGE);
    }

    public HElementRegisteredException(@Nullable String message) {
        super(getMessage(message));
    }

    public HElementRegisteredException(@Nullable String message, @Nullable Object element) {
        super(getMessage(message) + " element=" + element);
    }

    public HElementRegisteredException(@Nullable String message, @Nullable  Object key, @Nullable Object value) {
        super(getMessage(message) + " key=" + value + " element=" + value);
    }
}
