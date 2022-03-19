package Core.Mod.New;

import CraftWorld.Exception.RegisteredException;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;

import java.util.HashMap;
import java.util.Map;

public abstract class ElementUtil<T extends ElementImplement> {
    protected final Map<String, Class<? extends T>> map = new HashMap<>();

    public void register(String name, Class<? extends T> aClass) {
        try {
            if (map.containsKey(name))
                throw new RegisteredException(HStringHelper.merge("Registered name. name='", name, "' aClass=", aClass));
            if (map.containsValue(aClass))
                throw new RegisteredException(HStringHelper.merge("Registered class. name='", name, "' aClass=", aClass));
            map.put(name, aClass);
        } catch (RegisteredException exception) {
            exception.printStackTrace();
        }
    }

    public int getRegisteredCount() {
        return map.size();
    }

    public T get(String name) {
        try {
            if (!map.containsKey(name))
                throw new IllegalArgumentException(HStringHelper.merge("Type is not registered! [name='", name, "']"));
            T instance = HClassHelper.getInstance(map.get(name));
            if (instance == null)
                throw new NoSuchMethodException(HStringHelper.merge("No common constructor to get instance. [name='", name, "']"));
            return instance;
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
