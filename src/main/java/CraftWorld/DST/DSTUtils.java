package CraftWorld.DST;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;

@NewElementUtilCore(elementName = "DST")
public class DSTUtils extends ElementUtil<IDSTBase> {
    private static final DSTUtils instance = new DSTUtils();

    public static DSTUtils getInstance() {
        return instance;
    }
}
