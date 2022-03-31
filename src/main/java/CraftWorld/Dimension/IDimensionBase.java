package CraftWorld.Dimension;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.NewElementImplement;

@NewElementImplement(elementName = "Dimension")
public interface IDimensionBase extends ElementImplement {
    String getDimensionName();
    void setDimensionName(String name);
}
