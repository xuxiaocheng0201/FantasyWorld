package Core.Mod;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.ElementUtil;
import Core.Mod.New.ModImplement;
import Core.Mod.New.NewMod;
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
        getModList().removeIf(modClass -> modName.equals(HStringHelper.notNullOrEmpty(modClass.getAnnotation(NewMod.class).name().strip())));
    }

    public static Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return ModElementsRegisterer.getElementPairList();
    }
}
