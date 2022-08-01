package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWCharModsCallbackI;

/**
 * Post at CharModsCallback.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetCharModsCallback(long, GLFWCharModsCallbackI)
 * @author xuxiaocheng
 */
public record CharModsCallbackEvent(long window, int codePoint, int mods) {
}
