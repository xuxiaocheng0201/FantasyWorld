package Core.Addition.Mod;

import Core.Addition.Mod.BasicInformation.ModAvailableCraftworldVersion;
import Core.Addition.Mod.BasicInformation.ModName;
import Core.Addition.Mod.BasicInformation.ModVersion;
import Core.Addition.ModLauncher;
import Core.Addition.ModManager;
import Core.Craftworld;
import HeadLibs.Helper.HStringHelper;
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
    void mainInitialize() throws Exception;

    default @Nullable String getLanguagePath(@NotNull String lang) {
        NewMod mod = this.getClass().getAnnotation(NewMod.class);
        if (mod == null)
            return null;
        return Craftworld.ASSETS_PATH + HStringHelper.notNullStrip(mod.name()) + "\\lang\\" + lang + ".lang";
    }


    static ModName getModNameFromClass(Class<? extends ModImplement> modClass) {
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return new ModName();
        return new ModName(modAnnouncement.name());
    }

    static ModVersion getModVersionFromClass(Class<? extends ModImplement> modClass) {
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return new ModVersion();
        try {
            return new ModVersion(modAnnouncement.version());
        } catch (HVersionFormatException exception) {
            throw new IllegalArgumentException("Illegal mod version." + ModManager.crashClassInformation(modClass), exception);
        }
    }

    static ModAvailableCraftworldVersion getModAvailableCraftworldVersionFromClass(Class<? extends ModImplement> modClass) {
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return new ModAvailableCraftworldVersion();
        try {
            return new ModAvailableCraftworldVersion(modAnnouncement.availableCraftworldVersion());
        } catch (HVersionFormatException exception) {
            throw new IllegalArgumentException("Illegal mod version." + ModManager.crashClassInformation(modClass), exception);
        }
    }

    static String getModRequirementsFromClass(Class<? extends ModImplement> modClass) {
        NewMod modAnnouncement = modClass.getAnnotation(NewMod.class);
        if (modAnnouncement == null)
            return "";
        return modAnnouncement.requirements();
    }
}
