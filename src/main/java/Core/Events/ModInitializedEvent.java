package Core.Events;

import Core.Mod.New.ModImplement;

public record ModInitializedEvent(Class<? extends ModImplement> modClass, boolean success) {
}
