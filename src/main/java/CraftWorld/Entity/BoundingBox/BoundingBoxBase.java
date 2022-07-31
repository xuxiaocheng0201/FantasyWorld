package CraftWorld.Entity.BoundingBox;

import HeadLibs.Helper.HRandomHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class BoundingBoxBase implements IBoundingBoxBase {
    @Serial
    private static final long serialVersionUID = -7966130937678802153L;
    protected @NotNull UUID uuid = HRandomHelper.getRandomUUID();
    protected @Nullable IBoundingBoxBase baseBoundingBox;
    protected final @NotNull Set<UUID> additionalBoundingBox = new HashSet<>();
    protected boolean updated;

    @Override
    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    @Override
    public @Nullable IBoundingBoxBase getBaseBoundingBox() {
        return this.baseBoundingBox;
    }

    @Override
    public void setBaseBoundingBox(@Nullable IBoundingBoxBase baseBoundingBox) {
        if (baseBoundingBox != null) {
            if (this.baseBoundingBox != null)
                this.baseBoundingBox.removeAdditionalBoundingBox(this.uuid);
            baseBoundingBox.addAdditionalBoundingBox(this.uuid);
            this.baseBoundingBox = baseBoundingBox;
        } else if (this.baseBoundingBox != null) {
            this.baseBoundingBox.removeAdditionalBoundingBox(this.uuid);
            this.baseBoundingBox = null;
        }
    }

    @Override
    public void addAdditionalBoundingBox(@NotNull UUID uuid) {
        this.additionalBoundingBox.add(uuid);
    }

    @Override
    public void removeAdditionalBoundingBox(@NotNull UUID uuid) {
        this.additionalBoundingBox.remove(uuid);
    }

    @Override
    public boolean containAdditionalBoundingBox(@NotNull UUID uuid) {
        return this.additionalBoundingBox.contains(uuid);
    }

    @Override
    public void updatePos() {
        if (!this.getUpdated())
            return;
        if (this.baseBoundingBox != null)
            this.baseBoundingBox.updatePos();
        this.setUpdated(false);
    }

    @Override
    public boolean getUpdated() {
        if (this.baseBoundingBox != null && this.baseBoundingBox.getUpdated())
            return true;
        return this.updated;
    }

    @Override
    public void setUpdated(boolean updated) {
        this.updated = updated;
        if (this.baseBoundingBox != null)
            this.baseBoundingBox.setUpdated(updated);
    }
}
