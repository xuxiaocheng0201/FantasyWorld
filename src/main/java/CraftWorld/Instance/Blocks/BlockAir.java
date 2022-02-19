package CraftWorld.Instance.Blocks;

import CraftWorld.Block.BlockPos;
import CraftWorld.Block.BlockUtils;
import CraftWorld.Block.IBlockBase;
import CraftWorld.Instance.DST.DSTMetaCompound;

public class BlockAir implements IBlockBase {
    private String name = "Air";
    private BlockPos pos = new BlockPos();
    private DSTMetaCompound dst = new DSTMetaCompound();

    static {
        BlockUtils.register("BlockAir", BlockAir.class);
    }

    public BlockAir() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public DSTMetaCompound getDst() {
        return dst;
    }

    @Override
    public void setDst(DSTMetaCompound dst) {
        this.dst = dst;
    }
}
