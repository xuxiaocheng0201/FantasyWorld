package CraftWorld.Instance.Blocks;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.Block.BlockPos;
import CraftWorld.Block.BlockUtils;
import CraftWorld.Block.IBlockBase;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class BlockAir implements IBlockBase {
    private String name = "Air";
    private BlockPos pos = new BlockPos();
    private DSTMetaCompound dst = new DSTMetaCompound();

    static {
        try {
            BlockUtils.getInstance().register("BlockAir", BlockAir.class);
        } catch (ElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    public BlockAir() {
        super();

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
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public DSTMetaCompound getDst() {
        return this.dst;
    }

    @Override
    public void setDst(DSTMetaCompound dst) {
        this.dst = dst;
    }
}
