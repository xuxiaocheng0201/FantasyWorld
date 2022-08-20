package CraftWorld.Instance.Entity;

import CraftWorld.Entity.BasicInformation.EntityId;
import CraftWorld.Entity.IEntityBase;
import CraftWorld.Instance.DST.DSTComplexMeta;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class EntityHuman implements IEntityBase {
    @Serial
    private static final long serialVersionUID = 1632442590862972115L;
    public static final EntityId id = EntityId.getEntityIdInstance("EntityHuman");

    @Override
    public @NotNull EntityId getEntityId() {
        return id;
    }

    protected @NotNull String name = "Human";
    protected @NotNull DSTComplexMeta dst = new DSTComplexMeta();

    @Override
    public @NotNull String getEntityName() {
        return this.name;
    }

    @Override
    public void setEntityName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull DSTComplexMeta getEntityDST() {
        return this.dst;
    }

    @Override
    public boolean needDelete() {
        return false;
    }
}
