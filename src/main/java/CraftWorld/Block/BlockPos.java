package CraftWorld.Block;

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

    public BlockPos setX(int x) {
        this.x = BigInteger.valueOf(x);
        return this;
    }

    public BlockPos setX(BigInteger x) {
        this.x = x;
        return this;
    }

    public BlockPos setY(int y) {
        this.y = BigInteger.valueOf(y);
        return this;
    }

    public BlockPos setY(BigInteger y) {
        this.y = y;
        return this;
    }

    public BlockPos setZ(int z) {
        this.z = BigInteger.valueOf(z);
        return this;
    }

    public BlockPos setZ(BigInteger z) {
        this.z = z;
        return this;
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

    public BlockPos addX(int x) {
        return this.setX(this.x.add(BigInteger.valueOf(x)));
    }

    public BlockPos addX(BigInteger x) {
        return this.setX(this.x.add(x));
    }

    public BlockPos addY(int y) {
        return this.setY(this.y.add(BigInteger.valueOf(y)));
    }

    public BlockPos addY(BigInteger y) {
        return this.setY(this.y.add(y));
    }

    public BlockPos addZ(int z) {
        return this.setZ(this.z.add(BigInteger.valueOf(z)));
    }

    public BlockPos addZ(BigInteger z) {
        return this.setZ(this.z.add(z));
    }

    public BlockPos add(int x, int y, int z) {
        return this.addX(x).addY(y).addZ(z);
    }

    public BlockPos add(BigInteger x, BigInteger y, BigInteger z) {
        return this.addX(x).addY(y).addZ(z);
    }

    public BlockPos add(BlockPos pos) {
        return this.addX(pos.x).addY(pos.y).addZ(pos.y);
    }

    @Override
    public String toString() {
        return "BlockPos:[x=" + this.getBigX().toString() + ", y=" + this.getBigY().toString() + ", z=" + this.getBigZ().toString() + "]";
    }

    @Override
    public int hashCode() {
        // TODO: BlockPos hashCode
        return this.getX() << 16 | this.getY() << 4 | this.getZ();
    }
}
