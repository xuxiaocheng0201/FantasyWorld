package CraftWorld.Block;

import HeadLibs.Helper.HStringHelper;

import java.math.BigInteger;
import java.util.Objects;

public class BlockPos {
    private BigInteger x, y, z;

    public BlockPos() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public BlockPos(int x, int y, int z) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public BlockPos(BigInteger x, BigInteger y, BigInteger z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void add(BlockPos pos) {
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

    public void subtract(BlockPos pos) {
        this.x = this.x.subtract(pos.x);
        this.y = this.y.subtract(pos.y);
        this.z = this.z.subtract(pos.z);
    }

    @Override
    public String toString() {
        return HStringHelper.merge("BlockPos{",
                "x=", x,
                ", y=", y,
                ", z=", z,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof BlockPos))
            return false;
        return Objects.equals(this.x, ((BlockPos) a).x) &&
                Objects.equals(this.y, ((BlockPos) a).y) &&
                Objects.equals(this.z, ((BlockPos) a).z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
