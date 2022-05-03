package Core.EventBus.Events;

/**
 * Post when client stop.
 */
@SuppressWarnings("unused")
public record ClientStopEvent(boolean exitNormally) {
}
