package CraftWorld.Dimension;

import Core.Mod.NewElement.NewElementImplement;

@NewElementImplement(name = "Dimension")
public interface IDimensionBase {
    String getName();
    void setName(String name);
}
