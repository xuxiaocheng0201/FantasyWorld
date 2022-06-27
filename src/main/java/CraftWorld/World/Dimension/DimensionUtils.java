package CraftWorld.World.Dimension;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import CraftWorld.DST.DSTUtils;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

@NewElementUtilCore(modName = "CraftWorld", elementName = "Dimension")
public class DimensionUtils extends ElementUtil<IDimensionBase> {
    private static final DimensionUtils instance = new DimensionUtils();
    public static DimensionUtils getInstance() {
        return instance;
    }

    @Override
    public void register(@NotNull String key, @NotNull Class<? extends IDimensionBase> value) throws HElementRegisteredException {
        super.register(key, value);
        DSTUtils.getInstance().register(key, value);
    }

    @Override
    public void reset(@NotNull String key, @NotNull Class<? extends IDimensionBase> value) {
        super.reset(key, value);
        DSTUtils.getInstance().reset(key, value);
    }
}
