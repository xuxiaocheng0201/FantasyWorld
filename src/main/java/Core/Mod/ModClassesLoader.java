package Core.Mod;

import Core.Craftworld;
import Core.Mod.New.*;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;

import java.io.File;
import java.util.*;

class ModClassesLoader {
    @SuppressWarnings("FieldHasSetterButNoGetter")
    private static HLog logger;
    static final File MODS_FILE = (new File(HStringHelper.concat(Craftworld.RUNTIME_PATH, "mods"))).getAbsoluteFile();
    static {
        if (MODS_FILE.exists() && !MODS_FILE.isDirectory())
            HLog.logger(HELogLevel.ERROR, "Mods path is a file! MODS_PATH='", MODS_FILE.getPath(), "'.");
        else if (!MODS_FILE.exists() && !MODS_FILE.mkdirs())
            HLog.logger(HELogLevel.ERROR, "Creating MODS_PATH directory failed. MODS_PATH='", MODS_FILE.getPath(), "'.");
    }

    static void setLogger(HLog logger) {
        ModClassesLoader.logger = logger;
    }

    private static Map<Class<?>, File> allClassesWithJarFiles;

    static Set<Class<?>> getAllClasses() {
        return allClassesWithJarFiles.keySet();
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

    private static final List<List<Class<? extends ModImplement>>> sameMods = new ArrayList<>();
    private static final List<List<Class<? extends ElementImplement>>> sameImplements = new ArrayList<>();
    private static final List<List<Class<? extends ElementUtil<?>>>> sameUtils = new ArrayList<>();

    private static final List<Class<? extends ElementImplement>> singleImplements = new ArrayList<>();
    private static final List<Class<? extends ElementUtil<?>>> singleUtils = new ArrayList<>();

    static List<Class<? extends ModImplement>> getModList() {
        return modList;
    }

    static List<Class<? extends ElementImplement>> getImplementList() {
        return implementList;
    }

    static List<Class<? extends ElementUtil<?>>> getUtilList() {
        return utilList;
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

    @SuppressWarnings("unchecked")
    static void pickAllClasses() {
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.addJarFilesInDirectory(MODS_FILE);
        modsFinder.startFind();
        allClassesWithJarFiles = modsFinder.getClassListWithJarFile();
        HClassFinder modFilter = new HClassFinder();
        modFilter.addAnnotationClass(NewMod.class);
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

    static void checkSameMods() {
        for (Class<? extends ModImplement> classClass: mods) {
            NewMod classMod = classClass.getAnnotation(NewMod.class);
            String className = HStringHelper.noNull(classMod.name().strip());
            boolean not_found = true;
            for (Class<? extends ModImplement> savedClass: modList) {
                NewMod savedMod = savedClass.getAnnotation(NewMod.class);
                if (className.equals(HStringHelper.noNull(savedMod.name().strip()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same mod name '", savedMod.name(), "'. ",
                            "From: '", allClassesWithJarFiles.get(savedClass), "' and '", allClassesWithJarFiles.get(classClass), "'.");
                    boolean notFound = true;
                    for (List<Class<? extends ModImplement>> sameModFound: sameMods) {
                        if (sameModFound.isEmpty())
                            continue;
                        NewMod mod = sameModFound.get(0).getAnnotation(NewMod.class);
                        if (className.equals(HStringHelper.noNull(mod.name()))) {
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
            NewElementImplement classImplement = classClass.getAnnotation(NewElementImplement.class);
            String className = HStringHelper.noNull(classImplement.elementName());
            boolean not_found = true;
            for (Class<? extends ElementImplement> savedClass: implementList) {
                NewElementImplement savedImplement = savedClass.getAnnotation(NewElementImplement.class);
                if (className.equals(HStringHelper.noNull(savedImplement.elementName()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same element implement name '", savedImplement.elementName(), "'.");
                    boolean notFound = true;
                    for (List<Class<? extends ElementImplement>> sameImplementFound: sameImplements) {
                        if (sameImplementFound.isEmpty())
                            continue;
                        NewElementImplement implement = sameImplementFound.get(0).getAnnotation(NewElementImplement.class);
                        if (className.equals(HStringHelper.noNull(implement.elementName()))) {
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
            NewElementUtil classMod = classClass.getAnnotation(NewElementUtil.class);
            String className = HStringHelper.noNull(classMod.name());
            boolean not_found = true;
            for (Class<? extends ElementUtil<?>> savedClass: utilList) {
                NewElementUtil savedUtil = savedClass.getAnnotation(NewElementUtil.class);
                if (className.equals(HStringHelper.noNull(savedUtil.name()))) {
                    not_found = false;
                    logger.log(HELogLevel.FAULT, "Same element util name '", savedUtil.name(), "'.");
                    boolean notFound = true;
                    for (List<Class<? extends ElementUtil<?>>> sameUtilFound: sameUtils) {
                        if (sameUtilFound.isEmpty())
                            continue;
                        NewElementUtil util = sameUtilFound.get(0).getAnnotation(NewElementUtil.class);
                        if (className.equals(HStringHelper.noNull(util.name()))) {
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
            NewElementImplement elementImplement = implement.getAnnotation(NewElementImplement.class);
            String tempName = HStringHelper.noNull(elementImplement.elementName());
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
            ModElementsRegisterer.getElementPairList().put(tempName, new Pair<>(implement, elementUtil));
        }
        for (Class<? extends ElementUtil<?>> util: elementUtils) {
            NewElementUtil elementUtil = util.getAnnotation(NewElementUtil.class);
            String tempName = HStringHelper.noNull(elementUtil.name());
            Class<? extends ElementImplement> elementImplement = null;
            for (Class<? extends ElementImplement> implement: elementImplements) {
                NewElementImplement tempImplement = implement.getAnnotation(NewElementImplement.class);
                if (tempImplement == null)
                    continue;
                if (tempName.equals(HStringHelper.noNull(tempImplement.elementName()))) {
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
