package CraftWorld.Instance.Dimensions;

import CraftWorld.DST.DSTUtils;
import CraftWorld.Instance.DST.DSTComplexMeta;
import CraftWorld.World.Chunk.ChunkPos;
import CraftWorld.World.Dimension.DimensionUtils;
import CraftWorld.World.Dimension.IDimensionBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HLinkedSetRegisterer;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class DimensionEarthSurface implements IDimensionBase {
    @Serial
    private static final long serialVersionUID = -1129089963710423740L;

    public static String id = "DimensionEarthSurface";
    public static String prefix = DSTUtils.prefix(id);
    public static String suffix = DSTUtils.suffix(id);
    static {
        try {
            DimensionUtils.getInstance().register(id, DimensionEarthSurface.class);
            DSTUtils.getInstance().register(id, DimensionEarthSurface.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    @Override
    public String getDimensionId() {
        return id;
    }

    private String name = "EarthSurface";
    private final DSTComplexMeta dst;
    private final HLinkedSetRegisterer<ChunkPos> prepareChunkPos = new HLinkedSetRegisterer<>();
    {
        for (int x = -1; x < 2; ++x)
            for (int y = -1; y < 2; ++y)
                for (int z = -1; z < 2; ++z) {
                    try {
                        this.prepareChunkPos.register(new ChunkPos(x, y, z));
                    } catch (HElementRegisteredException ignore) {
                    }
                }
    }

    public DimensionEarthSurface() {
        super();
        this.dst = new DSTComplexMeta();
    }

    public DimensionEarthSurface(String name) {
        super();
        this.name = name;
        this.dst = new DSTComplexMeta();
    }

    public DimensionEarthSurface(DSTComplexMeta dst) {
        super();
        this.dst = dst;
    }

    public DimensionEarthSurface(String name, DSTComplexMeta dst) {
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
    public HLinkedSetRegisterer<ChunkPos> getPrepareChunkPos() {
        return this.prepareChunkPos;
    }

    @Override
    public DSTComplexMeta getDimensionDST() {
        return this.dst;
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

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
    }
}
