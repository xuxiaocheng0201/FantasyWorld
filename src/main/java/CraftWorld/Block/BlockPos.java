package CraftWorld.Block;

import HeadLibs.Helper.HStringHelper;

import java.math.BigInteger;

public class BlockPos {
    private BigInteger x, y, z;

    public BlockPos() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public BlockPos(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public BlockPos(BigInteger x, BigInteger y, BigInteger z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
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
        this.setX(this.x.add(BigInteger.valueOf(x)));
    }

    public void addX(BigInteger x) {
        this.setX(this.x.add(x));
    }

    public void addY(int y) {
        this.setY(this.y.add(BigInteger.valueOf(y)));
    }

    public void addY(BigInteger y) {
        this.setY(this.y.add(y));
    }

    public void addZ(int z) {
        this.setZ(this.z.add(BigInteger.valueOf(z)));
    }

    public void addZ(BigInteger z) {
        this.setZ(this.z.add(z));
    }

    public void add(int x, int y, int z) {
        this.addX(x);
        this.addY(y);
        this.addZ(z);
    }

    public void add(BigInteger x, BigInteger y, BigInteger z) {
        this.addX(x);
        this.addY(y);
        this.addZ(z);
    }

    public void add(BlockPos pos) {
        this.addX(pos.x);
        this.addY(pos.y);
        this.addZ(pos.z);
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
        return this.x.equals(((BlockPos) a).x) && this.y.equals(((BlockPos) a).y) && this.z.equals(((BlockPos) a).z);
    }

    @Override
    public int hashCode() {
        // TODO: BlockPos hashCode
        return this.getX() << 16 | this.getY() << 4 | this.getZ();
    }
}
