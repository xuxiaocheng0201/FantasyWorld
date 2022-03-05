package CraftWorld.Dimension;

import Core.Mod.NewElement.ElementUtil;
import Core.Mod.NewElement.NewElementUtil;

@NewElementUtil(name = "Dimension")
public class DimensionUtils extends ElementUtil<IDimensionBase> {
    private static final DimensionUtils instance = new DimensionUtils();

    public static DimensionUtils getInstance() {
        return instance;
    }
}
