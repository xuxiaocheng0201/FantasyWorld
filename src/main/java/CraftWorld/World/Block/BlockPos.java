package CraftWorld.World.Block;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Entity.EntityPos;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import HeadLibs.Helper.HMathHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings("unused")
public class BlockPos implements IDSTBase, Cloneable {
    @Serial
    private static final long serialVersionUID = -1091178102319329091L;
    public static final String id = "BlockPos";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, BlockPos.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    public static final int SAVE_RADIX = 16;

    private @NotNull ChunkPos chunkPos;
    private @Range(from = 0, to = Chunk.SIZE - 1) int x;
    private @Range(from = 0, to = Chunk.SIZE - 1) int y;
    private @Range(from = 0, to = Chunk.SIZE - 1) int z;

    public BlockPos(ChunkPos chunkPos) {
        this(chunkPos, 0, 0, 0);
    }

    public BlockPos(ChunkPos chunkPos, int x, int y, int z) {
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

    public @Range(from = 0, to = Chunk.SIZE - 1) int getX() {
        return this.x;
    }

    public @NotNull BigInteger getFullX() {
        return this.chunkPos.getBigX().multiply(Chunk.SIZE_BigInteger).add(BigInteger.valueOf(this.x));
    }

    public void setX(int x) {
        int chunk = HMathHelper.floorDivide(x, Chunk.SIZE);
        this.chunkPos.addX(chunk);
        this.x = (x - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullX(int x) {
        int chunk = HMathHelper.floorDivide(x, Chunk.SIZE);
        this.chunkPos.setX(chunk);
        this.x = (x - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullX(@Nullable BigInteger x) {
        if (x == null) {
            this.chunkPos.setX(BigInteger.ZERO);
            this.x = 0;
            return;
        }
        BigInteger chunk = HMathHelper.floorDivide(x, Chunk.SIZE_BigInteger);
        this.chunkPos.setX(chunk);
        this.x = x.subtract(chunk.multiply(Chunk.SIZE_BigInteger)).mod(Chunk.SIZE_BigInteger).intValue();
    }

    public @Range(from = 0, to = Chunk.SIZE - 1) int getY() {
        return this.y;
    }

    public @NotNull BigInteger getFullY() {
        return this.chunkPos.getBigY().multiply(Chunk.SIZE_BigInteger).add(BigInteger.valueOf(this.y));
    }

    public void setY(int y) {
        int chunk = HMathHelper.floorDivide(y, Chunk.SIZE);
        this.chunkPos.addY(chunk);
        this.y = (y - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullY(int y) {
        int chunk = HMathHelper.floorDivide(y, Chunk.SIZE);
        this.chunkPos.setY(chunk);
        this.y = (y - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullY(@Nullable BigInteger y) {
        if (y == null) {
            this.chunkPos.setY(BigInteger.ZERO);
            this.y = 0;
            return;
        }
        BigInteger chunk = HMathHelper.floorDivide(y, Chunk.SIZE_BigInteger);
        this.chunkPos.setY(chunk);
        this.y = y.subtract(chunk.multiply(Chunk.SIZE_BigInteger)).mod(Chunk.SIZE_BigInteger).intValue();
    }

    public @Range(from = 0, to = Chunk.SIZE - 1) int getZ() {
        return this.z;
    }

    public @NotNull BigInteger getFullZ() {
        return this.chunkPos.getBigZ().multiply(Chunk.SIZE_BigInteger).add(BigInteger.valueOf(this.z));
    }

    public void setZ(int z) {
        int chunk = HMathHelper.floorDivide(z, Chunk.SIZE);
        this.chunkPos.addZ(chunk);
        this.z = (z - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullZ(int z) {
        int chunk = HMathHelper.floorDivide(z, Chunk.SIZE);
        this.chunkPos.setZ(chunk);
        this.z = (z - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullZ(@Nullable BigInteger z) {
        if (z == null) {
            this.chunkPos.setZ(BigInteger.ZERO);
            this.z = 0;
            return;
        }
        BigInteger chunk = HMathHelper.floorDivide(z, Chunk.SIZE_BigInteger);
        this.chunkPos.setZ(chunk);
        this.z = z.subtract(chunk.multiply(Chunk.SIZE_BigInteger)).mod(Chunk.SIZE_BigInteger).intValue();
    }

    public void clear() {
        this.chunkPos.clear();
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void clearOffset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void set(@Nullable BlockPos pos) {
        if (pos == null) {
            this.clear();
            return;
        }
        this.chunkPos = pos.chunkPos.clone();
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public void set(@Nullable EntityPos pos) {
        if (pos == null) {
            this.clear();
            return;
        }
        this.chunkPos = pos.getChunkPos().clone();
        this.x = HMathHelper.floor(pos.getX());
        this.y = HMathHelper.floor(pos.getY());
        this.z = HMathHelper.floor(pos.getZ());
    }

    public void set(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(@Nullable ChunkPos pos, int x, int y, int z) {
        this.setChunkPos(pos);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void addX(int x) {
        this.setX(this.x + x);
    }

    public void addY(int y) {
        this.setY(this.y + y);
    }

    public void addZ(int z) {
        this.setZ(this.z + z);
    }

    public void add(int x, int y, int z) {
        this.setX(this.x + x);
        this.setY(this.y + y);
        this.setZ(this.z + z);
    }

    public void add(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.chunkPos.add(pos.chunkPos);
        this.setX(this.x + pos.x);
        this.setY(this.y + pos.y);
        this.setZ(this.z + pos.z);
    }

    public void addOffset(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.setX(this.x + pos.x);
        this.setY(this.y + pos.y);
        this.setZ(this.z + pos.z);
    }

    public void add(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.chunkPos.add(pos.getChunkPos());
        this.setX(this.x + HMathHelper.floor(pos.getX()));
        this.setY(this.y + HMathHelper.floor(pos.getY()));
        this.setZ(this.z + HMathHelper.floor(pos.getZ()));
    }

    public void addOffset(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.setX(this.x + HMathHelper.floor(pos.getX()));
        this.setY(this.y + HMathHelper.floor(pos.getY()));
        this.setZ(this.z + HMathHelper.floor(pos.getZ()));
    }

    public void subtractX(int x) {
        this.setX(this.x - x);
    }

    public void subtractY(int y) {
        this.setY(this.y - y);
    }

    public void subtractZ(int z) {
        this.setZ(this.z - z);
    }

    public void subtract(int x, int y, int z) {
        this.setX(this.x - x);
        this.setY(this.y - y);
        this.setZ(this.z - z);
    }

    public void subtract(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.chunkPos.subtract(pos.chunkPos);
        this.setX(this.x - pos.x);
        this.setY(this.y - pos.y);
        this.setZ(this.z - pos.z);
    }

    public void subtractOffset(@Nullable BlockPos pos) {
        if (pos == null)
            return;
        this.setX(this.x - pos.x);
        this.setY(this.y - pos.y);
        this.setZ(this.z - pos.z);
    }

    public void subtract(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.chunkPos.subtract(pos.getChunkPos());
        this.setX(this.x - HMathHelper.floor(pos.getX()));
        this.setY(this.y - HMathHelper.floor(pos.getY()));
        this.setZ(this.z - HMathHelper.floor(pos.getZ()));
    }

    public void subtractOffset(@Nullable EntityPos pos) {
        if (pos == null)
            return;
        this.setX(this.x - HMathHelper.floor(pos.getX()));
        this.setY(this.y - HMathHelper.floor(pos.getY()));
        this.setZ(this.z - HMathHelper.floor(pos.getZ()));
    }

    public void up() {
        this.addZ(1);
    }

    public void up(int n) {
        this.addZ(n);
    }

    public void down() {
        this.subtractZ(1);
    }

    public void down(int n) {
        this.subtractZ(n);
    }

    public void north() {
        this.addY(1);
    }

    public void north(int n) {
        this.addY(n);
    }

    public void south() {
        this.subtractY(1);
    }

    public void south(int n) {
        this.subtractY(n);
    }

    public void east() {
        this.addX(1);
    }

    public void east(int n) {
        this.addX(n);
    }

    public void west() {
        this.subtractX(1);
    }

    public void west(int n) {
        this.subtractX(n);
    }

    public void offset(EFacing facing) {
        switch (facing) {
            case UP -> this.up();
            case DOWN -> this.down();
            case EAST -> this.east();
            case WEST -> this.west();
            case NORTH -> this.north();
            case SOUTH -> this.south();
        }
    }

    public void offset(EFacing facing, int n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    @Override
    public void read(DataInput input) throws IOException {
        if (!ChunkPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.chunkPos.read(input);
        this.x = Integer.parseInt(input.readUTF(), SAVE_RADIX);
        this.y = Integer.parseInt(input.readUTF(), SAVE_RADIX);
        this.z = Integer.parseInt(input.readUTF(), SAVE_RADIX);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.chunkPos.write(output);
        output.writeUTF(Integer.toString(this.x, SAVE_RADIX));
        output.writeUTF(Integer.toString(this.y, SAVE_RADIX));
        output.writeUTF(Integer.toString(this.z, SAVE_RADIX));
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "BlockPos{" +
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
        BlockPos blockPos = (BlockPos) o;
        return this.x == blockPos.x && this.y == blockPos.y && this.z == blockPos.z && this.chunkPos.equals(blockPos.chunkPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chunkPos, this.x, this.y, this.z);
    }

    @Override
    public BlockPos clone() {
        BlockPos blockPos;
        try {
            blockPos = (BlockPos) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
        blockPos.chunkPos = this.chunkPos.clone();
        return blockPos;
    }

    public enum EFacing {
        NORTH, SOUTH, WEST, EAST, UP, DOWN
    }
}
