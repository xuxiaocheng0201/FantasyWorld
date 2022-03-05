package Core.Mod;

import Core.CraftWorld;
import Core.Event.ElementsCheckedEvent;
import Core.Event.ElementsCheckingEvent;
import Core.Event.EventSubscribe;
import Core.Mod.NewElement.ElementImplement;
import Core.Mod.NewElement.ElementUtil;
import Core.Mod.NewElement.NewElementImplement;
import Core.Mod.NewElement.NewElementUtil;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
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

    public static List<Class<?>> allClasses;
    public static final EventBus DEFAULT_EVENT_BUS = EventBus.getDefault();

    public static final List<Class<?>> modList = new ArrayList<>();
    public static final List<Pair<Class<?>, Class<?>>> elementList = new ArrayList<>();

    private static final List<Class<?>> implementList = new ArrayList<>();
    private static final List<Class<?>> utilList = new ArrayList<>();

    private static final List<Class<?>> mods = new ArrayList<>();
    private static final List<Class<?>> elementImplements = new ArrayList<>();
    private static final List<Class<?>> elementUtils = new ArrayList<>();

    private static final List<List<Class<?>>> sameMods = new ArrayList<>();
    private static final List<List<Class<?>>> sameImplements = new ArrayList<>();
    private static final List<List<Class<?>>> sameUtils = new ArrayList<>();

    private static final List<Class<?>> singleImplements = new ArrayList<>();
    private static final List<Class<?>> singleUtils = new ArrayList<>();

    public static boolean loadClasses() {
        if (!MODS_FILE.exists() || !MODS_FILE.isDirectory())
            return true;
        logger.log(HELogLevel.INFO, "Searching mods in '", MODS_FILE.getPath(), "'.");
        searchingMods();
        logger.log(HELogLevel.DEBUG, "Found @Mod in classes: ", mods);
        logger.log(HELogLevel.DEBUG, "Found @NewElementImplement in classes: ", elementImplements);
        logger.log(HELogLevel.DEBUG, "Found @NewElementUtil in classes: ", elementUtils);
        HClassFinder eventFilter = new HClassFinder();
        eventFilter.addAnnotationClass(EventSubscribe.class);
        for (Class<?> aClass: allClasses)
            if (eventFilter.checkAnnotation(aClass)) {
                EventSubscribe subscribe = aClass.getAnnotation(EventSubscribe.class);
                if (subscribe == null)
                    continue;
                EventBus eventBus = getEventBusByName(subscribe.eventBus());
                if (registerEvent(aClass, eventBus))
                    logger.log(HELogLevel.ERROR, "Can't register class '", aClass, "' to event bus '", subscribe.eventBus(), "'.");
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
        logger.log(HELogLevel.DEBUG, "Checked mods: ", modList);
        logger.log(HELogLevel.DEBUG, "Checked elements: ", elementList);
        DEFAULT_EVENT_BUS.post(new ElementsCheckedEvent());
        return false;
    }

    private static void searchingMods() {
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.addJarFilesInDirectory(MODS_FILE);
        modsFinder.startFind();
        allClasses = modsFinder.getClassList();
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
                mods.add(aClass);
            if (implementFilter.checkAnnotation(aClass) && implementFilter.checkSuper(aClass))
                elementImplements.add(aClass);
            if (utilFilter.checkAnnotation(aClass) && utilFilter.checkSuper(aClass))
                elementUtils.add(aClass);
        }
    }

    private static void checkingMods() {
        for (Class<?> classClass: mods) {
            Mod classMod = classClass.getAnnotation(Mod.class);
            String className = HStringHelper.noNull(classMod.name());
            boolean not_found = true;
            for (Class<?> savedClass: modList) {
                Mod savedMod = savedClass.getAnnotation(Mod.class);
                if (className.equals(HStringHelper.noNull(savedMod.name()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same mod name '", savedMod.name(), "'. " +
                            "With versions are: '", savedMod.version(), "' and '", classMod.version(), "'.");
                    boolean found = false;
                    for (List<Class<?>> sameModFound: sameMods) {
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

    private static void checkingImplements() {
        for (Class<?> classClass: elementImplements) {
            NewElementImplement classImplement = classClass.getAnnotation(NewElementImplement.class);
            String className = HStringHelper.noNull(classImplement.name());
            boolean not_found = true;
            for (Class<?> savedClass: implementList) {
                NewElementImplement savedImplement = savedClass.getAnnotation(NewElementImplement.class);
                if (className.equals(HStringHelper.noNull(savedImplement.name()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same element implement name '", savedImplement.name(), "'.");
                    boolean found = false;
                    for (List<Class<?>> sameImplementFound: sameImplements) {
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
                        List<Class<?>> temp = new ArrayList<>();
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
        for (Class<?> classClass: elementUtils) {
            NewElementUtil classMod = classClass.getAnnotation(NewElementUtil.class);
            String className = HStringHelper.noNull(classMod.name());
            boolean not_found = true;
            for (Class<?> savedClass: utilList) {
                NewElementUtil savedUtil = savedClass.getAnnotation(NewElementUtil.class);
                if (className.equals(HStringHelper.noNull(savedUtil.name()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same element util name '", savedUtil.name(), "'.");
                    boolean found = false;
                    for (List<Class<?>> sameUtilFound: sameUtils) {
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
                        List<Class<?>> temp = new ArrayList<>();
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
        for (Class<?> implement: elementImplements) {
            NewElementImplement elementImplement = implement.getAnnotation(NewElementImplement.class);
            String tempName = HStringHelper.noNull(elementImplement.name());
            Class<?> elementUtil = null;
            for (Class<?> util: elementUtils) {
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
            Pair<Class<?>, Class<?>> pair = new Pair<>(implement, elementUtil);
            elementList.add(pair);
        }
        for (Class<?> util: elementUtils) {
            NewElementUtil elementUtil = util.getAnnotation(NewElementUtil.class);
            String tempName = HStringHelper.noNull(elementUtil.name());
            Class<?> elementImplement = null;
            for (Class<?> implement: elementImplements) {
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

    public static EventBus getEventBusByName(String name) {
        //TODO
        return DEFAULT_EVENT_BUS;
    }

    public static boolean registerEvent(Class<?> aClass, EventBus eventBus) {
        boolean need_registered;
        try {
            need_registered = false;
            eventBus.register(aClass.getDeclaredConstructor().newInstance());
        } catch (NoSuchMethodException exception) {
            need_registered = true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return true;
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(String.class).newInstance(""));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(String.class, String.class).newInstance("", ""));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(String.class, String.class, String.class).newInstance("", "", ""));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(int.class).newInstance(0));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(int.class, int.class).newInstance(0, 0));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(int.class, int.class, int.class).newInstance(0, 0, 0));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(Integer.class).newInstance(0));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(Integer.class, Integer.class).newInstance(0, 0));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        if (need_registered) {
            try {
                need_registered = false;
                eventBus.register(aClass.getDeclaredConstructor(Integer.class, Integer.class, Integer.class).newInstance(0, 0, 0));
            } catch (NoSuchMethodException exception) {
                need_registered = true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
        //todo: Add more common constructors
        return false;
    }
}
