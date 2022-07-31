package CraftWorld.Utils;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Annotations.DoubleRange;
import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import HeadLibs.Helper.HMathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

@SuppressWarnings("MagicNumber")
public class Angle implements IDSTBase, Comparable<Angle> {
    @Serial
    private static final long serialVersionUID = -7098267381934801986L;
    public static final String id = "Angle";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    public static final double MIN_ANGLE = 0.0D;
    public static final double MAX_ANGLE = HMathHelper.DOUBLE_PI;

    protected @DoubleRange(minimum = MIN_ANGLE, maximum = MAX_ANGLE, maximum_equally = false) double angle;

    public Angle() {
        super();
    }

    public Angle(double angle) {
        super();
        this.setAngleRadian(angle);
    }

    public Angle(@Nullable Angle angle) {
        super();
        this.setAngleRadian(angle);
    }

    public double getAngleRadian() {
        return this.angle;
    }

    public double getAngleDegree() {
        return Math.toDegrees(this.angle);
    }

    public void setAngleRadian(double angle) {
        this.angle = HMathHelper.cyclicClamp(angle, MIN_ANGLE, MAX_ANGLE);
    }

    public void setAngleDegree(double angle) {
        this.angle = HMathHelper.cyclicClamp(Math.toRadians(angle), MIN_ANGLE, MAX_ANGLE);
    }

    public void setAngleRadian(@Nullable Angle angle) {
        if (angle == null)
            this.angle = 0.0D;
        else
            this.angle = angle.angle;
    }

    public void addAngleRadian(double angle) {
        this.angle += angle;
        if (this.angle < MIN_ANGLE || this.angle > MAX_ANGLE) // a bit quicker
            this.angle = HMathHelper.cyclicClamp(this.angle, MIN_ANGLE, MAX_ANGLE);
    }

    public void addAngleDegree(double angle) {
        this.addAngleRadian(Math.toRadians(angle));
    }

    public void addAngleRadian(@Nullable Angle angle) {
        if (angle == null)
            return;
        this.angle += angle.angle;
        if (this.angle < MIN_ANGLE || this.angle > MAX_ANGLE) // a bit quicker
            this.angle = HMathHelper.cyclicClamp(this.angle, MIN_ANGLE, MAX_ANGLE);
    }

    public void subAngleRadian(double angle) {
        this.angle -= angle;
        if (this.angle < MIN_ANGLE || this.angle > MAX_ANGLE) // a bit quicker
            this.angle = HMathHelper.cyclicClamp(this.angle, MIN_ANGLE, MAX_ANGLE);
    }

    public void subAngleDegree(double angle) {
        this.subAngleRadian(Math.toRadians(angle));
    }

    public void subAngleRadian(@Nullable Angle angle) {
        if (angle == null)
            return;
        this.angle -= angle.angle;
        if (this.angle < MIN_ANGLE || this.angle > MAX_ANGLE) // a bit quicker
            this.angle = HMathHelper.cyclicClamp(this.angle, MIN_ANGLE, MAX_ANGLE);
    }

    public double sin() {
        return HMathHelper.sin(this.angle);
    }

    public double cos() {
        return HMathHelper.cos(this.angle);
    }

    public double tan() {
        return HMathHelper.tan(this.angle);
    }

    public double cot() {
        return HMathHelper.cot(this.angle);
    }

    public @NotNull ImmutableAngle toImmutable() {
        return new ImmutableAngle(this);
    }

    public @NotNull UpdatableAngle toUpdatable() {
        return new UpdatableAngle(this);
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
        return "Angle:" + this.angle + " rad(" + this.angle * 180.0D / HMathHelper.PI + "°)";
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

    @Override
    public int compareTo(@NotNull Angle o) {
        return Double.compare(this.angle, o.angle);
    }

    public static class ImmutableAngle extends Angle implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(Angle.serialVersionUID);

        public ImmutableAngle() {
            super();
        }

        public ImmutableAngle(double angle) {
            super();
            super.setAngleRadian(angle);
        }

        public ImmutableAngle(@Nullable Angle angle) {
            super();
            super.setAngleRadian(angle);
        }

        @Override
        public void setAngleRadian(double angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAngleDegree(double angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAngleRadian(@Nullable Angle angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addAngleRadian(double angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addAngleDegree(double angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addAngleRadian(@Nullable Angle angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subAngleRadian(double angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subAngleDegree(double angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subAngleRadian(@Nullable Angle angle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutableAngle toImmutable() {
            return this;
        }
    }

    public static class UpdatableAngle extends Angle implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(Angle.serialVersionUID);

        protected boolean updated;

        public UpdatableAngle() {
            super();
        }

        public UpdatableAngle(double angle) {
            super();
            super.setAngleRadian(angle);
        }

        public UpdatableAngle(@Nullable Angle angle) {
            super();
            super.setAngleRadian(angle);
        }

        @Override
        public void setAngleRadian(double angle) {
            super.setAngleRadian(angle);
            this.updated = true;
        }

        @Override
        public void setAngleDegree(double angle) {
            super.setAngleDegree(angle);
            this.updated = true;
        }

        @Override
        public void setAngleRadian(@Nullable Angle angle) {
            super.setAngleRadian(angle);
            this.updated = true;
        }

        @Override
        public void addAngleRadian(double angle) {
            super.addAngleRadian(angle);
            this.updated = true;
        }

        @Override
        public void addAngleDegree(double angle) {
            super.addAngleDegree(angle);
            this.updated = true;
        }

        @Override
        public void addAngleRadian(@Nullable Angle angle) {
            super.addAngleRadian(angle);
            this.updated = true;
        }

        @Override
        public void subAngleRadian(double angle) {
            super.subAngleRadian(angle);
            this.updated = true;
        }

        @Override
        public void subAngleDegree(double angle) {
            super.subAngleDegree(angle);
            this.updated = true;
        }

        @Override
        public void subAngleRadian(@Nullable Angle angle) {
            super.subAngleRadian(angle);
            this.updated = true;
        }

        @Override
        public @NotNull UpdatableAngle toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            return this.updated;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
        }
    }
}
