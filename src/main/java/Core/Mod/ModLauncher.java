package Core.Mod;

import Core.Event.PostInitializationModsEvent;
import Core.Event.PreInitializationModsEvent;
import Core.Exception.ModRequirementsException;
import Core.Exception.ModRequirementsFormatException;
import Core.Exception.ModVersionUnmatchedException;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import HeadLibs.HVersionComparator;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModLauncher {
    private static final List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();
    private static HLog logger;
    //                                  class                               name    version
    private static final List<Pair<Pair<Class<? extends ModImplement>, Pair<String, String>>,
            //        after     name         version_range                  can_equal  border    single_version
            Pair<Pair<List<Pair<String, Pair<Pair<Pair<Boolean, String>, Pair<Boolean, String>>, List<String>>>>,
                    //        before    name    version_range                  can_equal  border
                    List<Pair<String, Pair<Pair<Pair<Boolean, String>, Pair<Boolean, String>>, List<String>>>>>,
                    //   afterAll beforeAll
                    Pair<Boolean, Boolean>>>> modContainer = new ArrayList<>();

    public static List<Class<? extends ModImplement>> getSortedMods() {
        return sortedMods;
    }

    public static List<ModRequirementsException> getExceptions() {
        return exceptions;
    }

    public static void buildModContainer() {
        logger = new HLog("ModLauncher", Thread.currentThread().getName());
        for (Class<? extends ModImplement> modClass: ModClassesLoader.getModList()) {
            Mod mod = modClass.getAnnotation(Mod.class);
            if (mod == null)
                continue;
            String modName = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(mod.name()));
            String modVersion = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(mod.version()));
            List<Pair<String, Pair<Pair<Pair<Boolean, String>, Pair<Boolean, String>>, List<String>>>> after = new ArrayList<>();
            List<Pair<String, Pair<Pair<Pair<Boolean, String>, Pair<Boolean, String>>, List<String>>>> before = new ArrayList<>();
            boolean afterAll = false;
            boolean beforeAll = false;
            String[] modRequirements = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(mod.require().split(";")));
            for (String modRequirement: modRequirements) {
                int locationColon = modRequirement.indexOf(":");
                if (locationColon == -1) {
                    exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Need ':' to modify mod sort." +
                            " At class '", modClass, "', requirement: '", modRequirement, "'.")));
                    continue;
                }
                if (locationColon == modRequirement.length() - 1) {
                    exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Must have mod name." +
                            " At class '", modClass, "', requirement: '", modRequirement, "'.")));
                    continue;
                }
                String requirementModification = modRequirement.substring(0, locationColon);
                String requirementInformation = modRequirement.substring(locationColon + 1);
                Pair<Pair<Boolean, String>, Pair<Boolean, String>> versionRange = new Pair<>(new Pair<>(false, null), new Pair<>(false, null));
                List<String> versionSingle = new ArrayList<>();
                int locationAt = requirementInformation.indexOf("@");
                String requirementName;
                if (locationAt == -1 || locationAt == requirementInformation.length() - 1) {
                    if (requirementInformation.equals("*")) {
                        switch (requirementModification) {
                            case "after" -> afterAll = true;
                            case "before" -> beforeAll = true;
                            default -> exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Unknown Modification in wildcard." +
                                    " At class: '", modClass, "', requirement: '", modRequirement, "', modification: '", requirementModification, "'.")));
                        }
                        continue;
                    }
                    requirementName = requirementInformation;
                }
                else {
                    requirementName = requirementInformation.substring(0, locationAt);
                    String requirementVersion = requirementInformation.substring(locationAt + 1);
                    byte canEqualLeft = 0;
                    if (requirementVersion.charAt(0) == '[')
                        canEqualLeft = 1;
                    if (requirementVersion.charAt(0) == '(')
                        canEqualLeft = -1;
                    if (requirementVersion.charAt(0) == '{')
                        canEqualLeft = 2;
                    byte canEqualRight = 0;
                    if (requirementVersion.charAt(requirementVersion.length() - 1) == ']')
                        canEqualRight = 1;
                    if (requirementVersion.charAt(requirementVersion.length() - 1) == ')')
                        canEqualRight = -1;
                    if (requirementVersion.charAt(requirementVersion.length() - 1) == '}')
                        canEqualRight = 2;
                    if (canEqualLeft == 0 && canEqualRight == 0) {
                        versionRange.setKey(new Pair<>(true, requirementVersion));
                        versionRange.setValue(new Pair<>(true, requirementVersion));
                    } else if (canEqualLeft == 2 && canEqualRight == 2) {
                        String[] versions = HStringHelper.delBlankHeadAndTail(requirementVersion.substring(1, requirementVersion.length() - 1).split(","));
                        versionSingle.addAll(Arrays.asList(versions));
                    } else if (canEqualLeft == 0 || canEqualRight == 0 || canEqualLeft == 2 || canEqualRight == 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Unknown symbols in brackets." +
                                " At class: '", modClass, "', requirement: '", modRequirement, "', requirementVersion: '", requirementVersion, "'.")));
                        continue;
                    } else {
                        String versions = HStringHelper.delBlankHeadAndTail(requirementVersion.substring(1, requirementVersion.length() - 1));
                        int locationComma = versions.indexOf(",");
                        int locationComma1 = versions.lastIndexOf(",");
                        if (locationComma != locationComma1) {
                            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many commas in versions." +
                                    " At class: '", modClass, "', requirement: '", modRequirement, "', versions: '", versions, "'.")));
                            continue;
                        }
                        String leftVersion = HStringHelper.delBlankHeadAndTail(versions.substring(0, locationComma));
                        String rightVersion = HStringHelper.delBlankHeadAndTail(versions.substring(locationComma + 1));
                        versionRange.setKey(new Pair<>((canEqualLeft == 1), leftVersion));
                        versionRange.setValue(new Pair<>((canEqualRight == 1), rightVersion));
                    }
                }
                switch (requirementModification) {
                    case "after", "require-after" -> after.add(new Pair<>(requirementName, new Pair<>(versionRange, versionSingle)));
                    case "before", "require-before" -> before.add(new Pair<>(requirementName, new Pair<>(versionRange, versionSingle)));
                    default -> exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Unknown Modification." +
                            " At class: '", modClass, "', requirement: '", modRequirement, "', modification: '", requirementModification, "'.")));
                }
            }
            modContainer.add(new Pair<>(new Pair<>(modClass, new Pair<>(modName, modVersion)),
                    new Pair<>(new Pair<>(after, before), new Pair<>(afterAll, beforeAll))));
        }
    }

    public static void sortMods() {
        //TODO: sort

        sortedMods.clear();
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, String>>,
                Pair<Pair<List<Pair<String, Pair<Pair<Pair<Boolean, String>, Pair<Boolean, String>>, List<String>>>>,
                        List<Pair<String, Pair<Pair<Pair<Boolean, String>, Pair<Boolean, String>>, List<String>>>>>,
                        Pair<Boolean, Boolean>>> pair: modContainer)
            sortedMods.add(pair.getKey().getKey());
        logger.log(HELogLevel.DEBUG, "Sorted Mod list: ", sortedMods);

    }

    private static boolean checkVersionSort(String require, String version) {
        byte can_equal_left = 0;
        if (require.charAt(0) == '(')
            can_equal_left = -1;
        if (require.charAt(0) == '[')
            can_equal_left = 1;
        if (can_equal_left == 0) {
            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Interval error. ", require)));
            return true;
        }
        byte can_equal_right = 0;
        if (require.charAt(require.length() - 1) == ')')
            can_equal_right = -1;
        if (require.charAt(require.length() - 1) == ']')
            can_equal_right = 1;
        if (can_equal_right == 0) {
            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Interval error. ", require)));
            return true;
        }
        String[] interval = require.substring(1, require.length() - 1).split(",");
        if (interval.length > 2) {
            exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("To many ',' in mod expression. ", require)));
            return true;
        }
        if (interval.length == 0)
            return false;
        boolean is_error = true;
        if (interval.length == 1) {
            if (require.charAt(1) == ',') {
                int compare = HVersionComparator.compareVersion(interval[0], version);
                if ((compare == 0 && can_equal_right == 1) || compare > 0)
                    is_error = false;
            }
            if (require.charAt(require.length() - 2) == ',') {
                int compare = HVersionComparator.compareVersion(interval[0], version);
                if ((compare == 0 && can_equal_left == 1) || compare < 0)
                    is_error = false;
            }
        }
        if (interval.length == 2) {
            boolean left_result = true;
            int left_compare = HVersionComparator.compareVersion(interval[0], version);
            boolean right_result = true;
            int right_compare = HVersionComparator.compareVersion(interval[1], version);
            if ((left_compare == 0 && can_equal_left == 1) || left_compare < 0)
                left_result = false;
            if ((right_compare == 0 && can_equal_right == 1) || right_compare > 0)
                right_result = false;
            is_error = left_result || right_result;
        }
        if (is_error) {
            exceptions.add(new ModVersionUnmatchedException(HStringHelper.merge("Version '", version, "' is out of range ", require)));
        }
        return false;
    }

    public static void launchMods() {
        ModClassesLoader.getDefaultEventBus().post(new PreInitializationModsEvent());
        for (Class<? extends ModImplement> aClass: sortedMods) {
            ModImplement instance = HClassHelper.getInstance(aClass);
            if (instance == null) {
                logger.log(HELogLevel.ERROR, "No Common Constructor for creating Mod class: ", aClass);
                continue;
            }
            instance.main();
        }
        ModClassesLoader.getDefaultEventBus().post(new PostInitializationModsEvent());
    }
}
