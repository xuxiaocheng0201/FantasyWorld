package Core.Addition.Element;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class ElementId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1689843735722673409L;

    protected @NotNull String elementId = "";

    protected ElementId() {
        super();
    }

    protected ElementId(@Nullable String elementId) {
        super();
        this.setId(elementId);
    }

    public @NotNull String getId() {
        return this.elementId;
    }

    public void setId(@Nullable String elementId) {
        this.elementId = HStringHelper.notNullStrip(elementId);
    }

    public boolean isEmpty() {
        return this.elementId.isEmpty();
    }

    @Override
    public @NotNull String toString() {
        return this.elementId;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementId that)) return false;
        return this.elementId.equals(that.elementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.elementId);
    }
}
