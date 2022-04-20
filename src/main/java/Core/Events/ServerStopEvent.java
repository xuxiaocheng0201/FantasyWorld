package Core.Events;

/**
 * Post when server stop.
 */
@SuppressWarnings("unused")
public record ServerStopEvent(boolean exitNormally) {
}
