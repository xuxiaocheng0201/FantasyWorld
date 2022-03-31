package Core;

import Core.EventBus.EventBusManager;
import Core.Events.ServerStartingEvent;
import Core.Events.ServerStoppingEvent;
import Core.Exceptions.ModRequirementsException;
import Core.Mod.ModLauncher;
import Core.Mod.ModManager;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

public class CraftworldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        logger.log(HELogLevel.FINEST, "Server Thread has started.");
        isRunning = true;
        EventBusManager.getDefaultEventBus().post(new ServerStartingEvent());
        if (ModLauncher.loadModClasses()) {
            HLog.logger(HELogLevel.BUG, "Mod Loading Error in loading! Server Thread exits.");
            EventBusManager.getDefaultEventBus().post(new ServerStoppingEvent(false));
            isRunning = false;
            return;
        }
        logger.log(HELogLevel.DEBUG, "Checked mods: ", ModManager.getModList());
        logger.log(HELogLevel.DEBUG, "Checked elements: ", ModManager.getElementPairList());
        if (ModLauncher.sortMods(logger)) {
            logger.log(HELogLevel.ERROR, ModLauncher.getSorterExceptions());
            for (ModRequirementsException exception: ModLauncher.getSorterExceptions())
                exception.printStackTrace();
            EventBusManager.getDefaultEventBus().post(new ServerStoppingEvent(false));
            isRunning = false;
            return;
        }
        logger.log(HELogLevel.FINEST, "Sorted Mod list: ", ModManager.getModList());
        System.gc();
        ModLauncher.launchMods();
        /* ********** Special Modifier ********** */
        CraftWorld.CraftWorld.getInstance().start();
        /* ********** \Special Modifier ********** */
        EventBusManager.getDefaultEventBus().post(new ServerStoppingEvent(true));
        isRunning = false;
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
    }
}
