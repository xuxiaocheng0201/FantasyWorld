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
import java.util.HashSet;

/**
 * Some tools about {@link Class}
 */
public class HClassHelper {
    private static final Collection<Class<?>> gottenFlags = new HashSet<>();

    /**
     * Get a new instance from a class.
     * If the class contains {@code getInstance()}, it will try invoking it.
     * @param aClass the class
     * @param <T> class's type
     * @return null - failed. notNull - the instance
     */
    public static <T> @Nullable T getInstance(@Nullable Class<T> aClass) {
        if (aClass == null)
            return null;
        T instance = getInstance0(aClass);
        gottenFlags.clear();
        return instance;
    }

    @SuppressWarnings("unchecked")
    private static <T> @Nullable T getInstance0(@Nullable Class<T> aClass) {
        if (aClass == null || gottenFlags.contains(aClass))
            return null;
        try {
            Method get = aClass.getDeclaredMethod("getInstance");
            T instance = (T) get.invoke(null);
            if (instance == null)
                throw new NoSuchMethodException();
            return instance;
        } catch (NoSuchMethodException ignore) {
        } catch (Exception exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            return null;
        }
        try {
            Method get = aClass.getDeclaredMethod("instance");
            T instance = (T) get.invoke(null);
            if (instance == null)
                throw new NoSuchMethodException();
            return instance;
        } catch (NoSuchMethodException ignore) {
        } catch (Exception exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            return null;
        }
        try {
            Field instance = aClass.getDeclaredField("instance");
            return (T) instance.get(null);
        } catch (NullPointerException | NoSuchFieldException ignore) {
        } catch (Exception exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            return null;
        }
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException ignore) {
        } catch (Exception exception) {
            HLog.logger(HLogLevel.ERROR, exception);
            return null;
        }
        Constructor<?>[] constructors = aClass.getDeclaredConstructors();
        for (Constructor<?> constructor: constructors) {
            @SuppressWarnings("SuspiciousArrayCast")
            Class<?>[] types = (Class<?>[]) constructor.getGenericParameterTypes();
            Collection<Object> args = new ArrayList<>(types.length);
            for (Class<?> type: types) {
                if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                    args.add(false);
                    continue;
                }
                if (type.equals(byte.class) || type.equals(Byte.class)
                        || type.equals(short.class) || type.equals(Short.class)
                        || type.equals(char.class) || type.equals(Character.class)
                        || type.equals(int.class) || type.equals(Integer.class)
                        || type.equals(long.class) || type.equals(Long.class)) {
                    args.add(0);
                    continue;
                }
                if (type.equals(float.class) || type.equals(Float.class)
                        || type.equals(double.class) || type.equals(Double.class)) {
                    args.add(0.0);
                    continue;
                }
                if (type.equals(String.class)) {
                    args.add("");
                    continue;
                }
                gottenFlags.add(aClass);
                args.add(getInstance0(type));
            }
            try {
                return (T) constructor.newInstance(args.toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignore) {
            }
        }
        return null;
    }

    public static boolean isInterface(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return aClass.isInterface();
    }

    public static boolean isAnnounce(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return aClass.isAnnotation();
    }

    public static boolean isEnum(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return aClass.isEnum();
    }

    public static boolean isRecord(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return aClass.isRecord();
    }

    public static boolean isAnonymousClass(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return aClass.isAnonymousClass();
    }

    public static boolean isMemberClass(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return aClass.isMemberClass();
    }

    public static boolean isLocalClass(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return aClass.isLocalClass();
    }

    public static boolean isClass(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return !(aClass.isInterface() || aClass.isAnnotation() || aClass.isEnum() || aClass.isRecord());
    }

    public static boolean isNormalClass(@Nullable Class<?> aClass) {
        if (aClass == null)
            return false;
        return !(aClass.isInterface() || aClass.isAnnotation() || aClass.isEnum() || aClass.isRecord()
                || aClass.isAnonymousClass() || aClass.isMemberClass() || aClass.isLocalClass());
    }
}
