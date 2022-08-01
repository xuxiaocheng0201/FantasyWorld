package Core.EventBus.Events;

import Core.Addition.Mod.ModImplement;

/**
 * Post after mod initialization.
 * @param modClass mod main class
 * @param success true - success. false - failure.
 */
public record ModInitializedEvent(Class<? extends ModImplement> modClass, boolean success) {
}
