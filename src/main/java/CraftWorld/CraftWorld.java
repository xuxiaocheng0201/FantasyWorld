package CraftWorld;

import Core.Craftworld;
import Core.EventBus.EventBusCreator;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.Events.PreInitializationModsEvent;
import Core.Mod.New.ModImplement;
import Core.Mod.New.NewMod;
import CraftWorld.Events.LoadedWorldEvent;
import CraftWorld.Events.LoadingWorldEvent;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ServerSocket;

@EventSubscribe
@NewMod(name = "CraftWorld", version = "0.0.0", require = "before:*")
public class CraftWorld implements ModImplement {
    private static final CraftWorld instance = new CraftWorld();
    public static CraftWorld getInstance() {
        return instance;
    }

    private static final HLog logger = new HLog(Thread.currentThread().getName());

    private static final EventBus CRAFT_WORLD_EVENT_BUS = EventBusCreator.loggerEventBusBuilder(logger)
            .throwSubscriberException(false).logSubscriberExceptions(true).sendSubscriberExceptionEvent(true)
            .logNoSubscriberMessages(false).sendNoSubscriberEvent(true).build();
    static {
        EventBusManager.addEventBus("CraftWorld", CRAFT_WORLD_EVENT_BUS);
    }
    public static EventBus getCraftWorldEventBus() {
        return CRAFT_WORLD_EVENT_BUS;
    }

    @Subscribe
    @SuppressWarnings({"unused", "MethodMayBeStatic"})
    public void preInitialize(PreInitializationModsEvent event) {
        logger.setName("CraftWorld", logger);
        Craftworld.extractFiles(CraftWorld.class, "assets\\CraftWorld", "assets\\CraftWorld");
    }

    @Override
    public void mainInitialize() throws Exception {
        throw new Exception();
    }

    public void start(ServerSocket server) throws InterruptedException {
        logger.log(HELogLevel.FINEST, "Loading world..." );
        CRAFT_WORLD_EVENT_BUS.post(new LoadingWorldEvent());
        //TODO: Load world
        CRAFT_WORLD_EVENT_BUS.post(new LoadedWorldEvent());
        synchronized (this) {
            this.wait(10000);
        }
    }
}
