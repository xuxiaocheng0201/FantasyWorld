package CraftWorld.World.Chunk;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.World.Block.BlockPos.EFacing;
import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

public class ChunkPos implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 1974205833401624407L;
    public static final String id = "ChunkPos";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull BigInteger x = BigInteger.ZERO;
    protected @NotNull BigInteger y = BigInteger.ZERO;
    protected @NotNull BigInteger z = BigInteger.ZERO;

    public ChunkPos() {
        super();
    }

    public ChunkPos(int x, int y, int z) {
        super();
        this.set(x, y, z);
    }

    public ChunkPos(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        super();
        this.set(x, y, z);
    }

    public ChunkPos(@Nullable ChunkPos chunkPos) {
        super();
        this.set(chunkPos);
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

    public void set(@Nullable ChunkPos chunkPos) {
        if (chunkPos == null) {
            this.x = BigInteger.ZERO;
            this.y = BigInteger.ZERO;
            this.z = BigInteger.ZERO;
        } else {
            this.x = chunkPos.x;
            this.y = chunkPos.y;
            this.z = chunkPos.z;
        }
    }

    public void addX(int x) {
        this.x = this.x.add(BigInteger.valueOf(x));
    }

    public void addX(@Nullable BigInteger x) {
        if (x != null)
            this.x = this.x.add(x);
    }

    public void addY(int y) {
        this.y = this.y.add(BigInteger.valueOf(y));
    }

    public void addY(@Nullable BigInteger y) {
        if (y != null)
            this.y = this.y.add(y);
    }

    public void addZ(int z) {
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void addZ(@Nullable BigInteger z) {
        if (z != null)
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

    public void add(@Nullable ChunkPos chunkPos) {
        if (chunkPos == null)
            return;
        this.x = this.x.add(chunkPos.x);
        this.y = this.y.add(chunkPos.y);
        this.z = this.z.add(chunkPos.z);
    }

    public void subtractX(int x) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
    }

    public void subtractX(@Nullable BigInteger x) {
        if (x != null)
            this.x = this.x.subtract(x);
    }

    public void subtractY(int y) {
        this.y = this.y.subtract(BigInteger.valueOf(y));
    }

    public void subtractY(@Nullable BigInteger y) {
        if (y != null)
            this.y = this.y.subtract(y);
    }

    public void subtractZ(int z) {
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtractZ(@Nullable BigInteger z) {
        if (z != null)
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

    public void subtract(@Nullable ChunkPos chunkPos) {
        if (chunkPos == null)
            return;
        this.x = this.x.subtract(chunkPos.x);
        this.y = this.y.subtract(chunkPos.y);
        this.z = this.z.subtract(chunkPos.z);
    }

    public void up() {
        this.y = this.y.add(BigInteger.ONE);
    }

    public void up(int n) {
        this.y = this.y.add(BigInteger.valueOf(n));
    }

    public void up(@Nullable BigInteger n) {
        if (n != null)
            this.y = this.y.add(n);
    }

    public void down() {
        this.y = this.y.subtract(BigInteger.ONE);
    }

    public void down(int n) {
        this.y = this.y.subtract(BigInteger.valueOf(n));
    }

    public void down(@Nullable BigInteger n) {
        if (n != null)
            this.y = this.y.subtract(n);
    }

    public void north() {
        this.x = this.x.add(BigInteger.ONE);
    }

    public void north(int n) {
        this.x = this.x.add(BigInteger.valueOf(n));
    }

    public void north(@Nullable BigInteger n) {
        if (n != null)
            this.x = this.x.add(n);
    }

    public void south() {
        this.x = this.x.subtract(BigInteger.ONE);
    }

    public void south(int n) {
        this.x = this.x.subtract(BigInteger.valueOf(n));
    }

    public void south(@Nullable BigInteger n) {
        if (n != null)
            this.x = this.x.subtract(n);
    }

    public void east() {
        this.z = this.z.add(BigInteger.ONE);
    }

    public void east(int n) {
        this.z = this.z.add(BigInteger.valueOf(n));
    }

    public void east(@Nullable BigInteger n) {
        if (n != null)
            this.z = this.z.add(n);
    }

    public void west() {
        this.z = this.z.subtract(BigInteger.ONE);
    }

    public void west(int n) {
        this.z = this.z.subtract(BigInteger.valueOf(n));
    }

    public void west(@Nullable BigInteger n) {
        if (n != null)
            this.z = this.z.subtract(n);
    }

    public void offset(@NotNull EFacing facing) {
        switch (facing) {
            case UP -> this.up();
            case DOWN -> this.down();
            case EAST -> this.east();
            case WEST -> this.west();
            case NORTH -> this.north();
            case SOUTH -> this.south();
        }
    }

    public void offset(@NotNull EFacing facing, int n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    public void offset(@NotNull EFacing facing, @Nullable BigInteger n) {
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

    public @NotNull ImmutableChunkPos toImmutable() {
        return new ImmutableChunkPos(this);
    }

    public @NotNull UpdatableChunkPos toUpdatable() {
        return new UpdatableChunkPos(this);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.x = new BigInteger(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        this.y = new BigInteger(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        this.z = new BigInteger(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.x.toString(ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(this.y.toString(ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(this.z.toString(ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "ChunkPos{" +
                "x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkPos that)) return false;
        //noinspection SuspiciousNameCombination
        return this.x.equals(that.x) && this.y.equals(that.y) && this.z.equals(that.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }

    public static class ImmutableChunkPos extends ChunkPos implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(ChunkPos.serialVersionUID);

        public ImmutableChunkPos() {
            super();
        }

        public ImmutableChunkPos(int x, int y, int z) {
            super();
            super.set(x, y, z);
        }

        public ImmutableChunkPos(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            super();
            super.set(x, y, z);
        }

        public ImmutableChunkPos(@Nullable ChunkPos chunkPos) {
            super();
            super.set(chunkPos);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setX(int x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setX(@Nullable BigInteger x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setY(@Nullable BigInteger y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setZ(@Nullable BigInteger z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int x, int y, int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable ChunkPos chunkPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addX(int x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addX(@Nullable BigInteger x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addY(@Nullable BigInteger y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addZ(@Nullable BigInteger z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int x, int y, int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(@Nullable ChunkPos chunkPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractX(int x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractX(@Nullable BigInteger x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractY(@Nullable BigInteger y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractZ(@Nullable BigInteger z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtract(int x, int y, int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtract(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtract(@Nullable ChunkPos chunkPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void up() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void up(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void up(@Nullable BigInteger n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void down() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void down(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void down(@Nullable BigInteger n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void north() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void north(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void north(@Nullable BigInteger n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void south() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void south(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void south(@Nullable BigInteger n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void east() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void east(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void east(@Nullable BigInteger n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void west() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void west(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void west(@Nullable BigInteger n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void offset(@NotNull EFacing facing) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void offset(@NotNull EFacing facing, int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void offset(@NotNull EFacing facing, @Nullable BigInteger n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutableChunkPos toImmutable() {
            return this;
        }
    }

    public static class UpdatableChunkPos extends ChunkPos implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(ChunkPos.serialVersionUID);

        protected boolean updated;

        public UpdatableChunkPos() {
            super();
        }

        public UpdatableChunkPos(int x, int y, int z) {
            super();
            super.set(x, y, z);
        }

        public UpdatableChunkPos(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            super();
            super.set(x, y, z);
        }

        public UpdatableChunkPos(@Nullable ChunkPos chunkPos) {
            super();
            super.set(chunkPos);
        }

        @Override
        public void clear() {
            super.clear();
            this.updated = true;
        }

        @Override
        public void setX(int x) {
            super.setX(x);
            this.updated = true;
        }

        @Override
        public void setX(@Nullable BigInteger x) {
            super.setX(x);
            this.updated = true;
        }

        @Override
        public void setY(int y) {
            super.setY(y);
            this.updated = true;
        }

        @Override
        public void setY(@Nullable BigInteger y) {
            super.setY(y);
            this.updated = true;
        }

        @Override
        public void setZ(int z) {
            super.setZ(z);
            this.updated = true;
        }

        @Override
        public void setZ(@Nullable BigInteger z) {
            super.setZ(z);
            this.updated = true;
        }

        @Override
        public void set(int x, int y, int z) {
            super.set(x, y, z);
            this.updated = true;
        }

        @Override
        public void set(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            super.set(x, y, z);
            this.updated = true;
        }

        @Override
        public void set(@Nullable ChunkPos chunkPos) {
            super.set(chunkPos);
            this.updated = true;
        }

        @Override
        public void addX(int x) {
            super.addX(x);
            this.updated = true;
        }

        @Override
        public void addX(@Nullable BigInteger x) {
            super.addX(x);
            this.updated = true;
        }

        @Override
        public void addY(int y) {
            super.addY(y);
            this.updated = true;
        }

        @Override
        public void addY(@Nullable BigInteger y) {
            super.addY(y);
            this.updated = true;
        }

        @Override
        public void addZ(int z) {
            super.addZ(z);
            this.updated = true;
        }

        @Override
        public void addZ(@Nullable BigInteger z) {
            super.addZ(z);
            this.updated = true;
        }

        @Override
        public void add(int x, int y, int z) {
            super.add(x, y, z);
            this.updated = true;
        }

        @Override
        public void add(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            super.add(x, y, z);
            this.updated = true;
        }

        @Override
        public void add(@Nullable ChunkPos chunkPos) {
            super.add(chunkPos);
            this.updated = true;
        }

        @Override
        public void subtractX(int x) {
            super.subtractX(x);
            this.updated = true;
        }

        @Override
        public void subtractX(@Nullable BigInteger x) {
            super.subtractX(x);
            this.updated = true;
        }

        @Override
        public void subtractY(int y) {
            super.subtractY(y);
            this.updated = true;
        }

        @Override
        public void subtractY(@Nullable BigInteger y) {
            super.subtractY(y);
            this.updated = true;
        }

        @Override
        public void subtractZ(int z) {
            super.subtractZ(z);
            this.updated = true;
        }

        @Override
        public void subtractZ(@Nullable BigInteger z) {
            super.subtractZ(z);
            this.updated = true;
        }

        @Override
        public void subtract(int x, int y, int z) {
            super.subtract(x, y, z);
            this.updated = true;
        }

        @Override
        public void subtract(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            super.subtract(x, y, z);
            this.updated = true;
        }

        @Override
        public void subtract(@Nullable ChunkPos chunkPos) {
            super.subtract(chunkPos);
            this.updated = true;
        }

        @Override
        public void up() {
            super.up();
            this.updated = true;
        }

        @Override
        public void up(int n) {
            super.up(n);
            this.updated = true;
        }

        @Override
        public void up(@Nullable BigInteger n) {
            super.up(n);
            this.updated = true;
        }

        @Override
        public void down() {
            super.down();
            this.updated = true;
        }

        @Override
        public void down(int n) {
            super.down(n);
            this.updated = true;
        }

        @Override
        public void down(@Nullable BigInteger n) {
            super.down(n);
            this.updated = true;
        }

        @Override
        public void north() {
            super.north();
            this.updated = true;
        }

        @Override
        public void north(int n) {
            super.north(n);
            this.updated = true;
        }

        @Override
        public void north(@Nullable BigInteger n) {
            super.north(n);
            this.updated = true;
        }

        @Override
        public void south() {
            super.south();
            this.updated = true;
        }

        @Override
        public void south(int n) {
            super.south(n);
            this.updated = true;
        }

        @Override
        public void south(@Nullable BigInteger n) {
            super.south(n);
            this.updated = true;
        }

        @Override
        public void east() {
            super.east();
            this.updated = true;
        }

        @Override
        public void east(int n) {
            super.east(n);
            this.updated = true;
        }

        @Override
        public void east(@Nullable BigInteger n) {
            super.east(n);
            this.updated = true;
        }

        @Override
        public void west() {
            super.west();
            this.updated = true;
        }

        @Override
        public void west(int n) {
            super.west(n);
            this.updated = true;
        }

        @Override
        public void west(@Nullable BigInteger n) {
            super.west(n);
            this.updated = true;
        }

        @Override
        public void offset(@NotNull EFacing facing) {
            super.offset(facing);
            this.updated = true;
        }

        @Override
        public void offset(@NotNull EFacing facing, int n) {
            super.offset(facing, n);
            this.updated = true;
        }

        @Override
        public void offset(@NotNull EFacing facing, @Nullable BigInteger n) {
            super.offset(facing, n);
            this.updated = true;
        }

        @Override
        public @NotNull UpdatableChunkPos toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            return this.updated;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
        }
    }
}
