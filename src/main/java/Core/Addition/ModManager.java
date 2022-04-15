package Core.Addition;

import Core.Addition.Implement.ElementImplement;
import Core.Addition.Implement.ElementUtil;
import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;

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

    public static String crashClassInformation(Class<? extends ModImplement> modClass) {
        return " At class '" + modClass.toString() + "' in file '" + getAllClassesWithJarFiles().get(modClass).getAbsolutePath() + "'.";
    }
}
