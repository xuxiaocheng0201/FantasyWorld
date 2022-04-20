package Core.Events;

/**
 * Post when client stop.
 */
@SuppressWarnings("unused")
public record ClientStoppingEvent(boolean exitNormally) {
}
