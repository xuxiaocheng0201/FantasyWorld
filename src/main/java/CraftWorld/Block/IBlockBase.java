package CraftWorld.Block;

import Core.Addition.Implement.ElementImplement;
import Core.Addition.Implement.NewElementImplement;
import CraftWorld.Instance.DST.DSTMetaCompound;

@NewElementImplement(elementName = "Block")
public interface IBlockBase extends ElementImplement {
    String getBlockName();
    void setBlockName(String name);
    BlockPos getPos();
    void setPos(BlockPos pos);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
