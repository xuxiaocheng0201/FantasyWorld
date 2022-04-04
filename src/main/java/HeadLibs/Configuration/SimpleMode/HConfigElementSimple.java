package HeadLibs.Configuration.SimpleMode;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * {@link HConfigurationsSimple} single element.
 * @author xuxiaocheng
 */
public class HConfigElementSimple {
    /**
     * Config element's name
     */
    private @NotNull String name = "";
    /**
     * Config element's value
     */
    private @NotNull String value = "";

    /**
     * Construct a new Config element.
     * @param name Config element's name
     * @param value Config element's value
     */
    public HConfigElementSimple(@Nullable String name, @Nullable String value) {
        super();
        this.setName(name);
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
     * Set config element's name.
     * @param name Config element's name
     */
    public void setName(@Nullable String name) {
        this.name = ((name == null) ? "null" : name);
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
     */
    public void setValue(@Nullable String value) {
        this.value = ((value == null) ? "null" : value);
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("HConfigElementSimple{",
                "name='", this.name, '\'',
                ", value='", this.value, '\'',
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigElementSimple))
            return false;
        return Objects.equals(this.name, ((HConfigElementSimple) a).name) && Objects.equals(this.value, ((HConfigElementSimple) a).value);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
