package CraftWorld.World.Block;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import CraftWorld.DST.DSTUtils;
import CraftWorld.World.Block.BasicInformation.BlockId;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

@NewElementUtilCore(modName = "CraftWorld", elementName = "Block")
public class BlockUtils extends ElementUtil<BlockId, IBlockBase> {
    private static final BlockUtils instance = new BlockUtils();
    public static BlockUtils getInstance() {
        return instance;
    }

    @Override
    public void register(@NotNull BlockId key, @NotNull Class<? extends IBlockBase> value) throws HElementRegisteredException {
        super.register(key, value);
        DSTUtils.getInstance().register(key, value);
    }

    @Override
    public void reset(@NotNull BlockId key, @NotNull Class<? extends IBlockBase> value) {
        super.reset(key, value);
        DSTUtils.getInstance().reset(key, value);
    }

    @Override
    public void deregisterKey(@NotNull BlockId key) {
        super.deregisterKey(key);
        DSTUtils.getInstance().deregisterKey(key);
    }

    @Override
    public void deregisterValue(@NotNull Class<? extends IBlockBase> value) {
        super.deregisterValue(value);
        DSTUtils.getInstance().deregisterValue(value);
    }

    @Override
    public void deregisterAll() {
        for (BlockId key: this.elements.keys())
            DSTUtils.getInstance().deregisterKey(key);
        super.deregisterAll();
    }
}
