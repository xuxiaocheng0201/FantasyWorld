package Core;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModLauncher;
import Core.Addition.ModManager;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.Exceptions.ModRequirementsException;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Configuration.HConfigElement;
import HeadLibs.Configuration.HConfigType;
import HeadLibs.Configuration.HConfigurations;
import HeadLibs.Configuration.HWrongConfigValueException;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Helper.HZipHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Version.HStringVersion;
import HeadLibs.Version.HVersionFormatException;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class Craftworld {
    public static final String CURRENT_VERSION_STRING = "0.0.0";
    public static final HStringVersion CURRENT_VERSION;
    public static final String RUNTIME_PATH = "Craftworld\\" + Craftworld.CURRENT_VERSION_STRING + "\\";
    public static final String GLOBAL_CONFIGURATION_PATH = RUNTIME_PATH + "global.cfg";
    public static final String ASSETS_PATH = RUNTIME_PATH + "assets\\";
    public static final String LOG_PATH;
    static {
        HStringVersion current_version;
        try {
            current_version = new HStringVersion(CURRENT_VERSION_STRING);
        } catch (HVersionFormatException exception) {
            HLog.logger(HLogLevel.CONFIGURATION, exception);
            HLog.logger(HLogLevel.FAULT, "Impossible exception!");
            current_version = null;
        }
        CURRENT_VERSION = current_version;
        String temp = RUNTIME_PATH + "log\\" + HStringHelper.getDate("yyyy-MM-dd");
        String log_path = temp + ".log";
        int i = 1;
        while ((new File(log_path)).exists())
            log_path = temp + "_" + ++i + ".log";
        LOG_PATH = log_path;
    }
    private static final String EXTRACT_TEMP_FILE = "extract_temp";

    public static void extractFiles(Class<? extends ModImplement> modClass, String sourcePath, String targetPath) throws IOException {
        File jarFilePath = modClass == null ? HClassFinder.thisCodePath : ModManager.getAllClassesWithJarFiles().get(modClass);
        if (jarFilePath == null)
            throw new IOException("Null jar file path for class '" + modClass + "'.s");
        File targetFilePath = new File(RUNTIME_PATH + targetPath).getAbsoluteFile();
        if (System.console() == null) {
            File runtimeFile = new File(Craftworld.RUNTIME_PATH).getAbsoluteFile();
            String srcResourcePath = runtimeFile.getParentFile().getParentFile().getParentFile().getPath() + "\\src\\main\\resources";
            HFileHelper.copyFiles(srcResourcePath + "\\" + sourcePath, targetFilePath.getPath(), Craftworld.OVERWRITE_FILES_WHEN_EXTRACTING);
        } else {
            HZipHelper.extractFilesFromJar(new JarFile(jarFilePath), sourcePath, Craftworld.EXTRACT_TEMP_FILE);
            HFileHelper.copyFiles(Craftworld.EXTRACT_TEMP_FILE, targetFilePath.getPath(), Craftworld.OVERWRITE_FILES_WHEN_EXTRACTING);
            HFileHelper.deleteDirectories(Craftworld.EXTRACT_TEMP_FILE);
        }
    }

    public static boolean isClient = true;

    public static HConfigurations GLOBAL_CONFIGURATIONS;
    public static String CURRENT_LANGUAGE = "zh_cn";
    public static boolean OVERWRITE_FILES_WHEN_EXTRACTING; // false
    @SuppressWarnings("MagicNumber")
    public static int GARBAGE_COLLECTOR_TIME_INTERVAL = 10000;
    public static int PORT = PortManager.getNextAvailablePort();
    /*
        Random random = new Random("Craftworld".hashCode());
        int r = random.nextInt();
        while (r < 1 || r > 65535)
            r = random.nextInt();
        HLog.logger(r);
     */
    private static HLog logger;

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("CraftworldMain");
        logger = new HLog("CraftworldMain");
        logger.log(HLogLevel.INFO, "Hello Craftworld!");
        HConfigurations canOverwrite;
        HConfigElement overwrite_when_extracting = null;
        try {
            canOverwrite = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
            canOverwrite.read();
            overwrite_when_extracting = canOverwrite.getByName("overwrite_when_extracting");
        } catch (IOException | HWrongConfigValueException | HElementRegisteredException | HElementNotRegisteredException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
        if (overwrite_when_extracting != null)
            OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());
        try {
            extractFiles(null, "assets\\Core", "assets\\Core");
        } catch (IOException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
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
                logger.log(HLogLevel.INFO, "Welcome to play again!");
                HLog.saveLogs(LOG_PATH);
            }
        });
        try {
            Thread gc = new Thread(new GCThread());
            gc.start();
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
            logger.log(HLogLevel.ERROR, exception);
        }
    }

    private static void GetConfigurations() throws IOException {
        try {
            GLOBAL_CONFIGURATIONS = new HConfigurations(GLOBAL_CONFIGURATION_PATH);
            GLOBAL_CONFIGURATIONS.read();
        } catch (IOException | HWrongConfigValueException | HElementRegisteredException | HElementNotRegisteredException exception) {
            logger.log(HLogLevel.CONFIGURATION, exception);
        }
        HConfigElement language = GLOBAL_CONFIGURATIONS.getByName("language");
        HConfigElement overwrite_when_extracting = GLOBAL_CONFIGURATIONS.getByName("overwrite_when_extracting");
        HConfigElement garbage_collector_time_interval = GLOBAL_CONFIGURATIONS.getByName("garbage_collector_time_interval");
        HConfigElement port = GLOBAL_CONFIGURATIONS.getByName("port");

        try {
            if (language == null)
                language = new HConfigElement("language", LanguageI18N.get("Core.configuration.language.name"), CURRENT_LANGUAGE);
            CURRENT_LANGUAGE = language.getValue();
            language.setNote(LanguageI18N.get("Core.configuration.language.name"));
        } catch (HWrongConfigValueException exception) {
            logger.log(HLogLevel.ERROR, exception);
            language = new HConfigElement();
        }

        try {
            if (overwrite_when_extracting == null)
                overwrite_when_extracting = new HConfigElement("overwrite_when_extracting", LanguageI18N.get("Core.configuration.overwrite_when_extracting.name"), HConfigType.BOOLEAN, OVERWRITE_FILES_WHEN_EXTRACTING ? "true" : "false");
            else
                overwrite_when_extracting.setNote(LanguageI18N.get("Core.configuration.overwrite_when_extracting.name"));
            OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());
        } catch (HWrongConfigValueException exception) {
            logger.log(HLogLevel.ERROR, exception);
            overwrite_when_extracting = new HConfigElement();
        }

        try {
            if (garbage_collector_time_interval == null)
                garbage_collector_time_interval = new HConfigElement("garbage_collector_time_interval", LanguageI18N.get("Core.configuration.garbage_collector_time_interval.name"), HConfigType.INT, String.valueOf(GARBAGE_COLLECTOR_TIME_INTERVAL));
            else
                garbage_collector_time_interval.setNote(LanguageI18N.get("Core.configuration.garbage_collector_time_interval.name"));
            if (Integer.parseInt(garbage_collector_time_interval.getValue()) < 10) {
                HLog.logger(HLogLevel.CONFIGURATION, "Garbage collector time interval too short: ", garbage_collector_time_interval.getValue(), ". Now use:", GARBAGE_COLLECTOR_TIME_INTERVAL);
                garbage_collector_time_interval.setValue(String.valueOf(GARBAGE_COLLECTOR_TIME_INTERVAL));
            }
            else
                GARBAGE_COLLECTOR_TIME_INTERVAL = Integer.parseInt(garbage_collector_time_interval.getValue());
        } catch (HWrongConfigValueException exception) {
            logger.log(HLogLevel.ERROR, exception);
            garbage_collector_time_interval = new HConfigElement();
        }

        try {
            if (port == null)
                port = new HConfigElement("port", LanguageI18N.get("Core.configuration.port.name"), HConfigType.INT, String.valueOf(PORT));
            else
                port.setNote(LanguageI18N.get("Core.configuration.port.name"));
            PORT = Integer.parseInt(port.getValue());
            if (PortManager.checkPortUnavailable(PORT)) {
                int availablePort = PortManager.getNextAvailablePort();
                logger.log(HLogLevel.CONFIGURATION, "Unavailable port: ", PORT, ". Now use:", availablePort);
                port.setValue(String.valueOf(availablePort));
                PORT = availablePort;
            }
        } catch (HWrongConfigValueException exception) {
            logger.log(HLogLevel.ERROR, exception);
            port = new HConfigElement();
        }

        GLOBAL_CONFIGURATIONS.clear();
        try {
            GLOBAL_CONFIGURATIONS.add(language);
            GLOBAL_CONFIGURATIONS.add(overwrite_when_extracting);
            GLOBAL_CONFIGURATIONS.add(garbage_collector_time_interval);
            GLOBAL_CONFIGURATIONS.add(port);
        } catch (HElementRegisteredException exception) {
            logger.log(HLogLevel.CONFIGURATION, exception);
            logger.log(HLogLevel.FAULT, "Impossible exception!");
        }
        try {
            GLOBAL_CONFIGURATIONS.write();
        } catch (IOException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
    }

    private static boolean loadMods() {
        HLog logger = new HLog(Thread.currentThread().getName());
        if (ModLauncher.loadModClasses()) {
            logger.log(HLogLevel.BUG, "Mod Loading Error in loading classes!");
            return false;
        }
        logger.log(HLogLevel.DEBUG, "Checked mods: ", ModManager.getModList());
        logger.log(HLogLevel.DEBUG, "Checked element pairs: ", ModManager.getElementPairList());
        if (ModLauncher.sortMods()) {
            logger.log(HLogLevel.ERROR, ModLauncher.getSorterExceptions());
            for (ModRequirementsException exception: ModLauncher.getSorterExceptions())
                HLog.logger(HLogLevel.ERROR, exception);
            return false;
        }
        logger.log(HLogLevel.FINEST, "Sorted Mod list: ", ModManager.getModList());
        ModLauncher.launchMods();
        return true;
    }

    @SuppressWarnings("unused")
    @EventSubscribe
    public static class DefaultEventBusRegister {
        private static final HLog logger = new HLog("DefaultEventBusReporter");
        @SuppressWarnings("MethodMayBeStatic")
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void defaultEventBusPostEvent(Object event) {
            logger.log(HLogLevel.FINE, "Default Event bus post event '", event, "'(at '", event.getClass().getName(), "').");
        }
    }

    @SuppressWarnings("unused")
    @EventSubscribe(eventBus = "*")
    public static class AllEventBusRegister {
        private static final HLog logger = new HLog("AllEventBusReporter");
        @SuppressWarnings("MethodMayBeStatic")
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void noSubscriberEvent(NoSubscriberEvent event) {
            logger.log(HLogLevel.FINE, "Event bus '", EventBusManager.getNameByEventBus(event.eventBus), "' post event '", event.originalEvent, "'(at '", event.originalEvent.getClass().getName(), "'), but no subscriber.");
        }
    }
}
