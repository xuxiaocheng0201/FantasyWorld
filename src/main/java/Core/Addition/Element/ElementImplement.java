package Core.Addition.Element;

import Core.Addition.Element.BasicInformation.ElementName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        if (elementAnnouncement == null)
            return new ElementName();
        return new ElementName(elementAnnouncement.elementName());
    }

    static @NotNull List<ElementName> getParentsElementNameFromClass(@Nullable Class<? extends ElementImplement> elementClass) {
        if (elementClass == null)
            return new ArrayList<>();
        List<ElementName> parents = new ArrayList<>();
        if (elementClass.getSuperclass() != null) {
            if (!ElementImplement.class.isAssignableFrom(elementClass.getSuperclass()))
                return new ArrayList<>();
            @SuppressWarnings("unchecked")
            Class<? extends ElementImplement> superclass = (Class<? extends ElementImplement>) elementClass.getSuperclass();
            parents.addAll(getParentsElementNameFromClass(superclass));
        }
        parents.add(getElementNameFromClass(elementClass));
        for (Class<?> superinterfaces: elementClass.getInterfaces())
            if (ElementImplement.class.isAssignableFrom(superinterfaces)) {
                @SuppressWarnings("unchecked")
                Class<? extends ElementImplement> superinterface = (Class<? extends ElementImplement>) superinterfaces;
                parents.addAll(getParentsElementNameFromClass(superinterface));
            }
        return parents.stream().filter(elementName -> !elementName.isEmpty()).collect(Collectors.toList());
    }
}
