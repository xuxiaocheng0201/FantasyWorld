package CraftWorld.Instance.Entity.BoundingBox;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BoundingBox.BoundingBoxUtils;
import CraftWorld.Entity.BoundingBox.IBoundingBoxBase;
import CraftWorld.Entity.EntityPos;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BoundingBoxSphere implements IBoundingBoxBase {
    @Serial
    private static final long serialVersionUID = 4710597949935360512L;
    public static final String id = "BoundingBoxSphere";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            BoundingBoxUtils.getInstance().register(id, BoundingBoxSphere.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    protected @NotNull EntityPos centre = new EntityPos();
    protected double range;

    public @NotNull EntityPos getCentre() {
        return this.centre;
    }

    public double getRange() {
        return this.range;
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
    public @Nullable IBoundingBoxBase getBaseBoundingBox() {
        return null;
    }

    @Override
    public @NotNull Set<IBoundingBoxBase> getAdditionalBoundingBox() {
        return new HashSet<>();
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
        return this.range == that.range && this.centre.equals(that.centre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.centre, this.range);
    }
}
