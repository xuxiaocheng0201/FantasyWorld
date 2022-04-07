package HeadLibs.Configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Single configuration element.
 * @author xuxiaocheng
 * @see HConfigurations
 */
@SuppressWarnings("unused")
public class HConfigElement implements Serializable {
    @Serial
    private static final long serialVersionUID = 6124564601710918431L;

    /**
     * Configuration name.
     */
    private @NotNull String name = "";
    /**
     * Configuration note.
     */
    private @NotNull String note = "";
    /**
     * Configuration type.
     */
    private @NotNull HConfigType type;
    /**
     * Configuration value.
     */
    private @NotNull String value = "";

    /**
     * Construct an empty Config element.
     */
    public HConfigElement() {
        super();
        this.type = HConfigType.STRING;
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     * @param note configuration note
     * @param type configuration type
     * @param value configuration value
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
     * @param name configuration name
     * @param note configuration note
     * @param value configuration value
     */
    public HConfigElement(@Nullable String name, @Nullable String note, @Nullable String value) throws HWrongConfigValueException {
        this(name, note, HConfigType.STRING, value);
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     * @param type configuration type
     * @param value configuration value
     */
    public HConfigElement(@Nullable String name, @NotNull HConfigType type, @Nullable String value) throws HWrongConfigValueException {
        this(name, "null", type, value);
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     * @param value configuration value
     */
    public HConfigElement(@Nullable String name, @Nullable String value) throws HWrongConfigValueException {
        this(name, "null", HConfigType.STRING, value);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = ((name == null) ? "null" : name);
    }

    public @NotNull String getNote() {
        return this.note;
    }

    public void setNote(@Nullable String note) {
        this.note = ((note == null) ? "null" : note);
    }

    public @NotNull HConfigType getType() {
        return this.type;
    }

    public void setType(@NotNull HConfigType type) throws HWrongConfigValueException {
        String fixed = type.fix(this.value);
        if (fixed == null)
            throw new HWrongConfigValueException(null, type, this.value);
        this.type = type;
        this.value = fixed;
    }

    public @NotNull String getValue() {
        return this.value;
    }

    public void setValue(@Nullable String value) throws HWrongConfigValueException {
        String fixed = this.type.fix(value);
        if (fixed == null)
            throw new HWrongConfigValueException(null, this.type, value);
        this.value = fixed;
    }

    public void setTypeAndValue(@NotNull HConfigType type, @Nullable String value) throws HWrongConfigValueException {
        this.type = type;
        this.setValue(value);
    }

    @Override
    public @NotNull String toString() {
        return "HConfigElement{" +
                "name='" + this.name + '\'' +
                ", note='" + this.note + '\'' +
                ", type=" + this.type +
                ", value='" + this.value + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object a) {
        if (!(a instanceof HConfigElement))
            return false;
        return Objects.equals(this.name, ((HConfigElement) a).name) && this.type == ((HConfigElement) a).type && Objects.equals(this.value, ((HConfigElement) a).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.type, this.value);
    }
}
