package Core.Exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

public class GLException extends Exception {
    @Serial
    private static final long serialVersionUID = 342007170816199688L;

    private static final String DEFAULT_MESSAGE = "LWJGL Error!";
    private static @NotNull String getGLMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public GLException() {
        super(DEFAULT_MESSAGE);
    }

    public GLException(@Nullable String message) {
        super(getGLMessage(message));
    }

    public GLException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public GLException(@Nullable String message, @Nullable Throwable throwable) {
        super(getGLMessage(message), throwable);
    }
}
