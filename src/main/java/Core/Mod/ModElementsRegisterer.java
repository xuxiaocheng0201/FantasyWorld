package Core.Mod;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.ElementUtil;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ModElementsRegisterer {
    private static final Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> elementPairList = new HashMap<>();

    static Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return elementPairList;
    }

    private static final Map<String, List<Class<?>>> allElementInstances = new HashMap<>();

    static Map<String, List<Class<?>>> getAllElementInstances() {
        return allElementInstances;
    }

    static void registerElements() {
        for (String name: ModManager.getElementPairList().keySet()) {
            List<Class<?>> instances = new ArrayList<>();
            HClassFinder classFilter = new HClassFinder();
            classFilter.addSuperClass(ModManager.getElementPairList().get(name).getKey());
            for (Class<?> aClass: ModClassesLoader.getAllClasses())
                if (classFilter.checkSuper(aClass) && !aClass.equals(ModManager.getElementPairList().get(name).getKey()))
                    instances.add(aClass);
            if (instances.isEmpty())
                (new HLog("ModLauncher", Thread.currentThread().getName()))
                        .log(HELogLevel.WARN, "No instance registered for element '", name, "'.");
            allElementInstances.put(name, instances);
        }
    }
}
