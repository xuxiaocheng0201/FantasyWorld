package CraftWorld;

import HeadLibs.Configuration.HConfig;
import HeadLibs.Configuration.HConfigurations;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;

import java.io.File;

public class CraftWorld {
    public static final String CURRENT_VERSION = "0.0.0";
    public static final String RUNTIME_PATH = HStringHelper.merge("CraftWorld\\", CURRENT_VERSION, "\\");
    public static final String GLOBAL_CONFIGURATION_PATH = HStringHelper.merge(RUNTIME_PATH, "global.cfg");
    public static final String LOG_PATH;
    static {
        String log_path = HStringHelper.merge(RUNTIME_PATH, "log\\", HStringHelper.getDate("yyyy-MM-dd"), ".log");
        int i = 1;
        while ((new File(log_path)).exists())
            log_path = HStringHelper.merge(RUNTIME_PATH, "log\\", HStringHelper.getDate("yyyy-MM-dd"), "_", ++i, ".log");
        LOG_PATH = log_path;
    }

    public static boolean isClient = true;
    public static HConfigurations GLOBAL_CONFIGURATIONS;
    public static String CURRENT_LANGUAGE = "zh_cn";

    public static void main(String[] args) {
        Thread.currentThread().setName("CraftWorldMain");
        HLog.logger("Hello CraftWorld!");

        GLOBAL_CONFIGURATIONS = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
        HConfig language = GLOBAL_CONFIGURATIONS.getByName("language");

        if (language == null)
            language = new HConfig("language", LanguageI18N.get("THE LANGUAGE"), CURRENT_LANGUAGE);
        else
            language.setNote(LanguageI18N.get("THE LANGUAGE"));
        CURRENT_LANGUAGE = language.getValue();

        GLOBAL_CONFIGURATIONS.clear();
        GLOBAL_CONFIGURATIONS.add(language);
        GLOBAL_CONFIGURATIONS.write();

        for (String i: args) {
            if (i == null)
                continue;
            if (i.equals("runClient"))
                isClient = true;
            if (i.equals("runServer"))
                isClient = false;
        }
        HLog.saveLogs(LOG_PATH);
        System.gc();
        try {
            if (isClient) {
                Thread client = new Thread(new CraftWorldClient(), "CraftWorldClient");
                client.start();
                client.join();
            } else {
                Thread server = new Thread(new CraftWorldServer(), "CraftWorldServer");
                server.start();
                server.join();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        HLog.logger("Welcome to play again!");
        HLog.saveLogs(LOG_PATH);
    }
}
