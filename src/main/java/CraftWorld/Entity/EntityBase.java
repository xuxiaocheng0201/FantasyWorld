package CraftWorld.Entity;

import CraftWorld.Instance.DST.DSTComplexMeta;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public abstract class EntityBase implements IEntityBase {
    @Serial
    private static final long serialVersionUID = 4633871649026420045L;
    protected /*final @NotNull*/ Entity entityInstance;
    @Override
    public void setExistingEntityInstance(@NotNull Entity entity) {
        this.entityInstance = entity;
    }

    protected @NotNull String name = "";
    protected final @NotNull DSTComplexMeta dst = new DSTComplexMeta();

    protected EntityBase() {
        super();
    }

    protected EntityBase(@NotNull String name) {
        super();
        this.name = name;
    }

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
}
