package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

/**
 * Post at MouseButton.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetMouseButtonCallback(long, GLFWMouseButtonCallbackI) 
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record MouseButtonEvent(long window, int button, int action, int mods) {
}
