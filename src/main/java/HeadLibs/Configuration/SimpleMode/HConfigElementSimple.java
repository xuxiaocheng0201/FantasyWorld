package HeadLibs.Configuration.SimpleMode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Simple single configuration element.
 * @author xuxiaocheng
 * @see HConfigurationsSimple
 */
public class HConfigElementSimple implements Serializable {
    @Serial
    private static final long serialVersionUID = -2691268100491960817L;

    /**
     * Configuration name.
     */
    private @NotNull String name = "null";
    /**
     * Configuration value.
     */
    private @NotNull String value = "null";

    /**
     * Construct a null Config element.
     */
    public HConfigElementSimple() {
        super();
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     */
    public HConfigElementSimple(@Nullable String name) {
        super();
        this.setName(name);
    }

    /**
     * Construct a new Config element.
     * @param name configuration name
     * @param value configuration value
     */
    public HConfigElementSimple(@Nullable String name, @Nullable String value) {
        super();
        this.setName(name);
        this.setValue(value);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = (name == null) ? "null" : name;
    }

    public @NotNull String getValue() {
        return this.value;
    }

    public void setValue(@Nullable String value) {
        this.value = (value == null) ? "null" : value;
    }

    @Override
    public @NotNull String toString() {
        return "HConfigElementSimple{" +
                "name='" + this.name + '\'' +
                ", value='" + this.value + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HConfigElementSimple that)) return false;
        return this.name.equals(that.name) && this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }
}
