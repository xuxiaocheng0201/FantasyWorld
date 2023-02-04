package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.FantasyWorld.Platform.Additions.Addition;
import com.xuxiaocheng.FantasyWorld.Platform.Utils.Version.VersionComplex;
import com.xuxiaocheng.HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class PlatformVersionNotSupportException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = 5223826546864501822L;

    private static final String DEFAULT_MESSAGE = "Platform version is not support.";

    public PlatformVersionNotSupportException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE));
    }

    public PlatformVersionNotSupportException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE), cause);
    }

    public PlatformVersionNotSupportException(@Nullable final String message, @Nullable final String id, @Nullable final VersionComplex version) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE) +
                " addition id: " + id + ", accept platform version: " + (version == null ? "null" : version.getVersion()));
    }

    public PlatformVersionNotSupportException(@Nullable final String message, @Nullable final String id, @Nullable final VersionComplex version, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, PlatformVersionNotSupportException.DEFAULT_MESSAGE) +
                " addition id: " + id + ", accept platform version: " + (version == null ? "null" : version.getVersion()), cause);
    }
}
