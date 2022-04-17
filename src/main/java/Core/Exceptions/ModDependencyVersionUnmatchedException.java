package Core.Exceptions;

import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.BasicInformation.ModVersion;
import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import HeadLibs.Version.HVersionComplex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when mod dependency version is unmatched.
 * IT IS A RUNTIME EXCEPTION!!!
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ModDependencyVersionUnmatchedException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -5074629509213313717L;

    private static final String DEFAULT_MESSAGE = "Mod dependency exists, but version was wrong!";
    private static @NotNull String getModDependencyVersionUnmatchedMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public ModDependencyVersionUnmatchedException() {
        super(DEFAULT_MESSAGE);
    }

    public ModDependencyVersionUnmatchedException(@Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion) {
        super(DEFAULT_MESSAGE + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.');
    }

    public ModDependencyVersionUnmatchedException(@Nullable Class<? extends ModImplement> modClass) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyVersionUnmatchedException(@Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion, @Nullable Class<? extends ModImplement> modClass) {
        super(DEFAULT_MESSAGE + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.' + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message) {
        super(getModDependencyVersionUnmatchedMessage(message));
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message, @Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion) {
        super(getModDependencyVersionUnmatchedMessage(message) + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.');
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass) {
        super(getModDependencyVersionUnmatchedMessage(message) + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message, @Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion, @Nullable Class<? extends ModImplement> modClass) {
        super(getModDependencyVersionUnmatchedMessage(message) + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.' + ModManager.crashClassInformation(modClass));
    }

    public ModDependencyVersionUnmatchedException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public ModDependencyVersionUnmatchedException(@Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.', throwable);
    }

    public ModDependencyVersionUnmatchedException(@Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyVersionUnmatchedException(@Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion, @Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.' + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message, @Nullable Throwable throwable) {
        super(getModDependencyVersionUnmatchedMessage(message), throwable);
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message, @Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion, @Nullable Throwable throwable) {
        super(getModDependencyVersionUnmatchedMessage(message) + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.', throwable);
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message, @Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(getModDependencyVersionUnmatchedMessage(message) + ModManager.crashClassInformation(modClass), throwable);
    }

    public ModDependencyVersionUnmatchedException(@Nullable String message, @Nullable ModName modName, @Nullable ModVersion modVersion, @Nullable HVersionComplex requireVersion, @Nullable Class<? extends ModImplement> modClass, @Nullable Throwable throwable) {
        super(getModDependencyVersionUnmatchedMessage(message) + "Mod '" + modName + (modVersion == null ? "" : "@" + modVersion) + "' exists, but require version in " + requireVersion + '.' + ModManager.crashClassInformation(modClass), throwable);
    }
}
