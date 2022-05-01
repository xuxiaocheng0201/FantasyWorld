package Core;

import HeadLibs.Configuration.HConfigElement;
import HeadLibs.Configuration.HConfigType;
import HeadLibs.Configuration.HConfigurations;
import HeadLibs.Configuration.HWrongConfigValueException;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.IOException;

@SuppressWarnings({"MagicNumber", "RedundantFieldInitialization"})
public class GlobalConfigurations {
    public static HConfigurations GLOBAL_CONFIGURATIONS;

    public static String CURRENT_LANGUAGE = "zh_cn";
    public static boolean OVERWRITE_FILES_WHEN_EXTRACTING = false;
    public static int GARBAGE_COLLECTOR_TIME_INTERVAL = 10000;
    public static String HOST = "127.0.0.1";
    public static int PORT = 61147; // PortManager.getNextAvailablePortRandom(HOST, false);
    public static int MAX_PLAYER = 10;
    public static boolean JOIN_THE_USER_EXPERIENCE_IMPROVEMENT_PROGRAM = false;

    public static void GetConfigurations() throws IOException {
        HLog logger = new HLog("GetGlobalConfigurations", Thread.currentThread().getName());
        logger.log(HLogLevel.INFO, "Read global configurations in '", FileTreeStorage.GLOBAL_CONFIGURATION_FILE + "'.");
        GLOBAL_CONFIGURATIONS = new HConfigurations(FileTreeStorage.GLOBAL_CONFIGURATION_FILE);
        try {
            GLOBAL_CONFIGURATIONS.read();
        } catch (HElementRegisteredException | HElementNotRegisteredException exception) {
            logger.log(HLogLevel.CONFIGURATION, exception);
        }

        HConfigElement language = GLOBAL_CONFIGURATIONS.getByName("language");
        if (language == null)
            try {
                language = new HConfigElement("language", CURRENT_LANGUAGE);
            } catch (HWrongConfigValueException exception) {
                logger.log(HLogLevel.ERROR, exception);
                language = new HConfigElement("language");
            }
        else
            CURRENT_LANGUAGE = language.getValue();
        language.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.language.name"));

        HConfigElement overwrite_when_extracting = GLOBAL_CONFIGURATIONS.getByName("overwrite_when_extracting");
        if (overwrite_when_extracting == null)
            try {
                overwrite_when_extracting = new HConfigElement("overwrite_when_extracting", HConfigType.BOOLEAN, OVERWRITE_FILES_WHEN_EXTRACTING ? "true" : "false");
            } catch (HWrongConfigValueException exception) {
                logger.log(HLogLevel.ERROR, exception);
                overwrite_when_extracting = new HConfigElement("overwrite_when_extracting");
            }
        else
            OVERWRITE_FILES_WHEN_EXTRACTING = Boolean.parseBoolean(overwrite_when_extracting.getValue());
        overwrite_when_extracting.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.overwrite_when_extracting.name"));

        HConfigElement garbage_collector_time_interval = GLOBAL_CONFIGURATIONS.getByName("garbage_collector_time_interval");
        if (garbage_collector_time_interval == null)
            try {
                garbage_collector_time_interval = new HConfigElement("garbage_collector_time_interval", HConfigType.INT, String.valueOf(GARBAGE_COLLECTOR_TIME_INTERVAL));
            } catch (HWrongConfigValueException exception) {
                logger.log(HLogLevel.ERROR, exception);
                garbage_collector_time_interval = new HConfigElement("garbage_collector_time_interval");
            }
        else {
            int time = Integer.parseInt(garbage_collector_time_interval.getValue());
            if (time < 10) {
                logger.log(HLogLevel.CONFIGURATION, "Garbage collector time interval is too short: ", time, ". Now use:", GARBAGE_COLLECTOR_TIME_INTERVAL);
                garbage_collector_time_interval.setValue(String.valueOf(GARBAGE_COLLECTOR_TIME_INTERVAL));
            }
            else
                GARBAGE_COLLECTOR_TIME_INTERVAL = time;
        }
        garbage_collector_time_interval.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.garbage_collector_time_interval.name"));

        HConfigElement host = GLOBAL_CONFIGURATIONS.getByName("host");
        if (host == null)
            try {
                host = new HConfigElement("host", HOST);
            } catch (HWrongConfigValueException exception) {
                logger.log(HLogLevel.ERROR, exception);
                host = new HConfigElement("host");
            }
        else
            HOST = host.getValue();
        host.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.host.name"));

        HConfigElement port = GLOBAL_CONFIGURATIONS.getByName("port");
        if (port == null)
            try {
                port = new HConfigElement("port", HConfigType.INT, String.valueOf(PORT));
            } catch (HWrongConfigValueException exception) {
                logger.log(HLogLevel.ERROR, exception);
                port = new HConfigElement("port");
            }
        else {
            PORT = Integer.parseInt(port.getValue());
            if (!PortManager.portIsAvailableForServer(HOST, PORT)) {
                int availablePort = PortManager.getNextAvailablePortRandom(HOST, false);
                if (availablePort == 0)
                    logger.log(HLogLevel.ERROR, "No port is available.");
                else
                    logger.log(HLogLevel.CONFIGURATION, "Port is unavailable: ", PORT, ". Now use: ", availablePort);
                port.setValue(String.valueOf(availablePort));
                PORT = availablePort;
            }
        }
        port.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.port.name"));

        HConfigElement max_player = GLOBAL_CONFIGURATIONS.getByName("max_player");
        if (max_player == null)
            try {
                max_player = new HConfigElement("max_player", HConfigType.INT, String.valueOf(MAX_PLAYER));
            } catch (HWrongConfigValueException exception) {
                logger.log(HLogLevel.ERROR, exception);
                max_player = new HConfigElement("max_player");
            }
        else {
            int players = Integer.parseInt(max_player.getValue());
            if (players < 1) {
                logger.log(HLogLevel.CONFIGURATION, "No player can log in this server! players: ", players, ". Now use:", MAX_PLAYER);
                max_player.setValue(String.valueOf(MAX_PLAYER));
            }
            else
                MAX_PLAYER = players;
        }
        max_player.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.max_player.name"));

        HConfigElement join_the_user_experience_improvement_program = GLOBAL_CONFIGURATIONS.getByName("join_the_user_experience_improvement_program");
        if (join_the_user_experience_improvement_program == null)
            try {
                join_the_user_experience_improvement_program = new HConfigElement("join_the_user_experience_improvement_program", HConfigType.BOOLEAN, JOIN_THE_USER_EXPERIENCE_IMPROVEMENT_PROGRAM ? "true" : "false");
            } catch (HWrongConfigValueException exception) {
                logger.log(HLogLevel.ERROR, exception);
                join_the_user_experience_improvement_program = new HConfigElement("join_the_user_experience_improvement_program");
            }
        else
            JOIN_THE_USER_EXPERIENCE_IMPROVEMENT_PROGRAM = Boolean.parseBoolean(join_the_user_experience_improvement_program.getValue());
        join_the_user_experience_improvement_program.setNote(LanguageI18N.get(Craftworld.class, "Core.configuration.join_the_user_experience_improvement_program.name"));

        GLOBAL_CONFIGURATIONS.clear();
        try {
            GLOBAL_CONFIGURATIONS.add(language);
            GLOBAL_CONFIGURATIONS.add(overwrite_when_extracting);
            GLOBAL_CONFIGURATIONS.add(garbage_collector_time_interval);
            GLOBAL_CONFIGURATIONS.add(host);
            GLOBAL_CONFIGURATIONS.add(port);
            GLOBAL_CONFIGURATIONS.add(max_player);
            GLOBAL_CONFIGURATIONS.add(join_the_user_experience_improvement_program);
        } catch (HElementRegisteredException ignore) {
        }
        GLOBAL_CONFIGURATIONS.write();
    }
}
