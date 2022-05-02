package Core.EventBus.Events;

public record KeyCallbackEvent(long window, int key, int scancode, int action, int mods) {
}
