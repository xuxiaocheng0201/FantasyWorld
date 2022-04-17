package Core.Exceptions;

import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import HeadLibs.Version.HVersionComplex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when missing mod dependency.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModDependencyMissingException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -7028339289330024072L;

    private static final String DEFAULT_MESSAGE = "Mod missing dependency!";
    private static @NotNull String getModMossingMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ModDependencyMissingException() {
        super(DEFAULT_MESSAGE);
    }

    public ModDependencyMissingException(@Nullable ModName modName) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName);
    }

    public ModDependencyMissingException(@Nullable ModName modName, @Nullable HVersionComplex modVersion) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion));
    }

    public ModDependencyMissingException(@Nullable Class<? extends ModImplement> modClass) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyMissingException(@Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyMissingException(@Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName, @Nullable HVersionComplex modVersion) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion) + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyMissingException(@Nullable String message) {
        super(getModMossingMessage(message));
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable ModName modName) {
        super(getModMossingMessage(message) + "Require mod name: " + modName);
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable ModName modName, @Nullable HVersionComplex modVersion) {
        super(getModMossingMessage(message) + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion));
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass) {
        super(getModMossingMessage(message) + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName) {
        super(getModMossingMessage(message) + "Require mod name: " + modName + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName, @Nullable HVersionComplex modVersion) {
        super(getModMossingMessage(message) + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion) + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyMissingException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ModDependencyMissingException(@Nullable ModName modName, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName, throwable);
    }

    public ModDependencyMissingException(@Nullable ModName modName, @Nullable HVersionComplex modVersion, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion), throwable);
    }

    public ModDependencyMissingException(@Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyMissingException(@Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyMissingException(@Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName, @Nullable HVersionComplex modVersion, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion) + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable Throwable throwable) {
        super(getModMossingMessage(message), throwable);
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable ModName modName, @Nullable Throwable throwable) {
        super(getModMossingMessage(message) + "Require mod name: " + modName, throwable);
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable ModName modName, @Nullable HVersionComplex modVersion, @Nullable Throwable throwable) {
        super(getModMossingMessage(message) + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion), throwable);
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(getModMossingMessage(message) + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName, @Nullable Throwable throwable) {
        super(getModMossingMessage(message) + "Require mod name: " + modName + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyMissingException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable ModName modName, @Nullable HVersionComplex modVersion, @Nullable Throwable throwable) {
        super(getModMossingMessage(message) + "Require mod name: " + modName + (modVersion == null ? "" : "@" + modVersion) + ModManager.crashClassInformation(modClass), throwable);
    }
}
