package CraftWorld.DST;

import CraftWorld.Exception.RegisteredException;
import HeadLibs.Helper.HStringHelper;

import java.util.HashMap;
import java.util.Map;

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

    public static IDSTBase get(String name) {
        try {
            if (!dst.containsKey(name))
                throw new IllegalArgumentException("Type is not registered!");
            return dst.get(name).newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String prefix(String name) {
        return HStringHelper.merge("start", name);
    }

    public static String suffix(String name) {
        return HStringHelper.merge("end", name);
    }
}
