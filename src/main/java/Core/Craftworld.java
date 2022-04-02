package Core;

import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.Exceptions.ModRequirementsException;
import Core.Mod.ModLauncher;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
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
        log_path = HStringHelper.merge(LOG_PATH, ".err");
        try {
            HFileHelper.createNewFile(log_path);
            System.setErr(new PrintStream(log_path));
        } catch (FileNotFoundException exception) {
            HLog.logger(HELogLevel.FAULT, exception);
        }
    }
    private static final String EXTRACT_TEMP_FILE = "extract_temp";

    public static void extractFiles(Class<? extends ModImplement> modClass, String sourcePath, String targetPath) {
        File jarFilePath = modClass == null ? HClassFinder.thisCodePath : ModManager.getAllClassesWithJarFiles().get(modClass);
        if (jarFilePath == null) //Unreachable
            jarFilePath = HClassFinder.thisCodePath;
        File targetFilePath = new File(HStringHelper.merge(RUNTIME_PATH, targetPath)).getAbsoluteFile();
        try {
            if (System.console() == null) {
                File runtimeFile = new File(Craftworld.RUNTIME_PATH).getAbsoluteFile();
                String srcResourcePath = HStringHelper.merge(runtimeFile.getParentFile().getParentFile().getParentFile().getPath(), "\\src\\main\\resources");
                HFileHelper.copyFiles(HStringHelper.merge(srcResourcePath, "\\", sourcePath), targetFilePath.getPath(), Craftworld.OVERWRITE_FILES_WHEN_EXTRACTING);
            } else {
                HFileHelper.extractFilesFromJar(new JarFile(jarFilePath), sourcePath, Craftworld.EXTRACT_TEMP_FILE);
                HFileHelper.copyFiles(Craftworld.EXTRACT_TEMP_FILE, targetFilePath.getPath(), Craftworld.OVERWRITE_FILES_WHEN_EXTRACTING);
                HFileHelper.deleteDirectories(Craftworld.EXTRACT_TEMP_FILE);
            }
        } catch (IOException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    public static boolean isClient = true;

    public static HConfigurations GLOBAL_CONFIGURATIONS;
    public static String CURRENT_LANGUAGE = "zh_cn";
    public static boolean OVERWRITE_FILES_WHEN_EXTRACTING = false;
    public static int GARBAGE_COLLECTOR_TIME_INTERVAL = 10000;
    public static int PORT = PortManager.getNextAvailablePort();
    /*
        Random random = new Random("Craftworld".hashCode());
        int r = random.nextInt();
        while (r < 1 || r > 65535)
            r = random.nextInt();
        HLog.logger(r);
     */

    public static void main(String[] args) {
        Thread.currentThread().setName("CraftworldMain");
        HLog.logger(HELogLevel.INFO, "Hello Craftworld!");
        HConfigurations canOverwrite = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
        HConfig overwrite_when_extracting = canOverwrite.getByName("overwrite_when_extracting");
        if (overwrite_when_extracting != null)
            OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());
        extractFiles(null, "assets\\Core", "assets\\Core");
        for (String arg: args) {
            if ("runClient".equals(arg))
                isClient = true;
            if ("runServer".equals(arg))
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
        try {
            Thread gc = new Thread(new GCThread());
            if (loadMods()) {
                ModLauncher.gc();
                Thread main;
                if (isClient)
                    main = new Thread(new CraftworldClient());
                else
                    main = new Thread(new CraftworldServer());
                main.start();
                main.join();
            }
            gc.interrupt();
        } catch (InterruptedException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private static void GetConfigurations() {
        GLOBAL_CONFIGURATIONS = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
        HConfig language = GLOBAL_CONFIGURATIONS.getByName("language");
        HConfig overwrite_when_extracting = GLOBAL_CONFIGURATIONS.getByName("overwrite_when_extracting");
        HConfig garbage_collector_time_interval = GLOBAL_CONFIGURATIONS.getByName("garbage_collector_time_interval");
        HConfig port = GLOBAL_CONFIGURATIONS.getByName("port");

        if (language == null)
            language = new HConfig("language", LanguageI18N.get("Core.configuration.language.name"), CURRENT_LANGUAGE);
        else
            language.setNote(LanguageI18N.get("Core.configuration.language.name"));
        CURRENT_LANGUAGE = language.getValue();
        language.setNote(LanguageI18N.get("Core.configuration.language.name"));

        if (overwrite_when_extracting == null)
            overwrite_when_extracting = new HConfig("overwrite_when_extracting", LanguageI18N.get("Core.configuration.overwrite_when_extracting.name"), HEConfigType.BOOLEAN, OVERWRITE_FILES_WHEN_EXTRACTING ? "true" : "false");
        else
            overwrite_when_extracting.setNote(LanguageI18N.get("Core.configuration.overwrite_when_extracting.name"));
        OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());

        if (garbage_collector_time_interval == null)
            garbage_collector_time_interval = new HConfig("garbage_collector_time_interval", LanguageI18N.get("Core.configuration.garbage_collector_time_interval.name"), HEConfigType.INT, String.valueOf(GARBAGE_COLLECTOR_TIME_INTERVAL));
        else
            garbage_collector_time_interval.setNote(LanguageI18N.get("Core.configuration.garbage_collector_time_interval.name"));
        if (Integer.parseInt(garbage_collector_time_interval.getValue()) < 10) {
            HLog.logger(HELogLevel.CONFIGURATION, "Garbage collector time interval too short: ", garbage_collector_time_interval.getValue(), ". Now use:", GARBAGE_COLLECTOR_TIME_INTERVAL);
            garbage_collector_time_interval.setValue(String.valueOf(GARBAGE_COLLECTOR_TIME_INTERVAL));
        }
        else
            GARBAGE_COLLECTOR_TIME_INTERVAL = Integer.parseInt(garbage_collector_time_interval.getValue());

        if (port == null)
            port = new HConfig("port", LanguageI18N.get("Core.configuration.port.name"), HEConfigType.INT, String.valueOf(PORT));
        else
            port.setNote(LanguageI18N.get("Core.configuration.port.name"));
        PORT = Integer.parseInt(port.getValue());
        if (PortManager.checkPortUnavailable(PORT)) {
            int availablePort = PortManager.getNextAvailablePort();
            HLog.logger(HELogLevel.CONFIGURATION, "Unavailable port: ", PORT, ". Now use:", availablePort);
            port.setValue(String.valueOf(availablePort));
            PORT = availablePort;
        }

        GLOBAL_CONFIGURATIONS.clear();
        GLOBAL_CONFIGURATIONS.add(language);
        GLOBAL_CONFIGURATIONS.add(overwrite_when_extracting);
        GLOBAL_CONFIGURATIONS.add(garbage_collector_time_interval);
        GLOBAL_CONFIGURATIONS.add(port);
        GLOBAL_CONFIGURATIONS.write();
    }

    private static boolean loadMods() {
        HLog logger = new HLog(Thread.currentThread().getName());
        if (ModLauncher.loadModClasses()) {
            logger.log(HELogLevel.BUG, "Mod Loading Error in loading classes!");
            return false;
        }
        logger.log(HELogLevel.DEBUG, "Checked mods: ", ModManager.getModList());
        logger.log(HELogLevel.DEBUG, "Checked element pairs: ", ModManager.getElementPairList());
        if (ModLauncher.sortMods(logger)) {
            logger.log(HELogLevel.ERROR, ModLauncher.getSorterExceptions());
            for (ModRequirementsException exception: ModLauncher.getSorterExceptions())
                HLog.logger(HELogLevel.ERROR, exception);
            return false;
        }
        logger.log(HELogLevel.FINEST, "Sorted Mod list: ", ModManager.getModList());
        ModLauncher.launchMods();
        return true;
    }

    @SuppressWarnings("unused")
    @EventSubscribe
    public static class DefaultEventBusRegister {
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void defaultEventBusPostEvent(Object event) {
            HLog.logger(HELogLevel.FINE, "Default Event bus post event '", event, "'(at '", event.getClass().getName(), "').");
        }
    }

    @SuppressWarnings("unused")
    @EventSubscribe(eventBus = "*")
    public static class AllEventBusRegister {
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void noSubscriberEvent(NoSubscriberEvent event) {
            HLog.logger(HELogLevel.FINE, "Event bus '", EventBusManager.getNameByEventBus(event.eventBus), "' post event '", event.originalEvent, "'(at '", event.originalEvent.getClass().getName(), "'), but no subscriber.");
        }
    }
}
