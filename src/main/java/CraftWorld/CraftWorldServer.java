package CraftWorld;

import CraftWorld.DST.*;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");

        //TODO: register
        new DSTTagShort();
        new DSTTagString();
        new DSTTagBoolean();
        new DSTTagByte();
        new DSTTagInt();
        new DSTTagChar();
        new DSTTagLong();
        new DSTTagDouble();
        new DSTTagFloat();

        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
