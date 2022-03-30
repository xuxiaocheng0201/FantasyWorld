package Core;

import Core.EventBus.EventBusManager;
import Core.Events.ClientStoppingEvent;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftworldClient implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldClient");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HELogLevel.FINEST, "Client Thread has started.");


        //TODO: Client

        try {
            Thread server = new Thread(new CraftworldServer());
            server.start();
            server.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }


        EventBusManager.getDefaultEventBus().post(new ClientStoppingEvent(true));
        isRunning = false;
        logger.log(HELogLevel.FINEST, "Client Thread exits.");
    }
}
