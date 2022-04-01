package CraftWorld.Instance.Dimensions;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Dimension.IDimensionBase;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class EarthSurfaceDimension implements IDimensionBase {
    private String name = "EarthSurface";

    static {
        try {
            DimensionUtils.getInstance().register("EarthSurfaceDimension", EarthSurfaceDimension.class);
        } catch (ElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    public EarthSurfaceDimension() {

    }

    @Override
    public String getDimensionName() {
        return name;
    }

    @Override
    public void setDimensionName(String name) {
        this.name = name;
    }
}
