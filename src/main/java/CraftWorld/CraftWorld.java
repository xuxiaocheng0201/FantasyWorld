package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.Craftworld;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.FileTreeStorage;
import Core.GlobalConfigurations;
import Core.Gui.Window;
import CraftWorld.Events.LoadedWorldEvent;
import CraftWorld.Events.LoadingWorldEvent;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
import CraftWorld.Instance.Gui.LoadingGui;
import CraftWorld.Instance.Gui.MenuGui;
import CraftWorld.World.World;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

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

    public void loading() {
        Window window = Window.getInstance();
        double intervalPerUpdate = 1.0d / GlobalConfigurations.MAX_UPS;
        LoadingGui loadingGui = new LoadingGui();
        loadingGui.init();
        Craftworld.loadingGuiInited = true;
        double accumulator = 0.0D;
        double lastLoopTime = GLFW.glfwGetTime();
        while (!window.windowShouldClose()) {
            double loopStartTime = GLFW.glfwGetTime();
            double elapsedTime = loopStartTime - lastLoopTime;
            lastLoopTime = loopStartTime;
            accumulator += elapsedTime;
            boolean breakLoop = false;
            while (accumulator >= intervalPerUpdate) {
                loadingGui.update(intervalPerUpdate);
                if (loadingGui.finished()) {
                    breakLoop = true;
                    break;
                }
                accumulator -= intervalPerUpdate;
            }
            if (breakLoop)
                break;
            window.flushResized();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            loadingGui.render();
            window.update();
            if (!window.isVSync()) {
                double loopEndTime = loopStartTime + intervalPerUpdate;
                while (GLFW.glfwGetTime() < loopEndTime)
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException exception) {
                        break;
                    }
            }
        }
    }

    public void menu() {
        Window window = Window.getInstance();
        double intervalPerUpdate = 1.0d / GlobalConfigurations.MAX_UPS;
        MenuGui menuGui = new MenuGui();
        menuGui.init();
        double accumulator = 0.0D;
        double lastLoopTime = GLFW.glfwGetTime();
        while (this.clientRunning) {
            double loopStartTime = GLFW.glfwGetTime();
            double elapsedTime = loopStartTime - lastLoopTime;
            lastLoopTime = loopStartTime;
            accumulator += elapsedTime;
            boolean breakLoop = false;
            while (accumulator >= intervalPerUpdate) {
                menuGui.update(intervalPerUpdate);
                if (menuGui.finished()) {
                    breakLoop = true;
                    break;
                }
                accumulator -= intervalPerUpdate;
            }
            if (breakLoop)
                break;
            window.flushResized();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            menuGui.render();
            window.update();
            if (!window.isVSync()) {
                double loopEndTime = loopStartTime + intervalPerUpdate;
                while (GLFW.glfwGetTime() < loopEndTime)
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException exception) {
                        break;
                    }
            }
        }
        if (!this.clientRunning)
            window.setWindowShouldClose(true);
    }

    private World world;

    public World getWorld() {
        return this.world;
    }

    public boolean serverRunning = true;
    public boolean clientRunning = true;

    public void startServer(ServerSocket server) throws IOException, InterruptedException {
        logger.log(HLogLevel.FINEST, "Loading world...");
        this.world = new World();
        CRAFT_WORLD_EVENT_BUS.post(new LoadingWorldEvent());
        try {
            this.world.readInformation();
        } catch (IOException exception) {
            this.world.addPrepareDimension(DimensionEarthSurface.id);
        }
        try {
            this.world.loadPrepareDimensions();
        } catch (HElementNotRegisteredException | NoSuchMethodException ignore) {
        }
        this.world.writeInformation();
        CRAFT_WORLD_EVENT_BUS.post(new LoadedWorldEvent());

        //while (this.serverRunning) {
            //TODO: Server
            synchronized (this) {
                this.wait(100);
            }
        //}
    }

    public void startClient(Socket client) {
//        while (this.clientRunning) {
//            if (GLFW.glfwWindowShouldClose(window))
//                break;
//            TODO: Client
//        }
        this.serverRunning = false;
    }
}
