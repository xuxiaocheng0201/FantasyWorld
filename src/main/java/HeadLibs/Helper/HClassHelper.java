package HeadLibs.Helper;

import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Some tools about {@link Class}
 */
public class HClassHelper {
    /**
     * Get a new instance from a class.
     * If the class contains {@code getInstance()}, it will try invoking it.
     * @param aClass the class
     * @param <T> class's type
     * @return null - failed. notNull - the instance
     */
    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getInstance(@Nullable Class<T> aClass) {
        if (aClass == null)
            return null;
        try {
            Method get = aClass.getDeclaredMethod("getInstance");
            T instance = (T) get.invoke(null);
            if (instance == null)
                throw new NoSuchMethodException();
            return instance;
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                HLog.logger(HLogLevel.ERROR, exception);
                return null;
            }
        }
        try {
            Method get = aClass.getDeclaredMethod("instance");
            T instance = (T) get.invoke(null);
            if (instance == null)
                throw new NoSuchMethodException();
            return instance;
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                HLog.logger(HLogLevel.ERROR, exception);
                return null;
            }
        }
        try {
            Field instance = aClass.getDeclaredField("instance");
            return (T) instance.get(null);
        } catch (NullPointerException ignore) {
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchFieldException)) {
                HLog.logger(HLogLevel.ERROR, exception);
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                HLog.logger(HLogLevel.ERROR, exception);
                return null;
            }
        }
        Constructor<?>[] constructors = aClass.getDeclaredConstructors();
        for (Constructor<?> constructor: constructors) {
            @SuppressWarnings("SuspiciousArrayCast")
            Class<?>[] types = (Class<?>[]) constructor.getGenericParameterTypes();
            Collection<Object> args = new ArrayList<>(types.length);
            for (Class<?> type: types) {
                //noinspection IfStatementWithTooManyBranches
                if (type.equals(boolean.class))
                    args.add(false);
                else if (type.equals(byte.class)
                        || type.equals(short.class)
                        || type.equals(char.class)
                        || type.equals(int.class)
                        || type.equals(long.class))
                    args.add(0);
                else if (type.equals(float.class)
                        || type.equals(double.class))
                    args.add(0.0);
                else
                    //todo: Add more common types.
                    args.add(null);
            }
            try {
                return (T) constructor.newInstance(args.toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignore) {
            }
        }
        return null;
    }
}
