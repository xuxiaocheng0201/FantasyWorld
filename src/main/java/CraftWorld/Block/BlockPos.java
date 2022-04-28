package CraftWorld.Block;

import CraftWorld.Chunk.Chunk;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
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
public class BlockPos implements IDSTBase {
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
//        this.setY(y);
//        this.setZ(z);
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
        return this.chunkPos.getBigX().multiply(Chunk.SIZE_B).add(BigInteger.valueOf(this.x));
    }

    public void setX(int x) {
        int chunk = HMathHelper.floorDivide(x, Chunk.SIZE);
        this.chunkPos.addX(chunk);
        this.x = (x - chunk * Chunk.SIZE) % Chunk.SIZE;
    }
/*
    public void setFullX(int x) {
        this.chunkPos.setX(x / Chunk.SIZE);
        this.x = x % Chunk.SIZE;
    }

    public @Range(from = 0, to = Chunk.SIZE - 1) int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.chunkPos.setY(y / Chunk.SIZE);
        this.y = y % Chunk.SIZE;
    }

    public @Range(from = 0, to = Chunk.SIZE - 1) int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.chunkPos.setZ(z / Chunk.SIZE);
        this.z = z % Chunk.SIZE;
    }

    public void clear() {
        this.chunkPos.clear();
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

    public void set(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(@Nullable BlockPos pos, int x, int y, int z) {
        this.set(pos);
        this.set(x, y, z);
    }

    public void addX(int x) {
        int t = this.x + x;
        this.chunkPos.addX(t / Chunk.SIZE);
        this.x = t % Chunk.SIZE;
    }

    public void addY(int y) {
        this.y = this.y.add(BigInteger.valueOf(y));
    }

    public void addZ(int z) {
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void add(int x, int y, int z) {
        this.x = this.x.add(BigInteger.valueOf(x));
        this.y = this.y.add(BigInteger.valueOf(y));
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void add(BlockPos pos) {
        this.x = this.x.add(pos.x);
        this.y = this.y.add(pos.y);
        this.z = this.z.add(pos.z);
    }

    public void subtractX(int x) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
    }

    public void subtractY(int y) {
        this.y = this.y.subtract(BigInteger.valueOf(y));
    }

    public void subtractZ(int z) {
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtract(int x, int y, int z) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
        this.y = this.y.subtract(BigInteger.valueOf(y));
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtract(BlockPos pos) {
        this.x = this.x.subtract(pos.x);
        this.y = this.y.subtract(pos.y);
        this.z = this.z.subtract(pos.z);
    }

    public void up() {
        this.y = this.y.add(BigInteger.ONE);
    }

    public void up(int n) {
        this.y = this.y.add(BigInteger.valueOf(n));
    }

    public void down() {
        this.y = this.y.subtract(BigInteger.ONE);
    }

    public void down(int n) {
        this.y = this.y.subtract(BigInteger.valueOf(n));
    }

    public void north() {
        this.x = this.x.add(BigInteger.ONE);
    }

    public void north(int n) {
        this.x = this.x.add(BigInteger.valueOf(n));
    }

    public void south() {
        this.x = this.x.subtract(BigInteger.ONE);
    }

    public void south(int n) {
        this.x = this.x.subtract(BigInteger.valueOf(n));
    }

    public void east() {
        this.z = this.z.add(BigInteger.ONE);
    }

    public void east(int n) {
        this.z = this.z.add(BigInteger.valueOf(n));
    }

    public void west() {
        this.z = this.z.subtract(BigInteger.ONE);
    }

    public void west(int n) {
        this.z = this.z.subtract(BigInteger.valueOf(n));
    }

    public void offset(ChunkPos.EFacing facing) {
        switch (facing) {
            case UP -> this.up();
            case DOWN -> this.down();
            case EAST -> this.east();
            case WEST -> this.west();
            case NORTH -> this.north();
            case SOUTH -> this.south();
        }
    }

    public void offset(ChunkPos.EFacing facing, int n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }
*/
    @Override
    public void read(DataInput input) throws IOException {
        this.x = Integer.parseInt(input.readUTF(), SAVE_RADIX);
        this.y = Integer.parseInt(input.readUTF(), SAVE_RADIX);
        this.z = Integer.parseInt(input.readUTF(), SAVE_RADIX);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
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


}
