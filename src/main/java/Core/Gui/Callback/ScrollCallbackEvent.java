package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWScrollCallbackI;

/**
 * Post at ScrollCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetScrollCallback(long, GLFWScrollCallbackI) 
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record ScrollCallbackEvent(long window, double xOffset, double yOffset) {
}
