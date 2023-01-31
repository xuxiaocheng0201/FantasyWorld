package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class MismatchedAdditionConstructorException extends IllegalAdditionException {
    @Serial
    private static final long serialVersionUID = 139080701781956407L;

    private static final String DEFAULT_MESSAGE = "Mismatched the parameterless constructor of addition class.";

    public MismatchedAdditionConstructorException() {
        super(MismatchedAdditionConstructorException.DEFAULT_MESSAGE);
    }

    public MismatchedAdditionConstructorException(@Nullable final Throwable cause) {
        super(MismatchedAdditionConstructorException.DEFAULT_MESSAGE, cause);
    }

    public MismatchedAdditionConstructorException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionConstructorException.DEFAULT_MESSAGE));
    }

    public MismatchedAdditionConstructorException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionConstructorException.DEFAULT_MESSAGE), cause);
    }

    public MismatchedAdditionConstructorException(@Nullable final String message, @Nullable final Class<?> aClass) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionConstructorException.DEFAULT_MESSAGE) +
                " class name: " + (aClass == null ? "null" : aClass.getName()));
    }

    public MismatchedAdditionConstructorException(@Nullable final String message, @Nullable final Class<?> aClass, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionConstructorException.DEFAULT_MESSAGE) +
                " class name: " + (aClass == null ? "null" : aClass.getName()), cause);
    }

    @Override
    public @NotNull HLogLevel getLevel() {
        return HLogLevel.ERROR;
    }
}
