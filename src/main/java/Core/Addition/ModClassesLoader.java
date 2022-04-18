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
import Core.Events.ElementsCheckedEvent;
import Core.Events.ElementsCheckingEvent;
import Core.Exceptions.ElementImplementNameClashException;
import Core.Exceptions.ElementNotPairException;
import Core.Exceptions.ElementUtilNameClashException;
import Core.Exceptions.ModNameClashException;
import Core.FileTreeStorage;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Load mod classes in {@link FileTreeStorage#MOD_PATH}.
 * Initialize all classes, register eventbuses and collect mod classes and element classes.
 * @author xuxiaocheng
 */
public class ModClassesLoader {
    public static final File MODS_FILE = (new File(FileTreeStorage.MOD_PATH)).getAbsoluteFile();

    private static Set<Class<?>> allClasses;
    private static Map<Class<?>, File> allClassesWithJarFiles;

    public static Set<Class<?>> getAllClasses() {
        return allClasses;
    }

    public static Map<Class<?>, File> getAllClassesWithJarFiles() {
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
    private static final Map<ElementName, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> elementPairList = new HashMap<>();

    private static final List<List<Class<? extends ModImplement>>> sameMods = new ArrayList<>();
    private static final List<List<Class<? extends ElementImplement>>> sameImplements = new ArrayList<>();
    private static final List<List<Class<? extends ElementUtil<?>>>> sameUtils = new ArrayList<>();

    private static final List<Class<? extends ElementImplement>> singleImplements = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> singleUtils = new ArrayList<>();

    private static final List<IllegalArgumentException> exceptions = new ArrayList<>();

    public static List<Class<? extends ModImplement>> getModList() {
        return modList;
    }

    public static Map<ElementName, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return elementPairList;
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

    public static List<IllegalArgumentException> getExceptions() {
        return exceptions;
    }

    @SuppressWarnings("unchecked")
    private static void pickAllClasses() throws IOException {
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.addJarFilesInDirectory(MODS_FILE);
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
            if (modFilter.checkAnnotation(aClass) && modFilter.checkSuper(aClass))
                mods.add((Class<? extends ModImplement>) aClass);
            if (implementFilter.checkAnnotation(aClass) && implementFilter.checkSuper(aClass))
                elementImplements.add((Class<? extends ElementImplement>) aClass);
            if (utilFilter.checkAnnotation(aClass) && utilFilter.checkSuper(aClass))
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
            elementPairList.put(implementName, new Pair<>(implement, elementUtil));
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

    public static List<IllegalArgumentException> loadModClasses() throws IOException {
        (new HLog("ModClassesLoader", Thread.currentThread().getName()))
                .log(HLogLevel.INFO, "Searching mods in '", MODS_FILE.getPath(), "'.");
        pickAllClasses();
        for (Class<?> aClass: allClasses)
            try {
                EventBusManager.register(aClass);
            } catch (NoSuchMethodException exception) {
                HLog.logger(HLogLevel.ERROR, exception);
            }
        EventBusManager.getDefaultEventBus().post(new ElementsCheckingEvent());
        checkSameMods();
        checkSameElementImplements();
        checkSameElementUtils();
        if (!exceptions.isEmpty())
            return exceptions;
        checkElementsPair();
        if (!exceptions.isEmpty())
            return exceptions;
        EventBusManager.getDefaultEventBus().post(new ElementsCheckedEvent());
        mods.clear();
        elementImplements.clear();
        elementUtils.clear();
        implementList.clear();
        utilList.clear();
        return null;
    }
}
