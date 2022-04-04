package Core;

import Core.EventBus.EventBusManager;
import Core.Events.ClientStoppingEvent;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class CraftworldClient implements Runnable {
    public static volatile boolean isRunning; //false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldClient");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HLogLevel.FINEST, "Client Thread has started.");

        //TODO: Menu
        try {
            Thread server = new Thread(new CraftworldServer());
            server.start();
            try {
                synchronized (this) {
                    this.wait(100);
                }
                Closeable client = new Socket("127.0.0.1", Craftworld.PORT);
                //TODO: Client
                client.close();
                server.join();
            } catch (InterruptedException exception) {
                HLog.logger(HLogLevel.ERROR, exception);
            }
        } catch (IOException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }


        EventBusManager.getDefaultEventBus().post(new ClientStoppingEvent(true));
        isRunning = false;
        logger.log(HLogLevel.FINEST, "Client Thread exits.");
    }
}
