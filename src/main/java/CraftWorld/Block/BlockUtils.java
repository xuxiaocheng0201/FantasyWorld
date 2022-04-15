package CraftWorld.Block;

import Core.Addition.Implement.ElementUtil;
import Core.Addition.Implement.NewElementUtil;

@NewElementUtil(name = "Block")
public class BlockUtils extends ElementUtil<IBlockBase> {
    private static final BlockUtils instance = new BlockUtils();

    public static BlockUtils getInstance() {
        return instance;
    }
}
