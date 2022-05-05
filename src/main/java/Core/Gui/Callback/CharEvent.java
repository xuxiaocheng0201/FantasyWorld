package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWCharCallbackI;

/**
 * Post at Char.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetCharCallback(long, GLFWCharCallbackI)
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record CharEvent(long window, int codePoint) {
}
