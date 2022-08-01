package Core.EventBus.Events;

/**
 * Post before elements check.
 * @param firstPost the first time {@code ElementsCheckingEvent} being posted
 */
public record ElementsCheckingEvent(boolean firstPost) {
}
