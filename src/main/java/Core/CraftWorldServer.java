package Core;

import Core.Event.ElementsCheckedEvent;
import Core.Event.EventSubscribe;
import Core.Mod.ModLoader;
import CraftWorld.Block.BlockUtils;
import CraftWorld.DST.DSTUtils;
import CraftWorld.Dimension.DimensionUtils;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.greenrobot.eventbus.Subscribe;

@EventSubscribe
public class CraftWorldServer implements Runnable {
    public volatile static boolean isRunning = false;

    @Override
    public void run() {
        ModLoader.DEFAULT_EVENT_BUS.register(this);
        Thread.currentThread().setName("CraftWorldServer");
        HLog logger = new HLog(Thread.currentThread().getName());
        isRunning = true;
        logger.log(HELogLevel.FINEST, "Server Thread has started.");
        if (ModLoader.loadClasses()) {
            HLog.logger(HELogLevel.BUG, "Mod Loading Error!");
            if (!CraftWorldClient.isRunning)
                return;
        }
        logger.log(HStringHelper.merge("Registered ", DSTUtils.getInstance().getRegisteredCount(), " DST types."));
        logger.log(HStringHelper.merge("Registered ", BlockUtils.getInstance().getRegisteredCount(), " Blocks."));
        logger.log(HStringHelper.merge("Registered ", DimensionUtils.getInstance().getRegisteredCount(), " Dimensions."));
        //TODO
        logger.log(HELogLevel.FINEST, "Server Thread exits.");
        ModLoader.DEFAULT_EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void E(ElementsCheckedEvent event) {

    }
}
