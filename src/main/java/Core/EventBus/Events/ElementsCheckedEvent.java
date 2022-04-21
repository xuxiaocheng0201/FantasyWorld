package Core.EventBus.Events;

/**
 * Post after elements check.
 * @param firstPost the first time {@code ElementsCheckedEvent} being posted
 */
@SuppressWarnings("unused")
public record ElementsCheckedEvent(boolean firstPost) {
}
