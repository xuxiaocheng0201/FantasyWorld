package CraftWorld;

import HeadLibs.Configuration.HConfig;
import HeadLibs.Configuration.HConfigurations;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;

import java.io.IOException;

public class CraftWorld {
    public static final String CURRENT_VERSION = "0.0.0";
    public static final String RUNTIME_PATH = HStringHelper.merge("CraftWorld\\", CURRENT_VERSION, "\\");
    public static final String LOG_PATH = HStringHelper.merge(RUNTIME_PATH, "log\\", HStringHelper.getDate("yyyy-MM-dd"), ".log");
    public static String CURRENT_LANGUAGE = "zh_cn";

    public static boolean isClient = true;
    public static HConfigurations GLOBAL_CONFIGURATIONS;

    public volatile static boolean isRunning = false;

    public static void main(String[] args) {
        Thread.currentThread().setName("CraftWorldMain");
        HLog.logger("Hello World!");
        try {
            GLOBAL_CONFIGURATIONS = new HConfigurations(HStringHelper.merge(RUNTIME_PATH, "global.cfg"));
            HConfig cfg_version = GLOBAL_CONFIGURATIONS.getByName("cfg_version");
            HConfig language = GLOBAL_CONFIGURATIONS.getByName("language");

            if (cfg_version == null)
                cfg_version = new HConfig("cfg_version", LanguageI18N.get("DON'T CHANGE IT EASILY!"), HConfigurations.CURRENT_VERSION);
            else {
                cfg_version.setNote(LanguageI18N.get("DON'T CHANGE IT EASILY!"));
                GLOBAL_CONFIGURATIONS.deleteByName("cfg_version");
            }
            if (language == null)
                language = new HConfig("language", LanguageI18N.get("THE LANGUAGE."), CURRENT_LANGUAGE);
            else {
                language.setNote(LanguageI18N.get("THE LANGUAGE."));
                GLOBAL_CONFIGURATIONS.deleteByName("language");
            }

            GLOBAL_CONFIGURATIONS.add(cfg_version);
            GLOBAL_CONFIGURATIONS.add(language);
            GLOBAL_CONFIGURATIONS.write();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

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
        Thread client = new Thread(new CraftWorldClient(), "CraftWorldClient");
        Thread server = new Thread(new CraftWorldServer(), "CraftWorldServer");
        server.start();
        if (isClient)
            client.start();

        try {
            server.join();
            if (isClient)
                client.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
