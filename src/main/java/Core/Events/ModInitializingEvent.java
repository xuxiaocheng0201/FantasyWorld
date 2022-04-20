package Core.Events;

import Core.Addition.Mod.ModImplement;

/**
 * Post before mod initialization.
 * @param modClass mod main class
 */
@SuppressWarnings("unused")
public record ModInitializingEvent(Class<? extends ModImplement> modClass) {
}
