package CraftWorld.Block;

import Core.Mod.NewElement.ElementImplement;
import Core.Mod.NewElement.NewElementImplement;
import CraftWorld.Instance.DST.DSTMetaCompound;

@NewElementImplement(name = "Block")
public interface IBlockBase extends ElementImplement {
    BlockPos getPos();
    void setPos(BlockPos pos);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
