package Core;

import Core.EventBus.EventBusManager;
import Core.EventBus.Events.ServerStartEvent;
import Core.EventBus.Events.ServerStopEvent;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.io.IOException;
import java.net.ServerSocket;

public class CraftworldServer implements Runnable {
    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        logger.log(HLogLevel.FINEST, "Server Thread has started.");
        EventBusManager.getDefaultEventBus().post(new ServerStartEvent());
        try {
            ServerSocket server = new ServerSocket(GlobalConfigurations.PORT);
            /* ********** Special Modifier ********** */
            CraftWorld.CraftWorld.getInstance().start(server);
            /* ********** \Special Modifier ********** */
            server.close();
            EventBusManager.getDefaultEventBus().post(new ServerStopEvent(true));
        } catch (IOException | InterruptedException exception) {
            logger.log(HLogLevel.ERROR, exception);
            EventBusManager.getDefaultEventBus().post(new ServerStopEvent(false));
        }
        logger.log(HLogLevel.FINEST, "Server Thread exits.");
    }
}
