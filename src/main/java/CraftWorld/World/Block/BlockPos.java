package CraftWorld.World.Block;

import CraftWorld.CraftWorld;
import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import HeadLibs.Helper.HMathHelper;
import HeadLibs.Helper.HMathHelper.BigIntegerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class BlockPos implements IDSTBase {
    @Serial
    private static final long serialVersionUID = -1091178102319329091L;
    public static final DSTId id = DSTId.getDstIdInstance("BlockPos");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected @NotNull ChunkPos chunkPos = new ChunkPos();
    protected @NotNull BlockPosOffset offset = new BlockPosOffset();

    public BlockPos() {
        super();
    }

    public BlockPos(int x, int y, int z) {
        super();
        this.set(x, y, z);
    }

    public BlockPos(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        super();
        this.set(x, y, z);
    }

    public BlockPos(@Nullable ChunkPos chunkPos, int x, int y, int z) {
        super();
        this.set(chunkPos, x, y, z);
    }

    public BlockPos(@Nullable ChunkPos chunkPos, @Nullable BlockPosOffset blockPosOffset) {
        super();
        this.set(chunkPos, blockPosOffset);
    }

    public BlockPos(@Nullable ChunkPos chunkPos) {
        super();
        this.chunkPos.set(chunkPos);
    }

    public BlockPos(@Nullable BlockPos blockPos) {
        super();
        this.set(blockPos);
    }

    public void clear() {
        this.chunkPos.clear();
        this.offset.clear();
    }

    public void clearOffset() {
        this.offset.clear();
    }

    public @NotNull ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public void setChunkPos(@Nullable ChunkPos chunkPos) {
        this.chunkPos.set(chunkPos);
    }

    public @NotNull BlockPosOffset getOffset() {
        return this.offset;
    }

    public void setOffset(@Nullable BlockPos blockPos) {
        this.offset.set(blockPos == null ? null : blockPos.offset);
    }

    public void setOffset(@Nullable BlockPosOffset blockPosOffset) {
        this.offset.set(blockPosOffset);
    }

    public @NotNull BigInteger getFullX() {
        return this.chunkPos.getBigX().multiply(Chunk.SIZE_BigInteger).add(BigInteger.valueOf(this.offset.x));
    }

    public void setX(int x) {
        int chunk = HMathHelper.floorDivide(x, Chunk.SIZE);
        this.chunkPos.addX(chunk);
        this.offset.x = (x - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullX(int x) {
        int chunk = HMathHelper.floorDivide(x, Chunk.SIZE);
        this.chunkPos.setX(chunk);
        this.offset.x = (x - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullX(@Nullable BigInteger x) {
        if (x == null) {
            this.chunkPos.setX(BigInteger.ZERO);
            this.offset.x = 0;
            return;
        }
        BigInteger chunk = BigIntegerHelper.floorDivide(x, Chunk.SIZE_BigInteger);
        this.chunkPos.setX(chunk);
        this.offset.x = x.subtract(chunk.multiply(Chunk.SIZE_BigInteger)).mod(Chunk.SIZE_BigInteger).intValue();
    }

    public @NotNull BigInteger getFullY() {
        return this.chunkPos.getBigY().multiply(Chunk.SIZE_BigInteger).add(BigInteger.valueOf(this.offset.y));
    }

    public void setY(int y) {
        int chunk = HMathHelper.floorDivide(y, Chunk.SIZE);
        this.chunkPos.addY(chunk);
        this.offset.y = (y - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullY(int y) {
        int chunk = HMathHelper.floorDivide(y, Chunk.SIZE);
        this.chunkPos.setY(chunk);
        this.offset.y = (y - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullY(@Nullable BigInteger y) {
        if (y == null) {
            this.chunkPos.setY(BigInteger.ZERO);
            this.offset.y = 0;
            return;
        }
        BigInteger chunk = BigIntegerHelper.floorDivide(y, Chunk.SIZE_BigInteger);
        this.chunkPos.setY(chunk);
        this.offset.y = y.subtract(chunk.multiply(Chunk.SIZE_BigInteger)).mod(Chunk.SIZE_BigInteger).intValue();
    }

    public @NotNull BigInteger getFullZ() {
        return this.chunkPos.getBigZ().multiply(Chunk.SIZE_BigInteger).add(BigInteger.valueOf(this.offset.z));
    }

    public void setZ(int z) {
        int chunk = HMathHelper.floorDivide(z, Chunk.SIZE);
        this.chunkPos.addZ(chunk);
        this.offset.z = (z - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullZ(int z) {
        int chunk = HMathHelper.floorDivide(z, Chunk.SIZE);
        this.chunkPos.setZ(chunk);
        this.offset.z = (z - chunk * Chunk.SIZE) % Chunk.SIZE;
    }

    public void setFullZ(@Nullable BigInteger z) {
        if (z == null) {
            this.chunkPos.setZ(BigInteger.ZERO);
            this.offset.z = 0;
            return;
        }
        BigInteger chunk = BigIntegerHelper.floorDivide(z, Chunk.SIZE_BigInteger);
        this.chunkPos.setZ(chunk);
        this.offset.z = z.subtract(chunk.multiply(Chunk.SIZE_BigInteger)).mod(Chunk.SIZE_BigInteger).intValue();
    }

    public void set(int x, int y, int z) {
        this.setFullX(x);
        this.setFullY(y);
        this.setFullZ(z);
    }

    public void set(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
        this.setFullX(x);
        this.setFullY(y);
        this.setFullZ(z);
    }

    public void set(@Nullable ChunkPos chunkPos, int x, int y, int z) {
        this.setChunkPos(chunkPos);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(@Nullable ChunkPos chunkPos, @Nullable BlockPosOffset blockPosOffset) {
        this.setChunkPos(chunkPos);
        this.setOffset(blockPosOffset);
    }

    public void set(@Nullable BlockPos blockPos) {
        if (blockPos == null) {
            this.chunkPos.clear();
            this.offset.clear();
        } else {
            this.chunkPos.set(blockPos.chunkPos);
            this.offset.set(blockPos.offset);
        }
    }

    public void addX(int x) {
        this.setX(this.offset.x + x);
    }

    public void addY(int y) {
        this.setY(this.offset.y + y);
    }

    public void addZ(int z) {
        this.setZ(this.offset.z + z);
    }

    public void add(int x, int y, int z) {
        this.setX(this.offset.x + x);
        this.setY(this.offset.y + y);
        this.setZ(this.offset.z + z);
    }

    public void add(@Nullable BlockPos blockPos) {
        if (blockPos == null)
            return;
        this.chunkPos.add(blockPos.chunkPos);
        this.setX(this.offset.x + blockPos.offset.x);
        this.setY(this.offset.y + blockPos.offset.y);
        this.setZ(this.offset.z + blockPos.offset.z);
    }

    public void addOffset(@Nullable BlockPosOffset blockPosOffset) {
        if (blockPosOffset == null)
            return;
        this.setX(this.offset.x + blockPosOffset.x);
        this.setY(this.offset.y + blockPosOffset.y);
        this.setZ(this.offset.z + blockPosOffset.z);
    }

    public void subtractX(int x) {
        this.setX(this.offset.x - x);
    }

    public void subtractY(int y) {
        this.setY(this.offset.y - y);
    }

    public void subtractZ(int z) {
        this.setZ(this.offset.z - z);
    }

    public void subtract(int x, int y, int z) {
        this.setX(this.offset.x - x);
        this.setY(this.offset.y - y);
        this.setZ(this.offset.z - z);
    }

    public void subtract(@Nullable BlockPos blockPos) {
        if (blockPos == null)
            return;
        this.chunkPos.subtract(blockPos.chunkPos);
        this.setX(this.offset.x - blockPos.offset.x);
        this.setY(this.offset.y - blockPos.offset.y);
        this.setZ(this.offset.z - blockPos.offset.z);
    }

    public void subtractOffset(@Nullable BlockPosOffset blockPosOffset) {
        if (blockPosOffset == null)
            return;
        this.setX(this.offset.x - blockPosOffset.x);
        this.setY(this.offset.y - blockPosOffset.y);
        this.setZ(this.offset.z - blockPosOffset.z);
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

    public void offset(@NotNull BlockDirection facing) {
        switch (facing) {
            case UP -> this.up();
            case DOWN -> this.down();
            case EAST -> this.east();
            case WEST -> this.west();
            case NORTH -> this.north();
            case SOUTH -> this.south();
        }
    }

    public void offset(@NotNull BlockDirection facing, int n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    public @NotNull ImmutableBlockPos toImmutable() {
        return new ImmutableBlockPos(this);
    }

    public @NotNull UpdatableBlockPos toUpdatable() {
        return new UpdatableBlockPos(this);
    }

    public @NotNull BigDecimal distance(@Nullable BlockPos that) {
        BigInteger x, y, z;
        if (that == null) {
            x = this.getFullX();
            y = this.getFullY();
            z = this.getFullZ();
        } else {
            x = this.getFullX().subtract(that.getFullX());
            y = this.getFullY().subtract(that.getFullY());
            z = this.getFullZ().subtract(that.getFullZ());
        }
        return (new BigDecimal(x.multiply(x).add(y.multiply(y)).add(z.multiply(z)))).sqrt(CraftWorld.divideMc);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        if (!ChunkPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.chunkPos.read(input);
        if (!BlockPosOffset.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.offset.read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.chunkPos.write(output);
        this.offset.write(output);
        output.writeUTF(suffix);
    }

    @Override
    public @NotNull String toString() {
        return "BlockPos{" +
                "chunkPos=" + this.chunkPos +
                ", offset=" + this.offset +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockPos blockPos)) return false;
        return this.offset.equals(blockPos.offset) && this.chunkPos.equals(blockPos.chunkPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chunkPos, this.offset);
    }

    public static class ImmutableBlockPos extends BlockPos implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(BlockPos.serialVersionUID);

        protected @NotNull BigInteger fullX = BigInteger.ZERO;
        protected @NotNull BigInteger fullY = BigInteger.ZERO;
        protected @NotNull BigInteger fullZ = BigInteger.ZERO;

        protected void init() {
            this.chunkPos = this.chunkPos.toImmutable();
            this.offset = this.offset.toImmutable();
            this.fullX = super.getFullX();
            this.fullY = super.getFullY();
            this.fullZ = super.getFullZ();
        }

        public ImmutableBlockPos() {
            super();
            this.init();
        }

        public ImmutableBlockPos(int x, int y, int z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public ImmutableBlockPos(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public ImmutableBlockPos(@Nullable ChunkPos chunkPos, int x, int y, int z) {
            super();
            super.set(chunkPos, x, y, z);
            this.init();
        }

        public ImmutableBlockPos(@Nullable ChunkPos chunkPos) {
            super();
            super.chunkPos.set(chunkPos);
            this.init();
        }

        public ImmutableBlockPos(@Nullable BlockPos blockPos) {
            super();
            super.set(blockPos);
            this.init();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clearOffset() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setChunkPos(@Nullable ChunkPos chunkPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setOffset(@Nullable BlockPos blockPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setOffset(@Nullable BlockPosOffset blockPosOffset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull BigInteger getFullX() {
            return this.fullX;
        }

        @Override
        public void setX(int x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullX(int x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullX(@Nullable BigInteger x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull BigInteger getFullY() {
            return this.fullY;
        }

        @Override
        public void setY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullY(int y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullY(@Nullable BigInteger y) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull BigInteger getFullZ() {
            return this.fullZ;
        }

        @Override
        public void setZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullZ(int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFullZ(@Nullable BigInteger z) {
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
        public void set(@Nullable ChunkPos chunkPos, int x, int y, int z) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable ChunkPos chunkPos, @Nullable BlockPosOffset blockPosOffset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(@Nullable BlockPos blockPos) {
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
        public void add(@Nullable BlockPos blockPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addOffset(@Nullable BlockPosOffset blockPosOffset) {
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
        public void subtract(@Nullable BlockPos blockPos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void subtractOffset(@Nullable BlockPosOffset blockPosOffset) {
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
        public void down() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void down(int n) {
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
        public void south() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void south(int n) {
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
        public void west() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void west(int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void offset(@NotNull BlockDirection facing) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void offset(@NotNull BlockDirection facing, int n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutableBlockPos toImmutable() {
            return this;
        }
    }

    public static class UpdatableBlockPos extends BlockPos implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(BlockPos.serialVersionUID);

        protected boolean updated = true;

        protected void init() {
            this.chunkPos = this.chunkPos.toUpdatable();
            this.offset = this.offset.toUpdatable();
        }

        public UpdatableBlockPos() {
            super();
            this.init();
        }

        public UpdatableBlockPos(int x, int y, int z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public UpdatableBlockPos(@Nullable BigInteger x, @Nullable BigInteger y, @Nullable BigInteger z) {
            super();
            super.set(x, y, z);
            this.init();
        }

        public UpdatableBlockPos(@Nullable ChunkPos chunkPos, int x, int y, int z) {
            super();
            super.set(chunkPos, x, y, z);
            this.init();
        }

        public UpdatableBlockPos(@Nullable ChunkPos chunkPos) {
            super();
            super.chunkPos.set(chunkPos);
            this.init();
        }

        public UpdatableBlockPos(@Nullable BlockPos blockPos) {
            super();
            super.set(blockPos);
            this.init();
        }

        @Override
        public void clear() {
            super.clear();
            this.updated = true;
        }

        @Override
        public void clearOffset() {
            super.clearOffset();
            this.updated = true;
        }

        @Override
        public void setChunkPos(@Nullable ChunkPos chunkPos) {
            super.setChunkPos(chunkPos);
            this.updated = true;
        }

        @Override
        public void setOffset(@Nullable BlockPos blockPos) {
            super.setOffset(blockPos);
            this.updated = true;
        }

        @Override
        public void setOffset(@Nullable BlockPosOffset blockPosOffset) {
            super.setOffset(blockPosOffset);
            this.updated = true;
        }

        @Override
        public void setX(int x) {
            super.setX(x);
            this.updated = true;
        }

        @Override
        public void setFullX(int x) {
            super.setFullX(x);
            this.updated = true;
        }

        @Override
        public void setFullX(@Nullable BigInteger x) {
            super.setFullX(x);
            this.updated = true;
        }

        @Override
        public void setY(int y) {
            super.setY(y);
            this.updated = true;
        }

        @Override
        public void setFullY(int y) {
            super.setFullY(y);
            this.updated = true;
        }

        @Override
        public void setFullY(@Nullable BigInteger y) {
            super.setFullY(y);
            this.updated = true;
        }

        @Override
        public void setZ(int z) {
            super.setZ(z);
            this.updated = true;
        }

        @Override
        public void setFullZ(int z) {
            super.setFullZ(z);
            this.updated = true;
        }

        @Override
        public void setFullZ(@Nullable BigInteger z) {
            super.setFullZ(z);
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
        public void set(@Nullable ChunkPos chunkPos, int x, int y, int z) {
            super.set(chunkPos, x, y, z);
            this.updated = true;
        }

        @Override
        public void set(@Nullable ChunkPos chunkPos, @Nullable BlockPosOffset blockPosOffset) {
            super.set(chunkPos, blockPosOffset);
            this.updated = true;
        }

        @Override
        public void set(@Nullable BlockPos blockPos) {
            super.set(blockPos);
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
        public void add(@Nullable BlockPos blockPos) {
            super.add(blockPos);
            this.updated = true;
        }

        @Override
        public void addOffset(@Nullable BlockPosOffset blockPosOffset) {
            super.addOffset(blockPosOffset);
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
        public void subtract(@Nullable BlockPos blockPos) {
            super.subtract(blockPos);
            this.updated = true;
        }

        @Override
        public void subtractOffset(@Nullable BlockPosOffset blockPosOffset) {
            super.subtractOffset(blockPosOffset);
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
        public void offset(@NotNull BlockDirection facing) {
            super.offset(facing);
            this.updated = true;
        }

        @Override
        public void offset(@NotNull BlockDirection facing, int n) {
            super.offset(facing, n);
            this.updated = true;
        }

        @Override
        public @NotNull UpdatableBlockPos toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            if (((IUpdatable) this.chunkPos).getUpdated())
                return true;
            if (((IUpdatable) this.offset).getUpdated())
                return true;
            return this.updated;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
            ((IUpdatable) this.chunkPos).setUpdated(updated);
            ((IUpdatable) this.offset).setUpdated(updated);
        }
    }
}
