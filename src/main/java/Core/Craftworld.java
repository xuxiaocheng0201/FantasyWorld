package Core;

import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.Mod.ModManager;
import Core.Mod.New.ModImplement;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Configuration.HConfig;
import HeadLibs.Configuration.HConfigurations;
import HeadLibs.Configuration.HEConfigType;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class Craftworld {
    public static final String CURRENT_VERSION = "0.0.0";
    public static final String RUNTIME_PATH = HStringHelper.merge("Craftworld\\", CURRENT_VERSION, "\\");
    public static final String GLOBAL_CONFIGURATION_PATH = HStringHelper.merge(RUNTIME_PATH, "global.cfg");
    public static final String ASSETS_PATH = HStringHelper.merge(RUNTIME_PATH, "assets\\");
    public static final String LOG_PATH;
    static {
        String log_path = HStringHelper.merge(RUNTIME_PATH, "log\\", HStringHelper.getDate("yyyy-MM-dd"), ".log");
        int i = 1;
        while ((new File(log_path)).exists())
            log_path = HStringHelper.merge(RUNTIME_PATH, "log\\", HStringHelper.getDate("yyyy-MM-dd"), "_", ++i, ".log");
        LOG_PATH = log_path;
    }
    private static final String EXTRACT_TEMP_FILE = "extract_temp";

    public static boolean isClient = true;

    public static HConfigurations GLOBAL_CONFIGURATIONS;
    public static String CURRENT_LANGUAGE = "zh_cn";
    public static boolean OVERWRITE_FILES_WHEN_EXTRACTING = false;

    public static void main(String[] args)  {
        Thread.currentThread().setName("CraftworldMain");
        HLog.logger(HELogLevel.INFO, "Hello Craftworld!");
        try {
            File runtimeFile = new File(Craftworld.RUNTIME_PATH).getAbsoluteFile();
            String targetAssetsPath = HStringHelper.merge(runtimeFile.getPath(), "\\assets");
            HConfigurations canOverwrite = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
            HConfig overwrite_when_extracting = canOverwrite.getByName("overwrite_when_extracting");
            if (overwrite_when_extracting != null)
                OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());
            if (System.console() == null) {
                String srcResourcePath = HStringHelper.merge(runtimeFile.getParentFile().getParentFile().getParentFile().getPath(), "\\src\\main\\resources");
                HFileHelper.copyFiles(HStringHelper.merge(srcResourcePath, "\\assets"), targetAssetsPath, Craftworld.OVERWRITE_FILES_WHEN_EXTRACTING);
            } else {
                HFileHelper.extractFilesFromJar(new JarFile(HClassFinder.thisCodePath), "assets", Craftworld.EXTRACT_TEMP_FILE);
                HFileHelper.copyFiles(Craftworld.EXTRACT_TEMP_FILE, targetAssetsPath, Craftworld.OVERWRITE_FILES_WHEN_EXTRACTING);
                HFileHelper.deleteDirectories(Craftworld.EXTRACT_TEMP_FILE);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        for (String i: args) {
            if ("runClient".equals(i))
                isClient = true;
            if ("runServer".equals(i))
                isClient = false;
        }
        GetConfigurations();
        HLog.saveLogs(LOG_PATH);
        Runtime.getRuntime().addShutdownHook(new Thread(Thread.currentThread().getName()) {
            @Override
            public void run() {
                HLog.logger(HELogLevel.INFO, "Welcome to play again!");
                HLog.saveLogs(LOG_PATH);
            }
        });
        System.gc();
        try {
            if (isClient) {
                Thread client = new Thread(new CraftworldClient());
                client.start();
                client.join();
            } else {
                Thread server = new Thread(new CraftworldServer());
                server.start();
                server.join();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private static void GetConfigurations() {
        GLOBAL_CONFIGURATIONS = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
        HConfig language = GLOBAL_CONFIGURATIONS.getByName("language");
        HConfig overwrite_when_extracting = GLOBAL_CONFIGURATIONS.getByName("overwrite_when_extracting");

        if (language == null)
            language = new HConfig("language", LanguageI18N.get("Core.configuration.language.name"), CURRENT_LANGUAGE);
        else
            language.setNote(LanguageI18N.get("Core.configuration.language.name"));
        CURRENT_LANGUAGE = language.getValue();
        if (overwrite_when_extracting == null)
            overwrite_when_extracting = new HConfig("overwrite_when_extracting", LanguageI18N.get("Core.configuration.overwrite_when_extracting.name"), HEConfigType.BOOLEAN, OVERWRITE_FILES_WHEN_EXTRACTING ? "true" : "false");
        else
            overwrite_when_extracting.setNote(LanguageI18N.get("Core.configuration.overwrite_when_extracting.name"));
        OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());

        GLOBAL_CONFIGURATIONS.clear();
        GLOBAL_CONFIGURATIONS.add(language);
        GLOBAL_CONFIGURATIONS.add(overwrite_when_extracting);
        GLOBAL_CONFIGURATIONS.write();
    }

    public static void extractFiles(Class<? extends ModImplement> modClass, String sourcePath, String targetPath) {
        File jarFilePath = modClass == null ? HClassFinder.thisCodePath : ModManager.getAllClassesWithJarFiles().get(modClass);
        File targetFilePath = new File(HStringHelper.merge(RUNTIME_PATH, targetPath)).getAbsoluteFile();
        try {
            HFileHelper.extractFilesFromJar(new JarFile(jarFilePath), sourcePath, Craftworld.EXTRACT_TEMP_FILE);
            HFileHelper.copyFiles(Craftworld.EXTRACT_TEMP_FILE, targetFilePath.getPath(), Craftworld.OVERWRITE_FILES_WHEN_EXTRACTING);
            HFileHelper.deleteDirectories(Craftworld.EXTRACT_TEMP_FILE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @EventSubscribe(eventBus = "default")
    public static class DefaultEventBusRegister {
        @Subscribe
        public void defaultEventBusPostEvent(Object event) {
            HLog.logger(HELogLevel.FINE, "Default Event bus post event '", event, "'(at '", event.getClass().getName(), "').");
        }
    }

    @SuppressWarnings("unused")
    @EventSubscribe(eventBus = "*")
    public static class AllEventBusRegister {
        @Subscribe
        public void noSubscriberEvent(NoSubscriberEvent event) {
            HLog.logger(HELogLevel.FINE, "Event bus '", EventBusManager.getNameByEventBus(event.eventBus), "' post event '", event.originalEvent, "'(at '", event.originalEvent.getClass().getName(), "'), but no subscriber.");
        }
    }
}
