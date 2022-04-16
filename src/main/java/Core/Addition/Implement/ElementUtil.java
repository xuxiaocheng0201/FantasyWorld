package Core.Addition.Implement;

import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implement at new mods  element util classes.
 * @author xuxiaocheng
 * @see NewElementUtilCore
 */
@SuppressWarnings("unused")
public abstract class ElementUtil<T extends ElementImplement> extends HMapRegisterer<String, Class<? extends T>> {
    public T getElementInstance(String name) throws HElementNotRegisteredException, NoSuchMethodException {
        Class<? extends T> aClass = this.getElement(name);
        if (aClass == null)
            throw new HElementNotRegisteredException(null, name);
        T instance = HClassHelper.getInstance(aClass);
        if (instance == null)
            throw new NoSuchMethodException(HStringHelper.concat("No common constructor to get instance. [name='", name, "']"));
        return instance;
    }

    public static @NotNull String prefix(@Nullable String name) {
        if (name == null)
            return "";
        return "start" + name;
    }

    public static @NotNull String dePrefix(@Nullable String prefix) {
        if (prefix == null)
            return "";
        if (prefix.startsWith("start"))
            return prefix.substring(5);
        return prefix;
    }

    public static @NotNull String suffix(@Nullable String name) {
        if (name == null)
            return "";
        return "end" + name;
    }

    public static @NotNull String deSuffix(@Nullable String suffix) {
        if (suffix == null)
            return "";
        if (suffix.startsWith("end"))
            return suffix.substring(3);
        return suffix;
    }
}
