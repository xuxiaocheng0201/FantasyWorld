package Core.Events;

import Core.Mod.New.ModImplement;

public record ModInitializingEvent(Class<? extends ModImplement> modClass) {
}
