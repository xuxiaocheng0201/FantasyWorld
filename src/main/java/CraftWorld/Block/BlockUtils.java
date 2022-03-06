package CraftWorld.Block;

import Core.Mod.New.ElementUtil;
import Core.Mod.New.NewElementUtil;

@NewElementUtil(name = "Block")
public class BlockUtils extends ElementUtil<IBlockBase> {
    private static final BlockUtils instance = new BlockUtils();

    public static BlockUtils getInstance() {
        return instance;
    }
}
