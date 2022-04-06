package HeadLibs.Version;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

@SuppressWarnings("unused")
public class HVersionFormatException extends Exception{
    @Serial
    private static final long serialVersionUID = -2079369912007846357L;

    public HVersionFormatException() {
        super();
    }

    public HVersionFormatException(@Nullable String message) {
        super(message == null ? "Wrong HVersion format." : message);
    }

    public HVersionFormatException(@Nullable String message, @Nullable String version) {
        super(HStringHelper.concat(message == null ? "Wrong HVersion format." : message, " Version=",
                version == null ? "null" : HStringHelper.concat("'", version, "'")));
    }

    public HVersionFormatException(@Nullable String message, @Nullable HStringVersion version) {
        super(HStringHelper.concat(message == null ? "Wrong HVersion format." : message, " Version=",
                version == null ? "null" : HStringHelper.concat("'", version.getVersion(), "'")));
    }
}
