package Core.Exceptions;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when mod information was wrong.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
public class ModInformationException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 9199196940721277887L;

    private static final String DEFAULT_MESSAGE = "Mod basic information was wrong!";
    private static @NotNull String getModInformationMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ModInformationException() {
        super(DEFAULT_MESSAGE);
    }

    public ModInformationException(@Nullable Class<? extends ModImplement> modClass) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass));
    }

    public ModInformationException(@Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(modClass));
    }

    public ModInformationException(@Nullable String message) {
        super(getModInformationMessage(message));
    }

    public ModInformationException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass) {
        super(getModInformationMessage(message) + ModManager.crashClassInformation(modClass));
    }

    public ModInformationException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage) {
        super(getModInformationMessage(message) + moreMessage + ModManager.crashClassInformation(modClass));
    }

    public ModInformationException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ModInformationException(@Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModInformationException(@Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModInformationException(@Nullable String message, @Nullable Throwable throwable) {
        super(getModInformationMessage(message), throwable);
    }

    public ModInformationException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(getModInformationMessage(message) + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModInformationException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getModInformationMessage(message) + moreMessage + ModManager.crashClassInformation(modClass), throwable);
    }
}
