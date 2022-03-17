package CraftWorld;

import Core.Craftworld;
import Core.Mod.New.Mod;
import Core.Mod.New.ModImplement;
import HeadLibs.Helper.HFileHelper;

import java.io.File;

@Mod(name = "CraftWorld", version = "0.0.0", require = "before:*")
public class CraftWorld implements ModImplement {
    static {
        if (System.console() != null)
            if (!(new File(Craftworld.ASSETS_PATH)).exists())
                HFileHelper.extractFilesFromJar("assets", Craftworld.ASSETS_PATH);
//        if (System.console() != null)
//            HFileHelper.extractFilesFromJar("natives", "natives");
    }

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
