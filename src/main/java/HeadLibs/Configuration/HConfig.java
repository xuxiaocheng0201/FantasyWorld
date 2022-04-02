package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HConfig {
    private @Nullable String name;
    private @Nullable String note;
    private @NotNull HEConfigType type;
    private @Nullable String value;

    public HConfig(String name, @Nullable String note, @NotNull HEConfigType type, String value) {
        this.setName(name);
        this.note = note;
        this.type = type;
        this.setValue(value);
    }

    public HConfig(String name, String note, String value) {
        this(name, note, HEConfigType.STRING, value);
    }

    public HConfig(String name, HEConfigType type, String value) {
        this(name, null, type, value);
    }

    public HConfig(String name, String value) {
        this(name, null, HEConfigType.STRING, value);
    }
    
    public void setName(@Nullable String name) {
        this.name = ((name == null) ? "null" : name);
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }

    public void setType(@NotNull HEConfigType type) {
        this.type = type;
    }

    public void setValue(String value) {
        if (this.type.checkString(value))
            this.value = value;
    }

    public @Nullable String getName() {
        return this.name;
    }

    public @Nullable String getNote() {
        return this.note;
    }

    public @NotNull HEConfigType getType() {
        return this.type;
    }

    public @Nullable String getValue() {
        return this.value;
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.merge("HConfig{",
                "name='", name, '\'',
                ", note='", note, '\'',
                ", type=", type,
                ", value='", value, '\'',
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfig))
            return false;
        return Objects.equals(this.name, ((HConfig) a).name) && this.type == ((HConfig) a).type && Objects.equals(this.value, ((HConfig) a).value);
    }

    @Override
    public int hashCode() {
        if (this.name == null)
            return 0;
        return this.name.hashCode();
    }
}
