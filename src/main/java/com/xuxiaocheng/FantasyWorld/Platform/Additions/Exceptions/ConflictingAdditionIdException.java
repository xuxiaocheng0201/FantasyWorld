package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class ConflictingAdditionIdException extends IllegalAdditionException {
    @Serial
    private static final long serialVersionUID = 7372946206307486964L;

    private static final String DEFAULT_MESSAGE = "Conflicting addition id.";

    public ConflictingAdditionIdException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE));
    }

    public ConflictingAdditionIdException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE), cause);
    }

    public ConflictingAdditionIdException(final @Nullable String message, final @Nullable String id, final @Nullable Class<?> a, final @Nullable Class<?> b) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE) + " addition id: " + id +
                ", class a: " + (a == null ? "null" : a.getName()) + ", class b: " + (b == null ? "null" : b.getName()));
    }

    public ConflictingAdditionIdException(final @Nullable String message, final @Nullable String id, final @Nullable Class<?> a, final @Nullable Class<?> b, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, ConflictingAdditionIdException.DEFAULT_MESSAGE) + " addition id: " + id +
                ", class a: " + (a == null ? "null" : a.getName()) + ", class b: " + (b == null ? "null" : b.getName()), cause);
    }
}
