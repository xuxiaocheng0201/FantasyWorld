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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;

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

    private final EntityPos bld = new EntityPos();
    private int rotationX;
    private int rotationY;
    private int rotationZ;
    private int length;
    private int width;
    private int height;

    /* f-front b-back  */
    /* l-left  r-right */
    /* u-up    d-down  */
    private boolean updatedPos = true;
    private final transient EntityPos flu = new EntityPos();
    private final transient EntityPos fld = new EntityPos();
    private final transient EntityPos fru = new EntityPos();
    private final transient EntityPos frd = new EntityPos();
    private final transient EntityPos blu = new EntityPos();
    private final transient EntityPos bru = new EntityPos();
    private final transient EntityPos brd = new EntityPos();
    private final transient EntityPos center = new EntityPos();

    public void updateCenterPos() {
        if (this.updatedPos)
            return;
        //TODO
    }

    @Override
    public @NotNull EntityPos getCentrePos() {
        this.updateCenterPos();
        return this.center;
    }

    @Override
    public double getMaxRadius() {
        return 0;
    }

    @Override
    public double getMinRadius() {
        return 0;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.updatedPos = false;
        if (!EntityPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.bld.read(input);
        this.rotationX = input.readInt();
        this.rotationY = input.readInt();
        this.rotationZ = input.readInt();
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
        output.writeInt(this.rotationX);
        output.writeInt(this.rotationY);
        output.writeInt(this.rotationZ);
        output.writeInt(this.length);
        output.writeInt(this.width);
        output.writeInt(this.height);
        output.writeUTF(suffix);
    }
}
