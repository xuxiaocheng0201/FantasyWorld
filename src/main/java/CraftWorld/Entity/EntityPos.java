package CraftWorld.Entity;

import CraftWorld.CraftWorld;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.World.Block.BlockDirection;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import HeadLibs.Annotations.DoubleRange;
import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import HeadLibs.Helper.HMathHelper;
import HeadLibs.Helper.HMathHelper.BigDecimalHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Immutable version: {@link ImmutableEntityPos}
 * Updatable version: {@link UpdatableEntityPos}
 */
public class EntityPos implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 6848352277198863392L;
    public static final DSTId id = DSTId.getDstIdInstance("EntityPos");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull ChunkPos chunkPos = new ChunkPos();
    protected @DoubleRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false) double x;
    protected @DoubleRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false) double y;
    protected @DoubleRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false) double z;

    public EntityPos() {
        super();
    }

    public EntityPos(double x, double y, double z) {
        super();
        this.set(x, y, z);
    }

    public EntityPos(@Nullable BigDecimal x, @Nullable BigDecimal y, @Nullable BigDecimal z) {
        super();
        this.set(x, y, z);
    }

    public EntityPos(@Nullable ChunkPos chunkPos, double x, double y, double z) {
        super();
        this.set(chunkPos, x, y, z);
    }

    public EntityPos(@Nullable ChunkPos chunkPos) {
        super();
        this.chunkPos.set(chunkPos);
    }

    public EntityPos(@Nullable EntityPos entityPos) {
        super();
        this.set(entityPos);
    }

    public void clear() {
        this.chunkPos.clear();
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public void clearOffset() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public @NotNull ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public void setChunkPos(@Nullable ChunkPos chunkPos) {
        this.chunkPos.set(chunkPos);
    }

    @DoubleRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false)
    public double getX() {
        return this.x;
    }

    public @NotNull BigDecimal getFullX() {
        return (new BigDecimal(this.chunkPos.getBigX().multiply(Chunk.SIZE_BigInteger))).add(BigDecimal.valueOf(this.x));
    }

    public void setX(double x) {
        int chunk = HMathHelper.floorDivide(x, Chunk.SIZE);
        this.chunkPos.addX(chunk);
        this.x = (x - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setClampX(double x) {
        this.x = HMathHelper.cyclicClamp(x, 0, Chunk.SIZE);
    }

    public void setFullX(double x) {
        int chunk = HMathHelper.floorDivide(x, Chunk.SIZE);
        this.chunkPos.setX(chunk);
        this.x = (x - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullX(@Nullable BigDecimal x) {
        if (x == null) {
            this.chunkPos.setX(BigInteger.ZERO);
            this.x = 0.0D;
            return;
        }
        BigInteger chunk = BigDecimalHelper.floorDivide(x, Chunk.SIZE_BigDecimal);
        this.chunkPos.setX(chunk);
        this.x = x.subtract(new BigDecimal(chunk.multiply(Chunk.SIZE_BigInteger))).remainder(Chunk.SIZE_BigDecimal).doubleValue();
    }

    @DoubleRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false)
    public double getY() {
        return this.y;
    }

    public @NotNull BigDecimal getFullY() {
        return (new BigDecimal(this.chunkPos.getBigY().multiply(Chunk.SIZE_BigInteger))).add(BigDecimal.valueOf(this.y));
    }

    public void setY(double y) {
        int chunk = HMathHelper.floorDivide(y, Chunk.SIZE);
        this.chunkPos.addY(chunk);
        this.y = (y - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setClampY(double y) {
        this.y = HMathHelper.cyclicClamp(y, 0, Chunk.SIZE);
    }

    public void setFullY(double y) {
        int chunk = HMathHelper.floorDivide(y, Chunk.SIZE);
        this.chunkPos.setY(chunk);
        this.y = (y - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullY(@Nullable BigDecimal y) {
        if (y == null) {
            this.chunkPos.setY(BigInteger.ZERO);
            this.y = 0.0D;
            return;
        }
        BigInteger chunk = BigDecimalHelper.floorDivide(y, Chunk.SIZE_BigDecimal);
        this.chunkPos.setY(chunk);
        this.y = y.subtract(new BigDecimal(chunk.multiply(Chunk.SIZE_BigInteger))).remainder(Chunk.SIZE_BigDecimal).doubleValue();
    }

    @DoubleRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false)
    public double getZ() {
        return this.z;
    }

    public @NotNull BigDecimal getFullZ() {
        return (new BigDecimal(this.chunkPos.getBigZ().multiply(Chunk.SIZE_BigInteger))).add(BigDecimal.valueOf(this.z));
    }

    public void setZ(double z) {
        int chunk = HMathHelper.floorDivide(z, Chunk.SIZE);
        this.chunkPos.addZ(chunk);
        this.z = (z - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setClampZ(double z) {
        this.z = HMathHelper.cyclicClamp(z, 0, Chunk.SIZE);
    }

    public void setFullZ(double z) {
        int chunk = HMathHelper.floorDivide(z, Chunk.SIZE);
        this.chunkPos.setZ(chunk);
        this.z = (z - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullZ(@Nullable BigDecimal z) {
        if (z == null) {
            this.chunkPos.setZ(BigInteger.ZERO);
            this.z = 0.0D;
            return;
        }
        BigInteger chunk = BigDecimalHelper.floorDivide(z, Chunk.SIZE_BigDecimal);
        this.chunkPos.setZ(chunk);
        this.z = z.subtract(new BigDecimal(chunk.multiply(Chunk.SIZE_BigInteger))).remainder(Chunk.SIZE_BigDecimal).doubleValue();
    }

    public void set(double x, double y, double z) {
        this.setFullX(x);
        this.setFullY(y);
        this.setFullZ(z);
    }

    public void set(@Nullable BigDecimal x, @Nullable BigDecimal y, @Nullable BigDecimal z) {
        this.setFullX(x);
        this.setFullY(y);
        this.setFullZ(z);
    }

    public void set(@Nullable ChunkPos chunkPos, double x, double y, double z) {
        this.chunkPos.set(chunkPos);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(@Nullable EntityPos entityPos) {
        if (entityPos == null) {
            this.chunkPos.clear();
            this.x = 0.0D;
            this.y = 0.0D;
            this.z = 0.0D;
        } else {
            this.chunkPos.set(entityPos.chunkPos);
            this.x = entityPos.x;
            this.y = entityPos.y;
            this.z = entityPos.z;
        }
    }

    public void setOffset(double x, double y, double z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void setClamp(double x, double y, double z) {
        this.setClampX(x);
        this.setClampY(y);
        this.setClampZ(z);
    }

    public void addX(double x) {
        this.setX(this.x + x);
    }

    public void addClampX(double x) {
        this.setClampX(this.x + x);
    }

    public void addY(double y) {
        this.setY(this.y + y);
    }

    public void addClampY(double y) {
        this.setClampY(this.y + y);
    }

    public void addZ(double z) {
        this.setZ(this.z + z);
    }

    public void addClampZ(double z) {
        this.setClampZ(this.z + z);
    }

    public void add(double x, double y, double z) {
        this.setX(this.x + x);
        this.setY(this.y + y);
        this.setZ(this.z + z);
    }

    public void addClamp(double x, double y, double z) {
        this.setClampX(this.x + x);
        this.setClampY(this.y + y);
        this.setClampZ(this.z + z);
    }

    public void add(@Nullable EntityPos entityPos) {
        if (entityPos == null)
            return;
        this.chunkPos.add(entityPos.chunkPos);
        this.setX(this.x + entityPos.x);
        this.setY(this.y + entityPos.y);
        this.setZ(this.z + entityPos.z);
    }

    public void addOffset(@Nullable EntityPos entityPos) {
        if (entityPos == null)
            return;
        this.setX(this.x + entityPos.x);
        this.setY(this.y + entityPos.y);
        this.setZ(this.z + entityPos.z);
    }

    public void subtractX(double x) {
        this.setX(this.x - x);
    }

    public void subtractClampX(double x) {
        this.setClampX(this.x - x);
    }

    public void subtractY(double y) {
        this.setY(this.y - y);
    }

    public void subtractClampY(double y) {
        this.setClampY(this.y - y);
    }

    public void subtractZ(double z) {
        this.setZ(this.z - z);
    }

    public void subtractClampZ(double z) {
        this.setClampZ(this.z - z);
    }

    public void subtract(double x, double y, double z) {
        this.setX(this.x - x);
        this.setY(this.y - y);
        this.setZ(this.z - z);
    }

    public void subtractClamp(double x, double y, double z) {
        this.setClampX(this.x - x);
        this.setClampY(this.y - y);
        this.setClampZ(this.z - z);
    }

    public void subtract(@Nullable EntityPos entityPos) {
        if (entityPos == null)
            return;
        this.chunkPos.subtract(entityPos.chunkPos);
        this.setX(this.x - entityPos.x);
        this.setY(this.y - entityPos.y);
        this.setZ(this.z - entityPos.z);
    }

    public void subtractOffset(@Nullable EntityPos entityPos) {
        if (entityPos == null)
            return;
        this.setX(this.x - entityPos.x);
        this.setY(this.y - entityPos.y);
        this.setZ(this.z - entityPos.z);
    }

    public void up(double n) {
        this.addZ(n);
    }

    public void down(double n) {
        this.subtractZ(n);
    }

    public void north(double n) {
        this.addY(n);
    }

    public void south(double n) {
        this.subtractY(n);
    }

    public void east(double n) {
        this.addX(n);
    }

    public void west(double n) {
        this.subtractX(n);
    }

    public void offset(@NotNull BlockDirection facing, double n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    public @NotNull ImmutableEntityPos toImmutable() {
        return new ImmutableEntityPos(this);
    }

    public @NotNull UpdatableEntityPos toUpdatable() {
        return new UpdatableEntityPos(this);
    }

    public @NotNull BigDecimal distance(@Nullable EntityPos that) {
        BigDecimal x, y, z;
        if (that == null) {
            x = this.getFullX();
            y = this.getFullY();
            z = this.getFullZ();
        } else {
            x = this.getFullX().subtract(that.getFullX());
            y = this.getFullY().subtract(that.getFullY());
            z = this.getFullZ().subtract(that.getFullZ());
        }
        return x.multiply(x).add(y.multiply(y)).add(z.multiply(z)).sqrt(CraftWorld.divideMc);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        if (!ChunkPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.chunkPos.read(input);
        this.x = input.readDouble();
        this.y = input.readDouble();
        this.z = input.readDouble();
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.chunkPos.write(output);
        output.writeDouble(this.x);
        output.writeDouble(this.y);
        output.writeDouble(this.z);
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "EntityPos{" +
                "chunkPos=" + this.chunkPos +
                ", x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityPos that)) return false;
        return Double.compare(that.x, this.x) == 0 && Double.compare(that.y, this.y) == 0 && Double.compare(that.z, this.z) == 0 && this.chunkPos.equals(that.chunkPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chunkPos, this.x, this.y, this.z);
    }

    public static class ImmutableEntityPos extends EntityPos implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(EntityPos.serialVersionUID);

        protected @NotNull BigDecimal fullX = BigDecimal.ZERO;
        protected @NotNull BigDecimal fullY = BigDecimal.ZERO;
        protected @NotNull BigDecimal fullZ = BigDecimal.ZERO;

        protected void init() {
            this.chunkPos = this.chunkPos.toImmutable();
            this.fullX = super.getFullX();
            this.fullY = super.getFullY();
            this.fullZ = super.getFullZ();
        }

        public ImmutableEntityPos() {
            super();
            this.init();
        }

        public ImmutableEntityPos(double x, double y, double z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public ImmutableEntityPos(@Nullable BigDecimal x, @Nullable BigDecimal y, @Nullable BigDecimal z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public ImmutableEntityPos(@Nullable ChunkPos chunkPos, double x, double y, double z) {
            super();
            super.set(chunkPos, x, y, z);
            this.init();
        }

        public ImmutableEntityPos(@Nullable ChunkPos chunkPos) {
            super();
            super.chunkPos.set(chunkPos);
            this.init();
        }

        public ImmutableEntityPos(@Nullable EntityPos entityPos) {
            super();
            super.set(entityPos);
            this.init();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clearOffset() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setChunkPos(@Nullable ChunkPos chunkPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull BigDecimal getFullX() {
            return this.fullX;
        }

        @Override
        public void setX(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setClampX(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullX(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullX(@Nullable BigDecimal x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull BigDecimal getFullY() {
            return this.fullY;
        }

        @Override
        public void setY(double y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setClampY(double y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullY(double y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullY(@Nullable BigDecimal y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull BigDecimal getFullZ() {
            return this.fullZ;
        }

        @Override
        public void setZ(double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setClampZ(double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullZ(double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullZ(@Nullable BigDecimal z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable BigDecimal x, @Nullable BigDecimal y, @Nullable BigDecimal z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable ChunkPos chunkPos, double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable EntityPos entityPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setOffset(double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setClamp(double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addX(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addClampX(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addY(double y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addClampY(double y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addZ(double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addClampZ(double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addClamp(double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(@Nullable EntityPos entityPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addOffset(@Nullable EntityPos entityPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractX(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractClampX(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractY(double y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractClampY(double y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractZ(double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractClampZ(double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtract(double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractClamp(double x, double y, double z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtract(@Nullable EntityPos entityPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractOffset(@Nullable EntityPos entityPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void up(double n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void down(double n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void north(double n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void south(double n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void east(double n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void west(double n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void offset(@NotNull BlockDirection facing, double n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutableEntityPos toImmutable() {
            return this;
        }
    }

    public static class UpdatableEntityPos extends EntityPos implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(EntityPos.serialVersionUID);

        protected boolean updated;

        protected void init() {
            this.chunkPos = this.chunkPos.toUpdatable();
        }

        public UpdatableEntityPos() {
            super();
            this.init();
        }

        public UpdatableEntityPos(double x, double y, double z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public UpdatableEntityPos(@Nullable BigDecimal x, @Nullable BigDecimal y, @Nullable BigDecimal z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public UpdatableEntityPos(@Nullable ChunkPos chunkPos, double x, double y, double z) {
            super();
            super.set(chunkPos, x, y, z);
            this.init();
        }

        public UpdatableEntityPos(@Nullable ChunkPos chunkPos) {
            super();
            super.chunkPos.set(chunkPos);
            this.init();
        }

        public UpdatableEntityPos(@Nullable EntityPos entityPos) {
            super();
            super.set(entityPos);
            this.init();
        }

        @Override
        public void clear() {
            super.clear();
            this.updated = true;
        }

        @Override
        public void clearOffset() {
            super.clearOffset();
            this.updated = true;
        }

        @Override
        public void setChunkPos(@Nullable ChunkPos chunkPos) {
            super.setChunkPos(chunkPos);
            this.updated = true;
        }

        @Override
        public void setX(double x) {
            super.setX(x);
            this.updated = true;
        }

        @Override
        public void setClampX(double x) {
            super.setClampX(x);
            this.updated = true;
        }

        @Override
        public void setFullX(double x) {
            super.setFullX(x);
            this.updated = true;
        }

        @Override
        public void setFullX(@Nullable BigDecimal x) {
            super.setFullX(x);
            this.updated = true;
        }

        @Override
        public void setY(double y) {
            super.setY(y);
            this.updated = true;
        }

        @Override
        public void setClampY(double y) {
            super.setClampY(y);
            this.updated = true;
        }

        @Override
        public void setFullY(double y) {
            super.setFullY(y);
            this.updated = true;
        }

        @Override
        public void setFullY(@Nullable BigDecimal y) {
            super.setFullY(y);
            this.updated = true;
        }

        @Override
        public void setZ(double z) {
            super.setZ(z);
            this.updated = true;
        }

        @Override
        public void setClampZ(double z) {
            super.setClampZ(z);
            this.updated = true;
        }

        @Override
        public void setFullZ(double z) {
            super.setFullZ(z);
            this.updated = true;
        }

        @Override
        public void setFullZ(@Nullable BigDecimal z) {
            super.setFullZ(z);
            this.updated = true;
        }

        @Override
        public void set(double x, double y, double z) {
            super.set(x, y, z);
            this.updated = true;
        }

        @Override
        public void set(@Nullable BigDecimal x, @Nullable BigDecimal y, @Nullable BigDecimal z) {
            super.set(x, y, z);
            this.updated = true;
        }

        @Override
        public void set(@Nullable ChunkPos chunkPos, double x, double y, double z) {
            super.set(chunkPos, x, y, z);
            this.updated = true;
        }

        @Override
        public void set(@Nullable EntityPos entityPos) {
            super.set(entityPos);
            this.updated = true;
        }

        @Override
        public void setOffset(double x, double y, double z) {
            super.setOffset(x, y, z);
            this.updated = true;
        }

        @Override
        public void setClamp(double x, double y, double z) {
            super.setClamp(x, y, z);
            this.updated = true;
        }

        @Override
        public void addX(double x) {
            super.addX(x);
            this.updated = true;
        }

        @Override
        public void addClampX(double x) {
            super.addClampX(x);
            this.updated = true;
        }

        @Override
        public void addY(double y) {
            super.addY(y);
            this.updated = true;
        }

        @Override
        public void addClampY(double y) {
            super.addClampY(y);
            this.updated = true;
        }

        @Override
        public void addZ(double z) {
            super.addZ(z);
            this.updated = true;
        }

        @Override
        public void addClampZ(double z) {
            super.addClampZ(z);
            this.updated = true;
        }

        @Override
        public void add(double x, double y, double z) {
            super.add(x, y, z);
            this.updated = true;
        }

        @Override
        public void addClamp(double x, double y, double z) {
            super.addClamp(x, y, z);
            this.updated = true;
        }

        @Override
        public void add(@Nullable EntityPos entityPos) {
            super.add(entityPos);
            this.updated = true;
        }

        @Override
        public void addOffset(@Nullable EntityPos entityPos) {
            super.addOffset(entityPos);
            this.updated = true;
        }

        @Override
        public void subtractX(double x) {
            super.subtractX(x);
            this.updated = true;
        }

        @Override
        public void subtractClampX(double x) {
            super.subtractClampX(x);
            this.updated = true;
        }

        @Override
        public void subtractY(double y) {
            super.subtractY(y);
            this.updated = true;
        }

        @Override
        public void subtractClampY(double y) {
            super.subtractClampY(y);
            this.updated = true;
        }

        @Override
        public void subtractZ(double z) {
            super.subtractZ(z);
            this.updated = true;
        }

        @Override
        public void subtractClampZ(double z) {
            super.subtractClampZ(z);
            this.updated = true;
        }

        @Override
        public void subtract(double x, double y, double z) {
            super.subtract(x, y, z);
            this.updated = true;
        }

        @Override
        public void subtractClamp(double x, double y, double z) {
            super.subtractClamp(x, y, z);
            this.updated = true;
        }

        @Override
        public void subtract(@Nullable EntityPos entityPos) {
            super.subtract(entityPos);
            this.updated = true;
        }

        @Override
        public void subtractOffset(@Nullable EntityPos entityPos) {
            super.subtractOffset(entityPos);
            this.updated = true;
        }

        @Override
        public void up(double n) {
            super.up(n);
            this.updated = true;
        }

        @Override
        public void down(double n) {
            super.down(n);
            this.updated = true;
        }

        @Override
        public void north(double n) {
            super.north(n);
            this.updated = true;
        }

        @Override
        public void south(double n) {
            super.south(n);
            this.updated = true;
        }

        @Override
        public void east(double n) {
            super.east(n);
            this.updated = true;
        }

        @Override
        public void west(double n) {
            super.west(n);
            this.updated = true;
        }

        @Override
        public void offset(@NotNull BlockDirection facing, double n) {
            super.offset(facing, n);
            this.updated = true;
        }

        @Override
        public @NotNull UpdatableEntityPos toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            if (((IUpdatable) this.chunkPos).getUpdated())
                return true;
            return this.updated;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
            ((IUpdatable) this.chunkPos).setUpdated(updated);
        }
    }
}
