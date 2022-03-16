package Core.Mod;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModLauncher {
    private static List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();
    private static final Set<Class<? extends ModImplement>> exceptions_flag = new HashSet<>();
    private static HLog logger;

    public static List<Class<? extends ModImplement>> getSortedMods() {
        return sortedMods;
    }

    public static List<ModRequirementsException> getExceptions() {
        return exceptions;
    }

    public static void sortMods() {
        logger = new HLog("ModLauncher", Thread.currentThread().getName());
        //TODO: Need recode!
        /*
        List<WrongMinecraftVersionException> wrongMinecraftExceptions = new ArrayList<>();
        List<MissingModsException> missingModsExceptions = new ArrayList<>();
        try
        {
            BiMap<String, ArtifactVersion> modVersions = HashBiMap.create();
            for (ModContainer mod : Iterables.concat(getActiveModList(), ModAPIManager.INSTANCE.getAPIList()))
            {
                modVersions.put(mod.getModId(), mod.getProcessedVersion());
            }

            ArrayListMultimap<String, String> reqList = ArrayListMultimap.create();
            for (ModContainer mod : getActiveModList())
            {
                if (!mod.acceptableMinecraftVersionRange().containsVersion(minecraft.getProcessedVersion()))
                {
                    FMLLog.log.fatal("The mod {} does not wish to run in Minecraft version {}. You will have to remove it to play.", mod.getModId(), getMCVersionString());
                    WrongMinecraftVersionException ret = new WrongMinecraftVersionException(mod, getMCVersionString());
                    FMLLog.log.fatal(ret.getMessage());
                    wrongMinecraftExceptions.add(ret);
                    continue;
                }

                reqList.putAll(mod.getModId(), Iterables.transform(mod.getRequirements(), ArtifactVersion::getLabel));

                Set<ArtifactVersion> allDeps = Sets.newHashSet();

                allDeps.addAll(mod.getDependants());
                allDeps.addAll(mod.getDependencies());
                allDeps.addAll(mod.getRequirements());

                MissingModsException missingModsException = new MissingModsException(mod.getModId(), mod.getName());
                for (ArtifactVersion acceptedVersion : allDeps)
                {
                    boolean required = mod.getRequirements().contains(acceptedVersion);
                    if (required || modVersions.containsKey(acceptedVersion.getLabel()))
                    {
                        ArtifactVersion currentVersion = modVersions.get(acceptedVersion.getLabel());
                        if (currentVersion == null || !acceptedVersion.containsVersion(currentVersion))
                        {
                            missingModsException.addMissingMod(acceptedVersion, currentVersion, required);
                        }
                    }
                }
                if (!missingModsException.getMissingModInfos().isEmpty())
                {
                    FMLLog.log.fatal(missingModsException.toString());
                    missingModsExceptions.add(missingModsException);
                }
            }

            if (wrongMinecraftExceptions.isEmpty() && missingModsExceptions.isEmpty())
            {
                FMLLog.log.trace("All mod requirements are satisfied");
            }
            else if (missingModsExceptions.size()==1 && wrongMinecraftExceptions.isEmpty())
            {
                throw missingModsExceptions.get(0);
            }
            else if (wrongMinecraftExceptions.size()==1 && missingModsExceptions.isEmpty())
            {
                throw wrongMinecraftExceptions.get(0);
            }
            else
            {
                throw new MultipleModsErrored(wrongMinecraftExceptions, missingModsExceptions);
            }

            reverseDependencies = Multimaps.invertFrom(reqList, ArrayListMultimap.create());
            ModSorter sorter = new ModSorter(getActiveModList(), namedMods);

            try
            {
                FMLLog.log.trace("Sorting mods into an ordered list");
                List<ModContainer> sortedMods = sorter.sort();
                // Reset active list to the sorted list
                modController.getActiveModList().clear();
                modController.getActiveModList().addAll(sortedMods);
                // And inject the sorted list into the overall list
                mods.removeAll(sortedMods);
                sortedMods.addAll(mods);
                mods = sortedMods;
                FMLLog.log.trace("Mod sorting completed successfully");
            }
            catch (ModSortingException sortException)
            {
                FMLLog.log.fatal("A dependency cycle was detected in the input mod set so an ordering cannot be determined");
                SortingExceptionData<ModContainer> exceptionData = sortException.getExceptionData();
                FMLLog.log.fatal("The first mod in the cycle is {}", exceptionData.getFirstBadNode());
                FMLLog.log.fatal("The mod cycle involves");
                for (ModContainer mc : exceptionData.getVisitedNodes())
                {
                    FMLLog.log.fatal("{} : before: {}, after: {}", mc.toString(), mc.getDependants(), mc.getDependencies());
                }
                FMLLog.log.error("The full error", sortException);
                throw sortException;
            }
        }
        finally
        {
            FMLLog.log.debug("Mod sorting data");
            int unprintedMods = mods.size();
            for (ModContainer mod : getActiveModList())
            {
                if (!mod.isImmutable())
                {
                    FMLLog.log.debug("\t{}({}:{}): {} ({})", mod.getModId(), mod.getName(), mod.getVersion(), mod.getSource().getName(), mod.getSortingRules());
                    unprintedMods--;
                }
            }
            if (unprintedMods == mods.size())
            {
                FMLLog.log.debug("No user mods found to sort");
            }
        }
         */
        sortedMods = ModClassesLoader.getModList();
        sortedMods.sort((o1, o2) -> {
            int result1 = 0;
            int result2 = 0;
            String name1 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o1.getAnnotation(Mod.class).name()));
            String name2 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o2.getAnnotation(Mod.class).name()));
            String version1 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o1.getAnnotation(Mod.class).version()));
            String version2 = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(o2.getAnnotation(Mod.class).version()));
            String[] require1 = HStringHelper.noNull(o1.getAnnotation(Mod.class).require()).split(";");
            String[] require2 = HStringHelper.noNull(o2.getAnnotation(Mod.class).require()).split(";");
            for (String require_source : require1) {
                String require = HStringHelper.delBlankHeadAndTail(require_source);
                if ((require.startsWith("after:") || require.startsWith("require-after:")) && !require.equals("after:*")) {
                    String requireMod = require.startsWith("after:") ? require.substring(6) : require.substring(14);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name2)) {
                        if (checkVersionSort(mod[1], version2))
                            continue;
                        result1 = 1;
                        break;
                    }
                }
                if ((require.startsWith("before:") || require.startsWith("require-before:")) && !require.equals("before:*")) {
                    String requireMod = require.startsWith("before:") ? require.substring(7) : require.substring(15);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name2)) {
                        if (checkVersionSort(mod[1], version2))
                            continue;
                        result1 = -1;
                        break;
                    }
                }
                if (require.equals("after:*")) {
                    result1 = 2;
                    break;
                }
                if (require.equals("before:*")) {
                    result1 = -2;
                    break;
                }
            }
            for (String require_source : require2) {
                String require = HStringHelper.delBlankHeadAndTail(require_source);
                if ((require.startsWith("after:") || require.startsWith("require-after:")) && !require.equals("after:*")) {
                    String requireMod = require.startsWith("after:") ? require.substring(6) : require.substring(14);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name1)) {
                        if (checkVersionSort(mod[1], version1))
                            continue;
                        result2 = -1;
                        break;
                    }
                }
                if ((require.startsWith("before:") || require.startsWith("require-before:")) && !require.equals("before:*")) {
                    String requireMod = require.startsWith("before:") ? require.substring(7) : require.substring(15);
                    String[] mod = requireMod.split("@");
                    if (mod.length != 2) {
                        exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Too many @ in mod expression. ", requireMod)));
                        continue;
                    }
                    mod[0] = HStringHelper.delBlankHeadAndTail(mod[0]);
                    mod[1] = HStringHelper.delBlankHeadAndTail(mod[1]);
                    if (mod[0].equals(name1)) {
                        if (checkVersionSort(mod[1], version1))
                            continue;
                        result2 = 1;
                        break;
                    }
                }
                if (require.equals("after:*")) {
                    result2 = -2;
                    break;
                }
                if (require.equals("before:*")) {
                    result2 = 2;
                    break;
                }
            }
            if (result1 == 1)
                return 1;
            if (result1 == -1)
                return -1;
            if (result2 == 1)
                return 1;
            if (result2 == -1)
                return -1;
            if (result1 == 2)
                return 2;
            if (result1 == -2)
                return -2;
            if (result2 == 2)
                return 2;
            if (result2 == -2)
                return -2;
            return 0;
        });
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

    public static void launcherMods() {
        for (Class<? extends ModImplement> aClass: sortedMods) {
            ModImplement instance = HClassHelper.getInstance(aClass);
            if (instance == null) {
                logger.log(HELogLevel.ERROR, "No Common Constructor for creating Mod class: ", aClass);
                continue;
            }
            instance.main();
        }
    }
}
