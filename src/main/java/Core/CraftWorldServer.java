package Core;

import CraftWorld.Block.BlockUtils;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Dimension.DimensionUtils;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import Mod.ModLoader;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");

        ModLoader.loadMods();
        HLog.logger(HStringHelper.merge("Registered ", DSTUtils.getRegisteredCount(), " DST types."));
        HLog.logger(HStringHelper.merge("Registered ", BlockUtils.getRegisteredCount(), " Blocks."));
        HLog.logger(HStringHelper.merge("Registered ", DimensionUtils.getRegisteredCount(), " Dimensions."));

        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
