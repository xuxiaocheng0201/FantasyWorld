package CraftWorld.Instance.Entity.BoundingBox;

import CraftWorld.CraftWorld;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BoundingBox.BoundingBoxBase;
import CraftWorld.Entity.EntityPos;
import CraftWorld.Entity.EntityPos.ImmutableEntityPos;
import CraftWorld.Entity.EntityPos.UpdatableEntityPos;
import CraftWorld.Utils.Angle;
import CraftWorld.Utils.Angle.UpdatableAngle;
import HeadLibs.Helper.HMathHelper;
import HeadLibs.Helper.HMathHelper.BigDecimalHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;

public class BoundingBoxCuboid extends BoundingBoxBase {
    @Serial
    private static final long serialVersionUID = -4387064885692092859L;
    public static final String id = "BoundingBoxCuboid";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected final @NotNull UpdatableEntityPos bld = new UpdatableEntityPos();
    protected final @NotNull UpdatableAngle rotationX = new UpdatableAngle();
    protected final @NotNull UpdatableAngle rotationY = new UpdatableAngle();
    protected final @NotNull UpdatableAngle rotationZ = new UpdatableAngle();
    protected double length;
    protected double width;
    protected double height;

    /* f-front b-back  *
     * l-left  r-right *
     * u-up    d-down  */
    protected transient @Nullable EntityPos flu;
    protected transient @Nullable EntityPos fld;
    protected transient @Nullable EntityPos fru;
    protected transient @Nullable EntityPos frd;
    protected transient @Nullable EntityPos blu;
    protected transient @Nullable EntityPos bru;
    protected transient @Nullable EntityPos brd;
    protected transient @Nullable EntityPos center;
    protected transient double minRadius;
    protected transient double maxRadius;

    public @NotNull Angle getRotationX() {
        return this.rotationX;
    }

    public @NotNull Angle getRotationY() {
        return this.rotationY;
    }

    public @NotNull Angle getRotationZ() {
        return this.rotationZ;
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(double length) {
        double new_length = Math.max(length, 0.0D);
        if (Double.compare(this.length, new_length) != 0) {
            this.updated = true;
            this.length = new_length;
        }
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        double new_width = Math.max(width, 0.0D);
        if (Double.compare(this.width, new_width) != 0) {
            this.updated = true;
            this.width = new_width;
        }
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        double new_height = Math.max(height, 0.0D);
        if (Double.compare(this.height, new_height) != 0) {
            this.updated = true;
            this.height = new_height;
        }
    }

    @Override
    public void updatePos() {
        if (!this.getUpdated())
            return;
        if (this.baseBoundingBox != null)
            this.baseBoundingBox.updatePos();
        if (this.flu == null)
            this.flu = new EntityPos();// = new EntityPos(null, 1, 1, 0);
        if (this.fld == null)
            this.fld = new EntityPos();// = new EntityPos(null, 1, 0, 0);
        if (this.fru == null)
            this.fru = new EntityPos();// = new EntityPos(null, 1, 1, 1);
        if (this.frd == null)
            this.frd = new EntityPos();// = new EntityPos(null, 1, 0, 1);
        if (this.blu == null)
            this.blu = new EntityPos();// = new EntityPos(null, 0, 1, 0);
        if (this.bru == null)
            this.bru = new EntityPos();// = new EntityPos(null, 0, 1, 1);
        if (this.brd == null)
            this.brd = new EntityPos();// = new EntityPos(null, 0, 0, 1);
        if (this.center == null)
            this.center = new EntityPos();
        double xy = this.rotationX.cos() * this.rotationY.cos();
        double xz = this.rotationX.cos() * this.rotationZ.cos();
        double yz = this.rotationY.cos() * this.rotationZ.cos();
        BigDecimal wx = new BigDecimal(this.width * yz, CraftWorld.divideMc);
        BigDecimal wy = new BigDecimal(this.width * xz, CraftWorld.divideMc);
        BigDecimal wz = new BigDecimal(this.width * xy, CraftWorld.divideMc);
        BigDecimal lx = new BigDecimal(this.length * yz, CraftWorld.divideMc);
        BigDecimal ly = new BigDecimal(this.length * xz, CraftWorld.divideMc);
        BigDecimal lz = new BigDecimal(this.length * xy, CraftWorld.divideMc);
        BigDecimal hx = new BigDecimal(this.height * yz, CraftWorld.divideMc);
        BigDecimal hy = new BigDecimal(this.height * xz, CraftWorld.divideMc);
        BigDecimal hz = new BigDecimal(this.height * xy, CraftWorld.divideMc);
        this.brd.setFullX(this.bld.getFullX().add(wx));
        this.brd.setFullY(this.bld.getFullY().add(wy));
        this.brd.setFullZ(this.bld.getFullZ().add(wz));
        this.fld.setFullX(this.bld.getFullX().add(lx));
        this.fld.setFullY(this.bld.getFullY().add(ly));
        this.fld.setFullZ(this.bld.getFullZ().add(lz));
        this.blu.setFullX(this.bld.getFullX().add(hx));
        this.blu.setFullY(this.bld.getFullY().add(hy));
        this.blu.setFullZ(this.bld.getFullZ().add(hz));
        this.frd.setFullX(this.brd.getFullX().add(lx));
        this.frd.setFullY(this.brd.getFullY().add(ly));
        this.frd.setFullZ(this.brd.getFullZ().add(lz));
        this.bru.setFullX(this.blu.getFullX().add(wx));
        this.bru.setFullY(this.blu.getFullY().add(wy));
        this.bru.setFullZ(this.blu.getFullZ().add(wz));
        this.flu.setFullX(this.fld.getFullX().add(hx));
        this.flu.setFullY(this.fld.getFullY().add(hy));
        this.flu.setFullZ(this.fld.getFullZ().add(hz));
        this.fru.setFullX(this.frd.getFullX().add(hx));
        this.fru.setFullY(this.frd.getFullY().add(hy));
        this.fru.setFullZ(this.frd.getFullZ().add(hz));
        this.center.setFullX(this.bld.getFullX().add(this.fru.getFullX()).divide(BigDecimalHelper.BigDecimal_TWO, CraftWorld.divideMc));
        this.center.setFullY(this.bld.getFullY().add(this.fru.getFullY()).divide(BigDecimalHelper.BigDecimal_TWO, CraftWorld.divideMc));
        this.center.setFullZ(this.bld.getFullZ().add(this.fru.getFullZ()).divide(BigDecimalHelper.BigDecimal_TWO, CraftWorld.divideMc));
        this.minRadius = HMathHelper.min(new double[]{this.length, this.width, this.height}) / 2;
        this.maxRadius = this.bld.distance(this.center).doubleValue();
        this.setUpdated(false);
    }

    public @NotNull ImmutableEntityPos getFlu() {
        this.updatePos();
        assert this.flu != null;
        return this.flu.toImmutable();
    }

    public @NotNull ImmutableEntityPos getFld() {
        this.updatePos();
        assert this.fld != null;
        return this.fld.toImmutable();
    }

    public @NotNull ImmutableEntityPos getFru() {
        this.updatePos();
        assert this.fru != null;
        return this.fru.toImmutable();
    }

    public @NotNull ImmutableEntityPos getFrd() {
        this.updatePos();
        assert this.frd != null;
        return this.frd.toImmutable();
    }

    public @NotNull ImmutableEntityPos getBlu() {
        this.updatePos();
        assert this.blu != null;
        return this.blu.toImmutable();
    }

    public @NotNull UpdatableEntityPos getBld() {
        this.updatePos();
        return this.bld;
    }

    public @NotNull ImmutableEntityPos getBru() {
        this.updatePos();
        assert this.bru != null;
        return this.bru.toImmutable();
    }

    public @NotNull ImmutableEntityPos getBrd() {
        this.updatePos();
        assert this.brd != null;
        return this.brd.toImmutable();
    }

    @Override
    public @NotNull ImmutableEntityPos getCentrePos() {
        this.updatePos();
        assert this.center != null;
        return this.center.toImmutable();
    }

    @Override
    public double getMaxRadius() {
        this.updatePos();
        return this.maxRadius;
    }

    @Override
    public double getMinRadius() {
        this.updatePos();
        return this.minRadius;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.updated = true;
        if (!EntityPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.bld.read(input);
        if (!Angle.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.rotationX.read(input);
        if (!Angle.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.rotationY.read(input);
        if (!Angle.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.rotationZ.read(input);
        this.length = input.readDouble();
        this.width = input.readDouble();
        this.height = input.readDouble();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.bld.write(output);
        this.rotationX.write(output);
        this.rotationY.write(output);
        this.rotationZ.write(output);
        output.writeDouble(this.length);
        output.writeDouble(this.width);
        output.writeDouble(this.height);
        output.writeUTF(suffix);
    }

    @Override
    public boolean getUpdated() {
        if (this.bld.getUpdated())
            return true;
        if (this.rotationX.getUpdated())
            return true;
        if (this.rotationY.getUpdated())
            return true;
        if (this.rotationZ.getUpdated())
            return true;
        return super.getUpdated();
    }

    @Override
    public void setUpdated(boolean updated) {
        super.setUpdated(updated);
        this.bld.setUpdated(updated);
        this.rotationX.setUpdated(updated);
        this.rotationY.setUpdated(updated);
        this.rotationZ.setUpdated(updated);
    }

    @Override
    public @NotNull String toString() {
        this.updatePos();
        return "BoundingBoxCuboid{" +
                "flu=" + this.flu +
                ", fld=" + this.fld +
                ", fru=" + this.fru +
                ", frd=" + this.frd +
                ", blu=" + this.blu +
                ", bld=" + this.bld +
                ", bru=" + this.bru +
                ", brd=" + this.brd +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof BoundingBoxCuboid that)) return false;
        return this.rotationX.equals(that.rotationX) && this.rotationY.equals(that.rotationY) && this.rotationZ.equals(that.rotationZ) && Double.compare(this.length, that.length) == 0 && Double.compare(this.width, that.width) == 0 && Double.compare(this.height, that.height) == 0 && this.bld.equals(that.bld);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bld, this.rotationX, this.rotationY, this.rotationZ, this.length, this.width, this.height);
    }
}
