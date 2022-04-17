package Core.Addition;

import Core.Addition.Implement.ElementImplement;
import Core.Addition.Implement.ElementUtil;
import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ModManager {
    public static Map<Class<?>, File> getAllClassesWithJarFiles() {
        return ModClassesLoader.getAllClassesWithJarFiles();
    }

    public static List<Class<? extends ModImplement>> getModList() {
        if (ModClassesSorter.getSortedMods().isEmpty())
            return ModClassesLoader.getModList();
        return ModClassesSorter.getSortedMods();
    }

    public static void deleteMod(String modName) {
        if (modName == null)
            return;
        getModList().removeIf(modClass -> modName.equals(HStringHelper.notNullStrip(modClass.getAnnotation(NewMod.class).name())));
    }

    public static Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return ModClassesLoader.getElementPairList();
    }

    public static @NotNull String crashClassInformation(@Nullable Class<? extends ModImplement> modClass) {
        if (modClass == null)
            return " At a null class.";
        File file = getAllClassesWithJarFiles().get(modClass);
        if (file == null)
            return " At class '" + modClass + "'.";
        return " At class '" + modClass + "' in file '" + file.getAbsolutePath() + "'.";
    }
}
