package Core.Mod;

import Core.Craftworld;
import Core.EventSubscribe;
import Core.Events.ElementsCheckedEvent;
import Core.Events.ElementsCheckingEvent;
import Core.Mod.New.*;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.*;

public class ModClassesLoader {
    private static HLog logger;
    public static final File MODS_FILE = (new File(HStringHelper.merge(Craftworld.RUNTIME_PATH, "mods"))).getAbsoluteFile();
    static {
        if (MODS_FILE.exists() && !MODS_FILE.isDirectory())
            HLog.logger(HELogLevel.ERROR, "Mods path is a file! MODS_PATH='", MODS_FILE.getPath(), "'.");
        else if (!MODS_FILE.exists() && !MODS_FILE.mkdirs())
            HLog.logger(HELogLevel.ERROR, "Creating MODS_PATH directory failed. MODS_PATH='", MODS_FILE.getPath(), "'.");
    }

    private static final EventBus DEFAULT_EVENT_BUS = EventBus.getDefault();
    private static final List<EventBus> ALL_EVENT_BUS = new ArrayList<>();
    static {
        ALL_EVENT_BUS.add(DEFAULT_EVENT_BUS);
    }

    private static Set<Class<?>> allClasses;
    private static Map<Class<?>, File> allClassesWithJarFiles;

    private static final List<Class<? extends ModImplement>> modList = new ArrayList<>();
    private static final List<Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> elementList = new ArrayList<>();

    private static final List<Class<? extends ElementImplement>> implementList = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> utilList = new ArrayList<>();

    private static final List<Class<? extends ModImplement>> mods = new ArrayList<>();
    private static final List<Class<? extends ElementImplement>> elementImplements = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> elementUtils = new ArrayList<>();

    private static final List<List<Class<? extends ModImplement>>> sameMods = new ArrayList<>();
    private static final List<List<Class<? extends ElementImplement>>> sameImplements = new ArrayList<>();
    private static final List<List<Class<? extends ElementUtil<?>>>> sameUtils = new ArrayList<>();

    private static final List<Class<? extends ElementImplement>> singleImplements = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> singleUtils = new ArrayList<>();

    public static Set<Class<?>> getAllClasses() {
        return allClasses;
    }

    public static Map<Class<?>, File> getAllClassesWithJarFiles() {
        return allClassesWithJarFiles;
    }

    public static List<Class<? extends ModImplement>> getModList() {
        return modList;
    }

    public static List<Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementList() {
        return elementList;
    }

    public static List<List<Class<? extends ModImplement>>> getSameMods() {
        return sameMods;
    }

    public static List<List<Class<? extends ElementImplement>>> getSameImplements() {
        return sameImplements;
    }

    public static List<List<Class<? extends ElementUtil<?>>>> getSameUtils() {
        return sameUtils;
    }

    public static List<Class<? extends ElementImplement>> getSingleImplements() {
        return singleImplements;
    }

    public static List<Class<? extends ElementUtil<?>>> getSingleUtils() {
        return singleUtils;
    }

    public static EventBus getDefaultEventBus() {
        return DEFAULT_EVENT_BUS;
    }

    public static EventBus getEventBusByName(String name) {
        //TODO: Add more event_bus
        return DEFAULT_EVENT_BUS;
    }

    public static List<EventBus> getAllEventBus() {
        return ALL_EVENT_BUS;
    }

    public static boolean loadClasses() {
        if (!MODS_FILE.exists() || !MODS_FILE.isDirectory())
            return true;
        logger = new HLog("ModClassesLoader", Thread.currentThread().getName());
        logger.log(HELogLevel.INFO, "Searching mods in '", MODS_FILE.getPath(), "'.");
        searchingMods();
        HClassFinder eventFilter = new HClassFinder();
        eventFilter.addAnnotationClass(EventSubscribe.class);
        for (Class<?> aClass: allClasses)
            if (eventFilter.checkAnnotation(aClass)) {
                EventSubscribe subscribe = aClass.getAnnotation(EventSubscribe.class);
                if (subscribe == null)
                    continue;
                if (subscribe.eventBus().equals("*")) {
                    Object instance = HClassHelper.getInstance(aClass);
                    if (instance == null)
                        logger.log(HELogLevel.ERROR, "Get instance failed. Can't register class '", aClass, "' to event bus '", subscribe.eventBus(), "'.");
                    else for (EventBus eventBus: getAllEventBus())
                        eventBus.register(instance);
                    continue;
                }
                EventBus eventBus = getEventBusByName(subscribe.eventBus());
                Object instance = HClassHelper.getInstance(aClass);
                if (instance == null)
                    logger.log(HELogLevel.ERROR, "Get instance failed. Can't register class '", aClass, "' to event bus '", subscribe.eventBus(), "'.");
                else
                    eventBus.register(instance);
            }
        DEFAULT_EVENT_BUS.post(new ElementsCheckingEvent());
        checkingMods();
        if (!sameMods.isEmpty())
            return true;
        checkingImplements();
        if (!sameImplements.isEmpty())
            return true;
        checkingUtils();
        if (!sameUtils.isEmpty())
            return true;
        checkingElements();
        if (!singleImplements.isEmpty())
            return true;
        if (!singleUtils.isEmpty())
            return true;
        DEFAULT_EVENT_BUS.post(new ElementsCheckedEvent());
        logger.log(HELogLevel.DEBUG, "Checked mods: ", modList);
        logger.log(HELogLevel.DEBUG, "Checked elements: ", elementList);
        return false;
    }

    @SuppressWarnings("unchecked")
    private static void searchingMods() {
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.addJarFilesInDirectory(MODS_FILE);
        modsFinder.startFind();
        allClasses = modsFinder.getClassList();
        allClassesWithJarFiles = modsFinder.getClassListWithJarFile();
        HClassFinder modFilter = new HClassFinder();
        modFilter.addAnnotationClass(Mod.class);
        modFilter.addSuperClass(ModImplement.class);
        HClassFinder implementFilter = new HClassFinder();
        implementFilter.addAnnotationClass(NewElementImplement.class);
        implementFilter.addSuperClass(ElementImplement.class);
        HClassFinder utilFilter = new HClassFinder();
        utilFilter.addAnnotationClass(NewElementUtil.class);
        utilFilter.addSuperClass(ElementUtil.class);
        for (Class<?> aClass: modsFinder.getClassList()) {
            if (modFilter.checkAnnotation(aClass) && modFilter.checkSuper(aClass))
                mods.add((Class<? extends ModImplement>) aClass);
            if (implementFilter.checkAnnotation(aClass) && implementFilter.checkSuper(aClass))
                elementImplements.add((Class<? extends ElementImplement>) aClass);
            if (utilFilter.checkAnnotation(aClass) && utilFilter.checkSuper(aClass))
                elementUtils.add((Class<? extends ElementUtil<?>>) aClass);
        }
    }

    private static void checkingMods() {
        for (Class<? extends ModImplement> classClass: mods) {
            Mod classMod = classClass.getAnnotation(Mod.class);
            String className = HStringHelper.noNull(classMod.name());
            boolean not_found = true;
            for (Class<? extends ModImplement> savedClass: modList) {
                Mod savedMod = savedClass.getAnnotation(Mod.class);
                if (className.equals(HStringHelper.noNull(savedMod.name()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same mod name '", savedMod.name(), "'. " +
                            "With versions are: '", savedMod.version(), "' and '", classMod.version(), "'.");
                    boolean found = false;
                    for (List<Class<? extends ModImplement>> sameModFound: sameMods) {
                        if (sameModFound.isEmpty())
                            continue;
                        Mod mod = sameModFound.get(0).getAnnotation(Mod.class);
                        if (className.equals(HStringHelper.noNull(mod.name()))) {
                            found = true;
                            sameModFound.add(classClass);
                            break;
                        }
                    }
                    if (!found) {
                        List<Class<? extends ModImplement>> temp = new ArrayList<>();
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

    private static void checkingImplements() {
        for (Class<? extends ElementImplement> classClass: elementImplements) {
            NewElementImplement classImplement = classClass.getAnnotation(NewElementImplement.class);
            String className = HStringHelper.noNull(classImplement.name());
            boolean not_found = true;
            for (Class<? extends ElementImplement> savedClass: implementList) {
                NewElementImplement savedImplement = savedClass.getAnnotation(NewElementImplement.class);
                if (className.equals(HStringHelper.noNull(savedImplement.name()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same element implement name '", savedImplement.name(), "'.");
                    boolean found = false;
                    for (List<Class<? extends ElementImplement>> sameImplementFound: sameImplements) {
                        if (sameImplementFound.isEmpty())
                            continue;
                        NewElementImplement implement = sameImplementFound.get(0).getAnnotation(NewElementImplement.class);
                        if (className.equals(HStringHelper.noNull(implement.name()))) {
                            found = true;
                            sameImplementFound.add(classClass);
                            break;
                        }
                    }
                    if (!found) {
                        List<Class<? extends ElementImplement>> temp = new ArrayList<>();
                        temp.add(classClass);
                        temp.add(savedClass);
                        sameImplements.add(temp);
                    }
                    break;
                }
            }
            if (not_found)
                implementList.add(classClass);
        }
    }

    private static void checkingUtils() {
        for (Class<? extends ElementUtil<?>> classClass: elementUtils) {
            NewElementUtil classMod = classClass.getAnnotation(NewElementUtil.class);
            String className = HStringHelper.noNull(classMod.name());
            boolean not_found = true;
            for (Class<? extends ElementUtil<?>> savedClass: utilList) {
                NewElementUtil savedUtil = savedClass.getAnnotation(NewElementUtil.class);
                if (className.equals(HStringHelper.noNull(savedUtil.name()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same element util name '", savedUtil.name(), "'.");
                    boolean found = false;
                    for (List<Class<? extends ElementUtil<?>>> sameUtilFound: sameUtils) {
                        if (sameUtilFound.isEmpty())
                            continue;
                        NewElementUtil util = sameUtilFound.get(0).getAnnotation(NewElementUtil.class);
                        if (className.equals(HStringHelper.noNull(util.name()))) {
                            found = true;
                            sameUtilFound.add(classClass);
                            break;
                        }
                    }
                    if (!found) {
                        List<Class<? extends ElementUtil<?>>> temp = new ArrayList<>();
                        temp.add(classClass);
                        temp.add(savedClass);
                        sameUtils.add(temp);
                    }
                    break;
                }
            }
            if (not_found)
                utilList.add(classClass);
        }
    }

    private static void checkingElements() {
        for (Class<? extends ElementImplement> implement: elementImplements) {
            NewElementImplement elementImplement = implement.getAnnotation(NewElementImplement.class);
            String tempName = HStringHelper.noNull(elementImplement.name());
            Class<? extends ElementUtil<?>> elementUtil = null;
            for (Class<? extends ElementUtil<?>> util: elementUtils) {
                NewElementUtil tempUtil = util.getAnnotation(NewElementUtil.class);
                if (tempUtil == null)
                    continue;
                if (tempName.equals(HStringHelper.noNull(tempUtil.name()))) {
                    elementUtil = util;
                    break;
                }
            }
            if (elementUtil == null) {
                logger.log(HELogLevel.ERROR, "No pair util for implement '", tempName, "'. Ignore it!");
                singleImplements.add(implement);
                continue;
            }
            Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>> pair = new Pair<>(implement, elementUtil);
            elementList.add(pair);
        }
        for (Class<? extends ElementUtil<?>> util: elementUtils) {
            NewElementUtil elementUtil = util.getAnnotation(NewElementUtil.class);
            String tempName = HStringHelper.noNull(elementUtil.name());
            Class<? extends ElementImplement> elementImplement = null;
            for (Class<? extends ElementImplement> implement: elementImplements) {
                NewElementImplement tempImplement = implement.getAnnotation(NewElementImplement.class);
                if (tempImplement == null)
                    continue;
                if (tempName.equals(HStringHelper.noNull(tempImplement.name()))) {
                    elementImplement = implement;
                    break;
                }
            }
            if (elementImplement == null) {
                logger.log(HELogLevel.ERROR, "No pair implement for util '", tempName, "'. Ignore it!");
                singleUtils.add(util);
            }
        }
    }

    private static final Map<String, List<Class<?>>> allElements = new HashMap<>();

    public static Map<String, List<Class<?>>> getAllElements() {
        return allElements;
    }

    public static void registerElements() {
        for (Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>> pair: elementList) {
            String name = HStringHelper.noNull(pair.getKey().getAnnotation(NewElementImplement.class).name());
            List<Class<?>> instances = new ArrayList<>();
            HClassFinder classFilter = new HClassFinder();
            classFilter.addSuperClass(pair.getKey());
            for (Class<?> aClass: allClasses)
                if (classFilter.checkSuper(aClass) && !aClass.equals(pair.getKey()))
                    instances.add(aClass);
            if (instances.isEmpty())
                logger.log(HELogLevel.WARN, "No instance registered for element '", name, "'.");
            allElements.put(name, instances);
        }
    }
}
