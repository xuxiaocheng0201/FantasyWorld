package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class AberrantAdditionConstructorException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = 139080701781956407L;

    private static final String DEFAULT_MESSAGE = "The parameterless constructor is aberrant.";

    public AberrantAdditionConstructorException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE));
    }

    public AberrantAdditionConstructorException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE), cause);
    }

    public AberrantAdditionConstructorException(final @Nullable String message, final @Nullable Class<?> aClass) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()));
    }

    public AberrantAdditionConstructorException(final @Nullable String message, final @Nullable Class<?> aClass, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()), cause);
    }
}
