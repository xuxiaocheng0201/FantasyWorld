package Core.Mod;

import Core.Mod.New.ElementImplement;
import Core.Mod.New.ElementUtil;
import Core.Mod.New.ModImplement;
import Core.Mod.New.NewMod;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModManager {
    public static Map<Class<?>, File> getAllClassesWithJarFiles() {
        return ModClassesLoader.getAllClassesWithJarFiles();
    }

    public static List<Class<? extends ModImplement>> getModList() {
        if (ModClassesSorter.getSortedMods().isEmpty())
            return ModClassesLoader.getMods();
        return ModClassesSorter.getSortedMods();
    }

    public static void deleteMod(String modName) {
        if (modName == null)
            return;
        getModList().removeIf(modClass -> modName.equals(HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(modClass.getAnnotation(NewMod.class).name()))));
    }

    public static Map<String, Pair<Class<? extends ElementImplement>, Class<? extends ElementUtil<?>>>> getElementPairList() {
        return ModElementsRegisterer.getElementPairList();
    }

    public static Map<String, elementInstances<?>> getAllElementInstances() {
        return ModElementsRegisterer.getAllElementInstances();
    }

    public static class elementInstances<T extends ElementImplement> {
        private final Class<T> implementClass;
        private final List<Class<? extends T>> instancesClasses = new ArrayList<>();

        public elementInstances(Class<T> implementClass) {
            this.implementClass = implementClass;
        }

        public Class<T> getImplementClass() {
            return implementClass;
        }

        public List<Class<? extends T>> getInstancesClasses() {
            return instancesClasses;
        }

        /**
         * WARNING: Please check type on your own!!!
         * Example: {@link Core.Mod.ModElementsRegisterer#registerElements()}
         */
        @SuppressWarnings("unchecked")
        public void addInstance(Class<?> aClass) {
            this.instancesClasses.add((Class<? extends T>) aClass);
        }

        @Override
        public String toString() {
            return HStringHelper.merge("elementInstances:", instancesClasses);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            elementInstances<?> that = (elementInstances<?>) o;
            return instancesClasses.equals(that.instancesClasses);
        }

        @Override
        public int hashCode() {
            return instancesClasses.hashCode();
        }
    }
}
