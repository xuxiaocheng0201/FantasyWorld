package CraftWorld.Block;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Exception.DSTFormatException;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class Block implements IDSTBase {
    private IBlockBase instance = null;

    public static final String id = "Block";
    public static final String prefix = id;
    static {
        DSTUtils.register(id, Block.class);
    }

    @Override
    public void read(DataInput input) throws IOException {
        String name = input.readUTF();
        if (name.equals("null")) {
            instance = null;
            return;
        }
        instance = BlockUtils.get(BlockUtils.deSuffix(name));
        if (instance == null)
            return;
        if (!BlockPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        instance.getPos().read(input);
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        instance.getDst().read(input);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        if (instance == null) {
            output.writeUTF("null");
            return;
        }
        output.writeUTF(instance.getName());
        instance.getPos().write(output);
        instance.getDst().write(output);
    }

    public IBlockBase getInstance() {
        return instance;
    }

    public void setInstance(IBlockBase instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("Block{",
                ", name=", (instance == null) ? "null" : instance.getName(),
                ", pos=", (instance == null) ? "null" : instance.getPos(),
                ", dst=", (instance == null) ? "null" : instance.getDst(),
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
        return Objects.hash(instance);
    }
}
