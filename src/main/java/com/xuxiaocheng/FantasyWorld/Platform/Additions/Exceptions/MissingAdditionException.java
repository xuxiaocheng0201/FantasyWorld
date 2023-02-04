package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class MissingAdditionException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = -7509084405785898171L;

    private static final String DEFAULT_MESSAGE = "Miss addition.";

    public MissingAdditionException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE));
    }

    public MissingAdditionException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE), cause);
    }

    public MissingAdditionException(@Nullable final String message, @Nullable final String id, @Nullable final String missing, @Nullable final VersionComplex missingVersion) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", require by: " + id);
    }

    public MissingAdditionException(@Nullable final String message, @Nullable final String id, @Nullable final String missing, @Nullable final VersionComplex missingVersion, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", require by: " + id, cause);
    }
}
