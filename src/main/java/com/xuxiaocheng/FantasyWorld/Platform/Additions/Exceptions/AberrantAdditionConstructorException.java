package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class AberrantAdditionConstructorException extends IllegalAdditionException {
    @Serial
    private static final long serialVersionUID = 139080701781956407L;

    private static final String DEFAULT_MESSAGE = "The parameterless constructor is aberrant.";

    public AberrantAdditionConstructorException() {
        super(AberrantAdditionConstructorException.DEFAULT_MESSAGE);
    }

    public AberrantAdditionConstructorException(@Nullable final Throwable cause) {
        super(AberrantAdditionConstructorException.DEFAULT_MESSAGE, cause);
    }

    public AberrantAdditionConstructorException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE));
    }

    public AberrantAdditionConstructorException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE), cause);
    }

    public AberrantAdditionConstructorException(@Nullable final String message, @Nullable final Class<?> aClass) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE) +
                " class name: " + (aClass == null ? "null" : aClass.getName()));
    }

    public AberrantAdditionConstructorException(@Nullable final String message, @Nullable final Class<?> aClass, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE) +
                " class name: " + (aClass == null ? "null" : aClass.getName()), cause);
    }

    @Override
    public @NotNull HLogLevel getLevel() {
        return HLogLevel.ERROR;
    }
}
