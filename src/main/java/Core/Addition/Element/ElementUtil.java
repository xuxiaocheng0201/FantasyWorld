package Core.Addition.Element;

import Core.Addition.Element.BasicInformation.ElementName;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Implement at new mods element util classes.
 * @author xuxiaocheng
 * @see NewElementUtilCore
 */
public abstract class ElementUtil<T extends ElementImplement> implements Iterable<Entry<String, Class<? extends T>>> {
    protected final HMapRegisterer<String, Class<? extends T>> elements = new HMapRegisterer<>(false, false, false);

    public void register(@NotNull String key, @NotNull Class<? extends T> value) throws HElementRegisteredException {
        this.elements.register(key, value);
    }

    public void reset(@NotNull String key, @NotNull Class<? extends T> value) {
        try {
            this.elements.reset(key, value);
        } catch (HElementRegisteredException ignore) {
        }
    }

    public void deregisterKey(@NotNull String key) {
        this.elements.deregisterKey(key);
    }

    public void deregisterValue(@NotNull Class<? extends T> value) {
        this.elements.deregisterValue(value);
    }

    public void deregisterAll() {
        this.elements.deregisterAll();
    }

    public boolean isRegisteredKey(@NotNull String key) {
        return this.elements.isRegisteredKey(key);
    }

    public boolean isRegisteredValue(@NotNull Class<? extends T> value) {
        return this.elements.isRegisteredValue(value);
    }

    public @Nullable Class<? extends T> getElement(@NotNull String key) throws HElementNotRegisteredException {
        return this.elements.getElement(key);
    }

    public @Nullable Class<? extends T> getElementNullable(@NotNull String key) {
        return this.elements.getElementNullable(key);
    }

    public int getRegisteredCount() {
        return this.elements.getRegisteredCount();
    }

    public @NotNull T getElementInstance(@NotNull String key) throws HElementNotRegisteredException, NoSuchMethodException {
        return this.getElementInstance(key, false);
    }

    public @NotNull T getElementInstance(@NotNull String key, boolean useCache) throws HElementNotRegisteredException, NoSuchMethodException {
        Class<? extends T> aClass = this.elements.getElement(key);
        if (aClass == null)
            throw new HElementNotRegisteredException(null, key);
        T instance = HClassHelper.getInstance(aClass, useCache);
        if (instance == null)
            throw new NoSuchMethodException("No common constructor to get instance. [key='" + key + "']");
        return instance;
    }

    @Override
    public @NotNull Iterator<Entry<String, Class<? extends T>>> iterator() {
        return this.elements.iterator();
    }

    public @NotNull Collection<String> keys() {
        return this.elements.keys();
    }

    public @NotNull Collection<Class<? extends T>> values() {
        return this.elements.values();
    }
    

    // -------------------- Mod basic information getter --------------------

    public static @NotNull ElementName getElementNameFromClass(@Nullable Class<? extends ElementUtil<?>> elementClass) {
        if (elementClass == null)
            return new ElementName();
        NewElementUtilCore elementAnnouncement = elementClass.getAnnotation(NewElementUtilCore.class);
        if (elementAnnouncement == null)
            return new ElementName();
        return new ElementName(elementAnnouncement.elementName());
    }
}
