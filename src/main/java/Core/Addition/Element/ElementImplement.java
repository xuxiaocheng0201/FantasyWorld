package Core.Addition.Element;

import Core.Addition.Element.BasicInformation.ElementName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
