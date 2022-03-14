package Core;

import Core.Mod.ModClassesLoader;
import Core.Mod.ModLauncher;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import CraftWorld.CraftWorld;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

@Mod(name = "testS", require = "before:CraftWorld@(,)")
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
        /* ********** Special Modifier ********** */
        ModClassesLoader.getModList().sort((o1, o2) -> {
            if (o1.equals(CraftWorld.class))
                return -1;
            if (o2.equals(CraftWorld.class))
                return 1;
            return 0;
        });
        /* ********** \<Special Modifier> ********** */
        ModClassesLoader.registerElements();
        logger.log(ModClassesLoader.getModList());
        ModLauncher.sortMods();
        //TODO
        logger.log(ModLauncher.getSortedMods());
        logger.log(ModLauncher.getExceptions());
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
    }
}
