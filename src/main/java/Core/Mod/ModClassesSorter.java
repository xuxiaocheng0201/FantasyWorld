package Core.Mod;

import Core.Craftworld;
import Core.Exceptions.*;
import Core.Mod.New.ModImplement;
import Core.Mod.New.NewMod;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;
import HeadLibs.Version.HStringVersion;
import HeadLibs.Version.HVersionComplex;
import HeadLibs.Version.HVersionFormatException;

import java.util.*;

class ModClassesSorter {
    private static final List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();
    private static final Collection<Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<HStringVersion, HVersionComplex>>>,
            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionComplex>>>, Set<Pair<Boolean, Pair<String, HVersionComplex>>>>, Pair<Boolean, Boolean>>>> modContainer = new HashSet<>();
    private static final Collection<Pair<Pair<Class<? extends ModImplement>, String>,
            Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>>> simpleModContainer = new HashSet<>();

    static List<Class<? extends ModImplement>> getSortedMods() {
        return sortedMods;
    }

    static List<ModRequirementsException> getExceptions() {
        return exceptions;
    }

    static void buildModContainer() {
        for (Class<? extends ModImplement> modClass: ModManager.getModList()) {
            NewMod mod = modClass.getAnnotation(NewMod.class);
            if (mod == null)
                continue;
            String modAvailable = HStringHelper.notEmptyStrip(mod.availableCraftworldVersion());
            HVersionComplex availableCraftworldVersion;
            try {
                availableCraftworldVersion = new HVersionComplex(modAvailable);
            } catch (HVersionFormatException exception) {
                exceptions.add(new ModRequirementsException("Wrong formation of available Craftworld version string." +
                        " At class '" + modClass + "'.", exception));
                continue;
            }
            HStringVersion modVersion;
            try {
                modVersion = new HStringVersion(mod.version());
            } catch (HVersionFormatException exception) {
                exceptions.add(new ModRequirementsException("Wrong formation of mod version string." +
                        " At class '" + modClass + "'.", exception));
                continue;
            }
            String modName = HStringHelper.notEmptyStrip(mod.name());
            Set<Pair<Boolean, Pair<String, HVersionComplex>>> after = new HashSet<>();
            Set<Pair<Boolean, Pair<String, HVersionComplex>>> before = new HashSet<>();
            boolean afterAll = false;
            boolean beforeAll = false;
            String[] modRequirements = HStringHelper.strip(mod.require().split(";"));
            for (String modRequirement: modRequirements) {
                int locationColon = modRequirement.indexOf(':');
                if (locationColon == -1) {
                    exceptions.add(new ModRequirementsFormatException("Need colon(':') to split mod requirement modification and information." +
                            " At class '" + modClass + "', requirement: '" + modRequirement + "'."));
                    continue;
                }
                if (locationColon == modRequirement.length() - 1) {
                    exceptions.add(new ModRequirementsFormatException("Must announce required mod name." +
                            " At class '" + modClass + "', requirement: '" + modRequirement + "'."));
                    continue;
                }
                String requirementModification = HStringHelper.notNullStrip(modRequirement.substring(0, locationColon));
                String requirementInformation = HStringHelper.notNullStrip(modRequirement.substring(locationColon + 1));
                int locationAt = requirementInformation.indexOf('@');
                String requirementModName;
                HVersionComplex requirementModVersions = new HVersionComplex();
                if (locationAt == -1 || locationAt == requirementInformation.length() - 1) {
                    if ("*".equals(requirementInformation)) {
                        switch (requirementModification) {
                            case "after" -> afterAll = true;
                            case "before" -> beforeAll = true;
                            default -> exceptions.add(new ModRequirementsFormatException("Unknown Modification in wildcard('*') mode." +
                                    " At class: '" + modClass + "', requirement: '" + modRequirement + "', modification: '" + requirementModification + "'."));
                        }
                        continue;
                    }
                    if (locationAt == -1)
                        requirementModName = requirementInformation;
                    else
                        requirementModName = HStringHelper.notNullStrip(requirementInformation.substring(0, requirementInformation.length() - 1));
                    requirementModVersions.setAll();
                }
                else {
                    requirementModName = HStringHelper.notNullStrip(requirementInformation.substring(0, locationAt));
                    String requirementVersion = requirementInformation.substring(locationAt + 1);
                    try {
                        requirementModVersions.addVersions(requirementVersion);
                    } catch (HVersionFormatException exception) {
                        exceptions.add(new ModRequirementsException("Wrong formation of mod requirement version string." +
                                " At class '" + modClass + "', requirement: '" + modRequirement + "', mod name: '" + requirementModName + "', mod version: '" + requirementVersion + "'.", exception));
                        continue;
                    }
                }
                switch (requirementModification) {
                    case "after" -> after.add(Pair.makePair(false, Pair.makePair(requirementModName, requirementModVersions)));
                    case "require-after" -> after.add(Pair.makePair(true, Pair.makePair(requirementModName, requirementModVersions)));
                    case "before" -> before.add(Pair.makePair(false, Pair.makePair(requirementModName, requirementModVersions)));
                    case "require-before" -> before.add(Pair.makePair(true, Pair.makePair(requirementModName, requirementModVersions)));
                    default -> exceptions.add(new ModRequirementsFormatException("Unknown Modification." +
                            " At class: '" + modClass + "', requirement: '" + modRequirement + "', modification: '" + requirementModification + "'."));
                }
            }
            modContainer.add(Pair.makePair(Pair.makePair(modClass, Pair.makePair(modName, Pair.makePair(modVersion, availableCraftworldVersion))),
                    Pair.makePair(Pair.makePair(after, before), Pair.makePair(afterAll, beforeAll))));
        }
    }

    @SuppressWarnings("ConstantConditions")
    static void checkModContainer() {
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<HStringVersion, HVersionComplex>>>,
                Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionComplex>>>, Set<Pair<Boolean, Pair<String, HVersionComplex>>>>,
                        Pair<Boolean, Boolean>>> mod: modContainer) {
            //available in current Craftworld version
            if (!mod.getKey().getValue().getValue().getValue().versionInRange(Craftworld.CURRENT_VERSION))
                exceptions.add(new WrongCraftworldVersionException("Current version '" + Craftworld.CURRENT_VERSION_STRING + "' is not in range " + mod.getKey().getValue().getValue().getValue() + "." +
                        " At class: '" + mod.getKey().getKey() + "' name: '" + mod.getKey().getValue().getKey() + "'."));
            //force requirement check
            for (Pair<Boolean, Pair<String, HVersionComplex>> requirements: mod.getValue().getKey().getKey()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<HStringVersion, HVersionComplex>>>,
                            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionComplex>>>, Set<Pair<Boolean, Pair<String, HVersionComplex>>>>,
                                    Pair<Boolean, Boolean>>> i : modContainer)
                        if (requireModName.equals(i.getKey().getValue().getKey())) {
                            flag = false;
                            break;
                        }
                    if (flag)
                        exceptions.add(new ModMissingException("Need mod '" + requireModName + "'" +
                                " for class: '" + mod.getKey().getKey() + "' name: '" + mod.getKey().getValue().getKey() + "'."));
                }
            }
            for (Pair<Boolean, Pair<String, HVersionComplex>> requirements: mod.getValue().getKey().getValue()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<HStringVersion, HVersionComplex>>>,
                            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionComplex>>>, Set<Pair<Boolean, Pair<String, HVersionComplex>>>>,
                                    Pair<Boolean, Boolean>>> i : modContainer)
                        if (requireModName.equals(i.getKey().getValue().getKey())) {
                            flag = false;
                            break;
                        }
                    if (flag)
                        exceptions.add(new ModMissingException("Need mod '" + requireModName + "'" +
                                " for class: '" + mod.getKey().getKey() + "' name: '" + mod.getKey().getValue().getKey() + "'."));
                }
            }
            //both after:* and before:*
            if (mod.getValue().getValue().getKey() && mod.getValue().getValue().getValue())
                exceptions.add(new ModRequirementsException("Both after:* and before:*" +
                        " for class: '" + mod.getKey().getKey() + "' name: '" + mod.getKey().getValue().getKey() + "'."));
            //request after and before the same mod
            Collection<String> requestAfterModName = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionComplex>> requirements: mod.getValue().getKey().getKey())
                requestAfterModName.add(requirements.getValue().getKey());
            for (Pair<Boolean, Pair<String, HVersionComplex>> requirements: mod.getValue().getKey().getValue())
                if (requestAfterModName.contains(requirements.getValue().getKey()))
                    exceptions.add(new ModRequirementsException("Request after and before the same mod '" + requirements.getValue().getKey() + "'." +
                            " At class: '" + mod.getKey().getKey() + "' name: '" + mod.getKey().getValue().getKey() + "'."));
            //mod version check
            for (Pair<Boolean, Pair<String, HVersionComplex>> requirements: mod.getValue().getKey().getKey())
                for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<HStringVersion, HVersionComplex>>>,
                        Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionComplex>>>, Set<Pair<Boolean, Pair<String, HVersionComplex>>>>,
                                Pair<Boolean, Boolean>>> b: modContainer)
                    if (requirements.getValue().getKey().equals(b.getKey().getValue().getKey()))
                        if (!requirements.getValue().getValue().versionInRange(b.getKey().getValue().getValue().getKey()))
                            exceptions.add(new ModVersionUnmatchedException("Have had mod '" + b.getKey().getValue().getKey() + "@" + b.getKey().getValue().getValue() + "' but need version in '" + requirements.getValue().getValue() + "'." +
                                    " At class: '" + mod.getKey().getKey() + "' name: '" + mod.getKey().getValue().getKey() + "'."));
            for (Pair<Boolean, Pair<String, HVersionComplex>> requirements: mod.getValue().getKey().getValue())
                for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<HStringVersion, HVersionComplex>>>,
                        Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionComplex>>>, Set<Pair<Boolean, Pair<String, HVersionComplex>>>>,
                                Pair<Boolean, Boolean>>> b: modContainer)
                    if (requirements.getValue().getKey().equals(b.getKey().getValue().getKey()))
                        if (!requirements.getValue().getValue().versionInRange(b.getKey().getValue().getValue().getKey()))
                            exceptions.add(new ModVersionUnmatchedException("Have had mod '" + b.getKey().getValue().getKey() + "@" + b.getKey().getValue().getValue() + "' but need version in '" + requirements.getValue().getValue() + "'." +
                                    " At class: '" + mod.getKey().getKey() + "' name: '" + mod.getKey().getValue().getKey() + "'."));
        }
    }
    
    @SuppressWarnings("ConstantConditions")
    static void toSimpleModContainer() {
        simpleModContainer.clear();
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<HStringVersion, HVersionComplex>>>,
                Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionComplex>>>, Set<Pair<Boolean, Pair<String, HVersionComplex>>>>,
                        Pair<Boolean, Boolean>>> mod: modContainer) {
            Class<? extends ModImplement> modClass = mod.getKey().getKey();
            String modName = mod.getKey().getValue().getKey();
            Set<String> modRequireAfter = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionComplex>> pair: mod.getValue().getKey().getKey())
                modRequireAfter.add(pair.getValue().getKey());
            Set<String> modRequireBefore = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionComplex>> pair: mod.getValue().getKey().getValue())
                modRequireBefore.add(pair.getValue().getKey());
            Pair<Boolean, Boolean> modRequireAll = mod.getValue().getValue();
            simpleModContainer.add(Pair.makePair(Pair.makePair(modClass, modName), Pair.makePair(Pair.makePair(modRequireAfter, modRequireBefore), modRequireAll)));
        }
        modContainer.clear(); //GC
    }

    private static final Collection<String> sortHasSearchedMods = new HashSet<>();

    @SuppressWarnings("ConstantConditions")
    static void sortMods() {
        sortedMods.clear();
        for (Pair<Pair<Class<? extends ModImplement>, String>,
                Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod: simpleModContainer) {
            sortHasSearchedMods.clear();
            addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                    mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void addSortMod(Class<? extends ModImplement> modClass, String modName,
                                   Collection<String> requireAfter, Collection<String> requireBefore, Pair<Boolean, Boolean> requireAll) {
        if (sortedMods.contains(modClass) || sortHasSearchedMods.contains(modName))
            return;
        sortHasSearchedMods.add(modName);
        for (String after : requireAfter)
            for (Pair<Pair<Class<? extends ModImplement>, String>,
                    Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod : simpleModContainer)
                if (after.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        for (String before : requireBefore)
            for (Pair<Pair<Class<? extends ModImplement>, String>,
                    Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod : simpleModContainer)
                if (before.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        int left = 0;
        int right = sortedMods.size();
        for (int i = 0; i < sortedMods.size(); ++i)
        {
            if (requireAfter.contains(sortedMods.get(i).getAnnotation(NewMod.class).name()))
                if (left == 0)
                    left = i + 1;
                else
                    left = Math.min(left, i + 1);
            if (requireBefore.contains(sortedMods.get(i).getAnnotation(NewMod.class).name()))
                if (right == sortedMods.size())
                    right = i;
                else
                    right = Math.max(right, i);
        }
        if (left > right)
            exceptions.add(new ModRequirementsException("Mod sort error! left=" + left + ", right=" + right + ", sortedMod=" + sortedMods +
                    ". At class: '" + modClass + "'."));
        else
            if (requireAll.getValue())
                sortedMods.add(left, modClass);
            else
                sortedMods.add(right, modClass);
    }

    static void gc() {
        exceptions.clear();
        modContainer.clear();
        simpleModContainer.clear();
    }
}
