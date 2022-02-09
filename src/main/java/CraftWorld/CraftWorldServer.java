package CraftWorld;

import CraftWorld.DST.DSTMetaCompound;
import CraftWorld.DST.DSTTagInt;
import CraftWorld.DST.DSTUtils;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");
        //TODO: register
        /* TEST */
        DSTMetaCompound metaCompound = new DSTMetaCompound();
        new DSTTagInt();
        try {
//            metaCompound.setName("meta");
//            metaCompound.getDstMap().put("tag1", new DSTTagInt("int1", 1));
//            metaCompound.write(new DataOutputStream(new FileOutputStream(CraftWorld.RUNTIME_PATH + "test.txt")));
            //DSTMetaCompound{name='meta', dstMap={tag1=DSTTagInt{name='int1', data=1}}}
            DataInputStream stream = new DataInputStream(new FileInputStream(CraftWorld.RUNTIME_PATH + "test.txt"));
            stream.readUTF();
            metaCompound.read(stream);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        HLog.logger(metaCompound);
    }
}
