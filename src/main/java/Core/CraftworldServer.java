package Core;

import Core.EventBus.EventBusManager;
import Core.EventBus.Events.ServerStartEvent;
import Core.EventBus.Events.ServerStopEvent;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class CraftworldServer implements Runnable {
    public static boolean needStop = false;
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
            /* ********** Special Modifier ********** */
            CraftWorld.CraftWorld.getInstance().start(server);
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
