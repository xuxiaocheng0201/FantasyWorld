package CraftWorld.DST;

import Core.Mod.NewElement.ElementUtil;
import Core.Mod.NewElement.NewElementUtil;

@NewElementUtil(name = "DST")
public class DSTUtils extends ElementUtil<IDSTBase> {
    private static final DSTUtils instance = new DSTUtils();

    public static DSTUtils getInstance() {
        return instance;
    }
}
