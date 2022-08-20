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
public abstract class ElementUtil<Id extends ElementId, Element extends ElementImplement> implements Iterable<Entry<Id, Class<? extends Element>>> {
    protected final HMapRegisterer<Id, Class<? extends Element>> elements = new HMapRegisterer<>(false, false, false);

    public void register(@NotNull Id key, @NotNull Class<? extends Element> value) throws HElementRegisteredException {
        this.elements.register(key, value);
    }

    public void reset(@NotNull Id key, @NotNull Class<? extends Element> value) {
        try {
            this.elements.reset(key, value);
        } catch (HElementRegisteredException ignore) {
        }
    }

    public void deregisterKey(@NotNull Id key) {
        this.elements.deregisterKey(key);
    }

    public void deregisterValue(@NotNull Class<? extends Element> value) {
        this.elements.deregisterValue(value);
    }

    public void deregisterAll() {
        this.elements.deregisterAll();
    }

    public boolean isRegisteredKey(@NotNull Id key) {
        return this.elements.isRegisteredKey(key);
    }

    public boolean isRegisteredValue(@NotNull Class<? extends Element> value) {
        return this.elements.isRegisteredValue(value);
    }

    public @Nullable Class<? extends Element> getElement(@NotNull Id key) throws HElementNotRegisteredException {
        return this.elements.getElement(key);
    }

    public @Nullable Class<? extends Element> getElementNullable(@NotNull Id key) {
        return this.elements.getElementNullable(key);
    }

    public int getRegisteredCount() {
        return this.elements.getRegisteredCount();
    }

    public @NotNull Element getElementInstance(@NotNull Id key) throws HElementNotRegisteredException, NoSuchMethodException {
        return this.getElementInstance(key, false);
    }

    public @NotNull Element getElementInstance(@NotNull Id key, boolean useCache) throws HElementNotRegisteredException, NoSuchMethodException {
        Class<? extends Element> aClass = this.elements.getElement(key);
        if (aClass == null)
            throw new HElementNotRegisteredException(null, key);
        Element instance = HClassHelper.getInstance(aClass, useCache);
        if (instance == null)
            throw new NoSuchMethodException("Failed to create a new instance. [key='" + key + "']");
        return instance;
    }

    @Override
    public @NotNull Iterator<Entry<Id, Class<? extends Element>>> iterator() {
        return this.elements.iterator();
    }

    public @NotNull Collection<Id> keys() {
        return this.elements.keys();
    }

    public @NotNull Collection<Class<? extends Element>> values() {
        return this.elements.values();
    }


    // -------------------- Mod basic information getter --------------------

    public static @NotNull ElementName getElementNameFromClass(@Nullable Class<? extends ElementUtil<?, ?>> elementClass) {
        if (elementClass == null)
            return new ElementName();
        NewElementUtilCore elementAnnouncement = elementClass.getAnnotation(NewElementUtilCore.class);
        if (elementAnnouncement == null)
            return new ElementName();
        return new ElementName(elementAnnouncement.elementName());
    }
}
