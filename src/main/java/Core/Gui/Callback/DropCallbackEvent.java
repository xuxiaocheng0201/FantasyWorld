package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWDropCallbackI;

import java.util.List;

/**
 * Post at DropCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetDropCallback(long, GLFWDropCallbackI)
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record DropCallbackEvent(long window, List<String> paths) {
}
