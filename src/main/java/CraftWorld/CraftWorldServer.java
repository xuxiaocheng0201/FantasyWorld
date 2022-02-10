package CraftWorld;

import CraftWorld.Block.Block;
import CraftWorld.DST.*;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.*;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");

        new DSTTagBoolean();
        new DSTTagByte();
        new DSTTagChar();
        new DSTTagShort();
        new DSTTagInt();
        new DSTTagLong();
        new DSTTagFloat();
        new DSTTagDouble();
        new DSTTagString();
        new DSTTagBooleanArray();
        new DSTTagByteArray();
        new DSTTagCharArray();
        new DSTTagShortArray();
        new DSTTagIntArray();
        new DSTTagLongArray();
        new DSTTagFloatArray();
        new DSTTagDoubleArray();
        new DSTTagStringArray();

        //TODO: register


        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
