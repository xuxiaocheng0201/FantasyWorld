package Core.Addition.Element;

import Core.Addition.Element.BasicInformation.ElementName;
import HeadLibs.Helper.HClassHelper;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Implement at new mods element util classes.
 * @author xuxiaocheng
 * @see NewElementUtilCore
 */
@SuppressWarnings("unused")
public abstract class ElementUtil<T extends ElementImplement> {
    private final HMapRegisterer<String, Class<? extends T>> elements = new HMapRegisterer<>(false);

    public void register(@Nullable String key, @Nullable Class<? extends T> value) throws HElementRegisteredException {
        this.elements.register(key, value);
    }

    public void reset(@Nullable String key, @Nullable Class<? extends T> value) {
        this.elements.reset(key, value);
    }

    public void deregisterKey(@Nullable String key) {
        this.elements.deregisterKey(key);
    }

    public void deregisterValue(@Nullable Class<? extends T> value) {
        this.elements.deregisterValue(value);
    }

    public void deregisterAll() {
        this.elements.deregisterAll();
    }

    public boolean isRegisteredKey(@Nullable String key) {
        return this.elements.isRegisteredKey(key);
    }

    public boolean isRegisteredValue(@Nullable Class<? extends T> value) {
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

    public @NotNull T getElementInstance(String name) throws HElementNotRegisteredException, NoSuchMethodException {
        return this.getElementInstance(name, false);
    }

    public @NotNull T getElementInstance(String name, boolean useCache) throws HElementNotRegisteredException, NoSuchMethodException {
        Class<? extends T> aClass = this.elements.getElement(name);
        if (aClass == null)
            throw new HElementNotRegisteredException(null, name);
        T instance = HClassHelper.getInstance(aClass, useCache);
        if (instance == null)
            throw new NoSuchMethodException("No common constructor to get instance. [name='" + name + "']");
        return instance;
    }

    public @NotNull Set<Map.Entry<String, Class<? extends T>>> getEntries() {
        return this.elements.getMap().entrySet();
    }

    public @NotNull HMapRegisterer<String, Class<? extends T>> getElements() {
        return this.elements;
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
