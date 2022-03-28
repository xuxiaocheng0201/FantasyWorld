package Core;

import Core.Mod.New.ModImplement;
import Core.Mod.New.NewMod;
import HeadLibs.Configuration.SimpleMode.HConfigSimple;
import HeadLibs.Configuration.SimpleMode.HConfigurationsSimple;
import HeadLibs.Helper.HStringHelper;

import java.util.HashMap;
import java.util.Map;

public class LanguageI18N {
    public static final String DEFAULT_LANGUAGE = "en_us";

    public static final Map<String, HConfigurationsSimple> languages = new HashMap<>();

    private static String getLanguageFilePath(Class<? extends ModImplement> modClass, String lang) {
        String modName = "Core";
        if (modClass != null) {
            NewMod mod = modClass.getDeclaredAnnotation(NewMod.class);
            if (mod != null)
                modName = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(mod.name()));
        }
        return HStringHelper.merge(Craftworld.ASSETS_PATH, modName, "\\lang\\", lang, ".lang");
    }

    public static String get(String name) {
        return get(null, name, Craftworld.CURRENT_LANGUAGE);
    }

    public static String get(String name, String lang) {
        return get(null, name, lang);
    }

    public static String get(Class<? extends ModImplement> modClass, String name) {
        return get(modClass, name, Craftworld.CURRENT_LANGUAGE);
    }

    public static String get(Class<? extends ModImplement> modClass, String name, String lang) {
        if (lang == null)
            return get(modClass, name, DEFAULT_LANGUAGE);
        lang = lang.toLowerCase();
        if (!languages.containsKey(lang)) {
            HConfigurationsSimple language = new HConfigurationsSimple(getLanguageFilePath(modClass, lang));
            languages.put(lang, language);
        }
        HConfigSimple translation = languages.get(lang).getByName(name);
        if (translation != null)
            return translation.getValue();
        if (lang.equals(DEFAULT_LANGUAGE)) {
            HConfigurationsSimple todo = new HConfigurationsSimple(getLanguageFilePath(modClass, "TODO"));
            if (todo.getByName(name) == null) {
                todo.add(new HConfigSimple(name, null));
                todo.write();
            }
            return name;
        }
        return get(modClass, name, DEFAULT_LANGUAGE);
    }
}
