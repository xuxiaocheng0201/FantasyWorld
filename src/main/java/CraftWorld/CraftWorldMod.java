package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.ElementsCheckingEvent;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.FileTreeStorage;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import CraftWorld.Utils.Angle;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

@SuppressWarnings("MethodMayBeStatic")
@EventSubscribe
@NewMod(name = "CraftWorld", version = "0.0.1", requirements = "before:*")
public class CraftWorldMod implements ModImplement {
    private static final HLog logger = new HLog(Thread.currentThread().getName());

    @Subscribe
    public void elementsChecking(ElementsCheckingEvent event) throws HElementRegisteredException {
        DSTUtils.getInstance().register(Angle.id, Angle.class);
    }

    @Subscribe
    public void preInitialize(PreInitializationModsEvent event) throws IOException {
        logger.setName("CraftWorld", Thread.currentThread().getName());
        FileTreeStorage.extractFiles(CraftWorldMod.class, "assets\\CraftWorld", "assets\\CraftWorld");
    }

    @Override
    public void mainInitialize() {
        CraftWorld.getInstance().getWorld().addPrepareDimension(DimensionEarthSurface.id);
    }
}
