package CraftWorld.Instance.Dimensions;

import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.DimensionBase;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class DimensionEarthSurface extends DimensionBase {
    @Serial
    private static final long serialVersionUID = -1129089963710423740L;
    public static final String id = "DimensionEarthSurface";

    @Override
    public @NotNull String getDimensionId() {
        return id;
    }

    public DimensionEarthSurface() {
        super("EarthSurface");
        for (int x = -1; x < 2; ++x)
            for (int y = -1; y < 2; ++y)
                for (int z = -1; z < 2; ++z) {
                    try {
                        this.prepareChunkPos.register(new ChunkPos(x, y, z));
                    } catch (HElementRegisteredException ignore) {
                    }
                }
    }

    public DimensionEarthSurface(String name) {
        super(name);
        for (int x = -1; x < 2; ++x)
            for (int y = -1; y < 2; ++y)
                for (int z = -1; z < 2; ++z) {
                    try {
                        this.prepareChunkPos.register(new ChunkPos(x, y, z));
                    } catch (HElementRegisteredException ignore) {
                    }
                }
    }

    @Override
    public @NotNull String toString() {
        return "DimensionEarthSurface{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        DimensionEarthSurface that = (DimensionEarthSurface) o;
        return this.name.equals(that.name) && this.dst.equals(that.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.dst);
    }
}
