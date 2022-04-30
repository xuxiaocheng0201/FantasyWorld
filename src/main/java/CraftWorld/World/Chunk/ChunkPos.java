package CraftWorld.World.Chunk;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.World.Block.BlockPos;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings("unused")
public class ChunkPos implements IDSTBase, Cloneable {
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

    private @NotNull BigInteger x;
    private @NotNull BigInteger y;
    private @NotNull BigInteger z;

    public ChunkPos() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public ChunkPos(int x, int y, int z) {
        super();
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public ChunkPos(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        super();
        this.x = Objects.requireNonNullElse(x, BigInteger.ZERO);
        this.y = Objects.requireNonNullElse(y, BigInteger.ZERO);
        this.z = Objects.requireNonNullElse(z, BigInteger.ZERO);
    }

    public ChunkPos(@Nullable ChunkPos pos) {
        this(pos == null ? BigInteger.ZERO : new BigInteger(pos.x.toString()),
                pos == null ? BigInteger.ZERO : new BigInteger(pos.y.toString()),
                pos == null ? BigInteger.ZERO : new BigInteger(pos.z.toString()));
    }

    public void clear() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
        this.z = BigInteger.ZERO;
    }

    public int getX() {
        return this.x.intValue();
    }

    public @NotNull BigInteger getBigX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = BigInteger.valueOf(x);
    }

    public void setX(@Nullable BigInteger x) {
        this.x = Objects.requireNonNullElse(x, BigInteger.ZERO);
    }

    public int getY() {
        return this.y.intValue();
    }

    public @NotNull BigInteger getBigY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = BigInteger.valueOf(y);
    }

    public void setY(@Nullable BigInteger y) {
        this.y = Objects.requireNonNullElse(y, BigInteger.ZERO);
    }

    public int getZ() {
        return this.z.intValue();
    }

    public @NotNull BigInteger getBigZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = BigInteger.valueOf(z);
    }

    public void setZ(@Nullable BigInteger z) {
        this.z = Objects.requireNonNullElse(z, BigInteger.ZERO);
    }

    public void set(int x, int y, int z) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public void set(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        this.x = Objects.requireNonNullElse(x, BigInteger.ZERO);
        this.y = Objects.requireNonNullElse(y, BigInteger.ZERO);
        this.z = Objects.requireNonNullElse(z, BigInteger.ZERO);
    }

    public void set(@Nullable ChunkPos pos) {
        if (pos == null) {
            this.clear();
            return;
        }
        this.x = new BigInteger(pos.x.toString());
        this.y = new BigInteger(pos.y.toString());
        this.z = new BigInteger(pos.z.toString());
    }

    public void addX(int x) {
        this.x = this.x.add(BigInteger.valueOf(x));
    }

    public void addX(@Nullable BigInteger x) {
        if (x == null)
            return;
        this.x = this.x.add(x);
    }

    public void addY(int y) {
        this.y = this.y.add(BigInteger.valueOf(y));
    }

    public void addY(@Nullable BigInteger y) {
        if (y == null)
            return;
        this.y = this.y.add(y);
    }

    public void addZ(int z) {
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void addZ(@Nullable BigInteger z) {
        if (z == null)
            return;
        this.z = this.z.add(z);
    }

    public void add(int x, int y, int z) {
        this.x = this.x.add(BigInteger.valueOf(x));
        this.y = this.y.add(BigInteger.valueOf(y));
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void add(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        if (x != null)
            this.x = this.x.add(x);
        if (y != null)
            this.y = this.y.add(y);
        if (z != null)
            this.z = this.z.add(z);
    }

    public void add(@Nullable ChunkPos pos) {
        if (pos == null)
            return;
        this.x = this.x.add(pos.x);
        this.y = this.y.add(pos.y);
        this.z = this.z.add(pos.z);
    }

    public void subtractX(int x) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
    }

    public void subtractX(@Nullable BigInteger x) {
        if (x == null)
            return;
        this.x = this.x.subtract(x);
    }

    public void subtractY(int y) {
        this.y = this.y.subtract(BigInteger.valueOf(y));
    }

    public void subtractY(@Nullable BigInteger y) {
        if (y == null)
            return;
        this.y = this.y.subtract(y);
    }

    public void subtractZ(int z) {
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtractZ(@Nullable BigInteger z) {
        if (z == null)
            return;
        this.z = this.z.subtract(z);
    }

    public void subtract(int x, int y, int z) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
        this.y = this.y.subtract(BigInteger.valueOf(y));
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtract(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        if (x != null)
            this.x = this.x.subtract(x);
        if (y != null)
            this.y = this.y.subtract(y);
        if (z != null)
            this.z = this.z.subtract(z);
    }

    public void subtract(@Nullable ChunkPos pos) {
        if (pos == null)
            return;
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

    public void up(@Nullable BigInteger n) {
        if (n == null)
            return;
        this.y = this.y.add(n);
    }

    public void down() {
        this.y = this.y.subtract(BigInteger.ONE);
    }

    public void down(int n) {
        this.y = this.y.subtract(BigInteger.valueOf(n));
    }

    public void down(@Nullable BigInteger n) {
        if (n == null)
            return;
        this.y = this.y.subtract(n);
    }

    public void north() {
        this.x = this.x.add(BigInteger.ONE);
    }

    public void north(int n) {
        this.x = this.x.add(BigInteger.valueOf(n));
    }

    public void north(@Nullable BigInteger n) {
        if (n == null)
            return;
        this.x = this.x.add(n);
    }

    public void south() {
        this.x = this.x.subtract(BigInteger.ONE);
    }

    public void south(int n) {
        this.x = this.x.subtract(BigInteger.valueOf(n));
    }

    public void south(@Nullable BigInteger n) {
        if (n == null)
            return;
        this.x = this.x.subtract(n);
    }

    public void east() {
        this.z = this.z.add(BigInteger.ONE);
    }

    public void east(int n) {
        this.z = this.z.add(BigInteger.valueOf(n));
    }

    public void east(@Nullable BigInteger n) {
        if (n == null)
            return;
        this.z = this.z.add(n);
    }

    public void west() {
        this.z = this.z.subtract(BigInteger.ONE);
    }

    public void west(int n) {
        this.z = this.z.subtract(BigInteger.valueOf(n));
    }

    public void west(@Nullable BigInteger n) {
        if (n == null)
            return;
        this.z = this.z.subtract(n);
    }

    public void offset(@NotNull BlockPos.EFacing facing) {
        switch (facing) {
            case UP -> this.up();
            case DOWN -> this.down();
            case EAST -> this.east();
            case WEST -> this.west();
            case NORTH -> this.north();
            case SOUTH -> this.south();
        }
    }

    public void offset(@NotNull BlockPos.EFacing facing, int n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    public void offset(@NotNull BlockPos.EFacing facing, @Nullable BigInteger n) {
        if (n == null)
            return;
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
    public void read(@NotNull DataInput input) throws IOException {
        this.x = new BigInteger(input.readUTF(), SAVE_RADIX);
        this.y = new BigInteger(input.readUTF(), SAVE_RADIX);
        this.z = new BigInteger(input.readUTF(), SAVE_RADIX);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.x.toString(SAVE_RADIX));
        output.writeUTF(this.y.toString(SAVE_RADIX));
        output.writeUTF(this.z.toString(SAVE_RADIX));
        output.writeUTF(suffix);
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

    @Override
    public ChunkPos clone() {
        ChunkPos chunkPos;
        try {
            chunkPos = (ChunkPos) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
        chunkPos.setX(new BigInteger(this.x.toString()));
        chunkPos.setY(new BigInteger(this.y.toString()));
        chunkPos.setZ(new BigInteger(this.z.toString()));
        return chunkPos;
    }
}
