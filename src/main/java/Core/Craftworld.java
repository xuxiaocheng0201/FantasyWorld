package Core;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
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
    public static final HStringVersion CURRENT_VERSION; static {
        HStringVersion current_version = null;
        try {
            current_version = new HStringVersion(CURRENT_VERSION_STRING);
        } catch (HVersionFormatException ignore) {
        }
        CURRENT_VERSION = current_version;
    }

    public static boolean isClient = true;
    private static final HLog logger = new HLog("CraftworldMain");

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("CraftworldMain");
        logger.log(HLogLevel.INFO, "Hello Craftworld!");
        Runtime.getRuntime().addShutdownHook(new Thread(Thread.currentThread().getName()) {
            @Override
            public void run() {
                logger.log(HLogLevel.INFO, "Welcome to play again!");
                HLog.saveLogs(FileTreeStorage.LOG_FILE);
            }
        });
        GlobalConfigurations.GetConfigurations();
        for (String arg: args) {
            if ("runClient".equals(arg))
                isClient = true;
            if ("runServer".equals(arg))
                isClient = false;
        }
        logger.log(HLogLevel.DEBUG, "Craftworld will run on " + (isClient ? "client!" : "server!"));
        FileTreeStorage.extractFiles(null, "assets\\Core", "assets\\Core");
        HLog.saveLogs(FileTreeStorage.LOG_FILE);
        Thread gc = new Thread(new GCThread());
        gc.start();
        if (ModManager.addModFilePath((new File(FileTreeStorage.MOD_PATH)).getAbsoluteFile())) {
            Thread main;
            if (isClient)
                main = new Thread(new CraftworldClient());
            else
                main = new Thread(new CraftworldServer());
            main.start();
            try {
            main.join();
            } catch (InterruptedException exception) {
                logger.log(HLogLevel.ERROR, exception);
            }
        }
        gc.interrupt();
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
        private static final HLog logger = new HLog("AllEventBusNoSubscriberReporter");
        @SuppressWarnings("MethodMayBeStatic")
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void noSubscriberEvent(NoSubscriberEvent event) {
            logger.log(HLogLevel.FINE, "Event bus '", EventBusManager.getNameByEventBus(event.eventBus), "' post event '", event.originalEvent, "'(at '", event.originalEvent.getClass().getName(), "'), but no subscriber.");
        }
    }
}
