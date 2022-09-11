package CraftWorld;

import Core.Craftworld;
import Core.EventBus.EventBusManager;
import Core.GlobalConfigurations;
import Core.Gui.Window;
import CraftWorld.Events.LoadedWorldEvent;
import CraftWorld.Events.LoadingWorldEvent;
import CraftWorld.Instance.Gui.LoadingGui;
import CraftWorld.Instance.Gui.MenuGui;
import CraftWorld.Utils.SevenZipUtils;
import CraftWorld.Utils.WorldSystemUtils;
import CraftWorld.World.World;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class CraftWorld {
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

    private final World world; {
        try {
            this.world = new World(WorldSystemUtils.getRandomSeed(null));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public World getWorld() {
        return this.world;
    }

    public boolean serverRunning = true;
    public boolean clientRunning = true;

    public void startServer(@NotNull SocketAddress socketAddress) throws IOException {

        logger.log(HLogLevel.FINEST, "Loading world...");
        CRAFT_WORLD_EVENT_BUS.post(new LoadingWorldEvent());
        this.world.load();
        CRAFT_WORLD_EVENT_BUS.post(new LoadedWorldEvent());
        while (this.serverRunning) {
            this.world.update();
            //TODO: Server
//            selector.select();
//            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//            while (iterator.hasNext()) {
//                SelectionKey key = iterator.next();
//                iterator.remove();
//                //TODO
//            }
            break;
        }
        this.world.unload();
    }

    public void startClient(Socket client) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
        while (this.clientRunning) {
            if (Window.getInstance().windowShouldClose())
                break;
            //TODO: Client
        }
        this.serverRunning = false;
        dataOutputStream.close();
        dataInputStream.close();
        SevenZipUtils.compressFile(this.world.getWorldSavedDirectory().getPath(), ConstantStorage.SAVED_WORLD_PATH + this.world.getWorldName() + ".cww");
    }

    public boolean needCreateNewServer() {
        return true;
    }

    public static final MathContext divideMc = new MathContext(ConstantStorage.CALCULATE_DECIMAL_DEGREE, RoundingMode.HALF_UP);
}
