package Core.Gui.Callback;

import Core.EventBus.EventBusManager;
import org.lwjgl.glfw.GLFWCharModsCallbackI;

/**
 * Post at CharMods.
 * @see EventBusManager#getGLEventBus()
 * @see org.lwjgl.glfw.GLFW#glfwSetCharModsCallback(long, GLFWCharModsCallbackI)
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public record CharModsEvent(long window, int codePoint, int mods) {
}
