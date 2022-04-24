package Core;

import Core.EventBus.EventBusManager;
import Core.EventBus.Events.ClientStartEvent;
import Core.EventBus.Events.ClientStoppingEvent;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class CraftworldClient implements Runnable {
    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldClient");
        HLog logger = new HLog(Thread.currentThread().getName());
        logger.log(HLogLevel.FINEST, "Client Thread has started.");
        EventBusManager.getDefaultEventBus().post(new ClientStartEvent());

        //TODO: Menu
        try {
            Thread server = new Thread(new CraftworldServer());
            server.start();
            try {
                synchronized (this) {
                    this.wait(100);
                }
                Closeable client = new Socket("localhost", GlobalConfigurations.PORT);
                //TODO: Client
                synchronized (this) {
                    this.wait(100);
                }
                client.close();
                server.join();
            } catch (InterruptedException exception) {
                logger.log(HLogLevel.ERROR, exception);
            }
        } catch (IOException exception) {
            logger.log(HLogLevel.ERROR, exception);
        }
        EventBusManager.getDefaultEventBus().post(new ClientStoppingEvent(true));
        logger.log(HLogLevel.FINEST, "Client Thread exits.");
    }
}
