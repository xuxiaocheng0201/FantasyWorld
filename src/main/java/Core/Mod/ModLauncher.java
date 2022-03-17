package Core.Mod;

import Core.Event.PostInitializationModsEvent;
import Core.Event.PreInitializationModsEvent;
import Core.Exception.ModMissingException;
import Core.Exception.ModRequirementsException;
import Core.Exception.ModRequirementsFormatException;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;
import HeadLibs.Version.VersionRange;

import java.util.ArrayList;
import java.util.List;

public class ModLauncher {
    private static final List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();
    private static HLog logger;
    //                                  class                               name         version available_Craftworld_version
    private static final List<Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, VersionRange>>>,
                //        after     required      name    version_range    before
                Pair<Pair<List<Pair<Boolean, Pair<String, VersionRange>>>, List<Pair<Boolean, Pair<String, VersionRange>>>>,
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
            String modAvailable = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(mod.availableCraftworldVersion()));
            VersionRange availableCraftworldVersion = new VersionRange(modAvailable);
            List<Pair<Boolean, Pair<String, VersionRange>>> after = new ArrayList<>();
            List<Pair<Boolean, Pair<String, VersionRange>>> before = new ArrayList<>();
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
                VersionRange versionRange;
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
                    versionRange = new VersionRange();
                }
                else {
                    requirementName = requirementInformation.substring(0, locationAt);
                    String requirementVersion = requirementInformation.substring(locationAt + 1);
                    versionRange = new VersionRange(requirementVersion);
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
logger.log(modContainer);
    }

    public static void checkModContainer() {
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, VersionRange>>>,
                Pair<Pair<List<Pair<Boolean, Pair<String, VersionRange>>>, List<Pair<Boolean, Pair<String, VersionRange>>>>,
                        Pair<Boolean, Boolean>>> mod: modContainer) {
            //force requirement check
            for (Pair<Boolean, Pair<String, VersionRange>> requirements: mod.getValue().getKey().getKey()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, VersionRange>>>,
                            Pair<Pair<List<Pair<Boolean, Pair<String, VersionRange>>>, List<Pair<Boolean, Pair<String, VersionRange>>>>,
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
            for (Pair<Boolean, Pair<String, VersionRange>> requirements: mod.getValue().getKey().getValue()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, VersionRange>>>,
                            Pair<Pair<List<Pair<Boolean, Pair<String, VersionRange>>>, List<Pair<Boolean, Pair<String, VersionRange>>>>,
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
            List<String> requestAfterModName = new ArrayList<>();
            for (Pair<Boolean, Pair<String, VersionRange>> requirements: mod.getValue().getKey().getKey())
                if (!requestAfterModName.contains(requirements.getValue().getKey()))
                    requestAfterModName.add(requirements.getValue().getKey());
            for (Pair<Boolean, Pair<String, VersionRange>> requirements: mod.getValue().getKey().getValue())
                if (requestAfterModName.contains(requirements.getValue().getKey()))
                    exceptions.add(new ModRequirementsException(HStringHelper.merge("Request after and before the same mod '", requirements.getValue().getKey(), "'.",
                            " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
            //available in current Craftworld version
//TODO
        }
    }

    public static void sortMods() {
        //TODO: sort

        sortedMods.clear();
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, VersionRange>>>,
                Pair<Pair<List<Pair<Boolean, Pair<String, VersionRange>>>, List<Pair<Boolean, Pair<String, VersionRange>>>>,
                        Pair<Boolean, Boolean>>> pair: modContainer)
            sortedMods.add(pair.getKey().getKey());
        logger.log(HELogLevel.DEBUG, "Sorted Mod list: ", sortedMods);

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
