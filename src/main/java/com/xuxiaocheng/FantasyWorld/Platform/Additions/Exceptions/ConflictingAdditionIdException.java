package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class ConflictingAdditionIdException extends IllegalAdditionException {
    @Serial
    private static final long serialVersionUID = 7372946206307486964L;

    private static final String DEFAULT_MESSAGE = "Conflicting addition id.";

    public ConflictingAdditionIdException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE));
    }

    public ConflictingAdditionIdException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE), cause);
    }

    public ConflictingAdditionIdException(@Nullable final String message, @Nullable final String id, @Nullable final Class<?> a, @Nullable final Class<?> b) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE) + " addition id: " + id +
                ", class a: " + (a == null ? "null" : a.getName()) + ", class b: " + (b == null ? "null" : b.getName()));
    }

    public ConflictingAdditionIdException(@Nullable final String message, @Nullable final String id, @Nullable final Class<?> a, @Nullable final Class<?> b, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE) + " addition id: " + id +
                ", class a: " + (a == null ? "null" : a.getName()) + ", class b: " + (b == null ? "null" : b.getName()), cause);
    }
}
