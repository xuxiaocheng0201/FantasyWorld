package CraftWorld.Dimension;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;

@NewElementImplementCore(elementName = "Dimension")
public interface IDimensionBase extends ElementImplement {
    String getDimensionName();
    void setDimensionName(String name);
}
