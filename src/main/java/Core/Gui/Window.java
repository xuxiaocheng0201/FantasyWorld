package Core.Gui;

import Core.EventBus.EventBusManager;
import Core.Exceptions.GLException;
import Core.FileTreeStorage;
import Core.GlobalConfigurations;
import Core.Gui.Callback.*;
import HeadLibs.Helper.HFileHelper;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Window {
    private static Window instance;
    public static Window getInstance() {
        return instance;
    }

    private final String title;
    private int width;
    private int height;
    private long windowHandle;
    private boolean resized;

    public Window(String title, int width, int height) {
        super();
        this.title = title;
        this.width = width;
        this.height = height;
        this.resized = false;
        instance = this;
    }

    public void init() throws GLException {
        PrintStream printStream;
        try {
            HFileHelper.createNewFile(FileTreeStorage.LOG_FILE + "_gl.error");
            printStream = new PrintStream(FileTreeStorage.LOG_FILE + "_gl.error");
        } catch (IOException exception) {
            throw new GLException(exception);
        }
        GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(printStream));
        if (!GLFW.glfwInit())
            throw new GLException("Unable to initialize GLFW.");
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);/*
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);*/
        this.windowHandle = GLFW.glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (this.windowHandle == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new GLException("Failed to create the GLFW window");
        }
        GLFW.glfwSetCursorEnterCallback(this.windowHandle, (window, entered) -> {
            EventBusManager.getGLEventBus().post(new CursorEnterEvent(window, entered));
        });
        GLFW.glfwSetCursorPosCallback(this.windowHandle, (window, xPos, yPos) ->  {
            EventBusManager.getGLEventBus().post(new CursorPosEvent(window, xPos, yPos));
        });
        GLFW.glfwSetMouseButtonCallback(this.windowHandle, (window, button, action, mods) -> {
            EventBusManager.getGLEventBus().post(new MouseButtonEvent(window, button, action, mods));
        });
        GLFW.glfwSetScrollCallback(this.windowHandle, (window, xOffset, yOffset) -> {
            EventBusManager.getGLEventBus().post(new ScrollEvent(window, xOffset, yOffset));
        });
        GLFW.glfwSetKeyCallback(this.windowHandle, (window, key, scancode, action, mods) -> {
            EventBusManager.getGLEventBus().post(new KeyEvent(window, key, scancode, action, mods));
        });
        GLFW.glfwSetCharCallback(this.windowHandle, (window, codePoint) -> {
            EventBusManager.getGLEventBus().post(new CharEvent(window, codePoint));
        });
        GLFW.glfwSetCharModsCallback(this.windowHandle, (window, codepoint, mods) -> {
            EventBusManager.getGLEventBus().post(new CharModsEvent(window, codepoint, mods));
        });
        GLFW.glfwSetDropCallback(this.windowHandle, (window, count, names) -> {
            List<String> paths = new ArrayList<>(count);
            PointerBuffer buffer = MemoryUtil.memPointerBuffer(names, count);
            for (int i = 0; i < count; ++i)
                paths.add(MemoryUtil.memUTF8Safe(buffer.get(i)));
            EventBusManager.getGLEventBus().post(new DropEvent(window, paths));
        });
        GLFW.glfwSetFramebufferSizeCallback(this.windowHandle, (window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            this.resized = true;
        });
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        assert vidMode != null;
        GLFW.glfwSetWindowPos(this.windowHandle, (vidMode.width() - this.width) >> 1, (vidMode.height() - this.height) >> 1);
        GLFW.glfwMakeContextCurrent(this.windowHandle);
        GL.createCapabilities();
        if (GlobalConfigurations.V_SYNC_MODE)
            GLFW.glfwSwapInterval(1);
        this.setColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glViewport(0, 0, this.width, this.height);
        GLFW.glfwShowWindow(this.windowHandle);
    }

    @SuppressWarnings("MethodMayBeStatic")
    public void setColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(this.windowHandle);
    }

    public void setWindowShouldClose(boolean shouldClose) {
        GLFW.glfwSetWindowShouldClose(this.windowHandle, shouldClose);
    }

    public void destroyWindow() {
        GLFW.glfwDestroyWindow(this.windowHandle);
        GLFW.glfwTerminate();
        instance = null;
    }

    public String getTitle() {
        return this.title;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean flushResized() {
        if (this.resized) {
            GL11.glViewport(0, 0, this.width, this.height);
            this.resized = false;
            return true;
        }
        return false;
    }

    @SuppressWarnings("MethodMayBeStatic")
    public boolean isVSync() {
        return GlobalConfigurations.V_SYNC_MODE;
    }

    @SuppressWarnings("MethodMayBeStatic")
    public void setVSync(boolean vSync) {
        GlobalConfigurations.V_SYNC_MODE = vSync;
    }

    public void update() {
        GLFW.glfwSwapBuffers(this.windowHandle);
        GLFW.glfwPollEvents();
    }

    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(this.windowHandle, keyCode) == GLFW.GLFW_PRESS;
    }
}
