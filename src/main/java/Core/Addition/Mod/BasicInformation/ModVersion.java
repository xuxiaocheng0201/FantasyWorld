package Core.Addition.Mod.BasicInformation;

import HeadLibs.Version.HStringVersion;
import HeadLibs.Version.HVersionFormatException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * Mod information - version.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModVersion implements Serializable {
    @Serial
    private static final long serialVersionUID = 81384734643607231L;

    private @NotNull HStringVersion version = new HStringVersion();

    public ModVersion() {
        super();
    }

    public ModVersion(@Nullable String modVersion) throws HVersionFormatException {
        super();
        this.setVersion(modVersion);
    }

    /**
     * Calling {@link ModVersion#setVersionAutoFix(String)}. The second parameter is just a placeholder.
     */
    public ModVersion(@Nullable String modVersion, @Nullable Object NULL) {
        super();
        this.setVersionAutoFix(modVersion);
    }

    public ModVersion(@Nullable HStringVersion modVersion) {
        super();
        this.setVersion(modVersion);
    }

    public @NotNull HStringVersion getVersion() {
        return this.version;
    }

    public @NotNull String getVersionString() {
        return this.version.getVersion();
    }

    public void setVersion(@Nullable String version) throws HVersionFormatException {
        this.version = new HStringVersion(version);
    }

    /**
     * Fix version string by deleting all illegal chars: ()[]{},&
     */
    public void setVersionAutoFix(@Nullable String version) {
        if (version == null) {
            this.version = new HStringVersion();
            return;
        }
        try {
            this.version = new HStringVersion(version);
        } catch (HVersionFormatException exception) {
            String fixedVersion = version
                    .replace("[", "")
                    .replace("]", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace(",", "")
                    .replace("&", "");
            try {
                this.version = new HStringVersion(fixedVersion);
            } catch (HVersionFormatException ignore) {
            }
        }
    }

    public void setVersion(@Nullable HStringVersion version) {
        if (version == null) {
            this.version = new HStringVersion();
            return;
        }
        try {
            this.version = new HStringVersion(version.getVersion());
        } catch (HVersionFormatException ignore) {
        }
    }
}
