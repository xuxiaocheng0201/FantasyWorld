package Core.Addition;

import Core.Addition.Element.BasicInformation.ElementName;
import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementImplementCore;
import Core.Addition.Element.NewElementUtilCore;
import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.Exceptions.ElementImplementNameClashException;
import Core.Exceptions.ElementUtilNameClashException;
import Core.Exceptions.ModNameClashException;
import Core.FileTreeStorage;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

class ModClassesLoader {
    @SuppressWarnings("FieldHasSetterButNoGetter")
    private static HLog logger;
    static final File MODS_FILE = (new File(FileTreeStorage.MOD_PATH)).getAbsoluteFile();

    static void setLogger(HLog logger) {
        ModClassesLoader.logger = logger;
    }

    private static Set<Class<?>> allClasses;
    private static Map<Class<?>, File> allClassesWithJarFiles;

    static Set<Class<?>> getAllClasses() {
        return allClasses;
    }

    static Map<Class<?>, File> getAllClassesWithJarFiles() {
        return allClassesWithJarFiles;
    }

    // searched
    private static final Collection<Class<? extends ModImplement>> mods = new ArrayList<>();
    private static final Collection<Class<? extends ElementImplement>> elementImplements = new ArrayList<>();
    private static final Collection<Class<? extends ElementUtil<?>>> elementUtils = new ArrayList<>();

    // checked
    private static final List<Class<? extends ModImplement>> modList = new ArrayList<>();
    private static final List<Class<? extends ElementImplement>> implementList = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> utilList = new ArrayList<>();
    private static final Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> elementPairList = new HashMap<>();

    private static final List<List<Class<? extends ModImplement>>> sameMods = new ArrayList<>();
    private static final List<List<Class<? extends ElementImplement>>> sameImplements = new ArrayList<>();
    private static final List<List<Class<? extends ElementUtil<?>>>> sameUtils = new ArrayList<>();

    private static final List<Class<? extends ElementImplement>> singleImplements = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> singleUtils = new ArrayList<>();

    static List<Class<? extends ModImplement>> getModList() {
        return modList;
    }

    static Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return elementPairList;
    }

    static List<List<Class<? extends ModImplement>>> getSameMods() {
        return sameMods;
    }

    static List<List<Class<? extends ElementImplement>>> getSameImplements() {
        return sameImplements;
    }

    static List<List<Class<? extends ElementUtil<?>>>> getSameUtils() {
        return sameUtils;
    }

    static List<Class<? extends ElementImplement>> getSingleImplements() {
        return singleImplements;
    }

    static List<Class<? extends ElementUtil<?>>> getSingleUtils() {
        return singleUtils;
    }

    private static final List<IllegalArgumentException> exceptions = new ArrayList<>();

    static List<IllegalArgumentException> getExceptions() {
        return exceptions;
    }

    @SuppressWarnings("unchecked")
    static void pickAllClasses() {
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.addJarFilesInDirectory(MODS_FILE);
        try {
            modsFinder.startFind();
        } catch (IOException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
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

    static void checkSameMods() {
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

    static void checkSameElementImplements() {
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

    static void checkSameElementUtils() {
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

    static void checkElementsPair() {
        for (Class<? extends ElementImplement> implement: elementImplements) {
            NewElementImplementCore elementImplement = implement.getAnnotation(NewElementImplementCore.class);
            String tempName = HStringHelper.notNullOrEmpty(elementImplement.elementName());
            Class<? extends ElementUtil<?>> elementUtil = null;
            for (Class<? extends ElementUtil<?>> util: elementUtils) {
                NewElementUtilCore tempUtil = util.getAnnotation(NewElementUtilCore.class);
                if (tempUtil == null)
                    continue;
                if (tempName.equals(HStringHelper.notNullOrEmpty(tempUtil.elementName()))) {
                    elementUtil = util;
                    break;
                }
            }
            if (elementUtil == null) {
                logger.log(HLogLevel.ERROR, "No pair util for implement '", tempName, "'. Ignore it!");
                singleImplements.add(implement);
                continue;
            }
            elementPairList.put(tempName, new Pair<>(implement, elementUtil));
        }
        for (Class<? extends ElementUtil<?>> util: elementUtils) {
            NewElementUtilCore elementUtil = util.getAnnotation(NewElementUtilCore.class);
            String tempName = HStringHelper.notNullOrEmpty(elementUtil.elementName());
            Class<? extends ElementImplement> elementImplement = null;
            for (Class<? extends ElementImplement> implement: elementImplements) {
                NewElementImplementCore tempImplement = implement.getAnnotation(NewElementImplementCore.class);
                if (tempImplement == null)
                    continue;
                if (tempName.equals(HStringHelper.notNullOrEmpty(tempImplement.elementName()))) {
                    elementImplement = implement;
                    break;
                }
            }
            if (elementImplement == null) {
                logger.log(HLogLevel.ERROR, "No pair implement for util '", tempName, "'. Ignore it!");
                singleUtils.add(util);
            }
        }
    }

    static void gc() {
        logger = null;
        if (!ModClassesSorter.getSortedMods().isEmpty())
            mods.clear();
        elementImplements.clear();
        elementUtils.clear();
        modList.clear();
        implementList.clear();
        utilList.clear();
        sameMods.clear();
        sameImplements.clear();
        sameUtils.clear();
        singleImplements.clear();
        singleUtils.clear();
    }
}
