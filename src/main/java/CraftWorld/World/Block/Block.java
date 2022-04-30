package CraftWorld.World.Block;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.Instance.DST.DSTMetaCompound;
import CraftWorld.World.Chunk.Chunk;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.Dimension;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class Block implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 6768714227234114009L;
    public static final String id = "Block";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, Block.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private final @NotNull Chunk chunk;
    private final @NotNull BlockPos pos;
    private @NotNull IBlockBase instance = new BlockAir();

    public Block(@NotNull Chunk chunk) {
        super();
        this.chunk = chunk;
        this.pos = new BlockPos(chunk.getPos());
    }

    public Block(@NotNull Chunk chunk, @Nullable BlockPos pos) {
        super();
        this.chunk = chunk;
        this.pos = Objects.requireNonNullElse(pos, new BlockPos(chunk.getPos()));
        this.pos.setChunkPos(chunk.getPos());
    }

    public Block(@NotNull Chunk chunk, @Nullable IBlockBase instance) {
        super();
        this.chunk = chunk;
        this.pos = new BlockPos(chunk.getPos());
        this.instance = Objects.requireNonNullElseGet(instance, BlockAir::new);
    }

    public Block(@NotNull Chunk chunk, @Nullable BlockPos pos, @Nullable IBlockBase instance) {
        super();
        this.chunk = chunk;
        this.pos = Objects.requireNonNullElse(pos, new BlockPos(chunk.getPos()));
        this.pos.setChunkPos(chunk.getPos());
        this.instance = Objects.requireNonNullElseGet(instance, BlockAir::new);
    }

    public @NotNull Dimension getDimension() {
        return this.chunk.getDimension();
    }

    public @NotNull Chunk getChunk() {
        return this.chunk;
    }

    public @NotNull BlockPos getPos() {
        return this.pos;
    }

    public void setChunkPos(@Nullable ChunkPos chunkPos) {
        this.chunk.setPos(chunkPos);
        this.pos.setChunkPos(chunkPos);
    }

    public void setPos(@Nullable BlockPos pos) {
        if (pos == null) {
            this.pos.clear();
            return;
        }
        this.pos.set(pos);
        this.chunk.setPos(this.pos.getChunkPos());
    }

    public void setPosOffset(@Nullable BlockPos pos) {
        if (pos == null) {
            this.pos.clearOffset();
            return;
        }
        this.pos.set(pos);
        this.pos.setChunkPos(this.chunk.getPos());
    }

    public @NotNull IBlockBase getInstance() {
        return this.instance;
    }

    public void setInstance(@Nullable IBlockBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, BlockAir::new);
    }

    @Override
    public void read(DataInput input) throws IOException {
        try {
            this.instance = BlockUtils.getInstance().getElementInstance(input.readUTF(), false);
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.instance = new BlockAir();
        }
        if (!BlockPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.pos.read(input);
        this.instance.setBlockName(input.readUTF());
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.instance.getBlockDST().read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.instance.getBlockId());
        this.pos.write(output);
        output.writeUTF(this.instance.getBlockName());
        this.instance.getBlockDST().write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Block{" +
                "pos=" + this.pos +
                ", instance=" + this.instance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return this.pos.equals(block.pos) && this.instance.equals(block.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pos, this.instance);
    }
}
