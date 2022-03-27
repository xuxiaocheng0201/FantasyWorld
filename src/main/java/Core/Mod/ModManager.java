package Core.Mod;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.ElementUtil;
import Core.Mod.New.ModImplement;
import HeadLibs.Pair;

import java.util.ArrayList;
import java.util.List;

public class ModManager {
    private static final List<Class<? extends ModImplement>> modList = new ArrayList<>();
    private static final List<Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> elementList = new ArrayList<>();

    public static List<Class<? extends ModImplement>> getModList() {
        return modList;
    }

    public static List<Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementList() {
        return elementList;
    }
}
