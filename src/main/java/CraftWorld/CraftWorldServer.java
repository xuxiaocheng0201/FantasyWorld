package CraftWorld;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");
        //TODO: register

    }
}
