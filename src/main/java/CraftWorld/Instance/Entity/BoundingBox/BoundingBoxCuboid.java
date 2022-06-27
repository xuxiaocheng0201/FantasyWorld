package CraftWorld.Instance.Entity.BoundingBox;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BoundingBox.IBoundingBoxBase;
import CraftWorld.Entity.EntityPos;
import CraftWorld.Utils.Angle;
import HeadLibs.Helper.HMathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BoundingBoxCuboid implements IBoundingBoxBase {
    @Serial
    private static final long serialVersionUID = -4387064885692092859L;
    public static final String id = "BoundingBoxCuboid";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    private final @NotNull EntityPos bld = new EntityPos();
    private final @NotNull Angle rotationX = new Angle();
    private final @NotNull Angle rotationY = new Angle();
    private final @NotNull Angle rotationZ = new Angle();
    private double length;
    private double width;
    private double height;

    /* f-front b-back  */
    /* l-left  r-right */
    /* u-up    d-down  */
    private boolean updatedPos = true;
    private transient @Nullable EntityPos flu;
    private transient @Nullable EntityPos fld;
    private transient @Nullable EntityPos fru;
    private transient @Nullable EntityPos frd;
    private transient @Nullable EntityPos blu;
    private transient @Nullable EntityPos bru;
    private transient @Nullable EntityPos brd;
    private transient @Nullable EntityPos center;
    private transient double minRadius;
    private transient double maxRadius;

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

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public void updatePos() {
        if (this.updatedPos)
            return;
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
        BigDecimal wx = new BigDecimal(this.width * yz);
        BigDecimal wy = new BigDecimal(this.width * xz);
        BigDecimal wz = new BigDecimal(this.width * xy);
        BigDecimal lx = new BigDecimal(this.length * yz);
        BigDecimal ly = new BigDecimal(this.length * xz);
        BigDecimal lz = new BigDecimal(this.length * xy);
        BigDecimal hx = new BigDecimal(this.height * yz);
        BigDecimal hy = new BigDecimal(this.height * xz);
        BigDecimal hz = new BigDecimal(this.height * xy);
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
        this.center.setFullX(this.bld.getFullX().add(this.fru.getFullX()).divide(HMathHelper.BigDecimalHelper.BigDecimal_TWO, ConstantStorage.CALCULATE_DECIMAL_DEGREE, RoundingMode.FLOOR));
        this.center.setFullY(this.bld.getFullY().add(this.fru.getFullY()).divide(HMathHelper.BigDecimalHelper.BigDecimal_TWO, ConstantStorage.CALCULATE_DECIMAL_DEGREE, RoundingMode.FLOOR));
        this.center.setFullZ(this.bld.getFullZ().add(this.fru.getFullZ()).divide(HMathHelper.BigDecimalHelper.BigDecimal_TWO, ConstantStorage.CALCULATE_DECIMAL_DEGREE, RoundingMode.FLOOR));
        this.minRadius = HMathHelper.min(new double[]{this.length, this.width, this.height}) / 2;
        this.maxRadius = this.bld.distance(this.center).doubleValue();
        this.updatedPos = true;
    }

    public @NotNull EntityPos getFlu() {
        this.updatePos();
        assert this.flu != null;
        return this.flu;
    }

    public @NotNull EntityPos getFld() {
        this.updatePos();
        assert this.fld != null;
        return this.fld;
    }

    public @NotNull EntityPos getFru() {
        this.updatePos();
        assert this.fru != null;
        return this.fru;
    }

    public @NotNull EntityPos getFrd() {
        this.updatePos();
        assert this.frd != null;
        return this.frd;
    }

    public @NotNull EntityPos getBlu() {
        this.updatePos();
        assert this.blu != null;
        return this.blu;
    }

    public @NotNull EntityPos getBld() {
        this.updatePos();
        return this.bld;
    }

    public @NotNull EntityPos getBru() {
        this.updatePos();
        assert this.bru != null;
        return this.bru;
    }

    public @NotNull EntityPos getBrd() {
        this.updatePos();
        assert this.brd != null;
        return this.brd;
    }

    @Override
    public @NotNull EntityPos getCentrePos() {
        this.updatePos();
        assert this.center != null;
        return this.center;
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
    public @Nullable IBoundingBoxBase getBaseBoundingBox() {
        return null;
    }

    @Override
    public @NotNull Set<IBoundingBoxBase> getAdditionalBoundingBox() {
        return new HashSet<>();
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.updatedPos = false;
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
