package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * HConfigurations single element.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfig {
    /**
     * Config element's name
     */
    private @NotNull String name = "";
    /**
     * Config element's note
     */
    private @NotNull String note = "";
    /**
     * Config element's type
     */
    private @NotNull HEConfigType type;
    /**
     * Config element's value
     */
    private @NotNull String value = "";

    /**
     * Construct a new Config element.
     * @param name Config element's name
     * @param note Config element's note
     * @param type Config element's type
     * @param value Config element's value
     */
    public HConfig(@Nullable String name, @Nullable String note, @NotNull HEConfigType type, @Nullable String value) {
        super();
        this.setName(name);
        this.setNote(note);
        this.type = type;
        this.setValue(value);
    }

    /**
     * Construct a new Config element.
     * @param name Config element's name
     * @param note Config element's note
     * @param value Config element's value
     */
    public HConfig(@Nullable String name, @Nullable String note, @Nullable String value) {
        this(name, note, HEConfigType.STRING, value);
    }

    /**
     * Construct a new Config element.
     * @param name Config element's name
     * @param type Config element's type
     * @param value Config element's value
     */
    public HConfig(@Nullable String name, @NotNull HEConfigType type, @Nullable String value) {
        this(name, "null", type, value);
    }

    /**
     * Construct a new Config element.
     * @param name Config element's name
     * @param value Config element's value
     */
    public HConfig(@Nullable String name, @Nullable String value) {
        this(name, "null", HEConfigType.STRING, value);
    }

    /**
     * Set config element's name.
     * @param name Config element's name
     */
    public void setName(@Nullable String name) {
        this.name = ((name == null) ? "null" : name);
    }

    /**
     * Set config element's note.
     * @param note Config element's note
     */
    public void setNote(@Nullable String note) {
        this.note = ((note == null) ? "null" : note);
    }

    /**
     * Set config element's type.
     * @param type Config element's type
     * @throws HWrongConfigValueException Value is wrong for Type.
     */
    public void setType(@NotNull HEConfigType type) {
        if (type.fix(this.value) == null)
            throw new HWrongConfigValueException(type);
        this.type = type;
    }

    /**
     * Set config element's value.
     * @param value Config element's value
     * @throws HWrongConfigValueException Value is wrong for type.
     */
    public void setValue(@Nullable String value) {
        String fixed = this.type.fix(value);
        if (fixed == null)
            throw new HWrongConfigValueException(value);
        this.value = fixed;
    }

    /**
     * Set config element's type and value.
     * @param type Config element's type
     * @param value Config element's value
     * @throws HWrongConfigValueException Value is wrong for type.
     */
    public void setTypeAndValue(@NotNull HEConfigType type, @Nullable String value) {
        this.type = type;
        this.setValue(value);
    }

    /**
     * Get config element's name.
     * @return Config element's name
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Get config element's note.
     * @return Config element's note
     */
    public @NotNull String getNote() {
        return this.note;
    }

    /**
     * Get config element's type.
     * @return Config element's type
     */
    public @NotNull HEConfigType getType() {
        return this.type;
    }

    /**
     * Get config element's value.
     * @return Config element's value
     */
    public @NotNull String getValue() {
        return this.value;
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.merge("HConfig{",
                "name='", this.name, '\'',
                ", note='", this.note, '\'',
                ", type=", this.type,
                ", value='", this.value, '\'',
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
        return this.name.hashCode();
    }
}
