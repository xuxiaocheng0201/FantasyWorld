package CraftWorld.Chunk;

import CraftWorld.Block.BlockPos;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

public class ChunkPos implements IDSTBase {
    private BigInteger x, y, z;

    public ChunkPos() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public ChunkPos(int x, int y, int z) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public ChunkPos(BigInteger x, BigInteger y, BigInteger z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static final String id = "ChunkPos";
    public static final String prefix = id;
    static {
        DSTUtils.register(id, ChunkPos.class);
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.x = new BigInteger(input.readUTF());
        this.y = new BigInteger(input.readUTF());
        this.z = new BigInteger(input.readUTF());
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.x.toString());
        output.writeUTF(this.y.toString());
        output.writeUTF(this.z.toString());
    }

    public void setX(int x) {
        this.x = BigInteger.valueOf(x);
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = BigInteger.valueOf(y);
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = BigInteger.valueOf(z);
    }

    public void setZ(BigInteger z) {
        this.z = z;
    }

    public void set(int x, int y, int z) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public void set(BigInteger x, BigInteger y, BigInteger z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(ChunkPos pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public int getX() {
        return x.intValue();
    }

    public BigInteger getBigX() {
        return x;
    }

    public int getY() {
        return y.intValue();
    }

    public BigInteger getBigY() {
        return y;
    }

    public int getZ() {
        return z.intValue();
    }

    public BigInteger getBigZ() {
        return z;
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
            case UP -> up();
            case DOWN -> down();
            case EAST -> east();
            case WEST -> west();
            case NORTH -> north();
            case SOUTH -> south();
        }
    }

    public void offset(BlockPos.EFacing facing, int n) {
        switch (facing) {
            case UP -> up(n);
            case DOWN -> down(n);
            case EAST -> east(n);
            case WEST -> west(n);
            case NORTH -> north(n);
            case SOUTH -> south(n);
        }
    }

    public void offset(BlockPos.EFacing facing, BigInteger n) {
        switch (facing) {
            case UP -> up(n);
            case DOWN -> down(n);
            case EAST -> east(n);
            case WEST -> west(n);
            case NORTH -> north(n);
            case SOUTH -> south(n);
        }
    }

    @Override
    public String toString() {
        return HStringHelper.merge("ChunkPos{",
                "x=", x,
                ", y=", y,
                ", z=", z,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof ChunkPos))
            return false;
        return Objects.equals(this.x, ((ChunkPos) a).x) &&
                Objects.equals(this.y, ((ChunkPos) a).y) &&
                Objects.equals(this.z, ((ChunkPos) a).z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
