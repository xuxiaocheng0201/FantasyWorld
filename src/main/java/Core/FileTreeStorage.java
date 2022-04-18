package Core;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.io.File;
import java.io.IOException;

public class FileTreeStorage {
    public static final String RUNTIME_PATH = (new File("Craftworld\\" + Craftworld.CURRENT_VERSION_STRING)).getAbsolutePath() + "\\";
    public static final String GLOBAL_CONFIGURATION_FILE = RUNTIME_PATH + "global.cfg";
    public static final String ASSETS_PATH = RUNTIME_PATH + "assets\\";
    public static final String LOG_PATH = RUNTIME_PATH + "logs\\";
    public static final String LOG_FILE;
    static {
        String temp = LOG_PATH + HStringHelper.getDate("yyyy-MM-dd");
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
    public static final String MOD_PATH = RUNTIME_PATH + "mods\\";

    static {
        try {
            HFileHelper.createNewDirectory(ASSETS_PATH);
            HFileHelper.createNewDirectory(LOG_PATH);
            HFileHelper.createNewDirectory(MOD_PATH);
        } catch (IOException exception) {
            HLog.logger(HLogLevel.FAULT, exception);
        }
    }
}
