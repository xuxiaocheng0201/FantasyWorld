package Core;

import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import Mod.ModLoader;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog.logger(HELogLevel.FINEST, "Server Thread has started.");

//        HClassFinder implementClasses = new HClassFinder();
//        implementClasses.addSuperClass(NewElementImplement.class);
//        implementClasses.startFind();
//        HClassFinder utilClasses = new HClassFinder();
//        utilClasses.addSuperClass(NewElementUtil.class);
//        utilClasses.startFind();
//        HLog.logger(HStringHelper.merge("Registered ", DSTUtils.getRegisteredCount(), " DST types."));
//        HLog.logger(HStringHelper.merge("Registered ", BlockUtils.getRegisteredCount(), " Blocks."));
//        HLog.logger(HStringHelper.merge("Registered ", DimensionUtils.getRegisteredCount(), " Dimensions."));
        ModLoader.loadMods();

        HLog.logger(HELogLevel.FINEST, "Server Thread exits.");
    }
}
