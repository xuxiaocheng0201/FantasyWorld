package Core.Addition.Element;

import Core.Addition.Element.BasicInformation.ElementName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implement at new mods element implement classes.
 * @author xuxiaocheng
 * @see NewElementImplementCore
 */
@SuppressWarnings("unused")
public interface ElementImplement {

    // -------------------- Mod basic information getter --------------------

    static @NotNull ElementName getElementNameFromClass(@Nullable Class<? extends ElementImplement> elementClass) {
        if (elementClass == null)
            return new ElementName();
        NewElementImplementCore elementAnnouncement = elementClass.getAnnotation(NewElementImplementCore.class);
        if (elementAnnouncement == null) {
            List<ElementName> names = getParentsElementNameFromClass(elementClass);
            if (names.isEmpty())
                return new ElementName();
            return names.get(0);
        }
        return new ElementName(elementAnnouncement.elementName());
    }

    static @NotNull List<ElementName> getParentsElementNameFromClass(@Nullable Class<? extends ElementImplement> elementClass) {
        if (elementClass == null)
            return new ArrayList<>();
        Collection<ElementName> parents = new ArrayList<>();
        if (elementClass.getSuperclass() != null && ElementImplement.class.isAssignableFrom(elementClass.getSuperclass())) {
            @SuppressWarnings("unchecked")
            Class<? extends ElementImplement> superclass = (Class<? extends ElementImplement>) elementClass.getSuperclass();
            parents.addAll(getParentsElementNameFromClass(superclass));
        }
        NewElementImplementCore elementAnnouncement = elementClass.getAnnotation(NewElementImplementCore.class);
        if (elementAnnouncement != null)
            parents.add(new ElementName(elementAnnouncement.elementName()));
        for (Class<?> superinterfaces: elementClass.getInterfaces())
            if (ElementImplement.class.isAssignableFrom(superinterfaces)) {
                @SuppressWarnings("unchecked")
                Class<? extends ElementImplement> superinterface = (Class<? extends ElementImplement>) superinterfaces;
                parents.addAll(getParentsElementNameFromClass(superinterface));
            }
        return parents.stream().filter(elementName -> !elementName.isEmpty()).collect(Collectors.toList());
    }
}
