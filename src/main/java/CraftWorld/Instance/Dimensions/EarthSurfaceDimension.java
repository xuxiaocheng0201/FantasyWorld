package CraftWorld.Instance.Dimensions;

import Core.Exceptions.ElementRegisteredException;
import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Dimension.IDimensionBase;

public class EarthSurfaceDimension implements IDimensionBase {
    private String name = "EarthSurface";

    static {
        try {
            DimensionUtils.getInstance().register("EarthSurfaceDimension", EarthSurfaceDimension.class);
        } catch (ElementRegisteredException exception) {
            exception.printStackTrace();
        }
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
