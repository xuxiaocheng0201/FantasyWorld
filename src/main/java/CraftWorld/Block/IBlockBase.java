package CraftWorld.Block;

import CraftWorld.Instance.DST.DSTMetaCompound;

public interface IBlockBase {
    String getName();
    void setName(String name);
    BlockPos getPos();
    void setPos(BlockPos pos);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
