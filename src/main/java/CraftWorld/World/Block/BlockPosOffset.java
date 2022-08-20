package CraftWorld.World.Block;

import CraftWorld.ConstantStorage;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.World.Chunk.Chunk;
import HeadLibs.Annotations.IntRange;
import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import HeadLibs.Helper.HMathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class BlockPosOffset implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -2305924643780153906L;
    public static final DSTId id = DSTId.getDstIdInstance("BlockPosOffset");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @IntRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false) int x;
    protected @IntRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false) int y;
    protected @IntRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false) int z;

    public BlockPosOffset() {
        super();
    }

    public BlockPosOffset(int x, int y, int z) {
        super();
        this.set(x, y, z);
    }

    public BlockPosOffset(@Nullable BlockPosOffset blockPosOffset) {
        super();
        this.set(blockPosOffset);
    }

    public void clear() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    @IntRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false)
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = HMathHelper.cyclicClamp(x, 0, Chunk.SIZE);
    }

    @IntRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false)
    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = HMathHelper.cyclicClamp(y, 0, Chunk.SIZE);
    }

    @IntRange(minimum = 0, maximum = Chunk.SIZE, maximum_equally = false)
    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = HMathHelper.cyclicClamp(z, 0, Chunk.SIZE);
    }

    public void set(int x, int y, int z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(@Nullable BlockPosOffset blockPosOffset) {
        if (blockPosOffset == null) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        } else {
            this.x = blockPosOffset.x;
            this.y = blockPosOffset.y;
            this.z = blockPosOffset.z;
        }
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

    public void add(@Nullable BlockPosOffset blockPosOffset) {
        if (blockPosOffset == null)
            return;
        this.setX(this.x + blockPosOffset.x);
        this.setY(this.y + blockPosOffset.y);
        this.setZ(this.z + blockPosOffset.z);
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

    public void subtract(@Nullable BlockPosOffset blockPosOffset) {
        if (blockPosOffset == null)
            return;
        this.setX(this.x - blockPosOffset.x);
        this.setY(this.y - blockPosOffset.y);
        this.setZ(this.z - blockPosOffset.z);
    }

    public @NotNull ImmutableBlockPosOffset toImmutable() {
        return new ImmutableBlockPosOffset(this);
    }

    public @NotNull UpdatableBlockPosOffset toUpdatable() {
        return new UpdatableBlockPosOffset(this);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.x = Integer.parseInt(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        this.y = Integer.parseInt(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        this.z = Integer.parseInt(input.readUTF(), ConstantStorage.SAVE_NUMBER_RADIX);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(Integer.toString(this.x, ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(Integer.toString(this.y, ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(Integer.toString(this.z, ConstantStorage.SAVE_NUMBER_RADIX));
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "BlockPosOffset{" +
                "x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockPosOffset that)) return false;
        return this.x == that.x && this.y == that.y && this.z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }

    public static class ImmutableBlockPosOffset extends BlockPosOffset implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(BlockPosOffset.serialVersionUID);

        public ImmutableBlockPosOffset() {
            super();
        }

        public ImmutableBlockPosOffset(int x, int y, int z) {
            super();
            super.set(x, y, z);
        }

        public ImmutableBlockPosOffset(@Nullable BlockPosOffset blockPosOffset) {
            super();
            super.set(blockPosOffset);
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
        public void setY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int x, int y, int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable BlockPosOffset blockPosOffset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addX(int x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int x, int y, int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(@Nullable BlockPosOffset blockPosOffset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractX(int x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtract(int x, int y, int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtract(@Nullable BlockPosOffset blockPosOffset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutableBlockPosOffset toImmutable() {
            return this;
        }
    }

    public static class UpdatableBlockPosOffset extends BlockPosOffset implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(BlockPosOffset.serialVersionUID);

        protected boolean updated;

        public UpdatableBlockPosOffset() {
            super();
        }

        public UpdatableBlockPosOffset(int x, int y, int z) {
            super();
            super.set(x, y, z);
        }

        public UpdatableBlockPosOffset(@Nullable BlockPosOffset blockPosOffset) {
            super();
            super.set(blockPosOffset);
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
        public void setY(int y) {
            super.setY(y);
            this.updated = true;
        }

        @Override
        public void setZ(int z) {
            super.setZ(z);
            this.updated = true;
        }

        @Override
        public void set(int x, int y, int z) {
            super.set(x, y, z);
            this.updated = true;
        }

        @Override
        public void set(@Nullable BlockPosOffset blockPosOffset) {
            super.set(blockPosOffset);
            this.updated = true;
        }

        @Override
        public void addX(int x) {
            super.addX(x);
            this.updated = true;
        }

        @Override
        public void addY(int y) {
            super.addY(y);
            this.updated = true;
        }

        @Override
        public void addZ(int z) {
            super.addZ(z);
            this.updated = true;
        }

        @Override
        public void add(int x, int y, int z) {
            super.add(x, y, z);
            this.updated = true;
        }

        @Override
        public void add(@Nullable BlockPosOffset blockPosOffset) {
            super.add(blockPosOffset);
            this.updated = true;
        }

        @Override
        public void subtractX(int x) {
            super.subtractX(x);
            this.updated = true;
        }

        @Override
        public void subtractY(int y) {
            super.subtractY(y);
            this.updated = true;
        }

        @Override
        public void subtractZ(int z) {
            super.subtractZ(z);
            this.updated = true;
        }

        @Override
        public void subtract(int x, int y, int z) {
            super.subtract(x, y, z);
            this.updated = true;
        }

        @Override
        public void subtract(@Nullable BlockPosOffset blockPosOffset) {
            super.subtract(blockPosOffset);
            this.updated = true;
        }

        @Override
        public @NotNull UpdatableBlockPosOffset toUpdatable() {
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
