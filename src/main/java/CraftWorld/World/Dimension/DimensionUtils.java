package CraftWorld.World.Dimension;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import CraftWorld.DST.DSTUtils;
import CraftWorld.World.Dimension.BasicInformation.DimensionId;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

@NewElementUtilCore(modName = "CraftWorld", elementName = "Dimension")
public class DimensionUtils extends ElementUtil<DimensionId, IDimensionBase> {
    private static final DimensionUtils instance = new DimensionUtils();
    public static DimensionUtils getInstance() {
        return instance;
    }

    @Override
    public void register(@NotNull DimensionId key, @NotNull Class<? extends IDimensionBase> value) throws HElementRegisteredException {
        super.register(key, value);
        DSTUtils.getInstance().register(key, value);
    }

    @Override
    public void reset(@NotNull DimensionId key, @NotNull Class<? extends IDimensionBase> value) {
        super.reset(key, value);
        DSTUtils.getInstance().reset(key, value);
    }

    @Override
    public void deregisterKey(@NotNull DimensionId key) {
        super.deregisterKey(key);
        DSTUtils.getInstance().deregisterKey(key);
    }

    @Override
    public void deregisterValue(@NotNull Class<? extends IDimensionBase> value) {
        super.deregisterValue(value);
        DSTUtils.getInstance().deregisterValue(value);
    }

    @Override
    public void deregisterAll() {
        for (DimensionId key: this.elements.keys())
            DSTUtils.getInstance().deregisterKey(key);
        super.deregisterAll();
    }
}
