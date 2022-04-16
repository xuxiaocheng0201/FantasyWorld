package Core.Addition.Mod;

import Core.Addition.Mod.BasicInformation.ModAvailableCraftworldVersion;
import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.BasicInformation.ModVersion;
import Core.Addition.ModLauncher;
import Core.Addition.ModManager;
import Core.Craftworld;
import HeadLibs.Version.HVersionFormatException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implement at new mods main classes.
 * @author xuxiaocheng
 * @see NewMod
 */
@SuppressWarnings("unused")
public interface ModImplement {
    /**
     * Main method. Be called when mod initializing.
     * @see ModLauncher#launchMods()
     */
    @SuppressWarnings("ProhibitedExceptionDeclared")
    default void mainInitialize() throws Exception {
    }

    default @Nullable String getLanguagePath(@Nullable String lang) {
        NewMod mod = this.getClass().getAnnotation(NewMod.class);
        if (mod == null)
            return null;
        return Craftworld.ASSETS_PATH + ModImplement.getModNameFromClass(this.getClass()) + "\\lang\\" +
                (lang == null ? Craftworld.CURRENT_LANGUAGE : lang) + ".lang";
    }


    // -------------------- Mod basic information getter --------------------

    static @NotNull ModName getModNameFromClass(@Nullable Class<? extends ModImplement> modClass) {
        if (modClass == null)
            return new ModName();
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return new ModName();
        return new ModName(modAnnouncement.name());
    }

    static @NotNull ModVersion getModVersionFromClass(@Nullable Class<? extends ModImplement> modClass) {
        if (modClass == null)
            return new ModVersion();
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return new ModVersion();
        try {
            return new ModVersion(modAnnouncement.version());
        } catch (HVersionFormatException exception) {
            throw new IllegalArgumentException("Illegal mod version." + ModManager.crashClassInformation(modClass), exception);
        }
    }

    static @NotNull ModAvailableCraftworldVersion getModAvailableCraftworldVersionFromClass(@Nullable Class<? extends ModImplement> modClass) {
        if (modClass == null)
            return new ModAvailableCraftworldVersion();
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return new ModAvailableCraftworldVersion();
        try {
            return new ModAvailableCraftworldVersion(modAnnouncement.availableCraftworldVersion());
        } catch (HVersionFormatException exception) {
            throw new IllegalArgumentException("Illegal mod available Craftworld version." + ModManager.crashClassInformation(modClass), exception);
        }
    }

    static @NotNull String getModRequirementsFromClass(@Nullable Class<? extends ModImplement> modClass) {
        if (modClass == null)
            return "";
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return "";
        return modAnnouncement.requirements();
    }
}
