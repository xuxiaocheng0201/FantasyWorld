package CraftWorld.DST;

import CraftWorld.Exception.RegisteredException;
import HeadLibs.Helper.HStringHelper;
import Mod.NewElementUtil;

import java.util.HashMap;
import java.util.Map;

@NewElementUtil(name = "DST")
public class DSTUtils {
    private static final Map<String, Class<? extends IDSTBase>> dst = new HashMap<>();

    public static void register(String name, Class<? extends IDSTBase> dstClass) {
        try {
            if (dst.containsKey(name))
                throw new RegisteredException("Registered name");
            if (dst.containsValue(dstClass))
                throw new RegisteredException("Registered class");
            dst.put(name, dstClass);
        } catch (RegisteredException exception) {
            exception.printStackTrace();
        }
    }

    public static int getRegisteredCount() {
        return dst.size();
    }

    public static IDSTBase get(String name) {
        try {
            if (!dst.containsKey(name))
                throw new IllegalArgumentException(HStringHelper.merge("Type is not registered! [name='", name, "']"));
            return dst.get(name).getDeclaredConstructor().newInstance();
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
