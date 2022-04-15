package Core.Addition.Mod.BasicInformation;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Mod information - name/id.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModName implements Serializable {
    @Serial
    private static final long serialVersionUID = 3003306076015173618L;

    private @NotNull String name = "";

    public ModName() {
        super();
    }

    public ModName(@Nullable String name) {
        super();
        this.setName(name);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = HStringHelper.notNullStrip(name);
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ModName modName = (ModName) o;
        return this.name.equals(modName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
