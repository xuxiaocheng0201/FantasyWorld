package CraftWorld.Instance.Entity.BoundingBox;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BoundingBox.BoundingBoxUtils;
import CraftWorld.Entity.BoundingBox.IBoundingBoxBase;
import CraftWorld.Entity.EntityPos;
import CraftWorld.Utils.Angle;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class BoundingBoxCuboid implements IBoundingBoxBase {
    @Serial
    private static final long serialVersionUID = -4387064885692092859L;
    public static final String id = "BoundingBoxCuboid";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            BoundingBoxUtils.getInstance().register(id, BoundingBoxCuboid.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private final @NotNull EntityPos bld = new EntityPos();
    private @NotNull Angle rotationX = new Angle();
    private @NotNull Angle rotationY = new Angle();
    private @NotNull Angle rotationZ = new Angle();
    private int length;
    private int width;
    private int height;

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

    public int getLength() {
        return this.length;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
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
        //TODO
        //this.brd.setFullX(this.height * this.rotationX.cos());
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
        this.length = input.readInt();
        this.width = input.readInt();
        this.height = input.readInt();
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
        output.writeInt(this.length);
        output.writeInt(this.width);
        output.writeInt(this.height);
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
        return this.rotationX == that.rotationX && this.rotationY == that.rotationY && this.rotationZ == that.rotationZ && this.length == that.length && this.width == that.width && this.height == that.height && this.bld.equals(that.bld);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bld, this.rotationX, this.rotationY, this.rotationZ, this.length, this.width, this.height);
    }
}
