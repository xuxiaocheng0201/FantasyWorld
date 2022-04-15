package CraftWorld.Dimension;

import Core.Addition.Implement.ElementUtil;
import Core.Addition.Implement.NewElementUtil;

@NewElementUtil(name = "Dimension")
public class DimensionUtils extends ElementUtil<IDimensionBase> {
    private static final DimensionUtils instance = new DimensionUtils();

    public static DimensionUtils getInstance() {
        return instance;
    }
}
