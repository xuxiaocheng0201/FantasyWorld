package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.FileTreeStorage;
import CraftWorld.Chunk.Chunk;
import CraftWorld.Events.LoadedWorldEvent;
import CraftWorld.Events.LoadingWorldEvent;
import CraftWorld.World.World;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.*;
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

    private World world = new World();

    public void start(ServerSocket server) throws InterruptedException, IOException {
        logger.log(HLogLevel.FINEST, "Loading world..." );
        CRAFT_WORLD_EVENT_BUS.post(new LoadingWorldEvent());
        //TODO: Load world
        try {
            this.world.read(new DataInputStream(new BufferedInputStream(new FileInputStream(ConstantStorage.WORLD_FILE))));
        } catch (IOException exception) {
            HFileHelper.createNewFile(ConstantStorage.WORLD_FILE);
            this.world.write(new DataOutputStream(new BufferedOutputStream(new FileOutputStream(ConstantStorage.WORLD_FILE))));
        }
        CRAFT_WORLD_EVENT_BUS.post(new LoadedWorldEvent());
        synchronized (this) {
            this.wait(3000);
        }

        DataOutput dataOutput = new DataOutputStream(new FileOutputStream("test.txt"));
        Chunk chunk = new Chunk(0,0,0);
        chunk.write(dataOutput);

        DataInput dataInput = new DataInputStream(new FileInputStream("test.txt"));
        dataInput.readUTF();
        Chunk chunk1 = new Chunk();
        chunk1.read(dataInput);

        logger.log(chunk);
        logger.log(chunk1);
        logger.log(chunk.equals(chunk1));
    }
}
