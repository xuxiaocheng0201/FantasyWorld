package CraftWorld.DST;

import Core.Addition.Implement.ElementUtil;
import Core.Addition.Implement.NewElementUtil;

@NewElementUtil(name = "DST")
public class DSTUtils extends ElementUtil<IDSTBase> {
    private static final DSTUtils instance = new DSTUtils();

    public static DSTUtils getInstance() {
        return instance;
    }
}
