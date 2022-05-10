package CraftWorld.Entity;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Instance.DST.DSTMetaCompound;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Entity")
public interface IEntityBase extends ElementImplement {
    String getEntityId();
    String getEntityName();
    void setEntityName(String name);
    DSTMetaCompound getEntityDST();
    boolean needDelete();
}
