package HeadLibs.DataStructures;

import org.jetbrains.annotations.Nullable;

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
}
