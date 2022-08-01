package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Post at KeyCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetKeyCallback(long, GLFWKeyCallbackI) 
 * @author xuxiaocheng
 */
public record KeyCallbackEvent(long window, int key, int scancode, int action, int mods) {
}
