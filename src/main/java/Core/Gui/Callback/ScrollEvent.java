package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWScrollCallbackI;

/**
 * Post at Scroll.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetScrollCallback(long, GLFWScrollCallbackI) 
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record ScrollEvent(long window, double xOffset, double yOffset) {
}
