package Core.Addition;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.ElementUtil;
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

    public static @NotNull String crashClassInformation(@Nullable Class<?> aClass) {
        if (aClass == null)
            return " At a null class.";
        File file = getAllClassesWithJarFiles().get(aClass);
        if (file == null)
            return " At class '" + aClass.getName() + "'.";
        return " At class '" + aClass + "' in file '" + file.getAbsolutePath() + "'.";
    }
}
