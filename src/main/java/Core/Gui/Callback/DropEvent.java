package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWDropCallbackI;

import java.util.List;

/**
 * Post at Drop.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetDropCallback(long, GLFWDropCallbackI)
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record DropEvent(long window, List<String> paths) {
}
