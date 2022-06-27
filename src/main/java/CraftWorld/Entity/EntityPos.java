package CraftWorld.Entity;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.World.Block.BlockPos;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import HeadLibs.Helper.HMathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

@SuppressWarnings("unused")
public class EntityPos implements IDSTBase, Cloneable {
    @Serial
    private static final long serialVersionUID = 6848352277198863392L;
    public static final String id = "EntityPos";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    private @NotNull ChunkPos chunkPos;
    private double x;
    private double y;
    private double z;

    public EntityPos() {
        this(new ChunkPos(), 0, 0, 0);
    }

    public EntityPos(@Nullable ChunkPos chunkPos) {
        this(chunkPos, 0, 0, 0);
    }

    public EntityPos(@Nullable ChunkPos chunkPos, int x, int y, int z) {
        super();
        this.chunkPos = Objects.requireNonNullElseGet(chunkPos, ChunkPos::new);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public @NotNull ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public void setChunkPos(@Nullable ChunkPos chunkPos) {
        this.chunkPos = Objects.requireNonNullElseGet(chunkPos, ChunkPos::new);
    }

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
        BigInteger chunk = HMathHelper.BigDecimalHelper.floorDivide(x, Chunk.SIZE_BigDecimal);
        this.chunkPos.setX(chunk);
        this.x = x.subtract(new BigDecimal(chunk.multiply(Chunk.SIZE_BigInteger))).remainder(Chunk.SIZE_BigDecimal).doubleValue();
    }

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
        BigInteger chunk = HMathHelper.BigDecimalHelper.floorDivide(y, Chunk.SIZE_BigDecimal);
        this.chunkPos.setY(chunk);
        this.y = y.subtract(new BigDecimal(chunk.multiply(Chunk.SIZE_BigInteger))).remainder(Chunk.SIZE_BigDecimal).doubleValue();
    }

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
        BigInteger chunk = HMathHelper.BigDecimalHelper.floorDivide(z, Chunk.SIZE_BigDecimal);
        this.chunkPos.setZ(chunk);
        this.z = z.subtract(new BigDecimal(chunk.multiply(Chunk.SIZE_BigInteger))).remainder(Chunk.SIZE_BigDecimal).doubleValue();
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

    public void set(@Nullable EntityPos pos) {
        if (pos == null) {
            this.clear();
            return;
        }
        this.chunkPos.set(pos.chunkPos);
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public void set(@Nullable BlockPos pos) {
        if (pos == null) {
            this.clear();
            return;
        }
        this.chunkPos = pos.getChunkPos().clone();
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public void set(double x, double y, double z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(@Nullable ChunkPos pos, double x, double y, double z) {
        this.setChunkPos(pos);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void addX(double x) {
        this.setX(this.x + x);
    }

    public void addY(double y) {
        this.setY(this.y + y);
    }

    public void addZ(double z) {
        this.setZ(this.z + z);
    }

    public void add(double x, double y, double z) {
        this.setX(this.x + x);
        this.setY(this.y + y);
        this.setZ(this.z + z);
    }

    public void add(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.chunkPos.add(pos.chunkPos);
        this.setX(this.x + pos.x);
        this.setY(this.y + pos.y);
        this.setZ(this.z + pos.z);
    }

    public void addOffset(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.setX(this.x + pos.x);
        this.setY(this.y + pos.y);
        this.setZ(this.z + pos.z);
    }

    public void add(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.chunkPos.add(pos.getChunkPos());
        this.setX(this.x + pos.getX());
        this.setY(this.y + pos.getY());
        this.setZ(this.z + pos.getZ());
    }

    public void addOffset(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.setX(this.x + pos.getX());
        this.setY(this.y + pos.getY());
        this.setZ(this.z + pos.getZ());
    }

    public void subtractX(double x) {
        this.setX(this.x - x);
    }

    public void subtractY(double y) {
        this.setY(this.y - y);
    }

    public void subtractZ(double z) {
        this.setZ(this.z - z);
    }

    public void subtract(double x, double y, double z) {
        this.setX(this.x - x);
        this.setY(this.y - y);
        this.setZ(this.z - z);
    }

    public void subtract(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.chunkPos.subtract(pos.chunkPos);
        this.setX(this.x - pos.x);
        this.setY(this.y - pos.y);
        this.setZ(this.z - pos.z);
    }

    public void subtractOffset(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.setX(this.x - pos.x);
        this.setY(this.y - pos.y);
        this.setZ(this.z - pos.z);
    }

    public void subtract(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.chunkPos.subtract(pos.getChunkPos());
        this.setX(this.x - pos.getX());
        this.setY(this.y - pos.getY());
        this.setZ(this.z - pos.getZ());
    }

    public void subtractOffset(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.setX(this.x - pos.getX());
        this.setY(this.y - pos.getY());
        this.setZ(this.z - pos.getZ());
    }

    public BigDecimal distance(@Nullable EntityPos that) {
        if (that == null)
            return this.getFullX().multiply(this.getFullX())
                    .add(this.getFullY().multiply(this.getFullY()))
                    .add(this.getFullZ().multiply(this.getFullZ()))
                    .sqrt(new MathContext(ConstantStorage.CALCULATE_DECIMAL_DEGREE, RoundingMode.HALF_UP));
        EntityPos delta = new EntityPos();
        delta.setFullX(this.getFullX().subtract(that.getFullX()));
        delta.setFullY(this.getFullY().subtract(that.getFullY()));
        delta.setFullZ(this.getFullZ().subtract(that.getFullZ()));
        return delta.distance(null);
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
    public String toString() {
        return "EntityPos{" +
                "chunkPos=" + this.chunkPos +
                ", x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        EntityPos entityPos = (EntityPos) o;
        return Double.compare(entityPos.x, this.x) == 0 && Double.compare(entityPos.y, this.y) == 0 && Double.compare(entityPos.z, this.z) == 0 && this.chunkPos.equals(entityPos.chunkPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chunkPos, this.x, this.y, this.z);
    }

    @Override
    public EntityPos clone() {
        EntityPos entityPos;
        try {
            entityPos = (EntityPos) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
        entityPos.chunkPos = this.chunkPos.clone();
        return entityPos;
    }
}
