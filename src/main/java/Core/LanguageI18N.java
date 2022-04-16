package Core;

import Core.Addition.Mod.ModImplement;
import HeadLibs.Configuration.SimpleMode.HConfigElementSimple;
import HeadLibs.Configuration.SimpleMode.HConfigurationsSimple;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;

import java.io.IOException;

/**
 * I18N string from *.lang file in directory assets/%modName%/lang.
 */
public class LanguageI18N {
    public static final String DEFAULT_LANGUAGE = "en_us";

    private static final HMapRegisterer<String, HConfigurationsSimple> languages = new HMapRegisterer<>();

    private static String getLanguageFilePath(Class<? extends ModImplement> modClass, String lang) {
        ModImplement mod = HClassHelper.getInstance(modClass);
        if (mod == null)
            return Craftworld.ASSETS_PATH + "Core\\lang\\" + lang + ".lang";
        return mod.getLanguagePath(lang);
    }

    public static String get(String name) throws IOException {
        return get(null, name, Craftworld.CURRENT_LANGUAGE);
    }

    public static String get(Class<? extends ModImplement> modClass, String name) throws IOException {
        return get(modClass, name, Craftworld.CURRENT_LANGUAGE);
    }

    public static String get(Class<? extends ModImplement> modClass, String name, String lang) throws IOException {
        String lang1 = lang == null ? Craftworld.CURRENT_LANGUAGE : lang.toLowerCase();
        HConfigurationsSimple language = languages.getElementNullable(lang1);
        if (language == null) {
            language = new HConfigurationsSimple(getLanguageFilePath(modClass, lang1));
            languages.reset(lang1, language);
        }
        HConfigElementSimple translation = language.getByName(name);
        if (translation != null)
            return translation.getValue();
        if (lang1.equals(DEFAULT_LANGUAGE)) {
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
}
