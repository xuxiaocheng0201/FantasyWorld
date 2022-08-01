package HeadLibs.Helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Quicker math calculations.
 */
@SuppressWarnings({"unused", "MagicNumber", "NumericCastThatLosesPrecision"})
public class HMathHelper {
    /** Archimede's constant PI, ratio of circle circumference to diameter. */
    public static final double PI = 3.14159265358979323846;  // Math.PI
    public static final double HALF_PI = 1.5707963267948966;  // PI / 2
    public static final double DOUBLE_PI = 6.283185307179586;  // PI * 2
    /** Napier's constant e, base of the natural logarithm. */
    public static final double E = 2.7182818284590452354;  //Math.E

    public static final float SQRT_2 = 1.4142135623730951F;
    public static final float SQRT_3 = 1.7320508075688772F;
    public static final float SQRT_5 = 2.2360679774997897F;
    public static final float SQRT_6 = 2.4494897427831781F;
    public static final float SQRT_7 = 2.6457513110645907F;

    public static double DECIMAL_ERROR = 1.0E-5F;

    private static final int TABLE_STEP = 65536;
    private static final int ARC_TABLE_STEP = 256;
    /**
     * A table of sin values computed from 0 (inclusive) to 2*pi (exclusive)
     * Step: 2*PI / 65536({@code TABLE_STEP}).
     */
    private static final double[] SIN_TABLE;
    private static final double[] COS_TABLE;
    private static final double[] TAN_TABLE;
    private static final double[] COT_TABLE;
    private static int toTableStep(double angle) {
        return (int)(angle * (TABLE_STEP / 2.0D / PI)) & (TABLE_STEP - 1);
    }

    //private static final double[] ACOS_TABLE;
    /**
     * Though it looks like an array, this is really more like a mapping.  Key (index of this array) is the upper 5 bits
     * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531.  Value
     * (value stored in the array) is the unique index (from the right) of the leftmost one-bit in a 32-bit unsigned
     * integer that can cause the upper 5 bits to get that value.  Used for highly optimized "find the log-base-2 of
     * this number" calculations.
     */
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;

    /**
     * sin looked up in a table.
     */
    public static double sin(double angle) {
        return SIN_TABLE[toTableStep(angle)];
    }

    /**
     * cos looked up in a table.
     */
    public static double cos(double angle) {
        return COS_TABLE[toTableStep(angle)];
    }

    /**
     * tan looked up in a table.
     */
    public static double tan(double angle) {
        return TAN_TABLE[toTableStep(angle)];
    }

    /**
     * cot looked up in a table.
     */
    public static double cot(double angle) {
        return COT_TABLE[toTableStep(angle)];
    }

    /**
     * Return the greatest integer less than or equal to the float argument.
     */
    public static int floor(float value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }

    /**
     * Return the greatest integer less than or equal to the double argument.
     */
    public static int floor(double value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }

    /**
     * Long version of floor().
     */
    public static long longFloor(double value) {
        long i = (long) value;
        return value < i ? i - 1L : i;
    }

    /**
     * Returns the greatest integer more than or equal to the float argument
     */
    public static int ceil(float value) {
        int i = (int) value;
        return value > i ? i + 1 : i;
    }

    /**
     * Returns the greatest integer more than or equal to the double argument
     */
    public static int ceil(double value) {
        int i = (int) value;
        return value > i ? i + 1 : i;
    }

    /**
     * Long version of ceil()
     */
    public static long longCeil(double value) {
        long i = (long) value;
        return value > i ? i + 1L : i;
    }

    /**
     * Return the unsigned value of floor().
     */
    public static int absFloor(double value) {
        return (int) (value >= 0.0D ? value : -value + 1.0D);
    }

    /**
     * Return par cast as an int, and no greater than Integer.MAX_VALUE-1024.
     */
    public static int fastFloor(double value) {
        return (int) (value + 1024.0D) - 1024;
    }

    /**
     * Gets the decimal portion of the given float.
     */
    public static float frac(float number) {
        return number - floor(number);
    }

    /**
     * Gets the decimal portion of the given double.
     */
    public static double frac(double number) {
        return number - floor(number);
    }


    /**
     * Minimum of some int numbers.
     */
    public static int min(int[] values) {
        if (values == null)
            return Integer.MAX_VALUE;
        int min_est = Integer.MAX_VALUE;
        for (int value: values)
            min_est = Math.min(min_est, value);
        return min_est;
    }

    /**
     * Minimum of some float numbers.
     */
    public static float min(float[] values) {
        if (values == null)
            return Float.MAX_VALUE;
        float min_est = Float.MAX_VALUE;
        for (float value: values)
            min_est = Math.min(min_est, value);
        return min_est;
    }

    /**
     * Minimum of some double numbers.
     */
    public static double min(double[] values) {
        if (values == null)
            return Double.MAX_VALUE;
        double min_est = Double.MAX_VALUE;
        for (double value: values)
            min_est = Math.min(min_est, value);
        return min_est;
    }


    /**
     * Maximum of some int numbers.
     */
    public static int max(int[] values) {
        if (values == null)
            return Integer.MIN_VALUE;
        int max_est = Integer.MIN_VALUE;
        for (int value: values)
            max_est = Math.max(max_est, value);
        return max_est;
    }

    /**
     * Maximum of some float numbers.
     */
    public static float max(float[] values) {
        if (values == null)
            return Float.MIN_VALUE;
        float max_est = Float.MIN_VALUE;
        for (float value: values)
            max_est = Math.max(max_est, value);
        return max_est;
    }

    /**
     * Maximum of some double numbers.
     */
    public static double max(double[] values) {
        if (values == null)
            return Double.MIN_VALUE;
        double max_est = Double.MIN_VALUE;
        for (double value: values)
            max_est = Math.max(max_est, value);
        return max_est;
    }

    /**
     * The absolute value of an int.
     */
    public static int abs(int value) {
        return value < 0 ? -value : value;
    }

    /**
     * The absolute value of a float.
     */
    public static float abs(float value) {
        return value < 0.0F ? -value : value;
    }

    /**
     * The absolute value of a double.
     */
    public static double abs(double value) {
        return value < 0.0D ? -value : value;
    }

    /**
     * Minimum of the absolute value of two int numbers.
     */
    public static int absMin(int value1, int value2) {
        int absValue1 = value1;
        int absValue2 = value2;
        if (absValue1 < 0.0D)
            absValue1 = -absValue1;
        if (absValue2 < 0.0D)
            absValue2 = -absValue2;
        return Math.min(absValue1, absValue2);
    }

    /**
     * Minimum of the absolute value of some int numbers.
     */
    public static int absMin(int[] values) {
        if (values == null)
            return 0;
        int min_est = 0;
        for (int value: values)
            min_est = absMin(min_est, value);
        return min_est;
    }

    /**
     * Minimum of the absolute value of two float numbers.
     */
    public static float absMin(float value1, float value2) {
        float absValue1 = value1;
        float absValue2 = value2;
        if (absValue1 < 0.0D)
            absValue1 = -absValue1;
        if (absValue2 < 0.0D)
            absValue2 = -absValue2;
        return Math.min(absValue1, absValue2);
    }

    /**
     * Minimum of the absolute value of some float numbers.
     */
    public static float absMin(float[] values) {
        if (values == null)
            return 0.0F;
        float min_est = 0.0F;
        for (float value: values)
            min_est = absMin(min_est, value);
        return min_est;
    }

    /**
     * Minimum of the absolute value of two double numbers.
     */
    public static double absMin(double value1, double value2) {
        double absValue1 = value1;
        double absValue2 = value2;
        if (absValue1 < 0.0D)
            absValue1 = -absValue1;
        if (absValue2 < 0.0D)
            absValue2 = -absValue2;
        return Math.min(absValue1, absValue2);
    }

    /**
     * Minimum of the absolute value of some double numbers.
     */
    public static double absMin(double[] values) {
        if (values == null)
            return 0.0D;
        double min_est = 0.0D;
        for (double value: values)
            min_est = absMin(min_est, value);
        return min_est;
    }

    /**
     * Maximum of the absolute value of two int numbers.
     */
    public static int absMax(int value1, int value2) {
        int absValue1 = value1;
        int absValue2 = value2;
        if (absValue1 < 0.0D)
            absValue1 = -absValue1;
        if (absValue2 < 0.0D)
            absValue2 = -absValue2;
        return Math.max(absValue1, absValue2);
    }

    /**
     * Maximum of the absolute value of some int numbers.
     */
    public static int absMax(int[] values) {
        if (values == null)
            return 0;
        int max_est = 0;
        for (int value: values)
            max_est = absMax(max_est, value);
        return max_est;
    }

    /**
     * Maximum of the absolute value of two float numbers.
     */
    public static float absMax(float value1, float value2) {
        float absValue1 = value1;
        float absValue2 = value2;
        if (absValue1 < 0.0D)
            absValue1 = -absValue1;
        if (absValue2 < 0.0D)
            absValue2 = -absValue2;
        return Math.max(absValue1, absValue2);
    }

    /**
     * Maximum of the absolute value of some float numbers.
     */
    public static float absMax(float[] values) {
        if (values == null)
            return 0.0F;
        float max_est = 0.0F;
        for (float value: values)
            max_est = absMax(max_est, value);
        return max_est;
    }

    /**
     * Maximum of the absolute value of two double numbers.
     */
    public static double absMax(double value1, double value2) {
        double absValue1 = value1;
        double absValue2 = value2;
        if (absValue1 < 0.0D)
            absValue1 = -absValue1;
        if (absValue2 < 0.0D)
            absValue2 = -absValue2;
        return Math.max(absValue1, absValue2);
    }

    /**
     * Maximum of the absolute value of some double numbers.
     */
    public static double absMax(double[] values) {
        if (values == null)
            return 0.0D;
        double max_est = 0.0D;
        for (double value: values)
            max_est = absMax(max_est, value);
        return max_est;
    }

    /**
     * Return the value of the num, clamped to be within the lower and upper limits.
     */
    public static int clamp(int num, int minimum, int maximum) {
        if (num < minimum)
            return minimum;
        return Math.min(num, maximum);
    }

    /**
     * Return the value of the num, clamped to be within the lower and upper limits.
     */
    public static float clamp(float num, float minimum, float maximum) {
        if (num < minimum)
            return minimum;
        return Math.min(num, maximum);
    }

    /**
     * Return the value of the num, clamped to be within the lower and upper limits.
     */
    public static double clamp(double num, double minimum, double maximum) {
        if (num < minimum)
            return minimum;
        return Math.min(num, maximum);
    }

    /**
     * Return ths slided value, clamped to be within the lower and upper limits.
     */
    public static float clampedLerp(float lowerBnd, float upperBnd, float slide) {
        if (slide < 0.0D)
            return lowerBnd;
        if (slide > 1.0D)
            return upperBnd;
        return lowerBnd + (upperBnd - lowerBnd) * slide;
    }

    /**
     * Return ths slided value, clamped to be within the lower and upper limits.
     */
    public static double clampedLerp(double lowerBnd, double upperBnd, double slide) {
        if (slide < 0.0D)
            return lowerBnd;
        if (slide > 1.0D)
            return upperBnd;
        return lowerBnd + (upperBnd - lowerBnd) * slide;
    }

    /**
     * Return ths slided value, cyclically clamped to be within the lower(inclusive) and upper(exclusive) limits.
     */
    public static int cyclicClamp(int num, int minimum, int maximum) {
        int interval = maximum - minimum;
        if (num < minimum)
            return num + interval * floorDivide(minimum - num, interval) + interval;
        if (num >= maximum)
            return num - interval * floorDivide(num - maximum, interval) - interval;
        return num;
    }

    /**
     * Return ths slided value, cyclically clamped to be within the lower(inclusive) and upper(exclusive) limits.
     */
    public static float cyclicClamp(float num, float minimum, float maximum) {
        float interval = maximum - minimum;
        if (num < minimum)
            return num + interval * floorDivide(minimum - num, interval) + interval;
        if (num >= maximum)
            return num - interval * floorDivide(num - maximum, interval) - interval;
        return num;
    }

    /**
     * Return ths slided value, cyclically clamped to be within the lower(inclusive) and upper(exclusive) limits.
     */
    public static double cyclicClamp(double num, double minimum, double maximum) {
        double interval = maximum - minimum;
        if (num < minimum)
            return num + interval * floorDivide(minimum - num, interval) + interval;
        if (num >= maximum)
            return num - interval * floorDivide(num - maximum, interval) - interval;
        return num;
    }

    /**
     * Buckets an integer with specified bucket sizes.
     */
    public static int floorDivide(int value1, int value2) {
        return value1 < 0 ? -((-value1 - 1) / value2) - 1 : value1 / value2;
    }

    /**
     * Float version of floorDivide().
     */
    public static int floorDivide(float value1, float value2) {
        return value1 < 0 ? -(floor((-value1 - 1) / value2)) - 1 : floor(value1 / value2);
    }

    /**
     * Double version of floorDivide().
     */
    public static int floorDivide(double value1, double value2) {
        return value1 < 0 ? -(floor((-value1 - 1) / value2)) - 1 : floor(value1 / value2);
    }

    /**
     * Return the average of an int array.
     */
    public static double average(int[] values) {
        int i = 0;
        for (int j: values)
            i += j;
        return (double) i / values.length;
    }

    /**
     * Return the average of a float array.
     */
    public static double average(float[] values) {
        float i = 0;
        for (float j: values)
            i += j;
        return (double) i / values.length;
    }

    /**
     * Return the average of a double array.
     */
    public static double average(double[] values) {
        double i = 0;
        for (double j: values)
            i += j;
        return  i / values.length;
    }

    /**
     * Compare two float values.
     */
    public static boolean decimalEqualsWithError(float value1, float value2) {
        return abs(value2 - value1) < DECIMAL_ERROR;
    }

    /**
     * Compare two double values.
     */
    public static boolean decimalEqualsWithError(double value1, double value2) {
        return abs(value2 - value1) < DECIMAL_ERROR;
    }

    /**
     * Parse the string as integer or return the default value if it fails
     */
    public static int getInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    /**
     * Parse the string as integer or return the default value if it fails. This value is capped to minimum.
     */
    public static int getInt(String value, int defaultValue, int minimum) {
        return Math.max(minimum, getInt(value, defaultValue));
    }

    /**
     * Parse the string as integer or return the default value if it fails. This value is clamped.
     */
    public static int getInt(String value, int defaultValue, int minimum, int maximum) {
        return clamp(getInt(value, defaultValue), minimum, maximum);
    }

    /**
     * Parse the string as double or return the default value if it fails
     */
    public static double getDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    /**
     * Parse the string as double or return the default value if it fails. This value is capped to minimum.
     */
    public static double getDouble(String value, double defaultValue, double minimum) {
        return Math.max(minimum, getDouble(value, defaultValue));
    }

    /**
     * Parse the string as double or return the default value if it fails. This value is clamped.
     */
    public static double getDouble(String value, double defaultValue, double minimum, double maximum) {
        return clamp(getDouble(value, defaultValue), minimum, maximum);
    }

    /**
     * Return the input value rounded up to the next power of two.
     */
    public static int roundUpToNextPowerTwo(int value) {
        int n = value - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return clamp(n + 1, 1, 1 << 30);
    }

    /**
     * Return the input value rounded up down the previous power of two.
     */
    public static int roundDownToPreviousPowerTwo(int value) {
        int n = value;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n - (n >>> 1);
    }

    /**
     * Is the given value a power of two?  (1, 2, 4, 8, 16, ...)
     */
    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    /**
     * Return a^n.
     */
    public static long quicklyPower(int a, int n) {
        int value1 = a;
        int value2 = n;
        long res = 1;
        while (value2 != 0) {
            if ((value2 & 1) != 0)
                res *= value1;
            value1 *= value1;
            value2 >>= 1;
        }
        return res;
    }

    /**
     * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
     * value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two,
     * then subtract 1 from the return value.
     */
    public static int quicklyLog2(int value) {
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(((value & -value) * 0x077CB531) >> 27) & 31];
    }

    /**
     * Efficiently calculates the floor of the base-2 log of an integer value.  This is effectively the index of the
     * highest bit that is set.  For example, if the number in binary is 0...100101, this will return 5.
     */
    public static int floorLog2(int value) {
        return quicklyLog2(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    /**
     * Rounds the value up to the next interval of {@code interval}.
     */
    public static int roundUpInterval(int value, int interval) {
        if (interval == 0)
            return value;
        if (value == 0)
            return interval;
        int interval_ = abs(interval);
        int n = value % interval_;
        return n == 0 ? value : value - n + interval_;
    }

    /**
     * Rounds the value down to the next interval of {@code interval}.
     */
    public static int roundDownInterval(int value, int interval) {
        if (interval == 0)
            return value;
        if (value == 0)
            return interval;
        int interval_ = abs(interval);
        return (value / interval_) * interval_;
    }

    /**
     * Compute 1/sqrt(n) using <a href="https://en.wikipedia.org/wiki/Fast_inverse_square_root">the fast inverse square root</a> with a constant of 0x5FE6EB50C7B537AA.
     */
    public static double quicklyInverseSqrt(double n) {
        double d = Double.longBitsToDouble(0x5FE6EB50C7B537AAL - (Double.doubleToRawLongBits(n) >> 1));
        return d * (1.5D - 0.5D * n * d * d);
    }

    public static double atan2(double y, double x) {
        double dx = abs(x);
        double dy = abs(y);
        double a = Math.min(dx, dy) / Math.max(dx, dy);
        //noinspection OverlyComplexArithmeticExpression
        double r = ((-0.0464964749 * a * a + 0.15931422) * a * a - 0.327622764) * a * a * a + a;
        if (dy > dx)
            r = HALF_PI - r;
        if (x < 0)
            r = PI - r;
        if (y < 0)
            r = DOUBLE_PI - r; // range: [0, 2*PI)
            // r = -r; // range:[-PI, PI)
        return r;
    }

    static {
        /* sin, cos, tan, cot */
        try {
            SIN_TABLE = new double[TABLE_STEP];
            COS_TABLE = new double[TABLE_STEP];
            TAN_TABLE = new double[TABLE_STEP];
            COT_TABLE = new double[TABLE_STEP];
        } catch (OutOfMemoryError error) {
            throw new RuntimeException("Failed to create trigonometric functions table.", error);
        }
        for (int i = 0; i < TABLE_STEP; ++i) {
            SIN_TABLE[i] = StrictMath.sin(i * PI * 2.0D / TABLE_STEP);
            COS_TABLE[i] = StrictMath.cos(i * PI * 2.0D / TABLE_STEP);
            TAN_TABLE[i] = StrictMath.tan(i * PI * 2.0D / TABLE_STEP);
            COT_TABLE[i] = 1 / TAN_TABLE[i];
        }


        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] {
                0,  1, 28,  2, 29, 14, 24,  3, 30, 22, 20, 15, 25, 17,  4,  8,
                31, 27, 13, 23, 21, 19, 16,  7, 26, 12, 18,  6, 11,  5, 10,  9};
    }

    public static class BigIntegerHelper {
        /**
         * BigInteger version of floorDivide()
         */
        public static BigInteger floorDivide(BigInteger value1, BigInteger value2) {
            return value1.signum() < 0 ? value1.negate().subtract(BigInteger.ONE).divide(value2).negate().subtract(BigInteger.ONE) : value1.divide(value2);
        }
    }

    public static class BigDecimalHelper {
        public static BigDecimal BigDecimal_TWO = new BigDecimal("2");

        /**
         * BigDecimal version of floorDivide()
         */
        public static BigInteger floorDivide(BigDecimal value1, BigDecimal value2) {
            return value1.signum() < 0 ?
                    value1.negate().subtract(BigDecimal.ONE).divide(value2, RoundingMode.FLOOR).negate().toBigInteger().subtract(BigInteger.ONE)
                    : value1.divide(value2, RoundingMode.FLOOR).toBigInteger();
        }

        public static BigDecimal sqrt(BigDecimal value, int scale) {
            int precision = 100;
            MathContext mc = new MathContext(scale, RoundingMode.HALF_UP);
            BigDecimal deviation = value;
            for (int cnt = 0; cnt < precision; ++cnt)
                deviation = deviation.add(value.divide(deviation, mc)).divide(BigDecimal_TWO, mc);
            return deviation.setScale(scale, RoundingMode.HALF_UP);
        }
    }
}
