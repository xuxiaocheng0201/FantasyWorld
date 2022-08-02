package CraftWorld.Instance.Dimensions;

import Core.EventBus.EventSubscribe;
import CraftWorld.Events.ChunkGenerateEvent;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.DimensionBase;
import HeadLibs.Registerer.HLinkedSetRegisterer;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

@EventSubscribe
public class NullDimension extends DimensionBase {
    @Serial
    private static final long serialVersionUID = -5202694397165174510L;
    public static final String id = "NullDimension";

    @Override
    public @NotNull String getDimensionId() {
        return id;
    }

    public NullDimension() {
        super("Null");
    }

    @Override
    public void setDimensionName(@NotNull String name) {
    }

    @Override
    public @NotNull HLinkedSetRegisterer<ChunkPos> getPrepareChunkPos() {
        return new HLinkedSetRegisterer<>();
    }

    @Override
    public @NotNull String toString() {
        return "NullDimension{" +
                "dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof NullDimension that)) return false;
        return this.dst.equals(that.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dst);
    }

    @SuppressWarnings("MethodMayBeStatic")
    @Subscribe
    public void generate(@NotNull ChunkGenerateEvent event) {
        if (event.chunk().getDimension().getInstance().getDimensionId().equals(id))
            event.chunk().clearBlocks();
    }
}
