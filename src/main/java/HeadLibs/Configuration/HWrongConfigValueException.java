package HeadLibs.Configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serial;

/**
 * Throw when Configuration value isn't suitable for the type.
 * @author xuxiaocheng
 */
public class HWrongConfigValueException extends IOException {
    @Serial
    private static final long serialVersionUID = -4695385007029343114L;

    private static final String DEFAULT_MESSAGE = "Configuration value is no longer suitable.";
    private static @NotNull String getWrongConfigValueMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public HWrongConfigValueException() {
        super(DEFAULT_MESSAGE);
    }

    public HWrongConfigValueException(@NotNull HConfigType type) {
        super(DEFAULT_MESSAGE + " type=" + type.getName());
    }

    public HWrongConfigValueException(@NotNull HConfigType type, @Nullable String value) {
        super(DEFAULT_MESSAGE + "type=" + type.getName() + " value='" + value + '\'');
    }

    public HWrongConfigValueException(@Nullable String message) {
        super(getWrongConfigValueMessage(message));
    }

    public HWrongConfigValueException(@Nullable String message, @NotNull HConfigType type) {
        super(getWrongConfigValueMessage(message) + " type=" + type.getName());
    }

    public HWrongConfigValueException(@Nullable String message, @NotNull HConfigType type, @Nullable String value) {
        super(getWrongConfigValueMessage(message) + "type=" + type.getName() + " value='" + value + '\'');
    }
}
