package Core.Events;

import Core.Addition.Mod.ModImplement;

public record ModInitializingEvent(Class<? extends ModImplement> modClass) {
}
