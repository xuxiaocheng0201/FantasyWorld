package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class PlatformVersionNotSupportException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = 5223826546864501822L;

    private static final String DEFAULT_MESSAGE = "Platform version is not support.";

    public PlatformVersionNotSupportException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE));
    }

    public PlatformVersionNotSupportException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE), cause);
    }

    public PlatformVersionNotSupportException(final @Nullable String message, final @Nullable String id, final @Nullable VersionComplex version) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE) +
                " addition id: " + id + ", accept platform version: " + (version == null ? "null" : version.getVersion()));
    }

    public PlatformVersionNotSupportException(final @Nullable String message, final @Nullable String id, final @Nullable VersionComplex version, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE) +
                " addition id: " + id + ", accept platform version: " + (version == null ? "null" : version.getVersion()), cause);
    }
}
