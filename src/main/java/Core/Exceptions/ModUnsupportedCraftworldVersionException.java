package Core.Exceptions;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import Core.Craftworld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when mod not supports this version Craftworld.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModUnsupportedCraftworldVersionException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -8851833450852889381L;

    private static final String DEFAULT_MESSAGE = "Mod not supports " + Craftworld.CURRENT_VERSION_STRING + " Craftworld!";
    private static @NotNull String getModUnsupportedCraftworldVersionMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ModUnsupportedCraftworldVersionException() {
        super(DEFAULT_MESSAGE);
    }

    public ModUnsupportedCraftworldVersionException(@Nullable Class<? extends ModImplement> modClass) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass));
    }

    public ModUnsupportedCraftworldVersionException(@Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass));
    }

    public ModUnsupportedCraftworldVersionException(@Nullable String message) {
        super(getModUnsupportedCraftworldVersionMessage(message));
    }

    public ModUnsupportedCraftworldVersionException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass) {
        super(getModUnsupportedCraftworldVersionMessage(message) + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass));
    }

    public ModUnsupportedCraftworldVersionException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage) {
        super(getModUnsupportedCraftworldVersionMessage(message) + moreMessage + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass));
    }

    public ModUnsupportedCraftworldVersionException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ModUnsupportedCraftworldVersionException(@Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass), throwable);
    }

    public ModUnsupportedCraftworldVersionException(@Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass), throwable);
    }

    public ModUnsupportedCraftworldVersionException(@Nullable String message, @Nullable Throwable throwable) {
        super(getModUnsupportedCraftworldVersionMessage(message), throwable);
    }

    public ModUnsupportedCraftworldVersionException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(getModUnsupportedCraftworldVersionMessage(message) + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass), throwable);
    }

    public ModUnsupportedCraftworldVersionException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getModUnsupportedCraftworldVersionMessage(message) + moreMessage + ModManager.crashClassInformation(modClass) + " Support Craftworld version: " + ModImplement.getModAvailableCraftworldVersionFromClass(modClass), throwable);
    }
}
