package Mod;

import Core.CraftWorld;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.File;

public class ModLoader {
    public static final String MODS_PATH = HStringHelper.merge(CraftWorld.RUNTIME_PATH, "mods");

    public static void loadMods() {
        File modPath = (new File(MODS_PATH)).getAbsoluteFile();
        if (modPath.exists() && !modPath.isDirectory()) {
            HLog.logger(HELogLevel.ERROR, "Mods path is a file! MODS_PATH='", MODS_PATH, "'.");
            return;
        }
        if (!modPath.exists() && !modPath.mkdirs()) {
            HLog.logger(HELogLevel.ERROR, "Creating MODS_PATH directory failed. MODS_PATH='", MODS_PATH, "'.");
            return;
        }
        HClassFinder modsFinder = new HClassFinder();
        modsFinder.getJarFiles().clear();
        modsFinder.addJarFilesInDirectory(modPath);
        modsFinder.addAnnotationClass(Mod.class);
        modsFinder.startFind();
        HLog.logger(modsFinder.getClassList());
        //TODO
    }
}
