import CraftWorld.Block.BlockUtils;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Dimension.DimensionUtils;
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

        HClassFinder classFinder = new HClassFinder();
        classFinder.startFind();
        HLog.logger(HELogLevel.DEBUG, HStringHelper.merge("Registered ", DSTUtils.getRegisteredCount(), " DST types."));
        HLog.logger(HELogLevel.DEBUG, HStringHelper.merge("Registered ", BlockUtils.getRegisteredCount(), " Blocks."));
        HLog.logger(HELogLevel.DEBUG, HStringHelper.merge("Registered ", DimensionUtils.getRegisteredCount(), " Dimensions."));

        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
