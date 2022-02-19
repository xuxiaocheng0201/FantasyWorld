package CraftWorld;

import CraftWorld.Chunk.Chunk;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");

        //TODO: register
        Chunk chunk = new Chunk(0, 0, 0);
        HLog.logger(chunk.getBlock(1, 1, 1));

        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
