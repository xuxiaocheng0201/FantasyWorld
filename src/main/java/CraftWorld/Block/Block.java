package CraftWorld.Block;

import CraftWorld.DST.DSTMetaCompound;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Exception.DSTFormatException;
import CraftWorld.Utils.IDResource;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class Block implements IDSTBase {
    private BlockPos pos = new BlockPos();
    private DSTMetaCompound dst = new DSTMetaCompound();
    private IDResource model = new IDResource();

    public static final String id = "Block";
    public static final String prefix = id;
    static {
        DSTUtils.register(id, Block.class);
    }

    @Override
    public void read(DataInput input) throws IOException {
        if (!BlockPos.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        pos.read(input);
        if (!DSTMetaCompound.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        dst.read(input);
        if (!IDResource.prefix.equals(input.readUTF()))
            throw new DSTFormatException();
        model.read(input);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        pos.write(output);
        dst.write(output);
        model.write(output);
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public DSTMetaCompound getDst() {
        return dst;
    }

    public void setDst(DSTMetaCompound dst) {
        this.dst = dst;
    }

    public IDResource getModel() {
        return model;
    }

    public void setModel(IDResource model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("Block{",
                "pos=", pos,
                ", dst=", dst,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof Block))
            return false;
        return Objects.equals(this.pos, ((Block) a).pos) &&
                Objects.equals(this.dst, ((Block) a).dst) &&
                Objects.equals(this.model, ((Block) a).model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, dst);
    }
}
