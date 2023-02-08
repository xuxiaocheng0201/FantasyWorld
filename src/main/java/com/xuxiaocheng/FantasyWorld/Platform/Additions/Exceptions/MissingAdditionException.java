package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class MissingAdditionException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = -7509084405785898171L;

    private static final String DEFAULT_MESSAGE = "Miss addition.";

    public MissingAdditionException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE));
    }

    public MissingAdditionException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE), cause);
    }

    public MissingAdditionException(final @Nullable String message, final @Nullable String id, final @Nullable String missing, final @Nullable VersionComplex missingVersion) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", require by: " + id);
    }

    public MissingAdditionException(final @Nullable String message, final @Nullable String id, final @Nullable String missing, final @Nullable VersionComplex missingVersion, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, MissingAdditionException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", require by: " + id, cause);
    }
}
