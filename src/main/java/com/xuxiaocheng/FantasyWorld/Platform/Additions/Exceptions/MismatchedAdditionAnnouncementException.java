package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class MismatchedAdditionAnnouncementException extends IllegalAdditionException {
    @Serial
    private static final long serialVersionUID = -5593015633129194149L;

    private static final String DEFAULT_MESSAGE = "Mismatched addition class announcement.";

    public MismatchedAdditionAnnouncementException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE));
    }

    public MismatchedAdditionAnnouncementException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE), cause);
    }

    public MismatchedAdditionAnnouncementException(final @Nullable String message, final @Nullable Class<?> aClass) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()));
    }

    public MismatchedAdditionAnnouncementException(final @Nullable String message, final @Nullable Class<?> aClass, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()), cause);
    }
}
