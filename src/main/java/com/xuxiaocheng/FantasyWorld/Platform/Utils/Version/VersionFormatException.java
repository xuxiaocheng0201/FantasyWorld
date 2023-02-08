package com.xuxiaocheng.FantasyWorld.Platform.Utils.Version;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

/**
 * Throw when finding wrong formation in formatting version string to Version.
 * @author xuxiaocheng
 */
public class VersionFormatException extends Exception {
    @Serial
    private static final long serialVersionUID = -2079369912007846357L;
    private static final String DEFAULT_MESSAGE = "Wrong formation of version.";

    public VersionFormatException() {
        super(VersionFormatException.DEFAULT_MESSAGE);
    }

    public VersionFormatException(final @Nullable Throwable cause) {
        super(VersionFormatException.DEFAULT_MESSAGE, cause);
    }

    public VersionFormatException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, VersionFormatException.DEFAULT_MESSAGE));
    }

    public VersionFormatException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, VersionFormatException.DEFAULT_MESSAGE), cause);
    }

    public VersionFormatException(final @Nullable String message, final @Nullable String version) {
        super(Objects.requireNonNullElse(message, VersionFormatException.DEFAULT_MESSAGE) +
                " Version string: " + (version == null ? "null" : '"' + version + '"'));
    }

    public VersionFormatException(final @Nullable String message, final @Nullable String version, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, VersionFormatException.DEFAULT_MESSAGE) +
                " Version string: " + (version == null ? "null" : '"' + version + '"'), cause);
    }
}
