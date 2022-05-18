package CraftWorld.World.Block;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Instance.DST.DSTComplexMeta;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Block")
public interface IBlockBase extends ElementImplement {
    String getBlockId();
    String getBlockName();
    void setBlockName(String name);
    DSTComplexMeta getBlockDST();
}
