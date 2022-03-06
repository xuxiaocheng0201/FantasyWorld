package CraftWorld.DST;

import Core.Mod.New.ElementUtil;
import Core.Mod.New.NewElementUtil;

@NewElementUtil(name = "DST")
public class DSTUtils extends ElementUtil<IDSTBase> {
    private static final DSTUtils instance = new DSTUtils();

    public static DSTUtils getInstance() {
        return instance;
    }
}
