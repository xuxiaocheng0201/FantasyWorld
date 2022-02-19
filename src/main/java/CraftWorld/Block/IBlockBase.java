package CraftWorld.Block;

import CraftWorld.Instance.DST.DSTMetaCompound;

public interface IBlockBase {
    BlockPos getPos();
    void setPos(BlockPos pos);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
    String getName();
    void setName(String name);
}
