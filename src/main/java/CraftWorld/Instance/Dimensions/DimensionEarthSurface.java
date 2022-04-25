package CraftWorld.Instance.Dimensions;

import CraftWorld.Chunk.ChunkPos;
import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Dimension.IDimensionBase;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DimensionEarthSurface implements IDimensionBase {
    public static String id = "DimensionEarthSurface";
    static {
        try {
            DimensionUtils.getInstance().register(id, DimensionEarthSurface.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    @Override
    public String getDimensionId() {
        return id;
    }

    private String name = "EarthSurface";
    private final Set<ChunkPos> prepareChunkPos = new HashSet<>();
    private DSTMetaCompound dst = new DSTMetaCompound();
    {
        for (int x = -1; x < 2; ++x)
            for (int y = -1; y < 2; ++y)
                for (int z = -1; z < 2; ++z)
                    this.prepareChunkPos.add(new ChunkPos(x, y, z));
    }

    public DimensionEarthSurface() {
        super();
    }

    public DimensionEarthSurface(String name) {
        super();
        this.name = name;
    }

    public DimensionEarthSurface(DSTMetaCompound dst) {
        super();
        this.dst = dst;
    }

    public DimensionEarthSurface(String name, DSTMetaCompound dst) {
        super();
        this.name = name;
        this.dst = dst;
    }

    @Override
    public String getDimensionName() {
        return this.name;
    }

    @Override
    public void setDimensionName(String name) {
        this.name = name;
    }

    @Override
    public Set<ChunkPos> getPrepareChunkPos() {
        return this.prepareChunkPos;
    }

    @Override
    public DSTMetaCompound getDst() {
        return this.dst;
    }

    @Override
    public void setDst(DSTMetaCompound dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "DimensionEarthSurface{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(Object o) {
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
