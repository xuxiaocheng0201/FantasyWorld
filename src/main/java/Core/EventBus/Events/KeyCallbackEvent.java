package Core.EventBus.Events;

import Core.EventBus.EventBusManager;

/**
 * Post at GLFWKeyCallback.
 * @see EventBusManager#getGLEventBus()
 * @author xuxiaocheng
 */
public record KeyCallbackEvent(long window, int key, int scancode, int action, int mods) {
}
