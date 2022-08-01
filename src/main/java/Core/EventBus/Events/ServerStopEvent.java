package Core.EventBus.Events;

/**
 * Post when server stop.
 */
public record ServerStopEvent(boolean exitNormally) {
}
