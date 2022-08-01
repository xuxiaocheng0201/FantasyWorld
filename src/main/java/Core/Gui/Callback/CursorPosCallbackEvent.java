package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

/**
 * Post at CursorPosCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetCursorPosCallback(long, GLFWCursorPosCallbackI) 
 * @author xuxiaocheng
 */
public record CursorPosCallbackEvent(long window, double xPos, double yPos) {
}
