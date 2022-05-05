package Core;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HTimeHelper;
import HeadLibs.Helper.HZipHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.io.File;
import java.io.IOException;

public class FileTreeStorage {
    public static final String RUNTIME_PATH = (new File("Craftworld\\" + Craftworld.CURRENT_VERSION_STRING)).getAbsolutePath() + "\\";
    public static final String LIBRARIES_PATH = (new File("Craftworld\\libraries")).getAbsolutePath() + "\\";
    public static final String DLLS_PATH = (new File("Craftworld\\dll")).getAbsolutePath() + "\\";
    public static final String ASSETS_PATH = RUNTIME_PATH + "assets\\";
    public static final String LOG_PATH = RUNTIME_PATH + "logs\\";
    public static final String MOD_PATH = RUNTIME_PATH + "mods\\";
    public static final String GLOBAL_CONFIGURATION_FILE = RUNTIME_PATH + "global.cfg";
    public static final String LOG_FILE; static {
        String temp = LOG_PATH + HTimeHelper.getDate("yyyy-MM-dd");
        String log_path = temp + ".log";
        int i = 1;
        while (true) {
            File file = new File(log_path);
            if (!file.exists())
                break;
            log_path = temp + "_" + (++i) + ".log";
        }
        LOG_FILE = log_path;
    }
    static {
        try {
            HFileHelper.createNewDirectory(ASSETS_PATH);
            HFileHelper.createNewDirectory(LOG_PATH);
            HFileHelper.createNewDirectory(MOD_PATH);
        } catch (IOException exception) {
            HLog.logger(HLogLevel.FAULT, exception);
        }
    }

    private static final String EXTRACT_TEMP_FILE = RUNTIME_PATH + "extract_temp";
    public synchronized static void extractFiles(Class<? extends ModImplement> modClass, String sourcePath, String targetPath) throws IOException {
        File jarFilePath = modClass == null ? HClassFinder.thisCodePath : ModManager.getAllClassesWithJarFiles().get(modClass);
        if (jarFilePath == null)
            throw new IOException("Null jar file path for class '" + modClass + "'.s");
        File targetFilePath = new File(RUNTIME_PATH + targetPath).getAbsoluteFile();
        if (System.console() == null) {
            File runtimeFile = new File(RUNTIME_PATH).getAbsoluteFile();
            String srcResourcePath = runtimeFile.getParentFile().getParentFile().getParentFile().getPath() + "\\src\\main\\resources";
            HFileHelper.copyFiles(srcResourcePath + "\\" + sourcePath, targetFilePath.getPath(), GlobalConfigurations.OVERWRITE_FILES_WHEN_EXTRACTING);
        } else {
            HZipHelper.extractFilesFromJar(jarFilePath.getPath(), sourcePath, EXTRACT_TEMP_FILE);
            HFileHelper.copyFiles(EXTRACT_TEMP_FILE, targetFilePath.getPath(), GlobalConfigurations.OVERWRITE_FILES_WHEN_EXTRACTING);
            if (!HFileHelper.deleteDirectories(EXTRACT_TEMP_FILE))
                throw new IOException("Failed to delete directory '" + EXTRACT_TEMP_FILE + "'.");
        }
    }
}
