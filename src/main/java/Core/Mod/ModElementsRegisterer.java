package Core.Mod;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.ElementUtil;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;

import java.util.HashMap;
import java.util.Map;

class ModElementsRegisterer {
    private static final Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> elementPairList = new HashMap<>();

    static Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return elementPairList;
    }

    private static final Map<String, ModManager.elementInstances<?>> allElementInstances = new HashMap<>();

    static Map<String, ModManager.elementInstances<?>> getAllElementInstances() {
        return allElementInstances;
    }

    static void registerElements() {
        for (String name: ModManager.getElementPairList().keySet()) {
            Class<? extends ElementImplement> implementClass = ModManager.getElementPairList().get(name).getKey();
            ModManager.elementInstances<? extends ElementImplement> instances = new ModManager.elementInstances<>(implementClass);
            HClassFinder classFilter = new HClassFinder();
            classFilter.addSuperClass(implementClass);
            for (Class<?> aClass: ModClassesLoader.getAllClasses())
                if (classFilter.checkSuper(aClass) && !aClass.equals(implementClass))
                    instances.addInstance(aClass);
            if (instances.getInstancesClasses().isEmpty())
                (new HLog("ModLauncher", Thread.currentThread().getName()))
                        .log(HELogLevel.WARN, "No instance registered for element '", name, "'.");
            allElementInstances.put(name, instances);
        }
    }
}
