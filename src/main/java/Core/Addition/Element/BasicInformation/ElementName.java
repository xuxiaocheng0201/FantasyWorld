package Core.Addition.Element.BasicInformation;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Element information - name/id.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ElementName implements Serializable {
    @Serial
    private static final long serialVersionUID = -4421475659404411813L;

    private @NotNull String name = "";

    public ElementName() {
        super();
    }

    public ElementName(@Nullable String name) {
        super();
        this.setName(name);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = HStringHelper.notNullStrip(name);
    }

    public boolean isEmpty() {
        return this.name.isEmpty();
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementName that)) return false;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
