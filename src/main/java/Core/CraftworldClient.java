package Core;

import Core.EventBus.EventBusManager;
import Core.EventBus.EventSubscribe;
import Core.EventBus.Events.ClientStartEvent;
import Core.EventBus.Events.ClientStoppingEvent;
import Core.EventBus.Events.KeyCallbackEvent;
import Core.Exceptions.GLException;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.nio.IntBuffer;

@EventSubscribe(eventBus = "gl")
public class CraftworldClient implements Runnable {
    private static final int window_weight = 1680;
    private static final int window_height = 480;
    public static boolean needStop = false;

    private static final GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private static final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            EventBusManager.getGLEventBus().post(new KeyCallbackEvent(window, key, scancode, action, mods));
        }
    };

    @Override
    public void run() {
        Thread.currentThread().setName("CraftworldClient");
        HLog logger = new HLog(Thread.currentThread().getName());
        logger.log(HLogLevel.FINEST, "Client Thread has started.");
        EventBusManager.getDefaultEventBus().post(new ClientStartEvent());

        GLFW.glfwSetErrorCallback (errorCallback);
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

        try {
            while (!GLFW.glfwWindowShouldClose(window)) {
                //TODO: Menu

                Thread server = new Thread(new CraftworldServer());
                server.start();
                synchronized (this) {
                    this.wait(100);
                }
                Closeable client = new Socket(GlobalConfigurations.HOST, GlobalConfigurations.PORT);
                while (!needStop) {
                    if (GLFW.glfwWindowShouldClose(window))
                        break;
                    //TODO: Client

                    float ratio;
                    /* Get width and height to calculate the ratio */
                    GLFW.glfwGetFramebufferSize(window, width, height);
                    ratio = width.get() / (float) height.get();
                    /* Rewind buffers for next get */
                    width.rewind();
                    height.rewind();
                    /* Set viewport and clear screen */
                    GL11.glViewport(0, 0, width.get(), height.get());
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                    /* Set orthographic projection */
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(-ratio, ratio, -1.0f, 1.0f, 1.0f, -1.0f);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    /* Rotate matrix */
                    GL11.glLoadIdentity();
                    GL11.glRotated(GLFW.glfwGetTime() * 50.0d, 0.0d, 0.0d, 1.0d);
                    /* Render triangle */
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    GL11.glColor3f(1.0f, 0.0f, 0.0f);
                    GL11.glVertex3f(-0.6f, -0.4f, 0.0f);
                    GL11.glColor3f(0.0f, 1.0f, 0.0f);
                    GL11.glVertex3f(0.6f, -0.4f, 0.0f);
                    GL11.glColor3f(0.0f, 0.0f, 1.0f);
                    GL11.glVertex3f(0.0f, 0.6f, 0.0f);
                    GL11.glEnd();
                    /* Swap buffers and poll Events */
                    GLFW.glfwSwapBuffers(window);
                    GLFW.glfwPollEvents();
                    /* Flip buffers for next loop */
                    width.flip();
                    height.flip();
                }
                client.close();
                CraftworldServer.needStop = true;
                server.join();
            }
        } catch(IOException | InterruptedException exception){
            logger.log(HLogLevel.ERROR, exception);
        }

        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);
        GLFW.glfwDestroyWindow(window);
        keyCallback.free();
        GLFW.glfwTerminate();
        errorCallback.free();

        EventBusManager.getDefaultEventBus().post(new ClientStoppingEvent(true));
        logger.log(HLogLevel.FINEST, "Client Thread exits.");
    }

    @SuppressWarnings("MethodMayBeStatic")
    @Subscribe
    public void exit(KeyCallbackEvent event) {
        if (event.key() == GLFW.GLFW_KEY_ESCAPE && event.action() == GLFW.GLFW_PRESS ) {
            GLFW.glfwSetWindowShouldClose(event.window(), true);
        }
    }
}
