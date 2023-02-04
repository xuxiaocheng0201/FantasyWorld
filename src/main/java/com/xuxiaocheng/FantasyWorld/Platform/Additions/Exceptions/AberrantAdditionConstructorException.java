package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class AberrantAdditionConstructorException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = 139080701781956407L;

    private static final String DEFAULT_MESSAGE = "The parameterless constructor is aberrant.";

    public AberrantAdditionConstructorException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE));
    }

    public AberrantAdditionConstructorException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE), cause);
    }

    public AberrantAdditionConstructorException(@Nullable final String message, @Nullable final Class<?> aClass) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()));
    }

    public AberrantAdditionConstructorException(@Nullable final String message, @Nullable final Class<?> aClass, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, AberrantAdditionConstructorException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()), cause);
    }
}
