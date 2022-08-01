package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;

/**
 * Post at CursorEnterCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetCursorEnterCallback(long, GLFWCursorEnterCallbackI) 
 * @author xuxiaocheng
 */
public record CursorEnterCallbackEvent(long window, boolean entered) {
}
