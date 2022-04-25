package CraftWorld.DST;

import Core.Addition.Element.ElementUtil;
import Core.Addition.Element.NewElementUtilCore;
import HeadLibs.DataStructures.DoubleHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NewElementUtilCore(modName = "CraftWorld", elementName = "DST")
public class DSTUtils extends ElementUtil<IDSTBase> {
    private static final DSTUtils instance = new DSTUtils();
    public static DSTUtils getInstance() {
        return instance;
    }


    private static final DoubleHashMap<String, String> prefixMap = new DoubleHashMap<>();
    private static final DoubleHashMap<String, String> suffixMap = new DoubleHashMap<>();

    public static @NotNull String prefix(@Nullable String name) {
        if (name == null)
            return "";
        if (prefixMap.containsKey(name))
            return prefixMap.getValue(name);
        String hash = String.valueOf(("start" + name).hashCode());
        prefixMap.put(name, hash);
        return hash;
    }

    public static @NotNull String dePrefix(@Nullable String prefix) {
        if (prefix == null)
            return "";
        if (prefixMap.containsValue(prefix))
            return prefixMap.getKey(prefix);
        return prefix;
    }

    public static @NotNull String suffix(@Nullable String name) {
        if (name == null)
            return "";
        if (suffixMap.containsKey(name))
            return suffixMap.getValue(name);
        String hash = String.valueOf(("end" + name).hashCode());
        suffixMap.put(name, hash);
        return hash;
    }

    public static @NotNull String deSuffix(@Nullable String suffix) {
        if (suffix == null)
            return "";
        if (suffixMap.containsValue(suffix))
            return suffixMap.getKey(suffix);
        return suffix;
    }
}
