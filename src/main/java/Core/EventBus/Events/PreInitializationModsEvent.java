package Core.EventBus.Events;

/**
 * Post before mod initialization.
 * @param firstPost the first time {@code PreInitializationModsEvent} being posted
 */
public record PreInitializationModsEvent(boolean firstPost) {
}
