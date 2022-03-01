package Mod;

import Core.CraftWorld;
import Core.Event.ElementsCheckedEvent;
import Core.Event.ElementsCheckingEvent;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ModLoader {
    private static final HLog logger = new HLog("ModLoader", Thread.currentThread().getName());
    public static final File MODS_FILE = (new File(HStringHelper.merge(CraftWorld.RUNTIME_PATH, "mods"))).getAbsoluteFile();
    static {
        if (MODS_FILE.exists() && !MODS_FILE.isDirectory())
            HLog.logger(HELogLevel.ERROR, "Mods path is a file! MODS_PATH='", MODS_FILE.getPath(), "'.");
        else if (!MODS_FILE.exists() && !MODS_FILE.mkdirs())
            HLog.logger(HELogLevel.ERROR, "Creating MODS_PATH directory failed. MODS_PATH='", MODS_FILE.getPath(), "'.");
    }

    public static final List<Class<?>> modList = new ArrayList<>();
    public static final List<Pair<Class<?>, Class<?>>> elementList = new ArrayList<>();
    public static final EventBus DEFAULT_EVENT_BUS = EventBus.getDefault();

    private static final List<Class<?>> mods = new ArrayList<>();
    private static final List<Class<?>> elementImplements = new ArrayList<>();
    private static final List<Class<?>> elementUtils = new ArrayList<>();

    private static final List<List<Class<?>>> sameMods = new ArrayList<>();

    public static void loadMods() {
        if (!MODS_FILE.exists() || !MODS_FILE.isDirectory())
            return;
        searchingMods();
        DEFAULT_EVENT_BUS.post(new ElementsCheckingEvent());
        checkingMods();
        DEFAULT_EVENT_BUS.post(new ElementsCheckedEvent());
        //TODO
        HLog.logger(sameMods);
    }

    private static void searchingMods() {
        logger.log(HELogLevel.INFO, "Searching mods in '", MODS_FILE.getPath(), "'.");
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.addJarFilesInDirectory(MODS_FILE);
        modsFinder.startFind();
        for (Class<?> aClass: modsFinder.getClassList())
            for (Annotation annotation: aClass.getAnnotations()) {
                if (annotation.annotationType().equals(Mod.class))
                    mods.add(aClass);
                if (annotation.annotationType().equals(NewElementImplement.class))
                    elementImplements.add(aClass);
                if (annotation.annotationType().equals(NewElementUtil.class))
                    elementUtils.add(aClass);
            }
        logger.log(HELogLevel.DEBUG, "Found @Mod in classes: ", mods);
        logger.log(HELogLevel.DEBUG, "Found @NewElementImplement in classes: ", elementImplements);
        logger.log(HELogLevel.DEBUG, "Found @NewElementUtil in classes: ", elementUtils);
    }

    private static void checkingMods() {
        for (Class<?> classClass: mods) {
            Mod classMod = classClass.getAnnotation(Mod.class);
            String className = classMod.name();
            if (className == null) {
                logger.log(HELogLevel.WARN, "Null mod name!");
                className = "null";
            }
            boolean not_found = true;
            for (Class<?> savedClass: modList) {
                Mod savedMod = savedClass.getAnnotation(Mod.class);
                if (className.equals(savedMod.name())) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same mod name '", savedMod.name(), "'. " +
                            "With versions are: '", savedMod.version(), "' and '", classMod.version(), "'.");
                    boolean found = false;
                    for (List<Class<?>> sameModFound: sameMods) {
                        if (sameModFound.isEmpty())
                            continue;
                        Mod mod = sameModFound.get(0).getAnnotation(Mod.class);
                        if (className.equals(mod.name())) {
                            found = true;
                            sameModFound.add(classClass);
                            break;
                        }
                    }
                    if (!found) {
                        List<Class<?>> temp = new ArrayList<>();
                        temp.add(classClass);
                        temp.add(savedClass);
                        sameMods.add(temp);
                    }
                    break;
                }
            }
            if (not_found)
                modList.add(classClass);
        }
    }
}
