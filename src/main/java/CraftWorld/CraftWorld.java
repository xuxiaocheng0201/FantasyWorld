package CraftWorld;

import Core.EventBus.EventSubscribe;
import Core.Events.PreInitializationModsEvent;
import Core.Mod.New.ModImplement;
import Core.Mod.New.NewMod;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@EventSubscribe
@NewMod(name = "CraftWorld", version = "0.0.0", require = "before:*")
public class CraftWorld implements ModImplement {
    private static final CraftWorld instance = new CraftWorld();
    public static CraftWorld getInstance() {
        return instance;
    }

    private final EventBus CRAFT_WORLD_EVENT_BUS = EventBus.builder()
            .build();


    private final HLog logger = new HLog();

    @Subscribe
    public void preInitialize(PreInitializationModsEvent event) {
        logger.setName("CraftWorld", Thread.currentThread().getName());
    }

    @Override
    public void mainInitialize() {

    }

    public void start() {
        logger.log(HELogLevel.FINEST, "Loading world..." );
        //TODO
    }
}
