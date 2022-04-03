package HeadLibs.Configuration.SimpleMode;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HConfigSimple {
    private @Nullable String name;
    private @Nullable String value;

    public HConfigSimple(String name, String value) {
        super();
        this.setName(name);
        this.setValue(value);
    }

    public void setName(@Nullable String name) {
        this.name = ((name == null) ? "null" : name);
    }

    public void setValue(@Nullable String value) {
        this.value = ((value == null) ? "null" : value);
    }

    public @Nullable String getName() {
        return this.name;
    }

    public @Nullable String getValue() {
        return this.value;
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("HConfigSimple{",
                "name='", this.name, '\'',
                ", value='", this.value, '\'',
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigSimple))
            return false;
        return Objects.equals(this.name, ((HConfigSimple) a).name) && Objects.equals(this.value, ((HConfigSimple) a).value);
    }

    @Override
    public int hashCode() {
        if (this.name == null)
            return 0;
        return this.name.hashCode();
    }
}
