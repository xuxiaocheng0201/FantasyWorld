package Core;

import Core.Exception.ModRequirementsException;
import Core.Mod.ModClassesLoader;
import Core.Mod.ModLauncher;
import CraftWorld.CraftWorld;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HELogLevel.FINEST, "Server Thread has started.");
        if (ModClassesLoader.loadClasses()) {
            HLog.logger(HELogLevel.BUG, "Mod Loading Error in loading! Server Thread exits.");
            isRunning = false;
            return;
        }
        ModClassesLoader.registerElements();
        ModLauncher.buildModContainer();
        ModLauncher.checkModContainer();
        if (!ModLauncher.getExceptions().isEmpty()) {
            logger.log(HELogLevel.BUG, "Mod Loading Error in checking requirements! Server Thread exits.");
            logger.log(HELogLevel.ERROR, ModLauncher.getExceptions());
            for (ModRequirementsException exception: ModLauncher.getExceptions())
                exception.printStackTrace();
            isRunning = false;
            return;
        }
        ModLauncher.toSimpleModContainer();
        System.gc();
        ModLauncher.sortMods();
        if (!ModLauncher.getExceptions().isEmpty()) {
            logger.log(HELogLevel.BUG, "Mod Loading Error in shorting! Server Thread exits.");
            logger.log(HELogLevel.ERROR, ModLauncher.getExceptions());
            for (ModRequirementsException exception: ModLauncher.getExceptions())
                exception.printStackTrace();
            isRunning = false;
            return;
        }
        logger.log(HELogLevel.FINEST, "Sorted Mod list: ", ModLauncher.getSortedMods());
        ModLauncher.launchMods();
        /* ********** Special Modifier ********** */
        CraftWorld.getInstance().start();
        /* ********** \Special Modifier ********** */
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
    }
}
