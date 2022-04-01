package Core;

import Core.EventBus.EventBusManager;
import Core.Events.ClientStoppingEvent;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.IOException;
import java.net.Socket;

public class CraftworldClient implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldClient");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HELogLevel.FINEST, "Client Thread has started.");
        try {
            Thread server = new Thread(new CraftworldServer());
            server.start();
            //TODO: Client
            try {
                synchronized (this) {
                    wait(100);
                }
                Socket client = new Socket("127.0.0.1", Craftworld.PORT);
                client.close();
                server.join();
            } catch (InterruptedException exception) {
                HLog.logger(HELogLevel.ERROR, exception);
            }
        } catch (IOException exception) {
            logger.log(HELogLevel.ERROR, exception);
        }
        EventBusManager.getDefaultEventBus().post(new ClientStoppingEvent(true));
        isRunning = false;
        logger.log(HELogLevel.FINEST, "Client Thread exits.");
    }
}
