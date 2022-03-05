package CraftWorld.Instance.Dimension;

import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Dimension.IDimensionBase;

public class DimensionEarthSurface implements IDimensionBase {
    private String name = "EarthSurface";

    static {
        DimensionUtils.getInstance().register("DimensionEarthSurface", DimensionEarthSurface.class);
    }

    public DimensionEarthSurface() {

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
