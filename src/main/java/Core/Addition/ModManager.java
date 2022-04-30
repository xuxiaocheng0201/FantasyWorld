package Core.Addition;

import Core.Addition.Element.BasicInformation.ElementName;
import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.ElementUtil;
import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.ModImplement;
import HeadLibs.DataStructures.Pair;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Mods manager.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModManager {
    /**
     * Get all loaded classes.
     * @return loaded classes
     */
    public static @NotNull Set<Class<?>> getAllClasses() {
        return ModClassesLoader.getAllClasses();
    }

    /**
     * Get all loaded classes with file.
     * @return loaded classes with file
     */
    public static @NotNull Map<Class<?>, File> getAllClassesWithJarFiles() {
        return ModClassesLoader.getAllClassesWithJarFiles();
    }

    /**
     * Get all mods classes.
     * @return all mods classes
     */
    public static @NotNull List<Class<? extends ModImplement>> getModList() {
        if (ModClassesSorter.getSortedMods().isEmpty())
            return ModClassesLoader.getModList();
        return ModClassesSorter.getSortedMods();
    }

    /**
     * Add mod files (*.jar) or directory path.
     * @param modsFile mod files (*.jar) or directory path
     * @return true - success. false - failure.
     */
    public static boolean addModFilePath(@Nullable File modsFile) {
        return ModClassesLoader.addMod(modsFile);
    }

    /**
     * Delete mod by its name.
     * @param modName mod name
     */
    public static void deleteMod(@Nullable ModName modName) {
        if (modName == null)
            return;
        getModList().removeIf(modClass -> modName.equals(ModImplement.getModNameFromClass(modClass)));
    }

    /**
     * Get all mod element pairs classes.
     * @return all mod element pairs classes
     */
    public static @NotNull HMapRegisterer<ElementName, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return ModClassesLoader.getElementPairList();
    }

    /**
     * Get crash class information.
     * @param aClass the class
     * @return string information
     */
    public static @NotNull String crashClassInformation(@Nullable Class<?> aClass) {
        if (aClass == null)
            return " At a null class.";
        File file = getAllClassesWithJarFiles().get(aClass);
        if (file == null)
            return " At class '" + aClass.getName() + "'.";
        return " At class '" + aClass + "' in file '" + file.getAbsolutePath() + "'.";
    }
}
