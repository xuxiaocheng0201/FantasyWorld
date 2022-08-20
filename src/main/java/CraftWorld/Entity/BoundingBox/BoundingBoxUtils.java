package CraftWorld.Entity.BoundingBox;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BoundingBox.BasicInformation.BoundingBoxId;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

@NewElementUtilCore(modName = "CraftWorld", elementName = "BoundingBox")
public class BoundingBoxUtils extends ElementUtil<BoundingBoxId, IBoundingBoxBase> {
    private static final BoundingBoxUtils instance = new BoundingBoxUtils();
    public static BoundingBoxUtils getInstance() {
        return instance;
    }

    @Override
    public void register(@NotNull BoundingBoxId key, @NotNull Class<? extends IBoundingBoxBase> value) throws HElementRegisteredException {
        super.register(key, value);
        DSTUtils.getInstance().register(key, value);
    }

    @Override
    public void reset(@NotNull BoundingBoxId key, @NotNull Class<? extends IBoundingBoxBase> value) {
        super.reset(key, value);
        DSTUtils.getInstance().reset(key, value);
    }

    @Override
    public void deregisterKey(@NotNull BoundingBoxId key) {
        super.deregisterKey(key);
        DSTUtils.getInstance().deregisterKey(key);
    }

    @Override
    public void deregisterValue(@NotNull Class<? extends IBoundingBoxBase> value) {
        super.deregisterValue(value);
        DSTUtils.getInstance().deregisterValue(value);
    }

    @Override
    public void deregisterAll() {
        for (BoundingBoxId key: this.elements.keys())
            DSTUtils.getInstance().deregisterKey(key);
        super.deregisterAll();
    }
}
