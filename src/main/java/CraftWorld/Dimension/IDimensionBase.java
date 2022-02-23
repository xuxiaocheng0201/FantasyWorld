package CraftWorld.Dimension;

import Mod.NewElementImplement;

@NewElementImplement(name = "Dimension")
public interface IDimensionBase {
    String getName();
    void setName(String name);
}
