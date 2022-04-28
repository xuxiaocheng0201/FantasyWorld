package CraftWorld.Block;

import CraftWorld.Chunk.Chunk;
import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;

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

    private final Chunk chunk;
    private BlockPos pos;
    private IBlockBase instance = new BlockAir();

    public Block(Chunk chunk) {
        super();
        this.chunk = chunk;
    }

    public Block(Chunk chunk, BlockPos pos) {
        super();
        this.chunk = chunk;
        this.pos = new BlockPos(chunk.getPos());
    }

    public Block(Chunk chunk, IBlockBase instance) {
        super();
        this.chunk = chunk;
        this.instance = Objects.requireNonNullElseGet(instance, BlockAir::new);
    }

    public Block(Chunk chunk, BlockPos pos, IBlockBase instance) {
        super();
        this.chunk = chunk;
        this.instance = Objects.requireNonNullElseGet(instance, BlockAir::new);
        this.pos = new BlockPos(chunk.getPos());
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = new BlockPos(chunk.getPos());
    }

    public IBlockBase getInstance() {
        return this.instance;
    }

    public void setInstance(IBlockBase instance) {
        this.instance = Objects.requireNonNullElseGet(instance, BlockAir::new);
    }

    @Override
    public void read(DataInput input) throws IOException {
        try {
            this.instance = BlockUtils.getInstance().getElementInstance(DSTUtils.dePrefix(input.readUTF()), false);
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.instance = new BlockAir();
        }
        if (!BlockPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.pos.read(input);
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.instance.getDst().read(input);
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.instance.getBlockId());
        this.pos.write(output);
        this.instance.getDst().write(output);
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
