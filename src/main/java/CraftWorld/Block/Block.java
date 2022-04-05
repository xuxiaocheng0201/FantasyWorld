package CraftWorld.Block;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Exception.DSTFormatException;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Helper.HStringHelper;
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

    private IBlockBase instance;

    public IBlockBase getInstance() {
        return this.instance;
    }

    public void setInstance(IBlockBase instance) {
        this.instance = instance;
    }

    public static final String id = "Block";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, Block.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;

    @Override
    public String getDSTName() {
        return this.name;
    }

    @Override
    public void setDSTName(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        String name = input.readUTF();
        if ("null".equals(name)) {
            this.instance = null;
            return;
        }
        try {
            this.instance = BlockUtils.getInstance().getElementInstance(BlockUtils.dePrefix(name));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
        if (this.instance == null) {
            this.instance = new BlockAir();
            return;
        }
        if (!BlockPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.instance.getPos().read(input);
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        this.instance.getDst().read(input);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        if (this.instance == null) {
            output.writeUTF("null");
            return;
        }
        output.writeUTF(this.instance.getBlockName());
        this.instance.getPos().write(output);
        this.instance.getDst().write(output);
    }

    @Override
    public String toString() {
        return HStringHelper.concat("Block{",
                ", name=", (this.instance == null) ? "null" : this.instance.getBlockName(),
                ", pos=", (this.instance == null) ? "null" : this.instance.getPos(),
                ", dst=", (this.instance == null) ? "null" : this.instance.getDst(),
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof Block))
            return false;
        return Objects.equals(this.instance, ((Block) a).instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance);
    }
}
