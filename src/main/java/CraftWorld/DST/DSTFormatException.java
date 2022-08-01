package CraftWorld.DST;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serial;

/**
 * Throw when read an impossible DST element.
 * @author xuxiaocheng
 */
public class DSTFormatException extends IOException {
    @Serial
    private static final long serialVersionUID = -8277448868946723580L;

    private static final String DEFAULT_MESSAGE = "DST format was wrong!";
    private static @NotNull String getDSTFormatMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public DSTFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public DSTFormatException(@Nullable String message) {
        super(getDSTFormatMessage(message));
    }

    public DSTFormatException(@Nullable Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public DSTFormatException(@Nullable String message, @Nullable Throwable cause) {
        super(getDSTFormatMessage(message), cause);
    }
}
