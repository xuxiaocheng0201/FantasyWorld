package Core.Mod;

import Core.Mod.New.NewElementImplement;
import HeadLibs.ClassFinder.HClassFinder;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Pair;

import java.util.ArrayList;
import java.util.List;

public class ModElementRegisterer {
    private static final List<Pair<String, List<Class<?>>>> allElements = new ArrayList<>();

    public static List<Pair<String, List<Class<?>>>> getAllElements() {
        return allElements;
    }

    public static void registerElements() {
        for (Pair<Class<?>, Class<?>> pair: ModClassesLoader.getElementList()) {
            String name = HStringHelper.noNull(pair.getKey().getAnnotation(NewElementImplement.class).name());
            List<Class<?>> instances = new ArrayList<>();
            HClassFinder classFilter = new HClassFinder();
            classFilter.addSuperClass(pair.getKey());
            for (Class<?> aClass: ModClassesLoader.getAllClasses())
                if (classFilter.checkSuper(aClass) && !aClass.equals(pair.getKey()))
                    instances.add(aClass);
            if (instances.isEmpty())
                (new HLog("ModElementRegisterer", Thread.currentThread().getName()))
                        .log(HELogLevel.WARN, "No instance registered for element '", name, "'.");
            allElements.add(new Pair<>(name, instances));
        }
    }
}
