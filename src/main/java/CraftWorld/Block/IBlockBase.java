package CraftWorld.Block;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Instance.DST.DSTMetaCompound;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Block")
public interface IBlockBase extends ElementImplement {
    String getBlockId();
    String getBlockName();
    void setBlockName(String name);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
