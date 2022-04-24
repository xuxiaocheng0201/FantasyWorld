package CraftWorld.Block;

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
    public static final String prefix = IDSTBase.prefix(id);
    public static final String suffix = IDSTBase.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, Block.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private IBlockBase instance = new BlockAir();

    public Block() {
        super();
    }

    public Block(IBlockBase instance) {
        super();
        this.setInstance(instance);
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
            this.instance = BlockUtils.getInstance().getElementInstance(IDSTBase.dePrefix(input.readUTF()));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            this.instance = new BlockAir();
        }
        if (!BlockPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.instance.getPos().read(input);
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
        this.instance.getPos().write(output);
        this.instance.getDst().write(output);
        output.writeUTF(suffix);
    }

    @Override
    public String toString() {
        return "Block{" +
                "name=" + this.instance.getBlockName() +
                ", pos=" + this.instance.getPos() +
                ", dst=" + this.instance.getDst() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return this.instance.equals(block.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance);
    }
}
