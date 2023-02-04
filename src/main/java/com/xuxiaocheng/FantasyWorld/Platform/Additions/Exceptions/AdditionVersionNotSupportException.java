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

    public AdditionVersionNotSupportException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE));
    }

    public AdditionVersionNotSupportException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE), cause);
    }

    public AdditionVersionNotSupportException(@Nullable final String message, @Nullable final String id, @Nullable final String missing, @Nullable final VersionComplex missingVersion, @Nullable final VersionSingle version) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", current: " + (version == null ? "null": version.getVersion()) + ", require by: " + id);
    }

    public AdditionVersionNotSupportException(@Nullable final String message, @Nullable final String id, @Nullable final String missing, @Nullable final VersionComplex missingVersion, @Nullable final VersionSingle version, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, AdditionVersionNotSupportException.DEFAULT_MESSAGE) +
                " missing: " + missing + (missingVersion == null ? "@null": '@' + missingVersion.getVersion()) +
                ", current: " + (version == null ? "null": version.getVersion()) + ", require by: " + id, cause);
    }
}
