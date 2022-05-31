package CraftWorld.Entity.BoundingBox;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Entity.EntityPos;
import org.jetbrains.annotations.NotNull;

@NewElementImplementCore(modName = "CraftWorld", elementName = "BoundingBox")
public interface IBoundingBoxBase extends IDSTBase {
    @NotNull EntityPos getCentrePos();
    double getMaxRadius();
    double getMinRadius();
}
