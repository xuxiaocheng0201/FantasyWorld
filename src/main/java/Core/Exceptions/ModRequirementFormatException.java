package Core.Exceptions;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when formation of mod requirement was wrong.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModRequirementFormatException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -4542217647636150904L;

    private static final String DEFAULT_MESSAGE = "Mod basic information was wrong!";
    private static @NotNull String getModRequirementFormatMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ModRequirementFormatException() {
        super(DEFAULT_MESSAGE);
    }

    public ModRequirementFormatException(@Nullable Class<? extends ModImplement> modClass) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass));
    }

    public ModRequirementFormatException(@Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass));
    }

    public ModRequirementFormatException(@Nullable String message) {
        super(getModRequirementFormatMessage(message));
    }

    public ModRequirementFormatException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass) {
        super(getModRequirementFormatMessage(message) + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass));
    }

    public ModRequirementFormatException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage) {
        super(getModRequirementFormatMessage(message) + moreMessage + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass));
    }

    public ModRequirementFormatException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ModRequirementFormatException(@Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass), throwable);
    }

    public ModRequirementFormatException(@Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + moreMessage + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass), throwable);
    }

    public ModRequirementFormatException(@Nullable String message, @Nullable Throwable throwable) {
        super(getModRequirementFormatMessage(message), throwable);
    }

    public ModRequirementFormatException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(getModRequirementFormatMessage(message) + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass), throwable);
    }

    public ModRequirementFormatException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getModRequirementFormatMessage(message) + moreMessage + ModManager.crashClassInformation(modClass) + " Requirements: " + ModImplement.getModRequirementsFromClass(modClass), throwable);
    }
}
