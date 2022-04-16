package CraftWorld.Dimension;

import Core.Addition.Implement.ElementImplement;
import Core.Addition.Implement.NewElementImplementCore;

@NewElementImplementCore(elementName = "Dimension")
public interface IDimensionBase extends ElementImplement {
    String getDimensionName();
    void setDimensionName(String name);
}
