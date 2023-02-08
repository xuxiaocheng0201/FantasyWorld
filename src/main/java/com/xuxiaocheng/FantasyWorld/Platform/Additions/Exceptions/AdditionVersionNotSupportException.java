package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionSingle;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class AdditionVersionNotSupportException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = -7509084405785898171L;

    private static final String DEFAULT_MESSAGE = "Addition version is not support.";

    public AdditionVersionNotSupportException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE));
    }

    public AdditionVersionNotSupportException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE), cause);
    }

    public AdditionVersionNotSupportException(final @Nullable String message, final @Nullable String id, final @Nullable String missing, final @Nullable VersionComplex missingVersion, final @Nullable VersionSingle version) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", current: " + (version == null ? "null": version.getVersion()) + ", require by: " + id);
    }

    public AdditionVersionNotSupportException(final @Nullable String message, final @Nullable String id, final @Nullable String missing, final @Nullable VersionComplex missingVersion, final @Nullable VersionSingle version, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", current: " + (version == null ? "null": version.getVersion()) + ", require by: " + id, cause);
    }
}
