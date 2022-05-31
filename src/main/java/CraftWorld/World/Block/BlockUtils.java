package CraftWorld.World.Block;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import CraftWorld.DST.DSTUtils;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

@NewElementUtilCore(modName = "CraftWorld", elementName = "Block")
public class BlockUtils extends ElementUtil<IBlockBase> {
    private static final BlockUtils instance = new BlockUtils();
    public static BlockUtils getInstance() {
        return instance;
    }

    @Override
    public void register(@NotNull String key, @NotNull Class<? extends IBlockBase> value) throws HElementRegisteredException {
        super.register(key, value);
        DSTUtils.getInstance().register(key, value);
    }

    @Override
    public void reset(@NotNull String key, @NotNull Class<? extends IBlockBase> value) {
        super.reset(key, value);
        DSTUtils.getInstance().reset(key, value);
    }
}
