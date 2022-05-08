package Core;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.ClientStopEvent;
import Core.EventBus.Events.ServerStartEvent;
import Core.EventBus.Events.ServerStopEvent;
import Core.Exceptions.GLException;
import Core.Gui.Window;
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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

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
    private static final int window_wight = 1680;
    private static final int window_height = 680;

    private static boolean isClient = true;
    private static final HLog logger = new HLog("CraftworldMain");

    public static boolean isClient() {
        return isClient;
    }

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
        logger.log(HLogLevel.INFO, "Craftworld will run on " + (isClient ? "client!" : "server!"));
        FileTreeStorage.extractFiles(null, "assets\\Core", "assets\\Core");
        HLog.saveLogs(FileTreeStorage.LOG_FILE);
        Thread gc = new Thread(new GCThread());
        gc.start();
        Thread main;
        if (isClient) {
            main = new Thread(new CraftworldClient());
            main.start();
        } else
            main = new Thread(new CraftworldServer());
        if (ModManager.addModFilePath((new File(FileTreeStorage.MOD_PATH)).getAbsoluteFile())) {
            if (!isClient)
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
        private static final HLog logger = new HLog("EventNoSubscriberReporter");
        @SuppressWarnings("MethodMayBeStatic")
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void noSubscriberEvent(NoSubscriberEvent event) {
            logger.log(HLogLevel.FINE, "Event bus '", EventBusManager.getNameByEventBus(event.eventBus), "' post event '", event.originalEvent, "'(at '", event.originalEvent.getClass().getName(), "'), but no subscriber.");
        }
    }

    public static class CraftworldServer implements Runnable {
        @Override
        public void run() {
            Thread.currentThread().setName("CraftworldServer");
            HLog logger = new HLog(Thread.currentThread().getName());
            logger.log(HLogLevel.FINEST, "Server Thread has started.");
            EventBusManager.getDefaultEventBus().post(new ServerStartEvent());
            try {
                SocketAddress socketAddress = new InetSocketAddress(GlobalConfigurations.HOST, GlobalConfigurations.PORT);
                ServerSocket server = new ServerSocket();
                server.bind(socketAddress, GlobalConfigurations.MAX_PLAYER);
                CraftworldClient.server_binding_flag = false;
                /* ********** Special Modifier ********** */
                CraftWorld.CraftWorld.getInstance().startServer(server);
                /* ********** \Special Modifier ********** */
                server.close();
                EventBusManager.getDefaultEventBus().post(new ServerStopEvent(true));
            } catch (Exception exception) {
                logger.log(HLogLevel.ERROR, exception);
                EventBusManager.getDefaultEventBus().post(new ServerStopEvent(false));
            }
            logger.log(HLogLevel.FINEST, "Server Thread exits.");
        }
    }

    public static class CraftworldClient implements Runnable {
        private static boolean server_binding_flag = true;
        @Override
        public void run() {
            Thread.currentThread().setName("CraftworldClient");
            HLog logger = new HLog(Thread.currentThread().getName());
            logger.log(HLogLevel.FINEST, "Client Thread has started.");
            Window window = new Window("Craftworld " + CURRENT_VERSION_STRING, window_wight, window_height);
            try {
                window.init();
            } catch (GLException exception) {
                logger.log(HLogLevel.FAULT, exception);
            }
            /* ********** Special Modifier ********** */
            CraftWorld.CraftWorld.getInstance().loading();
            /* ********** \Special Modifier ********** */
            try {
                while (!window.windowShouldClose()) {
                    /* ********** Special Modifier ********** */
                    CraftWorld.CraftWorld.getInstance().menu();
                    /* ********** \Special Modifier ********** */
                    Thread server = new Thread(new Craftworld.CraftworldServer());
                    server_binding_flag = true;
                    server.start();
                    while (server_binding_flag)
                        TimeUnit.MILLISECONDS.sleep(1);
                    Socket client = new Socket(GlobalConfigurations.HOST, GlobalConfigurations.PORT);
                    /* ********** Special Modifier ********** */
                    CraftWorld.CraftWorld.getInstance().startClient(client);
                    /* ********** \Special Modifier ********** */
                    client.close();
                    server.join();
                }
                window.destroyWindow();
                EventBusManager.getDefaultEventBus().post(new ClientStopEvent(true));
            } catch(Exception exception){
                logger.log(HLogLevel.ERROR, exception);
                EventBusManager.getDefaultEventBus().post(new ClientStopEvent(false));
            }
            logger.log(HLogLevel.FINEST, "Client Thread exits.");
        }
    }
}
