package CraftWorld.Entity;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Entity.BasicInformation.EntityId;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

@NewElementUtilCore(modName = "CraftWorld", elementName = "Entity")
public class EntityUtils extends ElementUtil<EntityId, IEntityBase> {
    private static final EntityUtils instance = new EntityUtils();
    public static EntityUtils getInstance() {
        return instance;
    }

    @Override
    public void register(@NotNull EntityId key, @NotNull Class<? extends IEntityBase> value) throws HElementRegisteredException {
        super.register(key, value);
        DSTUtils.getInstance().register(key, value);
    }

    @Override
    public void reset(@NotNull EntityId key, @NotNull Class<? extends IEntityBase> value) {
        super.reset(key, value);
        DSTUtils.getInstance().reset(key, value);
    }

    @Override
    public void deregisterKey(@NotNull EntityId key) {
        super.deregisterKey(key);
        DSTUtils.getInstance().deregisterKey(key);
    }

    @Override
    public void deregisterValue(@NotNull Class<? extends IEntityBase> value) {
        super.deregisterValue(value);
        DSTUtils.getInstance().deregisterValue(value);
    }

    @Override
    public void deregisterAll() {
        for (EntityId key: this.elements.keys())
            DSTUtils.getInstance().deregisterKey(key);
        super.deregisterAll();
    }
}
