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
    private @NotNull String name = "null";
    /**
     * Configuration note.
     */
    private @NotNull String note = "null";
    /**
     * Configuration type.
     */
    private @NotNull HConfigType type = HConfigType.STRING;
    /**
     * Configuration value.
     */
    private @NotNull String value = "null";

    /**
     * Construct an empty Config element.
     */
    public HConfigElement() {
        super();
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
        super();
        this.setName(name);
        this.setNote(note);
        this.setValue(value);
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     * @param type configuration type
     * @param value configuration value
     */
    public HConfigElement(@Nullable String name, @NotNull HConfigType type, @Nullable String value) throws HWrongConfigValueException {
        super();
        this.setName(name);
        this.type = type;
        this.setValue(value);
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     * @param value configuration value
     */
    public HConfigElement(@Nullable String name, @Nullable String value) throws HWrongConfigValueException {
        super();
        this.setName(name);
        this.setValue(value);
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     */
    public HConfigElement(@Nullable String name) {
        super();
        this.setName(name);
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
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HConfigElement that)) return false;
        return this.name.equals(that.name) && this.type.equals(that.type) && this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.type, this.value);
    }
}
