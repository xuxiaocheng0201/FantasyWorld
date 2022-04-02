package Core;

import Core.EventBus.EventBusManager;
import Core.Events.ServerStartEvent;
import Core.Events.ServerStopEvent;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.net.ServerSocket;

public class CraftworldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        logger.log(HELogLevel.FINEST, "Server Thread has started.");
        isRunning = true;
        EventBusManager.getDefaultEventBus().post(new ServerStartEvent());
        try {
            ServerSocket server = new ServerSocket(Craftworld.PORT);
            /* ********** Special Modifier ********** */
            CraftWorld.CraftWorld.getInstance().start(server);
            /* ********** \Special Modifier ********** */
            server.close();
            EventBusManager.getDefaultEventBus().post(new ServerStopEvent(true));
        } catch (Exception exception) {
            logger.log(exception);
            EventBusManager.getDefaultEventBus().post(new ServerStopEvent(false));
        }
        isRunning = false;
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
    }
}
