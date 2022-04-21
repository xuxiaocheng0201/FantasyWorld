package Core.EventBus.Events;

/**
 * Post after mod initialization.
 * @param firstPost the first time {@code PostInitializationModsEvent} being posted
 */
@SuppressWarnings("unused")
public record PostInitializationModsEvent(boolean firstPost) {
}
