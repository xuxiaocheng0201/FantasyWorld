package HeadLibs.Helper;

import java.math.BigDecimal;
import java.math.BigInteger;

public class HClassHelper {
    public static <T> T getInstance(Class<T> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(String.class).newInstance("");
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(String.class, String.class).newInstance("", "");
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(String.class, String.class, String.class).newInstance("", "", "");
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(boolean.class).newInstance(true);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(boolean.class, boolean.class).newInstance(true, true);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(boolean.class, boolean.class, boolean.class).newInstance(true, true, true);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Boolean.class).newInstance(true);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Boolean.class, Boolean.class).newInstance(true, true);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Boolean.class, Boolean.class, Boolean.class).newInstance(true, true, true);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(char.class).newInstance(' ');
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(char.class, char.class).newInstance(' ', ' ');
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(char.class, char.class, char.class).newInstance(' ', ' ', ' ');
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Character.class).newInstance(' ');
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Character.class, Character.class).newInstance(' ', ' ');
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Character.class, Character.class, Character.class).newInstance(' ', ' ', ' ');
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(int.class).newInstance(0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(int.class, int.class).newInstance(0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(int.class, int.class, int.class).newInstance(0, 0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Integer.class).newInstance(0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Integer.class, Integer.class).newInstance(0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Integer.class, Integer.class, Integer.class).newInstance(0, 0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(float.class).newInstance(0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(float.class, float.class).newInstance(0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(float.class, float.class, float.class).newInstance(0, 0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Float.class).newInstance(0F);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Float.class, Float.class).newInstance(0F, 0F);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Float.class, Float.class, Float.class).newInstance(0F, 0F, 0F);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(double.class).newInstance(0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(double.class, double.class).newInstance(0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(double.class, double.class, double.class).newInstance(0, 0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Double.class).newInstance(0D);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Double.class, Double.class).newInstance(0D, 0D);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Double.class, Double.class, Double.class).newInstance(0D, 0D, 0D);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(long.class).newInstance(0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(long.class, long.class).newInstance(0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(long.class, long.class, long.class).newInstance(0, 0, 0);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Long.class).newInstance(0L);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Long.class, Long.class).newInstance(0L, 0L);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Long.class, Long.class, Long.class).newInstance(0L, 0L, 0L);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(BigInteger.class).newInstance(BigInteger.ZERO);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(BigInteger.class, BigInteger.class).newInstance(BigInteger.ZERO, BigInteger.ZERO);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(BigInteger.class, BigInteger.class, BigInteger.class).newInstance(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(BigDecimal.class).newInstance(BigDecimal.ZERO);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(BigDecimal.class, BigDecimal.class).newInstance(BigDecimal.ZERO, BigDecimal.ZERO);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(BigDecimal.class, BigDecimal.class, BigDecimal.class).newInstance(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        //todo: Add more common constructors
        try {
            return aClass.getDeclaredConstructor(Object.class).newInstance(new Object());
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Object.class, Object.class).newInstance(null, null);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }
        try {
            return aClass.getDeclaredConstructor(Object.class, Object.class, Object.class).newInstance(null, null, null);
        } catch (Exception exception) {
            if (!(exception instanceof NoSuchMethodException)) {
                exception.printStackTrace();
                return null;
            }
        }

//        Constructor<?>[] constructors = aClass.getDeclaredConstructors();
//        for (Constructor<?> constructor: constructors) {
//            Type[] types = constructor.getGenericParameterTypes();
//            Object[] args = new Object[types.length];
//            for (Type type: types) {
//                if (type.getTypeName().equals("String"))
//                    args
//            }
//            constructor.newInstance();
//        }
        return null;
    }
}
