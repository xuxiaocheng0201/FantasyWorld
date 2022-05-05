package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;

/**
 * Post at CursorEnter.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetCursorEnterCallback(long, GLFWCursorEnterCallbackI) 
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record CursorEnterEvent(long window, boolean entered) {
}
