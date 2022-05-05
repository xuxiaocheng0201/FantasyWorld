package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWCharCallbackI;

/**
 * Post at CharCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetCharCallback(long, GLFWCharCallbackI)
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record CharCallbackEvent(long window, int codePoint) {
}
