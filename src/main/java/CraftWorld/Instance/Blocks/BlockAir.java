package CraftWorld.Instance.Blocks;

import CraftWorld.Block.BlockUtils;
import CraftWorld.Block.IBlockBase;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.util.Objects;

public class BlockAir implements IBlockBase {
    public static String id = "BlockAir";
    static {
        try {
            BlockUtils.getInstance().register(id, BlockAir.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    @Override
    public String getBlockId() {
        return id;
    }

    private String name = "Air";
    private DSTMetaCompound dst = new DSTMetaCompound();

    public BlockAir() {
        super();
    }

    public BlockAir(String name) {
        super();
        this.name = name;
    }

    public BlockAir(DSTMetaCompound dst) {
        super();
        this.dst = dst;
    }

    public BlockAir(String name, DSTMetaCompound dst) {
        super();
        this.name = name;
        this.dst = dst;
    }

    @Override
    public String getBlockName() {
        return this.name;
    }

    @Override
    public void setBlockName(String name) {
        this.name = name;
    }

    @Override
    public DSTMetaCompound getDst() {
        return this.dst;
    }

    @Override
    public void setDst(DSTMetaCompound dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "BlockAir{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        BlockAir blockAir = (BlockAir) o;
        return this.name.equals(blockAir.name) && this.dst.equals(blockAir.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dst);
    }
}
