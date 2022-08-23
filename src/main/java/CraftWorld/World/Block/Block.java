package CraftWorld.World.Block;

import CraftWorld.DST.BasicInformation.DSTId;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.World.Block.BasicInformation.BlockId;
import CraftWorld.World.Chunk.Chunk;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
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
    public static final DSTId id = DSTId.getDstIdInstance("Block");
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);

    protected final @NotNull Chunk chunk;
    protected final @NotNull BlockPosOffset posOffset = new BlockPosOffset();
    protected @NotNull IBlockBase instance = new BlockAir(); {this.instance.setExistingBlockInstance(this);}

    public Block(@NotNull Chunk chunk) {
        super();
        this.chunk = chunk;
    }

    public Block(@NotNull Chunk chunk, @Nullable BlockPosOffset posOffset) {
        super();
        this.chunk = chunk;
        this.posOffset.set(posOffset);
    }

    public Block(@NotNull Chunk chunk, @Nullable IBlockBase instance) {
        super();
        this.chunk = chunk;
        if (instance != null)
            this.setInstance(instance);
    }

    public Block(@NotNull Chunk chunk, @Nullable BlockPosOffset posOffset, @Nullable IBlockBase instance) {
        super();
        this.chunk = chunk;
        this.posOffset.set(posOffset);
        if (instance != null)
            this.setInstance(instance);
    }

    public @NotNull Chunk getChunk() {
        return this.chunk;
    }

    public @NotNull BlockPosOffset getPosOffset() {
        return this.posOffset;
    }

    public @NotNull BlockPos getBlockPos() {
        return new BlockPos(this.chunk.getPos(), this.posOffset);
    }

    public @NotNull IBlockBase getInstance() {
        return this.instance;
    }

    public void setInstance(@Nullable IBlockBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, BlockAir::new);
        this.instance.setExistingBlockInstance(this);
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        if (!BlockPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.posOffset.read(input);
        BlockId blockId = new BlockId();
        if (!BlockId.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        blockId.read(input);
        try {
            this.setInstance(BlockUtils.getInstance().getElementInstance(blockId, false));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.setInstance(null);
        }
        this.instance.read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        this.posOffset.write(output);
        this.instance.write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Block{" +
                "pos=" + this.posOffset +
                ", instance=" + this.instance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return this.posOffset.equals(block.posOffset) && this.instance.equals(block.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.posOffset, this.instance);
    }
}
