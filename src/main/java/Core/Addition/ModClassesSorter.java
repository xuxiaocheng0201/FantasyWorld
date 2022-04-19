package Core.Addition;

import Core.Addition.Mod.BasicInformation.ModAvailableCraftworldVersion;
import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.BasicInformation.ModRequirements;
import Core.Addition.Mod.BasicInformation.ModVersion;
import Core.Addition.Mod.ModImplement;
import Core.Craftworld;
import Core.Exceptions.ModDependencyMissingException;
import Core.Exceptions.ModDependencyVersionUnmatchedException;
import Core.Exceptions.ModInformationException;
import Core.Exceptions.ModUnsupportedCraftworldVersionException;
import HeadLibs.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Sort mods in {@link ModClassesLoader#getModList()}.
 * Check mod dependencies and sort them.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModClassesSorter {
    private static final List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModInformationException> exceptions = new ArrayList<>();

    public static List<Class<? extends ModImplement>> getSortedMods() {
        return sortedMods;
    }

    private static final Collection<Pair<Pair<Class<? extends ModImplement>, Pair<Pair<ModName, ModVersion>, ModAvailableCraftworldVersion>>, ModRequirements>> modContainer = new HashSet<>();
    private static final Collection<Pair<Pair<Class<? extends ModImplement>, ModName>, Pair<Pair<Set<ModName>, Set<ModName>>, ModRequirements.Requirement.Modifier.BasicModifier>>> simpleModContainer = new HashSet<>();

    private static void buildModContainer() {
        for (Class<? extends ModImplement> modClass: ModClassesLoader.getModList()) {
            ModName modName = ModImplement.getModNameFromClass(modClass);
            ModVersion modVersion = ModImplement.getModVersionFromClass(modClass);
            ModAvailableCraftworldVersion modAvailableCraftworldVersion = ModImplement.getModAvailableCraftworldVersionFromClass(modClass);
            ModRequirements modRequirements = ModImplement.getModRequirementsFromClass(modClass);
            modContainer.add(Pair.makePair(Pair.makePair(modClass, Pair.makePair(Pair.makePair(modName, modVersion), modAvailableCraftworldVersion)), modRequirements));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void checkModContainer() {
        for (Pair<Pair<Class<? extends ModImplement>, Pair<Pair<ModName, ModVersion>, ModAvailableCraftworldVersion>>, ModRequirements> mod: modContainer) {
            //available in current Craftworld version
            if (!mod.getKey().getValue().getValue().getVersion().versionInRange(Craftworld.CURRENT_VERSION))
                exceptions.add(new ModUnsupportedCraftworldVersionException(mod.getKey().getKey()));
            //force requirement check
            for (ModRequirements.Requirement requirement: mod.getValue().getRequirements())
                if (requirement.getModifier().isRequired()) {
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<Pair<ModName, ModVersion>, ModAvailableCraftworldVersion>>, ModRequirements> i: modContainer)
                        if (requirement.getModName().equals(i.getKey().getValue().getKey().getKey())) {
                            flag = false;
                            break;
                        }
                    if (flag)
                        exceptions.add(new ModDependencyMissingException(mod.getKey().getKey(), requirement.getModName(), requirement.getVersionComplex()));
                }
            //request after and before the same mod
            Collection<ModName> requestAfterModName = new HashSet<>();
            for (ModRequirements.Requirement requirement: mod.getValue().getRequirements())
                if (requirement.getModifier().getBasic() == ModRequirements.Requirement.Modifier.BasicModifier.AFTER)
                    requestAfterModName.add(requirement.getModName());
            for (ModRequirements.Requirement requirement: mod.getValue().getRequirements())
                if (requirement.getModifier().getBasic() == ModRequirements.Requirement.Modifier.BasicModifier.BEFORE
                        && requestAfterModName.contains(requirement.getModName()))
                    exceptions.add(new ModInformationException("Request after and before the same mod '" + requirement.getModName() + "'.", mod.getKey().getKey()));
            //mod version check
            for (ModRequirements.Requirement requirement: mod.getValue().getRequirements())
                for (Pair<Pair<Class<? extends ModImplement>, Pair<Pair<ModName, ModVersion>, ModAvailableCraftworldVersion>>, ModRequirements> i: modContainer)
                    if (requirement.getModName().equals(i.getKey().getValue().getKey().getKey())
                            && !requirement.getVersionComplex().versionInRange(i.getKey().getValue().getKey().getValue().getVersion()))
                            exceptions.add(new ModDependencyVersionUnmatchedException(i.getKey().getValue().getKey().getKey(), i.getKey().getValue().getKey().getValue(), requirement.getVersionComplex(), mod.getKey().getKey()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void toSimpleModContainer() {
        for (Pair<Pair<Class<? extends ModImplement>, Pair<Pair<ModName, ModVersion>, ModAvailableCraftworldVersion>>, ModRequirements> mod: modContainer) {
            Class<? extends ModImplement> modClass = mod.getKey().getKey();
            ModName modName = mod.getKey().getValue().getKey().getKey();
            Set<ModName> modRequireAfter = new HashSet<>();
            Set<ModName> modRequireBefore = new HashSet<>();
            for (ModRequirements.Requirement requirement: mod.getValue().getRequirements()) {
                if (requirement.getModifier().getBasic() == ModRequirements.Requirement.Modifier.BasicModifier.BEFORE)
                    modRequireBefore.add(requirement.getModName());
                if (requirement.getModifier().getBasic() == ModRequirements.Requirement.Modifier.BasicModifier.AFTER)
                    modRequireAfter.add(requirement.getModName());
            }
            ModRequirements.Requirement.Modifier.BasicModifier modRequireAll = mod.getValue().getAll();
            simpleModContainer.add(Pair.makePair(Pair.makePair(modClass, modName), Pair.makePair(Pair.makePair(modRequireAfter, modRequireBefore), modRequireAll)));
        }
        modContainer.clear(); //GC
    }

    private static final Collection<ModName> searchedModsFlag = new HashSet<>();

    @SuppressWarnings("ConstantConditions")
    private static void sortFromSimpleModContainer() {
        for (Pair<Pair<Class<? extends ModImplement>, ModName>, Pair<Pair<Set<ModName>, Set<ModName>>, ModRequirements.Requirement.Modifier.BasicModifier>> mod: simpleModContainer) {
            addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                    mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
            searchedModsFlag.clear();
        }
        simpleModContainer.clear(); //GC
    }

    @SuppressWarnings("ConstantConditions")
    private static void addSortMod(Class<? extends ModImplement> modClass, ModName modName,
                                   Collection<? extends ModName> requireAfter, Collection<? extends ModName> requireBefore, ModRequirements.Requirement.Modifier.BasicModifier requireAll) {
        if (sortedMods.contains(modClass) || searchedModsFlag.contains(modName))
            return;
        searchedModsFlag.add(modName);
        for (ModName after: requireAfter)
            for (Pair<Pair<Class<? extends ModImplement>, ModName>, Pair<Pair<Set<ModName>, Set<ModName>>, ModRequirements.Requirement.Modifier.BasicModifier>> mod : simpleModContainer)
                if (after.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        for (ModName before : requireBefore)
            for (Pair<Pair<Class<? extends ModImplement>, ModName>, Pair<Pair<Set<ModName>, Set<ModName>>, ModRequirements.Requirement.Modifier.BasicModifier>> mod : simpleModContainer)
                if (before.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        int left = 0;
        int right = sortedMods.size();
        for (int i = 0; i < sortedMods.size(); ++i)
        {
            if (requireAfter.contains(ModImplement.getModNameFromClass(sortedMods.get(i))))
                if (left == 0)
                    left = i + 1;
                else
                    left = Math.min(left, i + 1);
            if (requireBefore.contains(ModImplement.getModNameFromClass(sortedMods.get(i))))
                if (right == sortedMods.size())
                    right = i;
                else
                    right = Math.max(right, i);
        }
        if (left > right)
            exceptions.add(new ModInformationException("Mod sort error! left=" + left + ", right=" + right + ", sortedMod=" + sortedMods + ".", modClass));
        else
            if (requireAll == ModRequirements.Requirement.Modifier.BasicModifier.BEFORE)
                sortedMods.add(left, modClass);
            else
                sortedMods.add(right, modClass);
    }

    public static @Nullable List<ModInformationException> sortMods() {
        buildModContainer();
        checkModContainer();
        if (!exceptions.isEmpty())
            return exceptions;
        toSimpleModContainer();
        sortFromSimpleModContainer();
        if (!exceptions.isEmpty())
            return exceptions;
        return null;
    }
}
