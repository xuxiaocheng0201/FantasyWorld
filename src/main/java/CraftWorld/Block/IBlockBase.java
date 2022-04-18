package CraftWorld.Block;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Instance.DST.DSTMetaCompound;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Block")
public interface IBlockBase extends ElementImplement {
    String getBlockName();
    void setBlockName(String name);
    BlockPos getPos();
    void setPos(BlockPos pos);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
