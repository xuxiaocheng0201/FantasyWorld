package CraftWorld.Dimension;

import CraftWorld.Exception.RegisteredException;
import HeadLibs.Helper.HStringHelper;

import java.util.HashMap;
import java.util.Map;

public class DimensionUtils {
    private static final Map<String, Class<? extends IDimensionBase>> dimensions = new HashMap<>();

    public static void register(String name, Class<? extends IDimensionBase> dimensionClass) {
        try {
            if (dimensions.containsKey(name))
                throw new RegisteredException("Registered name");
            if (dimensions.containsValue(dimensionClass))
                throw new RegisteredException("Registered class");
            dimensions.put(name, dimensionClass);
        } catch (RegisteredException exception) {
            exception.printStackTrace();
        }
    }

    public static int getRegisteredCount() {
        return dimensions.size();
    }

    public static IDimensionBase get(String name) {
        try {
            if (!dimensions.containsKey(name))
                throw new IllegalArgumentException(HStringHelper.merge("Type is not registered! [name='", name, "']"));
            return dimensions.get(name).getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String prefix(String name) {
        return HStringHelper.merge("start", name);
    }

    public static String dePrefix(String prefix) {
        if (prefix == null)
            return "null";
        if (prefix.startsWith("start"))
            return prefix.substring(5);
        return prefix;
    }

    public static String suffix(String name) {
        return HStringHelper.merge("end", name);
    }

    public static String deSuffix(String suffix) {
        if (suffix == null)
            return "null";
        if (suffix.startsWith("end"))
            return suffix.substring(3);
        return suffix;
    }
}
