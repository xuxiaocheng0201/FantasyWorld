package Core;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.*;
import Core.Exceptions.GLException;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Version.HStringVersion;
import HeadLibs.Version.HVersionFormatException;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;

public class Craftworld implements ModImplement {
    public static final String CURRENT_VERSION_STRING = "0.0.1";
    public static final HStringVersion CURRENT_VERSION; static {
        HStringVersion current_version = null;
        try {
            current_version = new HStringVersion(CURRENT_VERSION_STRING);
        } catch (HVersionFormatException ignore) {
        }
        CURRENT_VERSION = current_version;
    }
    private static final int window_weight = 1680;
    private static final int window_height = 680;

    public static boolean isClient = true;
    private static final HLog logger = new HLog("CraftworldMain");

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("CraftworldMain");
        logger.log(HLogLevel.INFO, "Hello Craftworld!");
        Runtime.getRuntime().addShutdownHook(new Thread(Thread.currentThread().getName()) {
            @Override
            public void run() {
                logger.log(HLogLevel.INFO, "Welcome to play again!");
                HLog.saveLogs(FileTreeStorage.LOG_FILE);
            }
        });
        GlobalConfigurations.GetConfigurations();
        for (String arg: args) {
            if ("runClient".equals(arg))
                isClient = true;
            if ("runServer".equals(arg))
                isClient = false;
        }
        logger.log(HLogLevel.INFO, "Craftworld will run on " + (isClient ? "client!" : "server!"));
        FileTreeStorage.extractFiles(null, "assets\\Core", "assets\\Core");
        HLog.saveLogs(FileTreeStorage.LOG_FILE);
        Thread gc = new Thread(new GCThread());
        gc.start();
        if (ModManager.addModFilePath((new File(FileTreeStorage.MOD_PATH)).getAbsoluteFile())) {
            Thread main;
            if (isClient)
                main = new Thread(new CraftworldClient());
            else
                main = new Thread(new CraftworldServer());
            main.start();
            try {
            main.join();
            } catch (InterruptedException exception) {
                logger.log(HLogLevel.ERROR, exception);
            }
        }
        gc.interrupt();
    }

    @Override
    public @NotNull String getLanguagePath(@Nullable String lang) {
        return FileTreeStorage.ASSETS_PATH + "Core\\lang\\" + lang + ".lang";
    }

    @SuppressWarnings("unused")
    @EventSubscribe
    public static class DefaultEventBusRegister {
        private static final HLog logger = new HLog("DefaultEventBusReporter");
        @SuppressWarnings("MethodMayBeStatic")
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void defaultEventBusPostEvent(Object event) {
            logger.log(HLogLevel.FINE, "Default Event bus post event '", event, "'(at '", event.getClass().getName(), "').");
        }
    }

    @SuppressWarnings("unused")
    @EventSubscribe(eventBus = "*")
    public static class AllEventBusRegister {
        private static final HLog logger = new HLog("EventNoSubscriberReporter");
        @SuppressWarnings("MethodMayBeStatic")
        @Subscribe(priority = Integer.MAX_VALUE - 1)
        public void noSubscriberEvent(NoSubscriberEvent event) {
            logger.log(HLogLevel.FINE, "Event bus '", EventBusManager.getNameByEventBus(event.eventBus), "' post event '", event.originalEvent, "'(at '", event.originalEvent.getClass().getName(), "'), but no subscriber.");
        }
    }

    public static class CraftworldServer implements Runnable {
        @Override
        public void run() {
            Thread.currentThread().setName("CraftworldServer");
            HLog logger = new HLog(Thread.currentThread().getName());
            logger.log(HLogLevel.FINEST, "Server Thread has started.");
            EventBusManager.getDefaultEventBus().post(new ServerStartEvent());
            try {
                SocketAddress socketAddress = new InetSocketAddress(GlobalConfigurations.HOST, GlobalConfigurations.PORT);
                ServerSocket server = new ServerSocket();
                server.bind(socketAddress, GlobalConfigurations.MAX_PLAYER);
                CraftworldClient.server_binding_flag = false;
                /* ********** Special Modifier ********** */
                CraftWorld.CraftWorld.getInstance().startServer(server);
                /* ********** \Special Modifier ********** */
                server.close();
                EventBusManager.getDefaultEventBus().post(new ServerStopEvent(true));
            } catch (Exception exception) {
                logger.log(HLogLevel.ERROR, exception);
                EventBusManager.getDefaultEventBus().post(new ServerStopEvent(false));
            }
            logger.log(HLogLevel.FINEST, "Server Thread exits.");
        }
    }

    public static class CraftworldClient implements Runnable {
        private static boolean server_binding_flag = true;
        @Override
        public void run() {
            Thread.currentThread().setName("CraftworldClient");
            HLog logger = new HLog(Thread.currentThread().getName());
            logger.log(HLogLevel.FINEST, "Client Thread has started.");
            EventBusManager.getDefaultEventBus().post(new ClientStartEvent());
            try {
                GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(new PrintStream(FileTreeStorage.LOG_FILE));
                GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
                    @Override
                    public void invoke(long window, int key, int scancode, int action, int mods) {
                        EventBusManager.getGLEventBus().post(new KeyCallbackEvent(window, key, scancode, action, mods));
                    }
                };
                GLFW.glfwSetErrorCallback(errorCallback);
                if (!GLFW.glfwInit())
                    throw new GLException("Unable to initialize GLFW.");
                long window = GLFW.glfwCreateWindow(window_weight, window_height, "Simple example", MemoryUtil.NULL, MemoryUtil.NULL);
                if (window == MemoryUtil.NULL) {
                    GLFW.glfwTerminate();
                    throw new GLException("Failed to create the GLFW window");
                }
                GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
                assert vidMode != null;
                GLFW.glfwSetWindowPos(window, (vidMode.width() - window_weight) >> 1, (vidMode.height() - window_height) >> 1);
                GLFW.glfwMakeContextCurrent(window);
                GL.createCapabilities();
                GLFW.glfwSwapInterval(1);
                GLFW.glfwSetKeyCallback(window, keyCallback);
                IntBuffer width = MemoryUtil.memAllocInt(1);
                IntBuffer height = MemoryUtil.memAllocInt(1);
                while (!GLFW.glfwWindowShouldClose(window)) {
                    /* ********** Special Modifier ********** */
                    CraftWorld.CraftWorld.getInstance().menu(window, width, height);
                    /* ********** \Special Modifier ********** */
                    Thread server = new Thread(new Craftworld.CraftworldServer());
                    server_binding_flag = true;
                    server.start();
                    while (server_binding_flag)
                        TimeUnit.MILLISECONDS.sleep(1);
                    Socket client = new Socket(GlobalConfigurations.HOST, GlobalConfigurations.PORT);
                    /* ********** Special Modifier ********** */
                    CraftWorld.CraftWorld.getInstance().startClient(client, window, width, height);
                    /* ********** \Special Modifier ********** */
                    client.close();
                    server.join();
                }
                MemoryUtil.memFree(width);
                MemoryUtil.memFree(height);
                GLFW.glfwDestroyWindow(window);
                keyCallback.free();
                GLFW.glfwTerminate();
                errorCallback.free();
                EventBusManager.getDefaultEventBus().post(new ClientStopEvent(true));
            } catch(Exception exception){
                logger.log(HLogLevel.ERROR, exception);
                EventBusManager.getDefaultEventBus().post(new ClientStopEvent(false));
            }
            logger.log(HLogLevel.FINEST, "Client Thread exits.");
        }
    }
}
