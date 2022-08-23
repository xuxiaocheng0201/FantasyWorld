package CraftWorld.World.Dimension;

import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Chunk.ChunkPos;
import HeadLibs.Registerer.HLinkedSetRegisterer;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public abstract class DimensionBase implements IDimensionBase {
    @Serial
    private static final long serialVersionUID = 6496633959075347635L;
    protected /*final @NotNull*/ Dimension dimensionInstance;
    @Override
    public void setExistingDimensionInstance(@NotNull Dimension dimension) {
        this.dimensionInstance = dimension;
    }

    protected String name = "";
    protected final @NotNull DSTComplexMeta dst = new DSTComplexMeta();
    protected final @NotNull HLinkedSetRegisterer<ChunkPos> prepareChunkPos = new HLinkedSetRegisterer<>(false);

    protected DimensionBase() {
        super();
    }

    protected DimensionBase(@NotNull String name) {
        super();
        this.name = name;
    }

    @Override
    public @NotNull String getDimensionName() {
        return this.name;
    }

    @Override
    public void setDimensionName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull HLinkedSetRegisterer<ChunkPos> getPrepareChunkPos() {
        return this.prepareChunkPos;
    }

    @Override
    public @NotNull DSTComplexMeta getDimensionDST() {
        return this.dst;
    }
}
