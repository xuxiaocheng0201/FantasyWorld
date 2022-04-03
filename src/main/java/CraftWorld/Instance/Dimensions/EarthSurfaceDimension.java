package CraftWorld.Instance.Dimensions;

import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Dimension.IDimensionBase;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HElementRegisteredException;

public class EarthSurfaceDimension implements IDimensionBase {
    private String name = "EarthSurface";

    static {
        try {
            DimensionUtils.getInstance().register("EarthSurfaceDimension", EarthSurfaceDimension.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    public EarthSurfaceDimension() {
        super();

    }

    @Override
    public String getDimensionName() {
        return this.name;
    }

    @Override
    public void setDimensionName(String name) {
        this.name = name;
    }
}
