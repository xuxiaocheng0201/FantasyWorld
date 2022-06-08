package CraftWorld.Utils;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HMathHelper;
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

public class Angle implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -7098267381934801986L;
    public static final String id = "Angle";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, Angle.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private static final double MIN_ANGLE = 0.0D;
    private static final double MAX_ANGLE = 2 * HMathHelper.PI;

    private double angle;

    public Angle() {
        super();
    }

    public Angle(double angle) {
        super();
        this.setAngle(angle);
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = HMathHelper.cyclicClamp(angle, MIN_ANGLE, MAX_ANGLE);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.angle = input.readDouble();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeDouble(this.angle);
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "Angle:" + this.angle;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof Angle that)) return false;
        return Double.compare(that.angle, this.angle) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.angle);
    }
}
