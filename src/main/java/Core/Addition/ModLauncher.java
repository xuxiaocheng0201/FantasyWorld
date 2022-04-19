package Core.Addition;

import Core.Addition.Mod.ModImplement;
import Core.EventBus.EventBusManager;
import Core.Events.ModInitializedEvent;
import Core.Events.ModInitializingEvent;
import Core.Events.PostInitializationModsEvent;
import Core.Events.PreInitializationModsEvent;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

public class ModLauncher {
    public static void launchMods() {
        HLog logger = new HLog("ModLauncher", Thread.currentThread().getName());
        EventBusManager.getDefaultEventBus().post(new PreInitializationModsEvent());
        for (Class<? extends ModImplement> aClass: ModClassesSorter.getSortedMods()) {
            EventBusManager.getDefaultEventBus().post(new ModInitializingEvent(aClass));
            ModImplement instance = HClassHelper.getInstance(aClass);
            if (instance == null) {
                logger.log(HLogLevel.ERROR, "No Common Constructor for creating Mod class: ", aClass);
                continue;
            }
            try {
                instance.mainInitialize();
                EventBusManager.getDefaultEventBus().post(new ModInitializedEvent(aClass, true));
            } catch (Exception exception) {
                EventBusManager.getDefaultEventBus().post(new ModInitializedEvent(aClass, false));
            }
        }
        EventBusManager.getDefaultEventBus().post(new PostInitializationModsEvent());
    }
}
