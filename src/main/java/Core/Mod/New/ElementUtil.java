package Core.Mod.New;

import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Registerer.ElementNotRegisteredException;
import HeadLibs.Registerer.MapRegisterer;

public abstract class ElementUtil<T extends ElementImplement> extends MapRegisterer<Class<? extends T>> {
    public T getElementInstance(String name) throws ElementNotRegisteredException, NoSuchMethodException {
        T instance = HClassHelper.getInstance(this.getElement(name));
        if (instance == null)
            throw new NoSuchMethodException(HStringHelper.merge("No common constructor to get instance. [name='", name, "']"));
        return instance;
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
