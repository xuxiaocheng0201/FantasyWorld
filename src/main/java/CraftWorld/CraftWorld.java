package CraftWorld;

import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;

@Mod(name = "CraftWorld", version = "0.0.0", require = "before:*")
public class CraftWorld implements ModImplement {
    private static final CraftWorld instance = new CraftWorld();
    static {
//        if (System.console() != null)
//            if (!(new File(ASSETS_PATH)).exists())
//                HFileHelper.extractFilesFromJar("assets", ASSETS_PATH);
//        if (System.console() != null)
//            HFileHelper.extractFilesFromJar("natives", "natives");
    }

    public static CraftWorld getInstance() {
        return instance;
    }

    @Override
    public void main() {

    }

    public void start() {

    }
}
