package Core.Exceptions;

import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when mod names clash.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModNameClashException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -8621018554566357361L;

    private static final String DEFAULT_MESSAGE = "Mods name clash!";
    private static @NotNull String getModNameClashMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ModNameClashException() {
        super(DEFAULT_MESSAGE);
    }

    public ModNameClashException(@Nullable ModName modName) {
        super(DEFAULT_MESSAGE + "Mod names are '" + modName + "'.");
    }

    public ModNameClashException(@Nullable ModName modName, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + "Mod names are '" + modName + "'." + moreMessage);
    }

    public ModNameClashException(@Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2) {
        //TODO List classes.
        super(DEFAULT_MESSAGE + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2));
    }

    public ModNameClashException(@Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2, @Nullable String moreMessage) {
        super(DEFAULT_MESSAGE + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + moreMessage + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2));
    }

    public ModNameClashException(@Nullable String message) {
        super(getModNameClashMessage(message));
    }

    public ModNameClashException(@Nullable String message, @Nullable ModName modName) {
        super(getModNameClashMessage(message) + "Mod names are '" + modName + "'.");
    }

    public ModNameClashException(@Nullable String message, @Nullable ModName modName, @Nullable String moreMessage) {
        super(getModNameClashMessage(message) + "Mod names are '" + modName + "'." + moreMessage);
    }

    public ModNameClashException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2) {
        super(getModNameClashMessage(message) + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2));
    }

    public ModNameClashException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2, @Nullable String moreMessage) {
        super(getModNameClashMessage(message) + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + moreMessage + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2));
    }

    public ModNameClashException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ModNameClashException(@Nullable ModName modName, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Mod names are '" + modName + "'.", throwable);
    }

    public ModNameClashException(@Nullable ModName modName, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Mod names are '" + modName + "'." + moreMessage, throwable);
    }

    public ModNameClashException(@Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2), throwable);
    }

    public ModNameClashException(@Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + moreMessage + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2), throwable);
    }

    public ModNameClashException(@Nullable String message, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message), throwable);
    }

    public ModNameClashException(@Nullable String message, @Nullable ModName modName, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Mod names are '" + modName + "'.", throwable);
    }

    public ModNameClashException(@Nullable String message, @Nullable ModName modName, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Mod names are '" + modName + "'." + moreMessage, throwable);
    }

    public ModNameClashException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2), throwable);
    }

    public ModNameClashException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass1, @Nullable Class<? extends ModImplement> modClass2, @Nullable String moreMessage, @Nullable Throwable throwable) {
        super(getModNameClashMessage(message) + "Mod names are '" + ModImplement.getModNameFromClass(modClass1) + "'." + moreMessage + ModManager.crashClassInformation(modClass1) + ModManager.crashClassInformation(modClass2), throwable);
    }
}
