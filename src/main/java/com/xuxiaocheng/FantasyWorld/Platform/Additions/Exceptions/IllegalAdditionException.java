package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;
import java.util.jar.JarFile;

public class IllegalAdditionException extends Exception {
    @Serial
    private static final long serialVersionUID = -722299491491029280L;

    private static final String DEFAULT_MESSAGE = "Illegal addition information.";

    public IllegalAdditionException() {
        super(IllegalAdditionException.DEFAULT_MESSAGE);
    }

    public IllegalAdditionException(@Nullable final Throwable cause) {
        super(IllegalAdditionException.DEFAULT_MESSAGE, cause);
    }

    public IllegalAdditionException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, IllegalAdditionException.DEFAULT_MESSAGE));
    }

    public IllegalAdditionException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, IllegalAdditionException.DEFAULT_MESSAGE), cause);
    }

    public IllegalAdditionException(@Nullable final String message, @Nullable final JarFile jarFile) {
        super(Objects.requireNonNullElse(message, IllegalAdditionException.DEFAULT_MESSAGE) +
                " jarFile path: " + (jarFile == null ? "null" : jarFile.getName()));
    }

    public IllegalAdditionException(@Nullable final String message, @Nullable final JarFile jarFile, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, IllegalAdditionException.DEFAULT_MESSAGE) +
                " jarFile path: " + (jarFile == null ? "null" : jarFile.getName()), cause);
    }

    public @NotNull HLogLevel getLevel() {
        return HLogLevel.WARN;
    }
}
