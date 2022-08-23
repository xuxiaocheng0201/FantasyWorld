package CraftWorld.Instance.Entity;

import CraftWorld.Entity.BasicInformation.EntityId;
import CraftWorld.Entity.EntityBase;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class EntityItem extends EntityBase {
    @Serial
    private static final long serialVersionUID = -8667431873160993005L;
    public static final EntityId id = EntityId.getEntityIdInstance("EntityItem");

    @Override
    public @NotNull EntityId getEntityId() {
        return id;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean needDelete() {
        return false;
    }
}
