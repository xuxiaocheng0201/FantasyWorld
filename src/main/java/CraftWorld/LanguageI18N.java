package CraftWorld;

import HeadLibs.Configuration.SimpleMode.HConfigSimple;
import HeadLibs.Configuration.SimpleMode.HConfigurationsSimple;
import HeadLibs.Helper.HStringHelper;

import java.util.HashMap;
import java.util.Map;

public class LanguageI18N {
    public static final String DEFAULT_LANGUAGE = "en_us";
    public static final String LANGUAGE_DIRECTORY = CraftWorld.RUNTIME_PATH + "lang\\";

    public static Map<String, HConfigurationsSimple> languages = new HashMap<>();

    public static String get(String name) {
        return get(name, CraftWorld.CURRENT_LANGUAGE);
    }

    public static String get(String name, String lang) {
        if (lang == null)
            return get(name, DEFAULT_LANGUAGE);
        lang = lang.toLowerCase();
        HConfigurationsSimple language = new HConfigurationsSimple(HStringHelper.merge(LANGUAGE_DIRECTORY, lang, ".lang"));
        if (!languages.containsKey(lang))
            languages.put(lang, language);
        if (language.getByName(name) != null)
            return language.getByName(name).getValue();
        if (lang.equals(DEFAULT_LANGUAGE)) {
            HConfigurationsSimple todo = new HConfigurationsSimple(HStringHelper.merge(LANGUAGE_DIRECTORY, "TODO.lang"));
            todo.add(new HConfigSimple(name, null));
            return name;
        }
        return get(name, DEFAULT_LANGUAGE);
    }
}
