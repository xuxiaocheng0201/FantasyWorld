package CraftWorld.Instance.Entity.BoundingBox;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BoundingBox.BoundingBoxBase;
import CraftWorld.Entity.EntityPos;
import CraftWorld.Entity.EntityPos.UpdatableEntityPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class BoundingBoxSphere extends BoundingBoxBase {
    @Serial
    private static final long serialVersionUID = 4710597949935360512L;
    public static final String id = "BoundingBoxSphere";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull UpdatableEntityPos centre = new UpdatableEntityPos();
    protected double range;

    public double getRange() {
        return this.range;
    }

    public void setRange(double range) {
        double new_range = Math.max(range, 0.0D);
        if (Double.compare(this.range, new_range) != 0) {
            this.updated = true;
            this.range = new_range;
        }
    }

    public @NotNull UpdatableEntityPos getCentre() {
        return this.centre;
    }

    @Override
    public @NotNull EntityPos getCentrePos() {
        return this.centre;
    }

    @Override
    public double getMaxRadius() {
        return this.range;
    }

    @Override
    public double getMinRadius() {
        return this.range;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        if (!EntityPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.centre.read(input);
        this.range = input.readDouble();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.centre.write(output);
        output.writeDouble(this.range);
        output.writeUTF(suffix);
    }

    @Override
    public boolean getUpdated() {
        if (this.centre.getUpdated())
            return true;
        return super.getUpdated();
    }

    @Override
    public void setUpdated(boolean updated) {
        super.setUpdated(updated);
        this.centre.setUpdated(updated);
    }

    @Override
    public @NotNull String toString() {
        return "BoundingBoxSphere{" +
                "centre=" + this.centre +
                ", range=" + this.range +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof BoundingBoxSphere that)) return false;
        return Double.compare(this.range, that.range) == 0 && this.centre.equals(that.centre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.centre, this.range);
    }
}
