package CraftWorld.Chunk;

import CraftWorld.Block.BlockPos;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings("unused")
public class ChunkPos implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 1974205833401624407L;
    public static final String id = "ChunkPos";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, ChunkPos.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    public static final int SAVE_RADIX = 16;

    private BigInteger x, y, z;

    public ChunkPos() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public ChunkPos(int x, int y, int z) {
        super();
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public ChunkPos(BigInteger x, BigInteger y, BigInteger z) {
        super();
        this.x = Objects.requireNonNullElse(x, BigInteger.ZERO);
        this.y = Objects.requireNonNullElse(y, BigInteger.ZERO);
        this.z = Objects.requireNonNullElse(z, BigInteger.ZERO);
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.x = new BigInteger(input.readUTF(), SAVE_RADIX);
        this.y = new BigInteger(input.readUTF(), SAVE_RADIX);
        this.z = new BigInteger(input.readUTF(), SAVE_RADIX);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.x.toString(SAVE_RADIX));
        output.writeUTF(this.y.toString(SAVE_RADIX));
        output.writeUTF(this.z.toString(SAVE_RADIX));
        output.writeUTF(suffix);
    }

    public void clear() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
        this.z = BigInteger.ZERO;
    }

    public void setX(int x) {
        this.x = BigInteger.valueOf(x);
    }

    public void setX(BigInteger x) {
        this.x = Objects.requireNonNullElse(x, BigInteger.ZERO);
    }

    public void setY(int y) {
        this.y = BigInteger.valueOf(y);
    }

    public void setY(BigInteger y) {
        this.y = Objects.requireNonNullElse(y, BigInteger.ZERO);
    }

    public void setZ(int z) {
        this.z = BigInteger.valueOf(z);
    }

    public void setZ(BigInteger z) {
        this.z = Objects.requireNonNullElse(z, BigInteger.ZERO);
    }

    public void set(int x, int y, int z) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public void set(BigInteger x, BigInteger y, BigInteger z) {
        this.x = Objects.requireNonNullElse(x, BigInteger.ZERO);
        this.y = Objects.requireNonNullElse(y, BigInteger.ZERO);
        this.z = Objects.requireNonNullElse(z, BigInteger.ZERO);
    }

    public void set(ChunkPos pos) {
        if (pos == null) {
            this.clear();
            return;
        }
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public int getX() {
        return this.x.intValue();
    }

    public BigInteger getBigX() {
        return this.x;
    }

    public int getY() {
        return this.y.intValue();
    }

    public BigInteger getBigY() {
        return this.y;
    }

    public int getZ() {
        return this.z.intValue();
    }

    public BigInteger getBigZ() {
        return this.z;
    }

    public void addX(int x) {
        this.x = this.x.add(BigInteger.valueOf(x));
    }

    public void addX(BigInteger x) {
        this.x = this.x.add(x);
    }

    public void addY(int y) {
        this.y = this.y.add(BigInteger.valueOf(y));
    }

    public void addY(BigInteger y) {
        this.y = this.y.add(y);
    }

    public void addZ(int z) {
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void addZ(BigInteger z) {
        this.z = this.z.add(z);
    }

    public void add(int x, int y, int z) {
        this.x = this.x.add(BigInteger.valueOf(x));
        this.y = this.y.add(BigInteger.valueOf(y));
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void add(BigInteger x, BigInteger y, BigInteger z) {
        this.x = this.x.add(x);
        this.y = this.y.add(y);
        this.z = this.z.add(z);
    }

    public void add(ChunkPos pos) {
        this.x = this.x.add(pos.x);
        this.y = this.y.add(pos.y);
        this.z = this.z.add(pos.z);
    }

    public void subtractX(int x) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
    }

    public void subtractX(BigInteger x) {
        this.x = this.x.subtract(x);
    }

    public void subtractY(int y) {
        this.y = this.y.subtract(BigInteger.valueOf(y));
    }

    public void subtractY(BigInteger y) {
        this.y = this.y.subtract(y);
    }

    public void subtractZ(int z) {
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtractZ(BigInteger z) {
        this.z = this.z.subtract(z);
    }

    public void subtract(int x, int y, int z) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
        this.y = this.y.subtract(BigInteger.valueOf(y));
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtract(BigInteger x, BigInteger y, BigInteger z) {
        this.x = this.x.subtract(x);
        this.y = this.y.subtract(y);
        this.z = this.z.subtract(z);
    }

    public void subtract(ChunkPos pos) {
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

    public void up(BigInteger n) {
        this.y = this.y.add(n);
    }

    public void down() {
        this.y = this.y.subtract(BigInteger.ONE);
    }

    public void down(int n) {
        this.y = this.y.subtract(BigInteger.valueOf(n));
    }

    public void down(BigInteger n) {
        this.y = this.y.subtract(n);
    }

    public void north() {
        this.x = this.x.add(BigInteger.ONE);
    }

    public void north(int n) {
        this.x = this.x.add(BigInteger.valueOf(n));
    }

    public void north(BigInteger n) {
        this.x = this.x.add(n);
    }

    public void south() {
        this.x = this.x.subtract(BigInteger.ONE);
    }

    public void south(int n) {
        this.x = this.x.subtract(BigInteger.valueOf(n));
    }

    public void south(BigInteger n) {
        this.x = this.x.subtract(n);
    }

    public void east() {
        this.z = this.z.add(BigInteger.ONE);
    }

    public void east(int n) {
        this.z = this.z.add(BigInteger.valueOf(n));
    }

    public void east(BigInteger n) {
        this.z = this.z.add(n);
    }

    public void west() {
        this.z = this.z.subtract(BigInteger.ONE);
    }

    public void west(int n) {
        this.z = this.z.subtract(BigInteger.valueOf(n));
    }

    public void west(BigInteger n) {
        this.z = this.z.subtract(n);
    }

    public void offset(BlockPos.EFacing facing) {
        switch (facing) {
            case UP -> this.up();
            case DOWN -> this.down();
            case EAST -> this.east();
            case WEST -> this.west();
            case NORTH -> this.north();
            case SOUTH -> this.south();
        }
    }

    public void offset(BlockPos.EFacing facing, int n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    public void offset(BlockPos.EFacing facing, BigInteger n) {
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
    public String toString() {
        return "ChunkPos{" +
                "x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ChunkPos chunkPos = (ChunkPos) o;
        //noinspection SuspiciousNameCombination
        return this.x.equals(chunkPos.x) && this.y.equals(chunkPos.y) && this.z.equals(chunkPos.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }
}
