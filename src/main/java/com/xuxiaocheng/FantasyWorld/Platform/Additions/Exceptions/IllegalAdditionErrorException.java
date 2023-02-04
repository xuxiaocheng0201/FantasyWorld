package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

public class IllegalAdditionErrorException extends IllegalAdditionException {
    @Serial
    private static final long serialVersionUID = -7501409732731868727L;

    public IllegalAdditionErrorException(@Nullable final String message) {
        super(message);
    }

    public IllegalAdditionErrorException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    @Override
    public @NotNull HLogLevel getLevel() {
        return HLogLevel.ERROR;
    }
}
