package CraftWorld.Entity;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.Instance.DST.DSTComplexMeta;

@NewElementImplementCore(modName = "CraftWorld", elementName = "Entity")
public interface IEntityBase extends ElementImplement {
    String getEntityId();
    String getEntityName();
    void setEntityName(String name);
    DSTComplexMeta getEntityDST();
    boolean needDelete();
}
