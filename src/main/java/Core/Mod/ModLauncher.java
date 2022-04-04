package Core.Mod;

import Core.EventBus.EventBusManager;
import Core.Events.*;
import Core.Exceptions.ModRequirementsException;
import Core.Mod.New.ModImplement;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.util.List;

public class ModLauncher {
    @SuppressWarnings("FieldHasSetterButNoGetter")
    private static HLog logger;

    public static void setLogger(HLog logger) {
        ModLauncher.logger = logger;
    }

    public static boolean loadModClasses() {
        logger = new HLog("ModClassesLoader", Thread.currentThread().getName());
        if (!ModClassesLoader.MODS_FILE.exists() || !ModClassesLoader.MODS_FILE.isDirectory())
            return true;
        ModClassesLoader.setLogger(logger);
        logger.log(HLogLevel.INFO, "Searching mods in '", ModClassesLoader.MODS_FILE.getPath(), "'.");
        ModClassesLoader.pickAllClasses();
        for (Class<?> aClass: ModClassesLoader.getAllClasses())
            try {
                EventBusManager.register(aClass);
            } catch (NoSuchMethodException exception) {
                HLog.logger(HLogLevel.ERROR, exception);
            }
        EventBusManager.getDefaultEventBus().post(new ElementsCheckingEvent());
        ModClassesLoader.checkSameMods();
        if (!ModClassesLoader.getSameMods().isEmpty())
            return true;
        ModClassesLoader.checkSameElementImplements();
        if (!ModClassesLoader.getSameImplements().isEmpty())
            return true;
        ModClassesLoader.checkSameElementUtils();
        if (!ModClassesLoader.getSameUtils().isEmpty())
            return true;
        ModClassesLoader.checkElementsPair();
        if (!ModClassesLoader.getSingleImplements().isEmpty())
            return true;
        if (!ModClassesLoader.getSingleUtils().isEmpty())
            return true;
        EventBusManager.getDefaultEventBus().post(new ElementsCheckedEvent());
        return false;
    }

    public static boolean sortMods(HLog logger) {
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

    public static List<ModRequirementsException> getSorterExceptions() {
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
        ModClassesLoader.gc();
        ModClassesSorter.gc();
        System.gc();
    }
}
