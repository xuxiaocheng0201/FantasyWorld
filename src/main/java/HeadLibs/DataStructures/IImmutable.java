package HeadLibs.DataStructures;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface IImmutable {
    static long getSerialVersionUID(long sourceUID) {
        return sourceUID;
    }

    static boolean hasImmutableVersion(@Nullable Class<?> object) {
        if (object == null)
            return false;
        Method method;
        try {
            method = object.getDeclaredMethod("toImmutable");
        } catch (NoSuchMethodException exception) {
            return false;
        }
        return IImmutable.class.isAssignableFrom(method.getReturnType());
    }

    static @Nullable IImmutable getImmutableVersion(@Nullable Object object) {
        if (object == null)
            return null;
        Method method;
        try {
            method = object.getClass().getDeclaredMethod("toImmutable");
        } catch (NoSuchMethodException exception) {
            return null;
        }
        if (IImmutable.class.isAssignableFrom(method.getReturnType()))
            try {
                return (IImmutable) method.invoke(object);
            } catch (IllegalAccessException | InvocationTargetException ignore) {
            }
        return null;
    }
}
