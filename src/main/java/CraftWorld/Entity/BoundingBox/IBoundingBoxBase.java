package CraftWorld.Entity.BoundingBox;

import Core.Addition.Element.NewElementImplementCore;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Entity.EntityPos;
import HeadLibs.DataStructures.IUpdatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@NewElementImplementCore(modName = "CraftWorld", elementName = "BoundingBox")
public interface IBoundingBoxBase extends IDSTBase, IUpdatable {
    @Contract(pure = true) @NotNull UUID getUUID();

    @Nullable IBoundingBoxBase getBaseBoundingBox();
    void setBaseBoundingBox(@Nullable IBoundingBoxBase baseBoundingBox);
    void addAdditionalBoundingBox(@NotNull UUID uuid);
    void removeAdditionalBoundingBox(@NotNull UUID uuid);
    boolean containAdditionalBoundingBox(@NotNull UUID uuid);

    void updatePos();
    @NotNull EntityPos getCentrePos();
    double getMaxRadius();
    double getMinRadius();
}
