import CraftWorld.Block.Block;
import CraftWorld.Block.BlockPos;
import CraftWorld.Block.BlockUtils;
import CraftWorld.Chunk.Chunk;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Dimension.Dimension;
import CraftWorld.Dimension.DimensionUtils;
import CraftWorld.Instance.Blocks.BlockAir;
import CraftWorld.Instance.DST.*;
import CraftWorld.Instance.Dimension.DimensionEarthSurface;
import HeadLibs.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");

        new BlockPos();
        new Block();
        new ChunkPos();
        new Chunk();
        new Dimension();
        /* Register Instances */
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
        new DSTMetaCompound();
        HLog.logger(HELogLevel.DEBUG, HStringHelper.merge("Registered ", DSTUtils.getRegisteredCount(), " DST types."));
        new BlockAir();
        HLog.logger(HELogLevel.DEBUG, HStringHelper.merge("Registered ", BlockUtils.getRegisteredCount(), " Blocks."));
        new DimensionEarthSurface();
        HLog.logger(HELogLevel.DEBUG, HStringHelper.merge("Registered ", DimensionUtils.getRegisteredCount(), " Dimensions."));

        HClassFinder classFinder = new HClassFinder();
        HLog.logger(classFinder.getClassList());

        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
