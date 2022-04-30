package Core.Addition.Element;

import Core.Addition.Element.BasicInformation.ElementName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
        NewElementImplementCore elementAnnouncement = elementClass.getAnnotation(NewElementImplementCore.class);
        if (elementAnnouncement == null)
            return new ArrayList<>();
        List<ElementName> parents = new ArrayList<>();
        for (String name: elementAnnouncement.parentElements().split(";"))
            parents.add(new ElementName(name));
        return parents;
    }
}
