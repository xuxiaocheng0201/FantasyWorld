package CraftWorld;

import HeadLibs.Configuration.HConfigurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LanguageI18N {
    public static final String DEFAULT_LANGUAGE = "en_us";
    public static final String LANGUAGE_DIRECTORY = CraftWorld.RUNTIME_PATH + "lang\\";

    public static Map<String, HConfigurations> languages = new HashMap<>();

    public static String get(String name) {
        return get(name, CraftWorld.CURRENT_LANGUAGE);
    }

    public static String get(String name, String lang) throws IllegalArgumentException{
        if (lang == null)
            throw new IllegalArgumentException("Translate failed! lang is null! [name=" + name + "]");
        lang = lang.toLowerCase();
        try {
            HConfigurations language = new HConfigurations(LANGUAGE_DIRECTORY + lang + ".lang");
            if (!languages.containsKey(lang))
                languages.put(lang, language);
            if (language.getByName(name) != null)
                return language.getByName(name).getValue();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        if (lang.equals(DEFAULT_LANGUAGE))
            return name;
        return get(name, DEFAULT_LANGUAGE);
    }
}
