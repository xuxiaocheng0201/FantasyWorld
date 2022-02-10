package CraftWorld;

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
        try {
            DSTMetaCompound compound = new DSTMetaCompound("com");

//            DSTTagBooleanArray array = new DSTTagBooleanArray("ba");
//            array.getData().put("test", true);
//            DSTTagIntArray iArray = new DSTTagIntArray("ia");
//            iArray.getData().put("it", 1);
//            DSTTagInt tagInt = new DSTTagInt("int", 100);
//            DSTMetaCompound metaCompound = new DSTMetaCompound("in");
//            metaCompound.getDstMap().put("b", array);
//            metaCompound.getDstMap().put("ia", iArray);
//            metaCompound.getDstMap().put("i", tagInt);
//            compound.getDstMap().put("w", metaCompound);
//            DataOutput output = new DataOutputStream(new FileOutputStream(CraftWorld.RUNTIME_PATH + "test.txt"));
//            compound.write(output);
            //DSTMetaCompound{name='com', dstMap={pw=DSTMetaCompound{name='in', dstMap={b=DSTTagBooleanArray{name='ba', data={test=true}}, ia=DSTTagIntArray{name='ia', data={it=1}}, i=DSTTagInt{name='int', data=100}}}}}

            DataInput input = new DataInputStream(new FileInputStream(CraftWorld.RUNTIME_PATH + "test.txt"));
            input.readUTF();
            compound.read(input);

            HLog.logger(compound);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
