package Core;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import HeadLibs.Configuration.HConfigElement;
import HeadLibs.Configuration.HConfigType;
import HeadLibs.Configuration.HConfigurations;
import HeadLibs.Configuration.HWrongConfigValueException;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Version.HStringVersion;
import HeadLibs.Version.HVersionFormatException;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class Craftworld implements ModImplement {
    public static final String CURRENT_VERSION_STRING = "0.0.1";
    public static final HStringVersion CURRENT_VERSION;
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
    }

    public static boolean isClient = true;

    public static HConfigurations GLOBAL_CONFIGURATIONS;
    public static String CURRENT_LANGUAGE = "zh_cn";
    public static boolean OVERWRITE_FILES_WHEN_EXTRACTING;
    @SuppressWarnings("MagicNumber")
    public static int GARBAGE_COLLECTOR_TIME_INTERVAL = 10000;
    public static int PORT = PortManager.getNextAvailablePortRandom("localhost");
    /*
        Random random = new Random("Craftworld".hashCode());
        int r = random.nextInt();
        while (r < 1 || r > 65535)
            r = random.nextInt();
        HLog.logger(r);
     */
    private static final HLog logger = new HLog("CraftworldMain");

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("CraftworldMain");
        logger.log(HLogLevel.INFO, "Hello Craftworld!");
        HConfigurations canOverwrite;
        HConfigElement overwrite_when_extracting = null;
        try {
            canOverwrite = new HConfigurations(FileTreeStorage.GLOBAL_CONFIGURATION_FILE);
            canOverwrite.read();
            overwrite_when_extracting = canOverwrite.getByName("overwrite_when_extracting");
        } catch (IOException | HElementRegisteredException | HElementNotRegisteredException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
        if (overwrite_when_extracting != null)
            OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());
        for (String arg: args) {
            if ("runClient".equals(arg))
                isClient = true;
            if ("runServer".equals(arg))
                isClient = false;
        }
        try {
            FileTreeStorage.extractFiles(null, "assets\\Core", "assets\\Core");
        } catch (IOException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
        GetConfigurations();
        HLog.saveLogs(FileTreeStorage.LOG_FILE);
        Runtime.getRuntime().addShutdownHook(new Thread(Thread.currentThread().getName()) {
            @Override
            public void run() {
                logger.log(HLogLevel.INFO, "Welcome to play again!");
                HLog.saveLogs(FileTreeStorage.LOG_FILE);
            }
        });
        try {
            Thread gc = new Thread(new GCThread());
            gc.start();
            if (ModManager.addModFilePath((new File(FileTreeStorage.MOD_PATH)).getAbsoluteFile())) {
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
            GLOBAL_CONFIGURATIONS = new HConfigurations(FileTreeStorage.GLOBAL_CONFIGURATION_FILE);
            GLOBAL_CONFIGURATIONS.read();
        } catch (IOException | HElementRegisteredException | HElementNotRegisteredException exception) {
            logger.log(HLogLevel.CONFIGURATION, exception);
        }
        HConfigElement language = GLOBAL_CONFIGURATIONS.getByName("language");
        HConfigElement overwrite_when_extracting = GLOBAL_CONFIGURATIONS.getByName("overwrite_when_extracting");
        HConfigElement garbage_collector_time_interval = GLOBAL_CONFIGURATIONS.getByName("garbage_collector_time_interval");
        HConfigElement port = GLOBAL_CONFIGURATIONS.getByName("port");

        try {
            if (language == null)
                language = new HConfigElement("language", LanguageI18N.get(Craftworld.class, "Core.configuration.language.name"), CURRENT_LANGUAGE);
            CURRENT_LANGUAGE = language.getValue();
            language.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.language.name"));
        } catch (HWrongConfigValueException exception) {
            logger.log(HLogLevel.ERROR, exception);
            language = new HConfigElement();
        }

        try {
            if (overwrite_when_extracting == null)
                overwrite_when_extracting = new HConfigElement("overwrite_when_extracting", LanguageI18N.get(Craftworld.class, "Core.configuration.overwrite_when_extracting.name"), HConfigType.BOOLEAN, OVERWRITE_FILES_WHEN_EXTRACTING ? "true" : "false");
            else
                overwrite_when_extracting.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.overwrite_when_extracting.name"));
            OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());
        } catch (HWrongConfigValueException exception) {
            logger.log(HLogLevel.ERROR, exception);
            overwrite_when_extracting = new HConfigElement();
        }

        try {
            if (garbage_collector_time_interval == null)
                garbage_collector_time_interval = new HConfigElement("garbage_collector_time_interval", LanguageI18N.get(Craftworld.class, "Core.configuration.garbage_collector_time_interval.name"), HConfigType.INT, String.valueOf(GARBAGE_COLLECTOR_TIME_INTERVAL));
            else
                garbage_collector_time_interval.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.garbage_collector_time_interval.name"));
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
                port = new HConfigElement("port", LanguageI18N.get(Craftworld.class, "Core.configuration.port.name"), HConfigType.INT, String.valueOf(PORT));
            else
                port.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.port.name"));
            PORT = Integer.parseInt(port.getValue());
            if (PortManager.portIsAvailable("localhost", PORT)) {
                int availablePort = PortManager.getNextAvailablePortRandom("localhost");
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

    @Override
    public @NotNull String getLanguagePath(@Nullable String lang) {
        return FileTreeStorage.ASSETS_PATH + "Core\\lang\\" + lang + ".lang";
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
