package Core;

import Core.Mod.ModClassesLoader;
import Core.Mod.ModLauncher;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

@Mod(name = "testS", require = "before:CraftWorld@(,);after:*")
public class CraftWorldServer implements Runnable, ModImplement {

    @Override
    public void main() {

    }

    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        Thread.currentThread().setName("CraftWorldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HELogLevel.FINEST, "Server Thread has started.");
        if (ModClassesLoader.loadClasses()) {
            HLog.logger(HELogLevel.BUG, "Mod Loading Error! Server Thread exits.");
            isRunning = false;
            return;
        }
        ModClassesLoader.registerElements();
        logger.log(ModClassesLoader.getAllElements());
        ModLauncher.sortMods();
        //TODO
        logger.log(ModLauncher.getSortedMods());
        logger.log(ModLauncher.getExceptions());
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
    }
}
