package CraftWorld;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldClient implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        HLog.logger(HELogLevel.FINEST, "Client Thread has started.");

    }
}
