package Core.Events;

import Core.Addition.Mod.ModImplement;

/**
 * Post after mod initialization.
 * @param modClass mod main class
 * @param success true - success. false - failure.
 */
@SuppressWarnings("unused")
public record ModInitializedEvent(Class<? extends ModImplement> modClass, boolean success) {
}
