package HeadLibs.DataStructures;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Random;

public interface IUpdatable {
    boolean getUpdated();
    void setUpdated(boolean updated);

    @SuppressWarnings("UnsecureRandomNumberGeneration")
    static long getSerialVersionUID(long sourceUID) {
        return (new Random(sourceUID)).nextLong();
    }

    static boolean hasUpdatableVersion(@Nullable Class<?> object) {
        if (object == null)
            return false;
        Method method;
        try {
            method = object.getDeclaredMethod("toUpdatable");
        } catch (NoSuchMethodException exception) {
            return false;
        }
        return IUpdatable.class.isAssignableFrom(method.getReturnType());
    }
}
