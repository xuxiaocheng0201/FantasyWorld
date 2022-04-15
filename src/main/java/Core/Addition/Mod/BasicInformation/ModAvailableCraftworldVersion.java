package Core.Addition.Mod.BasicInformation;

import HeadLibs.Version.HVersionComplex;
import HeadLibs.Version.HVersionFormatException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * Mod information - available craftworld version.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModAvailableCraftworldVersion implements Serializable {
    @Serial
    private static final long serialVersionUID = -2356864710659062812L;

    private @NotNull HVersionComplex version = new HVersionComplex();

    public ModAvailableCraftworldVersion() {
        super();
    }

    public ModAvailableCraftworldVersion(@Nullable String version) throws HVersionFormatException {
        super();
        this.setVersion(version);
    }

    public ModAvailableCraftworldVersion(@Nullable HVersionComplex version) {
        super();
        this.setVersion(version);
    }

    public @NotNull HVersionComplex getVersion() {
        return this.version;
    }

    public @NotNull String getVersionString() {
        return this.version.toString();
    }

    public void setVersion(@Nullable String version) throws HVersionFormatException {
        this.version = new HVersionComplex(version);
    }

    public void setVersion(@Nullable HVersionComplex version) {
        if (version == null) {
            this.version = new HVersionComplex();
            return;
        }
        try {
            this.version = new HVersionComplex(version.toString());
        } catch (HVersionFormatException ignore) {
        }
    }
}
