package Core;

import Core.Addition.Mod.ModImplement;
import Core.Addition.ModManager;
import HeadLibs.Configuration.SimpleMode.HConfigElementSimple;
import HeadLibs.Configuration.SimpleMode.HConfigurationsSimple;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * I18N string from *.lang file in directory assets/%modName%/lang.
 * @author xuxiaocheng
 * @see ModImplement#getLanguagePath(String)
 */
@SuppressWarnings("unused")
public class LanguageI18N {
    public static final String DEFAULT_LANGUAGE = "en_us";

    private static final HMapRegisterer<String, HConfigurationsSimple> languages = new HMapRegisterer<>();

    /**
     * Get *.lang file path.
     * @param modClass main mod class
     * @param lang language
     * @return file path
     */
    private static @NotNull String getLanguageFilePath(@NotNull Class<? extends ModImplement> modClass, @Nullable String lang) {
        ModImplement mod = HClassHelper.getInstance(modClass);
        if (mod == null) {
            Exception exception = new NoSuchMethodException("Fail to get instance from mod class." + ModManager.crashClassInformation(modClass));
            HLog.logger(HLogLevel.FAULT, exception);
            throw new RuntimeException("Forced crash.", exception);
        }
        return mod.getLanguagePath(lang);
    }

    /**
     * I18N string.
     * @param modClass main mod class
     * @param name the string id
     * @return I18Ned string
     * @throws IOException Fail to read file.
     */
    public static String get(@NotNull Class<? extends ModImplement> modClass, @Nullable String name) throws IOException {
        return get(modClass, name, Craftworld.CURRENT_LANGUAGE);
    }

    /**
     * I18N string.
     * @param modClass main mod class
     * @param name the string id
     * @return I18Ned string
     */
    public static String getNoException(@NotNull Class<? extends ModImplement> modClass, @Nullable String name) {
        try {
            return get(modClass, name, Craftworld.CURRENT_LANGUAGE);
        } catch (IOException exception) {
            return name;
        }
    }

    /**
     * I18N string.
     * @param modClass main mod class
     * @param name the string id
     * @param lang the language
     * @return I18Ned string
     * @throws IOException Fail to read file.
     */
    public static String get(@NotNull Class<? extends ModImplement> modClass, @Nullable String name, @Nullable String lang) throws IOException {
        if (name == null)
            return "null";
        String lang1 = lang == null ? Craftworld.CURRENT_LANGUAGE : lang.toLowerCase();
        HConfigurationsSimple language = languages.getElementNullable(lang1);
        if (language == null) {
            language = new HConfigurationsSimple(getLanguageFilePath(modClass, lang1));
            try {
                language.read();
            } catch (HElementRegisteredException exception) {
                throw new IOException(exception);
            }
            try {
                languages.register(lang1, language);
            } catch (HElementRegisteredException ignore) {
            }
        }
        HConfigElementSimple translation = language.getByName(name);
        if (translation != null)
            return translation.getValue();
        if (DEFAULT_LANGUAGE.equals(lang1)) {
            HConfigurationsSimple todo = new HConfigurationsSimple(getLanguageFilePath(modClass, "TODO"));
            try {
                todo.add(new HConfigElementSimple(name, null));
                todo.write();
            } catch (HElementRegisteredException ignore) {
            }
            return name;
        }
        return get(modClass, name, DEFAULT_LANGUAGE);
    }

    /**
     * I18N string.
     * @param modClass main mod class
     * @param name the string id
     * @param lang the language
     * @return I18Ned string
     */
    public static String getNoException(@NotNull Class<? extends ModImplement> modClass, @Nullable String name, @Nullable String lang) {
        try {
            return get(modClass, name, lang);
        } catch (IOException exception) {
            return name;
        }
    }
}
