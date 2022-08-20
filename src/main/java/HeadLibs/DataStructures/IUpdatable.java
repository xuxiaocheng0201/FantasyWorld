package HeadLibs.DataStructures;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public interface IUpdatable {
    boolean getUpdated();
    void setUpdated(boolean updated);

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

    static @Nullable IUpdatable getUpdatableVersion(@Nullable Object object) {
        if (object == null)
            return null;
        Method method;
        try {
            method = object.getClass().getDeclaredMethod("toUpdatable");
        } catch (NoSuchMethodException exception) {
            return null;
        }
        if (IUpdatable.class.isAssignableFrom(method.getReturnType()))
            try {
                return (IUpdatable) method.invoke(object);
            } catch (IllegalAccessException | InvocationTargetException ignore) {
            }
        return null;
    }
}
