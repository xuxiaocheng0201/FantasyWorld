package Mod;

import Core.CraftWorld;
import Core.Event.ElementsCheckedEvent;
import Core.Event.ElementsCheckingEvent;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModLoader {
    private static final HLog logger = new HLog("ModLoader", Thread.currentThread().getName());
    public static final File MODS_FILE = (new File(HStringHelper.merge(CraftWorld.RUNTIME_PATH, "mods"))).getAbsoluteFile();
    static {
        if (MODS_FILE.exists() && !MODS_FILE.isDirectory())
            HLog.logger(HELogLevel.ERROR, "Mods path is a file! MODS_PATH='", MODS_FILE.getPath(), "'.");
        else if (!MODS_FILE.exists() && !MODS_FILE.mkdirs())
            HLog.logger(HELogLevel.ERROR, "Creating MODS_PATH directory failed. MODS_PATH='", MODS_FILE.getPath(), "'.");
    }

    public static final List<Mod> modList = new ArrayList<>();
    public static final List<String> elementList = new ArrayList<>();
    public static final EventBus DEFAULT_EVENT_BUS = EventBus.getDefault();

    private static List<Class<?>> mods;
    private static List<Class<?>> elementImplements;
    private static List<Class<?>> elementUtils;
    private static final List<List<Mod>> sameMods = new ArrayList<>();

    public static void loadMods() {
        if (!MODS_FILE.exists() || !MODS_FILE.isDirectory())
            return;
        searchingMods();
        DEFAULT_EVENT_BUS.post(new ElementsCheckingEvent());
        checkElements();
        DEFAULT_EVENT_BUS.post(new ElementsCheckedEvent());
        //TODO
        HLog.logger(sameMods);
    }

    private static void searchingMods() {
        logger.log(HELogLevel.INFO, "Searching mods in '", MODS_FILE.getPath(), "'.");
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.addJarFilesInDirectory(MODS_FILE);
        modsFinder.addAnnotationClass(Mod.class);
        modsFinder.startFind();
        mods = modsFinder.getClassList();
        logger.log(HELogLevel.DEBUG, "Found @Mod in classes: ", mods);
        modsFinder.clearClassList();
        modsFinder.clearAnnotationClass();
        modsFinder.addAnnotationClass(NewElementImplement.class);
        modsFinder.startFind();
        elementImplements = modsFinder.getClassList();
        logger.log(HELogLevel.DEBUG, "Found @NewElementImplement in classes: ", elementImplements);
        modsFinder.clearClassList();
        modsFinder.clearAnnotationClass();
        modsFinder.addAnnotationClass(NewElementUtil.class);
        modsFinder.startFind();
        elementUtils = modsFinder.getClassList();
        logger.log(HELogLevel.DEBUG, "Found @NewElementUtil in classes: ", elementUtils);
    }

    private static void checkElements() {
        mods.removeIf(Objects::nonNull);
        for (Class<?> aClass: mods) {
            Mod classMod = aClass.getAnnotation(Mod.class);
            String classModName = classMod.name();
        }
        for (Class<?> aClass: elementImplements)
            elementList.add(aClass.getAnnotation(NewElementImplement.class).name());

    }
}
