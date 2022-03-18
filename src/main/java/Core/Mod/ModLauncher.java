package Core.Mod;

import Core.Craftworld;
import Core.Event.PostInitializationModsEvent;
import Core.Event.PreInitializationModsEvent;
import Core.Exception.*;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;
import HeadLibs.Version.HVersionRange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModLauncher {
    private static final List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();
    private static HLog logger;
    //                                  class                               name         version available_Craftworld_version
    private static final Set<Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
            //        after     required      name    version_range    before
            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                    //   afterAll beforeAll
                    Pair<Boolean, Boolean>>>> modContainer = new HashSet<>();
    //                                  class                          name
    private static final Set<Pair<Pair<Class<? extends ModImplement>, String>,
            //        after        before             afterAll beforeAll
            Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>>> simpleModContainer = new HashSet<>();

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
            String modAvailable = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(mod.availableCraftworldVersion()));
            HVersionRange availableCraftworldVersion = new HVersionRange(modAvailable);
            Set<Pair<Boolean, Pair<String, HVersionRange>>> after = new HashSet<>();
            Set<Pair<Boolean, Pair<String, HVersionRange>>> before = new HashSet<>();
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
                int locationAt = requirementInformation.indexOf("@");
                String requirementName;
                HVersionRange versionRange;
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
                    versionRange = new HVersionRange();
                }
                else {
                    requirementName = requirementInformation.substring(0, locationAt);
                    String requirementVersion = requirementInformation.substring(locationAt + 1);
                    versionRange = new HVersionRange(requirementVersion);
                }
                switch (requirementModification) {
                    case "after" -> after.add(new Pair<>(false, new Pair<>(requirementName, versionRange)));
                    case "require-after" -> after.add(new Pair<>(true, new Pair<>(requirementName, versionRange)));
                    case "before" -> before.add(new Pair<>(false, new Pair<>(requirementName, versionRange)));
                    case "require-before" -> before.add(new Pair<>(true, new Pair<>(requirementName, versionRange)));
                    default -> exceptions.add(new ModRequirementsFormatException(HStringHelper.merge("Unknown Modification." +
                            " At class: '", modClass, "', requirement: '", modRequirement, "', modification: '", requirementModification, "'.")));
                }
            }
            modContainer.add(new Pair<>(new Pair<>(modClass, new Pair<>(modName, new Pair<>(modVersion, availableCraftworldVersion))),
                    new Pair<>(new Pair<>(after, before), new Pair<>(afterAll, beforeAll))));
        }
    }

    public static void checkModContainer() {
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                        Pair<Boolean, Boolean>>> mod: modContainer) {
            //available in current Craftworld version
            if (!mod.getKey().getValue().getValue().getValue().versionInRange(Craftworld.CURRENT_VERSION))
                exceptions.add(new WrongCraftworldVersionException(HStringHelper.merge("Current version '", Craftworld.CURRENT_VERSION, "' is not in range '", mod.getKey().getValue().getValue().getValue(), "'.",
                        " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
            //force requirement check
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getKey()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                    Pair<Boolean, Boolean>>> i : modContainer)
                        if (requireModName.equals(i.getKey().getValue().getKey())) {
                            flag = false;
                            break;
                        }
                    if (flag)
                        exceptions.add(new ModMissingException(HStringHelper.merge("Need mod '", requireModName, "'",
                                " for class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                }
            }
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getValue()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                    Pair<Boolean, Boolean>>> i : modContainer)
                        if (requireModName.equals(i.getKey().getValue().getKey())) {
                            flag = false;
                            break;
                        }
                    if (flag)
                        exceptions.add(new ModMissingException(HStringHelper.merge("Need mod '", requireModName, "'",
                                " for class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                }
            }
            //both after:* and before:*
            if (mod.getValue().getValue().getKey() && mod.getValue().getValue().getValue())
                exceptions.add(new ModRequirementsException(HStringHelper.merge("Both after:* and before:*",
                        " for class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
            //request after and before the same mod
            Set<String> requestAfterModName = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getKey())
                requestAfterModName.add(requirements.getValue().getKey());
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getValue())
                if (requestAfterModName.contains(requirements.getValue().getKey()))
                    exceptions.add(new ModRequirementsException(HStringHelper.merge("Request after and before the same mod '", requirements.getValue().getKey(), "'.",
                            " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
            //mod version check
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getKey())
                for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                        Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                Pair<Boolean, Boolean>>> b: modContainer)
                    if (requirements.getValue().getKey().equals(b.getKey().getValue().getKey())) {
                        if (!requirements.getValue().getValue().versionInRange(b.getKey().getValue().getValue().getKey()))
                            exceptions.add(new ModVersionUnmatchedException(HStringHelper.merge("Have had mod '", b.getKey().getValue().getKey(), "@", b.getKey().getValue().getValue(), "' but need version '", requirements.getValue().getValue(), "'.",
                                    " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                    }
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getValue())
                for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                        Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                Pair<Boolean, Boolean>>> b: modContainer)
                    if (requirements.getValue().getKey().equals(b.getKey().getValue().getKey())) {
                        if (!requirements.getValue().getValue().versionInRange(b.getKey().getValue().getValue().getKey()))
                            exceptions.add(new ModVersionUnmatchedException(HStringHelper.merge("Have had mod '", b.getKey().getValue().getKey(), "@", b.getKey().getValue().getValue(), "' but need version '", requirements.getValue().getValue(), "'.",
                                    " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                    }
        }
    }
    
    public static void toSimpleModContainer() {
        simpleModContainer.clear();
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                        Pair<Boolean, Boolean>>> mod: modContainer) {
            Class<? extends ModImplement> modClass = mod.getKey().getKey();
            String modName = mod.getKey().getValue().getKey();
            Set<String> modRequireAfter = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionRange>> pair: mod.getValue().getKey().getKey())
                modRequireAfter.add(pair.getValue().getKey());
            Set<String> modRequireBefore = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionRange>> pair: mod.getValue().getKey().getValue())
                modRequireBefore.add(pair.getValue().getKey());
            Pair<Boolean, Boolean> modRequireAll = mod.getValue().getValue();
            simpleModContainer.add(new Pair<>(new Pair<>(modClass, modName), new Pair<>(new Pair<>(modRequireAfter, modRequireBefore), modRequireAll)));
        }
        modContainer.clear(); //GC
    }

    private static final Set<String> sortHasSearchedMods = new HashSet<>();

    public static void sortMods() {
        sortedMods.clear();
        for (Pair<Pair<Class<? extends ModImplement>, String>,
                Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod: simpleModContainer) {
            sortHasSearchedMods.clear();
            addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                    mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
        }
    }

    private static void addSortMod(Class<? extends ModImplement> modClass, String modName,
                                   Set<String> requireAfter, Set<String> requireBefore, Pair<Boolean, Boolean> requireAll) {
        if (sortedMods.contains(modClass))
            return;
        if (sortHasSearchedMods.contains(modName))
            return;
        sortHasSearchedMods.add(modName);
        for (String after: requireAfter)
            for (Pair<Pair<Class<? extends ModImplement>, String>,
                    Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod: simpleModContainer)
                if (after.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        for (String before: requireBefore)
            for (Pair<Pair<Class<? extends ModImplement>, String>,
                    Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod: simpleModContainer)
                if (before.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        int left = 0;
        for (int i = 0; i < sortedMods.size(); ++i)
            if (requireAfter.contains(sortedMods.get(i).getAnnotation(Mod.class).name()))
                if (left == 0)
                    left = i + 1;
                else
                    left = Math.min(left, i + 1);
        int right = sortedMods.size();
        for (int i = 0; i < sortedMods.size(); ++i)
            if (requireBefore.contains(sortedMods.get(i).getAnnotation(Mod.class).name()))
                if (right == sortedMods.size())
                    right = i;
                else
                    right = Math.max(right, i);
        if (left > right)
            exceptions.add(new ModRequirementsException(HStringHelper.merge("Mod sort error! left=", left, ", right=", right, ", sortedMod=", sortedMods,
                    " At class: '", modClass, "' name: '", modName, "'.")));
        else
            if (requireAll.getValue())
                sortedMods.add(left, modClass);
            else
                sortedMods.add(right, modClass);
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
