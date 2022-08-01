package Core.EventBus.Events;

/**
 * Post when client stop.
 */
public record ClientStopEvent(boolean exitNormally) {
}
