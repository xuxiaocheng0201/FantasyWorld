package Core.EventBus.Events;

import Core.Addition.Mod.ModImplement;

/**
 * Post before mod initialization.
 * @param modClass mod main class
 */
public record ModInitializingEvent(Class<? extends ModImplement> modClass) {
}
