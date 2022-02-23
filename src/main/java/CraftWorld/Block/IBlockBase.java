package CraftWorld.Block;

import CraftWorld.Instance.DST.DSTMetaCompound;
import Mod.NewElementImplement;

@NewElementImplement(name = "Block")
public interface IBlockBase {
    String getName();
    void setName(String name);
    BlockPos getPos();
    void setPos(BlockPos pos);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
