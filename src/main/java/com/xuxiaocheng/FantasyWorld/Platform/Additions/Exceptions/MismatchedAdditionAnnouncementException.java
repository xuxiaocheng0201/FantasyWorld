package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class MismatchedAdditionAnnouncementException extends IllegalAdditionException {
    @Serial
    private static final long serialVersionUID = -5593015633129194149L;

    private static final String DEFAULT_MESSAGE = "Mismatched addition class announcement.";

    public MismatchedAdditionAnnouncementException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE));
    }

    public MismatchedAdditionAnnouncementException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE), cause);
    }

    public MismatchedAdditionAnnouncementException(@Nullable final String message, @Nullable final Class<?> aClass) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()));
    }

    public MismatchedAdditionAnnouncementException(@Nullable final String message, @Nullable final Class<?> aClass, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, MismatchedAdditionAnnouncementException.DEFAULT_MESSAGE) +
                " class: " + (aClass == null ? "null" : aClass.getName()), cause);
    }
}
