package Core;

import Core.Events.ClientStoppingEvent;
import Core.Mod.ModClassesLoader;
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


        ModClassesLoader.getDefaultEventBus().post(new ClientStoppingEvent());
        isRunning = false;
        logger.log(HELogLevel.FINEST, "Client Thread exits.");
    }
}
