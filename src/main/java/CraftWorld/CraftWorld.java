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
import java.security.SecureRandom;
import java.util.random.RandomGenerator;

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

    public void start(ServerSocket server) throws InterruptedException, IOException {
        logger.log(HLogLevel.FINEST, "Loading world..." );
        CRAFT_WORLD_EVENT_BUS.post(new LoadingWorldEvent());
        //TODO: Load world
        try {
            this.world.read(new File(ConstantStorage.WORLD_PATH));
        } catch (IOException exception) {
            HFileHelper.createNewDirectory(ConstantStorage.WORLD_PATH);
            this.world.write(new File(ConstantStorage.WORLD_PATH));
        }
        CRAFT_WORLD_EVENT_BUS.post(new LoadedWorldEvent());
        synchronized (this) {
            this.wait(3000);
        }

        RandomGenerator random = new SecureRandom();
        for (int i = 0; i < 10000; ++i) {
            int a = random.nextInt()*i;
            int b = random.nextInt()*i;
            int c = random.nextInt()*i;

            DataOutputStream dataOutput = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("test.txt")));
            Chunk chunk = new Chunk(a, b, c);
            chunk.write(dataOutput);
            dataOutput.close();

            DataInputStream dataInput = new DataInputStream(new BufferedInputStream(new FileInputStream("test.txt")));
            dataInput.readUTF();
            Chunk chunk1 = new Chunk();
            chunk1.read(dataInput);
            dataInput.close();

            if (!chunk.equals(chunk1))
                logger.log("False: " + a + " " + b + " " + c);
        }
    }
}
