package CraftWorld.Instance.Blocks;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.Block.BlockPos;
import CraftWorld.Block.BlockUtils;
import CraftWorld.Block.IBlockBase;
import CraftWorld.Instance.DST.DSTMetaCompound;

public class BlockAir implements IBlockBase {
    private String name = "Air";
    private BlockPos pos = new BlockPos();
    private DSTMetaCompound dst = new DSTMetaCompound();

    static {
        try {
            BlockUtils.getInstance().register("BlockAir", BlockAir.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
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
