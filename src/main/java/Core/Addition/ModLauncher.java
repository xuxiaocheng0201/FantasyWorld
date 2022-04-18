package Core.Addition;

import Core.Addition.Mod.ModImplement;
import Core.EventBus.EventBusManager;
import Core.Events.ModInitializedEvent;
import Core.Events.ModInitializingEvent;
import Core.Events.PostInitializationModsEvent;
import Core.Events.PreInitializationModsEvent;
import Core.Exceptions.ModInformationException;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.util.List;

public class ModLauncher {
    private static HLog logger;

    public static boolean sortMods() {
        logger = new HLog("ModSorter", Thread.currentThread().getName());
        ModClassesSorter.buildModContainer();
        ModClassesSorter.checkModContainer();
        if (!ModClassesSorter.getExceptions().isEmpty()) {
            logger.log(HLogLevel.BUG, "Mod Loading Error in checking requirements!");
            return true;
        }
        ModClassesSorter.toSimpleModContainer();
        ModClassesSorter.sortMods();
        if (!ModClassesSorter.getExceptions().isEmpty()) {
            logger.log(HLogLevel.BUG, "Mod Loading Error in shorting!");
            return true;
        }
        return false;
    }

    public static List<ModInformationException> getSorterExceptions() {
        return ModClassesSorter.getExceptions();
    }

    public static void launchMods() {
        logger = new HLog("ModLauncher", Thread.currentThread().getName());
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

    public static void gc() {
        ModClassesSorter.gc();
        System.gc();
    }
}
