package Core.Events;

import Core.Addition.Mod.ModImplement;

public record ModInitializedEvent(Class<? extends ModImplement> modClass, boolean success) {
}
