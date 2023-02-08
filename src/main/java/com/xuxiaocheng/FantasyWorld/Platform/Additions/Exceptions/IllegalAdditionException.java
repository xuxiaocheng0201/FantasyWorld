package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class IllegalAdditionException extends Exception {
    @Serial
    private static final long serialVersionUID = -722299491491029280L;

    private static final String DEFAULT_MESSAGE = "Illegal addition information.";

    public IllegalAdditionException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, IllegalAdditionException.DEFAULT_MESSAGE));
    }

    public IllegalAdditionException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, IllegalAdditionException.DEFAULT_MESSAGE), cause);
    }

    public @NotNull HLogLevel getLevel() {
        return HLogLevel.WARN;
    }
}
