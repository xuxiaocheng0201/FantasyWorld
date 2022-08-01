package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

/**
 * Post at MouseButtonCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetMouseButtonCallback(long, GLFWMouseButtonCallbackI) 
 * @author xuxiaocheng
 */
public record MouseButtonCallbackEvent(long window, int button, int action, int mods) {
}
