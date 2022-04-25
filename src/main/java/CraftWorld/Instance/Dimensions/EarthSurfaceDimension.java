package CraftWorld.Instance.Dimensions;

import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Dimension.IDimensionBase;
import CraftWorld.Instance.DST.DSTMetaCompound;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;

import java.util.Objects;

public class EarthSurfaceDimension implements IDimensionBase {
    public static String id = "DimensionEarthSurface";
    static {
        try {
            DimensionUtils.getInstance().register(id, EarthSurfaceDimension.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    @Override
    public String getDimensionId() {
        return id;
    }

    private String name = "EarthSurface";
    private DSTMetaCompound dst = new DSTMetaCompound();

    public EarthSurfaceDimension() {
        super();
    }

    public EarthSurfaceDimension(String name) {
        super();
        this.name = name;
    }

    public EarthSurfaceDimension(DSTMetaCompound dst) {
        super();
        this.dst = dst;
    }

    public EarthSurfaceDimension(String name, DSTMetaCompound dst) {
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
    public DSTMetaCompound getDst() {
        return this.dst;
    }

    @Override
    public void setDst(DSTMetaCompound dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "EarthSurfaceDimension{" +
                "name='" + this.name + '\'' +
                ", dst=" + this.dst +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        EarthSurfaceDimension that = (EarthSurfaceDimension) o;
        return this.name.equals(that.name) && this.dst.equals(that.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.dst);
    }
}
