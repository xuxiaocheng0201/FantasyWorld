package CraftWorld.Block;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.NewElementImplement;
import CraftWorld.Instance.DST.DSTMetaCompound;

@NewElementImplement(name = "Block")
public interface IBlockBase extends ElementImplement {
    BlockPos getPos();
    void setPos(BlockPos pos);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
