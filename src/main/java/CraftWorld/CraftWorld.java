package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.FileTreeStorage;
import CraftWorld.Block.Block;
import CraftWorld.Chunk.ChunkPos;
import CraftWorld.Dimension.Dimension;
import CraftWorld.Events.LoadedWorldEvent;
import CraftWorld.Events.LoadingWorldEvent;
import CraftWorld.Instance.Blocks.BlockStone;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import CraftWorld.World.World;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
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

    private final World world = new World();

    public void start(ServerSocket server) throws IOException {
        logger.log(HLogLevel.FINEST, "Loading world..." );
        CRAFT_WORLD_EVENT_BUS.post(new LoadingWorldEvent());
        //TODO: Load world
        try {
            this.world.read(new File(ConstantStorage.WORLD_PATH));
        } catch (IOException exception) {
            HFileHelper.createNewDirectory(ConstantStorage.WORLD_PATH);
            Dimension dimension = new Dimension(new DimensionEarthSurface());
            dimension.prepareChunks();
            this.world.addDimension(dimension);
            this.world.write(new File(ConstantStorage.WORLD_PATH));
        }
        CRAFT_WORLD_EVENT_BUS.post(new LoadedWorldEvent());

        this.world.getDimension(DimensionEarthSurface.id).getChunk(new ChunkPos(0, 0, 0))
                .setBlock(0, 0, 0, new Block(new BlockStone()));
        this.world.write(new File(ConstantStorage.WORLD_PATH));
        logger.log(this.world);
        logger.log(this.world.getDimension(DimensionEarthSurface.id).getChunk(new ChunkPos(0, 0, 0)).getBlock(0, 0, 0));

        World world1 = new World();
        world1.read(new File(ConstantStorage.WORLD_PATH));
        logger.log(this.world.equals(world1));
        logger.log(world1.getDimension(DimensionEarthSurface.id).getChunk(new ChunkPos(0, 0, 0)).getBlock(0, 0, 0));
    }
}
