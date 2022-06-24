package CraftWorld.Entity.BoundingBox;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Entity.EntityPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@NewElementImplementCore(modName = "CraftWorld", elementName = "BoundingBox")
public interface IBoundingBoxBase extends IDSTBase {
    @NotNull EntityPos getCentrePos();
    double getMaxRadius();
    double getMinRadius();

    @Nullable IBoundingBoxBase getBaseBoundingBox();
    @NotNull Set<IBoundingBoxBase> getAdditionalBoundingBox();
}
