package Core;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldClient implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldClient");
        HLog.logger(HELogLevel.FINEST, "Client Thread has started.");

        //TODO: Client

        try {
            Thread server = new Thread(new CraftWorldServer());
            server.start();
            server.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }


        HLog.logger(HELogLevel.FINEST, "Client Thread exits.");
    }
}
