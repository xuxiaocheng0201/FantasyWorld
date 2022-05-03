package CraftWorld;

import Core.Addition.Mod.ModImplement;
import Core.Addition.Mod.NewMod;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.PreInitializationModsEvent;
import Core.FileTreeStorage;
import Core.GlobalConfigurations;
import CraftWorld.Events.LoadedWorldEvent;
import CraftWorld.Events.LoadingWorldEvent;
import CraftWorld.Gui.MainMenu;
import CraftWorld.Instance.Dimensions.DimensionEarthSurface;
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
import java.nio.IntBuffer;

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

    public boolean serverRunning = true;
    public boolean clientRunning = true;

    public void startServer(ServerSocket server) throws IOException, InterruptedException {
        logger.log(HLogLevel.FINEST, "Loading world...");
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

        while (this.serverRunning) {
            //TODO: Server
            synchronized (this) {
                this.wait(100);
            }
        }
    }

    public void menu(long window, IntBuffer width, IntBuffer height) {
        double secsPerUpdate = 1.0d / GlobalConfigurations.MAX_FPS;
        double previous = GLFW.glfwGetTime();
        double steps = 0.0;
        MainMenu mainMenu = new MainMenu();
        EventBusManager.getGLEventBus().register(mainMenu);
        while (this.clientRunning) {
            double loopStartTime = GLFW.glfwGetTime();
            double loopEndTime = loopStartTime + secsPerUpdate;
            double elapsed = loopStartTime - previous;
            previous = loopStartTime;
            steps += elapsed;
            boolean flag = false;
            while (steps >= secsPerUpdate) {
                if (mainMenu.update()) {
                    flag = true;
                    break;
                }
                steps -= secsPerUpdate;
            }
            if (flag)
                break;
            GLFW.glfwGetFramebufferSize(window, width, height);
            GL11.glViewport(0, 0, width.get(), height.get());
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            width.rewind();
            height.rewind();
            mainMenu.render(width.get(), height.get());
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
            width.flip();
            height.flip();
            while(GLFW.glfwGetTime() < loopEndTime) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {
                }
            }
        }
        if (!this.clientRunning)
            GLFW.glfwSetWindowShouldClose(window, true);
    }

    public void startClient(Socket client, long window, IntBuffer width, IntBuffer height) {

        //while (this.clientRunning) {
            //if (GLFW.glfwWindowShouldClose(window))
        //        break;
            //TODO: Client
        //}

        this.serverRunning = false;
    }
}
