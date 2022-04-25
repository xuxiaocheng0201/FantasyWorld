package CraftWorld.Dimension;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Instance.DST.DSTMetaCompound;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Dimension")
public interface IDimensionBase extends ElementImplement {
    String getDimensionId();
    String getDimensionName();
    void setDimensionName(String name);
    DSTMetaCompound getDst();
    void setDst(DSTMetaCompound dst);
}
