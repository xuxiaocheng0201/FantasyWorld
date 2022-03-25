package CraftWorld.Instance.Dimensions;

import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Dimension.IDimensionBase;

public class EarthSurfaceDimension implements IDimensionBase {
    private String name = "EarthSurface";

    static {
        DimensionUtils.getInstance().register("EarthSurfaceDimension", EarthSurfaceDimension.class);
    }

    public EarthSurfaceDimension() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
