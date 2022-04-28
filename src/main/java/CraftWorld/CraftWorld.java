package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.FileTreeStorage;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.Events.LoadedWorldEvent;
import CraftWorld.Events.LoadingWorldEvent;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import CraftWorld.World.World;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.ServerSocket;

@EventSubscribe
@NewMod(name = "CraftWorld", version = "0.0.0", requirements = "before:*")
public class CraftWorld implements ModImplement {
    private static final CraftWorld instance = new CraftWorld();
    public static CraftWorld getInstance() {
        return instance;
    }

    private static final HLog logger = new HLog(Thread.currentThread().getName());

    private static final EventBus CRAFT_WORLD_EVENT_BUS = EventBusManager.loggerEventBusBuilder(logger)
            .throwSubscriberException(false).logSubscriberExceptions(true).sendSubscriberExceptionEvent(true)
            .logNoSubscriberMessages(false).sendNoSubscriberEvent(true).build();
    static {
        try {
            EventBusManager.addEventBus("CraftWorld", CRAFT_WORLD_EVENT_BUS);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }
    public static EventBus getCraftWorldEventBus() {
        return CRAFT_WORLD_EVENT_BUS;
    }

    @Subscribe
    @SuppressWarnings({"unused", "MethodMayBeStatic"})
    public void preInitialize(PreInitializationModsEvent event) throws IOException {
        logger.setName("CraftWorld", Thread.currentThread().getName());
        FileTreeStorage.extractFiles(CraftWorld.class, "assets\\CraftWorld", "assets\\CraftWorld");
    }

    private World world;

    public World getWorld() {
        return this.world;
    }

    public void start(ServerSocket server) throws IOException {
        logger.log(HLogLevel.FINEST, "Loading world..." );
        this.world = new World();
        CRAFT_WORLD_EVENT_BUS.post(new LoadingWorldEvent());
        try {
            this.world.readAll();
        } catch (IOException | HElementNotRegisteredException | NoSuchMethodException exception) {
            this.world.addPrepareDimension(DimensionEarthSurface.id);
        }
        try {
            this.world.loadPrepareDimensions();
        } catch (HElementNotRegisteredException | NoSuchMethodException ignore) {
        }
        this.world.writeAll();
        CRAFT_WORLD_EVENT_BUS.post(new LoadedWorldEvent());

        //TODO: server

        try {
            logger.log(this.world.loadDimension(DimensionEarthSurface.id).loadChunk(new ChunkPos(0, 0, 0)).getBlock(0, 0, 0));
//            this.world.loadDimension(DimensionEarthSurface.id).loadChunk(new ChunkPos(0, 0, 0)).setBlock(0, 0, 0, new Block(new BlockStone()));
//            logger.log(this.world.loadDimension(DimensionEarthSurface.id).loadChunk(new ChunkPos(0, 0, 0)).getBlock(0, 0, 0));
        } catch (HElementNotRegisteredException | NoSuchMethodException exception) {
            logger.log(exception);
        }

        this.world.unloadAllDimensions();
    }
}
