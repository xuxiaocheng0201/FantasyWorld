package Core.Mod.New;

import Core.Exceptions.ElementRegisteredException;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Helper.HStringHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class ElementUtil<T extends ElementImplement> {
    protected final Map<String, Class<? extends T>> map = new HashMap<>();

    public void register(String name, Class<? extends T> aClass) throws ElementRegisteredException {
        if (this.map.containsKey(name))
            throw new ElementRegisteredException(HStringHelper.merge("Registered name. name='", name, "' aClass=", aClass));
        if (this.map.containsValue(aClass))
            throw new ElementRegisteredException(HStringHelper.merge("Registered class. name='", name, "' aClass=", aClass));
        this.map.put(name, aClass);
    }

    public void deregister(String name) {
        this.map.remove(name);
    }

    public void deregister(Class<? extends T> aClass) {
        if (!this.map.containsValue(aClass))
            return;
        for (Map.Entry<String, Class<? extends T>> entry : this.map.entrySet())
            if (entry.getValue().equals(aClass))
                this.map.remove(entry.getKey());
    }

    public boolean isRegistered(String name) {
        return this.map.containsKey(name);
    }

    public boolean isRegistered(Class<? extends T> aClass) {
        return this.map.containsValue(aClass);
    }

    public int getRegisteredCount() {
        return this.map.size();
    }

    public T getElementInstance(String name) throws NoSuchElementException, NoSuchMethodException {
        if (!this.map.containsKey(name))
            throw new NoSuchElementException(HStringHelper.merge("Type is not registered! [name='", name, "']"));
        T instance = HClassHelper.getInstance(this.map.get(name));
        if (instance == null)
            throw new NoSuchMethodException(HStringHelper.merge("No common constructor to get instance. [name='", name, "']"));
        return instance;
    }

    public Map<String, Class<? extends T>> getMap() {
        return this.map;
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
