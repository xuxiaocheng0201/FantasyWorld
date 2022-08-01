package HeadLibs.Version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when finding wrong formation in formatting version string to Version.
 * @author xuxiaocheng
 */
public class HVersionFormatException extends Exception{
    @Serial
    private static final long serialVersionUID = -2079369912007846357L;

    private static final String DEFAULT_MESSAGE = "Wrong formation of version string.";
    private static @NotNull String getVersionFormatMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public HVersionFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public HVersionFormatException(@Nullable HStringVersion version) {
        super(DEFAULT_MESSAGE + " Version='" + (version == null ? "null" : version.getVersion()) + "'");
    }

    public HVersionFormatException(@Nullable String message) {
        super(getVersionFormatMessage(message));
    }

    public HVersionFormatException(@Nullable String message, @Nullable String version) {
        super(getVersionFormatMessage(message) + " Version='" + (version == null ? "null" : version) + "'");
    }

    public HVersionFormatException(@Nullable String message, @Nullable HStringVersion version) {
        super(getVersionFormatMessage(message) + " Version='" + (version == null ? "null" : version.getVersion()) + "'");
    }
}
