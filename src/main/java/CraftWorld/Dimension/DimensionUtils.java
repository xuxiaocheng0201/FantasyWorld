package CraftWorld.Dimension;

import Core.Mod.New.ElementUtil;
import Core.Mod.New.NewElementUtil;

@NewElementUtil(name = "Dimension")
public class DimensionUtils extends ElementUtil<IDimensionBase> {
    private static final DimensionUtils instance = new DimensionUtils();

    public static DimensionUtils getInstance() {
        return instance;
    }
}
