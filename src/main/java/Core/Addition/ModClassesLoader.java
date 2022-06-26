package Core.Addition;

import Core.Addition.Element.BasicInformation.ElementName;
import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementImplementCore;
import Core.Addition.Element.NewElementUtilCore;
import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.*;
import Core.Exceptions.*;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.DataStructures.Pair;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Load mod classes in {@link ModClassesLoader#MODS_FILES}.
 * Initialize all classes, register eventbuses and collect mod classes and element classes.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModClassesLoader {
    private static final List<File> MODS_FILES = new ArrayList<>();

    public static List<File> getModsFiles() {
        return MODS_FILES;
    }

    private static Set<Class<?>> allClasses;
    private static Map<Class<?>, File> allClassesWithJarFiles;

    public static @NotNull Set<Class<?>> getAllClasses() {
        return allClasses;
    }

    public static @NotNull Map<Class<?>, File> getAllClassesWithJarFiles() {
        return allClassesWithJarFiles;
    }

    // searched
    private static final Collection<Class<? extends ModImplement>> mods = new ArrayList<>();
    private static final Collection<Class<? extends ElementImplement>> elementImplements = new ArrayList<>();
    private static final Collection<Class<? extends ElementUtil<?>>> elementUtils = new ArrayList<>();

    // checked
    private static final List<Class<? extends ModImplement>> modList = new ArrayList<>();
    private static final Collection<Class<? extends ElementImplement>> implementList = new ArrayList<>();
    private static final Collection<Class<? extends ElementUtil<?>>> utilList = new ArrayList<>();
    private static final HMapRegisterer<ElementName, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> elementPairList = new HMapRegisterer<>();

    private static final List<List<Class<? extends ModImplement>>> sameMods = new ArrayList<>();
    private static final List<List<Class<? extends ElementImplement>>> sameImplements = new ArrayList<>();
    private static final List<List<Class<? extends ElementUtil<?>>>> sameUtils = new ArrayList<>();

    private static final List<Class<? extends ElementImplement>> singleImplements = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> singleUtils = new ArrayList<>();

    private static final List<IllegalArgumentException> exceptions = new ArrayList<>();

    public static @NotNull List<Class<? extends ModImplement>> getModList() {
        return modList;
    }

    public static @NotNull HMapRegisterer<ElementName, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return elementPairList;
    }

    public static @NotNull List<List<Class<? extends ModImplement>>> getSameMods() {
        return sameMods;
    }

    public static @NotNull List<List<Class<? extends ElementImplement>>> getSameImplements() {
        return sameImplements;
    }

    public static @NotNull List<List<Class<? extends ElementUtil<?>>>> getSameUtils() {
        return sameUtils;
    }

    public static @NotNull List<Class<? extends ElementImplement>> getSingleImplements() {
        return singleImplements;
    }

    public static @NotNull List<Class<? extends ElementUtil<?>>> getSingleUtils() {
        return singleUtils;
    }

    @SuppressWarnings("unchecked")
    private static void pickAllClasses() throws IOException {
        HClassFinder modsFinder = new HClassFinder();
        for (File file: MODS_FILES)
            modsFinder.addJarFilesInDirectory(file);
        modsFinder.startFind();
        allClasses = modsFinder.getClassList();
        allClassesWithJarFiles = modsFinder.getClassListWithJarFile();
        HClassFinder modFilter = new HClassFinder();
        modFilter.addAnnotationClass(NewMod.class);
        modFilter.addSuperClass(ModImplement.class);
        HClassFinder implementFilter = new HClassFinder();
        implementFilter.addAnnotationClass(NewElementImplementCore.class);
        implementFilter.addSuperClass(ElementImplement.class);
        HClassFinder utilFilter = new HClassFinder();
        utilFilter.addAnnotationClass(NewElementUtilCore.class);
        utilFilter.addSuperClass(ElementUtil.class);
        for (Class<?> aClass: allClasses) {
            if (modFilter.checkAnnotation(aClass) && modFilter.checkSuper(aClass) && HClassHelper.isClass(aClass))
                mods.add((Class<? extends ModImplement>) aClass);
            if (implementFilter.checkAnnotation(aClass) && implementFilter.checkSuper(aClass) && HClassHelper.isInterface(aClass))
                elementImplements.add((Class<? extends ElementImplement>) aClass);
            if (utilFilter.checkAnnotation(aClass) && utilFilter.checkSuper(aClass) && HClassHelper.isClass(aClass))
                elementUtils.add((Class<? extends ElementUtil<?>>) aClass);
        }
    }

    private static void checkSameMods() {
        for (Class<? extends ModImplement> classClass: mods) {
            ModName className = ModImplement.getModNameFromClass(classClass);
            boolean not_found = true;
            for (Class<? extends ModImplement> savedClass: modList) {
                ModName savedName = ModImplement.getModNameFromClass(savedClass);
                if (className.equals(savedName)) {
                    not_found = false;
                    exceptions.add(new ModNameClashException(classClass, savedClass));
                    boolean notFound = true;
                    for (List<Class<? extends ModImplement>> sameModFound: sameMods) {
                        ModName sameName = ModImplement.getModNameFromClass(sameModFound.get(0));
                        if (className.equals(sameName)) {
                            notFound = false;
                            sameModFound.add(classClass);
                            break;
                        }
                    }
                    if (notFound) {
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

    private static void checkSameElementImplements() {
        for (Class<? extends ElementImplement> classClass: elementImplements) {
            ElementName className = ElementImplement.getElementNameFromClass(classClass);
            boolean not_found = true;
            for (Class<? extends ElementImplement> savedClass: implementList) {
                ElementName savedName = ElementImplement.getElementNameFromClass(savedClass);
                if (className.equals(savedName)) {
                    not_found = false;
                    exceptions.add(new ElementImplementNameClashException(classClass, savedClass));
                    boolean notFound = true;
                    for (List<Class<? extends ElementImplement>> sameImplementFound: sameImplements) {
                        ElementName sameName = ElementImplement.getElementNameFromClass(sameImplementFound.get(0));
                        if (className.equals(sameName)) {
                            notFound = false;
                            sameImplementFound.add(classClass);
                            break;
                        }
                    }
                    if (notFound) {
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

    private static void checkSameElementUtils() {
        for (Class<? extends ElementUtil<?>> classClass: elementUtils) {
            ElementName className = ElementUtil.getElementNameFromClass(classClass);
            boolean not_found = true;
            for (Class<? extends ElementUtil<?>> savedClass: utilList) {
                ElementName savedName = ElementUtil.getElementNameFromClass(savedClass);
                if (className.equals(savedName)) {
                    not_found = false;
                    exceptions.add(new ElementUtilNameClashException(classClass, savedClass));
                    boolean notFound = true;
                    for (List<Class<? extends ElementUtil<?>>> sameUtilFound: sameUtils) {
                        ElementName sameName = ElementUtil.getElementNameFromClass(sameUtilFound.get(0));
                        if (className.equals(sameName)) {
                            notFound = false;
                            sameUtilFound.add(classClass);
                            break;
                        }
                    }
                    if (notFound) {
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

    private static void checkElementsPair() {
        for (Class<? extends ElementImplement> implement: elementImplements) {
            ElementName implementName = ElementImplement.getElementNameFromClass(implement);
            Class<? extends ElementUtil<?>> elementUtil = null;
            for (Class<? extends ElementUtil<?>> util: elementUtils) {
                ElementName utilName = ElementUtil.getElementNameFromClass(util);
                if (implementName.equals(utilName)) {
                    elementUtil = util;
                    break;
                }
            }
            if (elementUtil == null) {
                exceptions.add(new ElementNotPairException(implement, (Object) null));
                singleImplements.add(implement);
                continue;
            }
            try {
                elementPairList.register(implementName, new Pair<>(implement, elementUtil));
            } catch (HElementRegisteredException ignore) {
            }
        }
        for (Class<? extends ElementUtil<?>> util: elementUtils) {
            ElementName utilName = ElementUtil.getElementNameFromClass(util);
            Class<? extends ElementImplement> elementImplement = null;
            for (Class<? extends ElementImplement> implement: elementImplements) {
                ElementName implementName = ElementImplement.getElementNameFromClass(implement);
                if (utilName.equals(implementName)) {
                    elementImplement = implement;
                    break;
                }
            }
            if (elementImplement == null) {
                exceptions.add(new ElementNotPairException((Object) null, util));
                singleUtils.add(util);
            }
        }
    }

    private static @Nullable List<IllegalArgumentException> loadModClasses(boolean firstTime) throws IOException {
        if (MODS_FILES.isEmpty())
            return null;
        StringBuilder builder = new StringBuilder(MODS_FILES.get(0).getPath());
        for (int i = 1; i < MODS_FILES.size(); ++i)
            builder.append("; ").append(MODS_FILES.get(i));
        (new HLog("ModClassesLoader", Thread.currentThread().getName()))
                .log(HLogLevel.INFO, "Searching mods in '", builder.toString(), "'.");
        pickAllClasses();
        List<IllegalArgumentException> errors = new ArrayList<>();
        Collection<Method> needInvokeMethods = new ArrayList<>();
        for (Class<?> aClass : allClasses)
            if (aClass.getAnnotation(InvokeBeforeEventsRegister.class) != null && HClassHelper.isClass(aClass))
                for (Method method : aClass.getDeclaredMethods())
                    if (method.getAnnotation(InvokeBeforeEventsRegister.class) != null)
                        needInvokeMethods.add(method);
        for (Method method: needInvokeMethods)
            try {
                method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                errors.add(new IllegalArgumentException(exception));
            }
        for (Class<?> aClass: allClasses) {
            EventSubscribe annotation = aClass.getAnnotation(EventSubscribe.class);
            if (annotation != null && annotation.autoRegister())
                try {
                    EventBusManager.register(aClass);
                } catch (NoSuchMethodException exception) {
                    errors.add(new IllegalArgumentException(exception));
                }
        }
        EventBusManager.getDefaultEventBus().post(new ElementsCheckingEvent(firstTime));
        checkSameMods();
        checkSameElementImplements();
        checkSameElementUtils();
        if (!exceptions.isEmpty())
            return exceptions;
        checkElementsPair();
        if (!exceptions.isEmpty())
            return exceptions;
        EventBusManager.getDefaultEventBus().post(new ElementsCheckedEvent(firstTime));
        mods.clear();
        elementImplements.clear();
        elementUtils.clear();
        implementList.clear();
        utilList.clear();
        if (!errors.isEmpty())
            return errors;
        return null;
    }

    private static final Collection<Class<? extends ModImplement>> initializedModsFlag = new HashSet<>();

    private static boolean loadMods(boolean firstTime) {
        HLog logger = new HLog("ModLauncher", Thread.currentThread().getName());
        List<IllegalArgumentException> loaderExceptions = List.of();
        try {
            loaderExceptions = loadModClasses(firstTime);
        } catch (IOException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
        if (loaderExceptions != null) {
            logger.log(HLogLevel.BUG, "Mod Loading Error in loading classes!");
            for (IllegalArgumentException exception: loaderExceptions)
                logger.log(HLogLevel.FAULT, exception);
            return false;
        }
        List<ModInformationException> sorterExceptions = ModClassesSorter.sortMods();
        if (sorterExceptions != null) {
            logger.log(HLogLevel.BUG, "Mod Loading Error in sorting classes!");
            for (ModInformationException exception: sorterExceptions)
                logger.log(HLogLevel.ERROR, exception);
            return false;
        }
        logger.log(HLogLevel.FINEST, "Sorted Mod list: ", ModManager.getModList());
        return true;
    }

    private static void initializeMods(boolean firstTime) {
        HLog logger = new HLog("ModLauncher", Thread.currentThread().getName());
        EventBusManager.getDefaultEventBus().post(new PreInitializationModsEvent(firstTime));
        Collection<Class<? extends ModImplement>> sortedMods = new ArrayList<>(ModClassesSorter.getSortedMods());
        for (Class<? extends ModImplement> aClass: sortedMods) {
            if (initializedModsFlag.contains(aClass))
                continue;
            initializedModsFlag.add(aClass);
            EventBusManager.getDefaultEventBus().post(new ModInitializingEvent(aClass));
            ModImplement instance = HClassHelper.getInstance(aClass);
            if (instance == null) {
                logger.log(HLogLevel.ERROR, "No Common Constructor for creating Mod instance." + ModManager.crashClassInformation(aClass));
                continue;
            }
            try {
                instance.mainInitialize();
                EventBusManager.getDefaultEventBus().post(new ModInitializedEvent(aClass, true));
            } catch (Exception exception) {
                logger.log(HLogLevel.BUG, "Mod main initialization failed.", ModManager.crashClassInformation(aClass), exception);
                EventBusManager.getDefaultEventBus().post(new ModInitializedEvent(aClass, false));
            }
            if (!sortedMods.equals(ModClassesSorter.getSortedMods()))
                break;
        }
        EventBusManager.getDefaultEventBus().post(new PostInitializationModsEvent(firstTime));
    }

    private static boolean firstTime = true;
    
    static boolean addMod(@Nullable File modsFile) {
        if (modsFile == null || !modsFile.exists() || MODS_FILES.contains(modsFile))
            return false;
        boolean first = firstTime;
        firstTime = false;
        ModClassesSorter.getSortedMods().clear();
        MODS_FILES.add(modsFile);
        if (loadMods(first)) {
            initializeMods(first);
            return ModClassesSorter.getExceptions().isEmpty();
        }
        return false;
    }
}
