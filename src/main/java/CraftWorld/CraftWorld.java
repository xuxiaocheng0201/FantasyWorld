package CraftWorld;

import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;

@Mod(name = "CraftWorld", version = "0.0.0", require = "before:*")
public class CraftWorld implements ModImplement {
    private static final CraftWorld instance = new CraftWorld();

    public static CraftWorld getInstance() {
        return instance;
    }

    @Override
    public void main() {

    }

    public void start() {

    }
}
