package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * {@link HConfigurations) single element.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfigElement {
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
    private @NotNull HConfigType type;
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
    public HConfigElement(@Nullable String name, @Nullable String note, @NotNull HConfigType type, @Nullable String value) throws HWrongConfigValueException {
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
    public HConfigElement(@Nullable String name, @Nullable String note, @Nullable String value) throws HWrongConfigValueException {
        this(name, note, HConfigType.STRING, value);
    }

    /**
     * Construct a new Config element.
     * @param name Config element's name
     * @param type Config element's type
     * @param value Config element's value
     */
    public HConfigElement(@Nullable String name, @NotNull HConfigType type, @Nullable String value) throws HWrongConfigValueException {
        this(name, "null", type, value);
    }

    /**
     * Construct a new Config element.
     * @param name Config element's name
     * @param value Config element's value
     */
    public HConfigElement(@Nullable String name, @Nullable String value) throws HWrongConfigValueException {
        this(name, "null", HConfigType.STRING, value);
    }

    /**
     * Get config element's name.
     * @return Config element's name
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Set config element's name.
     * @param name Config element's name
     */
    public void setName(@Nullable String name) {
        this.name = ((name == null) ? "null" : name);
    }

    /**
     * Get config element's note.
     * @return Config element's note
     */
    public @NotNull String getNote() {
        return this.note;
    }

    /**
     * Set config element's note.
     * @param note Config element's note
     */
    public void setNote(@Nullable String note) {
        this.note = ((note == null) ? "null" : note);
    }

    /**
     * Get config element's type.
     * @return Config element's type
     */
    public @NotNull HConfigType getType() {
        return this.type;
    }

    /**
     * Set config element's type.
     * @param type Config element's type
     * @throws HWrongConfigValueException Value is wrong for Type.
     */
    public void setType(@NotNull HConfigType type) throws HWrongConfigValueException {
        String fixed = type.fix(this.value);
        if (fixed == null)
            throw new HWrongConfigValueException(type);
        this.type = type;
        this.value = fixed;
    }

    /**
     * Get config element's value.
     * @return Config element's value
     */
    public @NotNull String getValue() {
        return this.value;
    }

    /**
     * Set config element's value.
     * @param value Config element's value
     * @throws HWrongConfigValueException Value is wrong for type.
     */
    public void setValue(@Nullable String value) throws HWrongConfigValueException {
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
    public void setTypeAndValue(@NotNull HConfigType type, @Nullable String value) throws HWrongConfigValueException {
        this.type = type;
        this.setValue(value);
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("HConfigElement{",
                "name='", this.name, '\'',
                ", note='", this.note, '\'',
                ", type=", this.type,
                ", value='", this.value, '\'',
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigElement))
            return false;
        return Objects.equals(this.name, ((HConfigElement) a).name) && this.type == ((HConfigElement) a).type && Objects.equals(this.value, ((HConfigElement) a).value);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
