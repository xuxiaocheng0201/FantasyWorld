package CraftWorld.World.Block;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;

@NewElementUtilCore(modName = "CraftWorld", elementName = "Block")
public class BlockUtils extends ElementUtil<IBlockBase> {
    private static final BlockUtils instance = new BlockUtils();
    public static BlockUtils getInstance() {
        return instance;
    }
}
