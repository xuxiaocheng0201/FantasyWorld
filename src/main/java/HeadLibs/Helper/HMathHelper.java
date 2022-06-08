package HeadLibs.Helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Quicker math calculations.
 */
@SuppressWarnings({"unused", "MagicNumber", "NumericCastThatLosesPrecision"})
public class HMathHelper {
    /** Archimede's constant PI, ratio of circle circumference to diameter. */
    public static final double PI = 3.14159265358979323846;  // Math.PI
    public static final double HALF_PI = 1.5707963267948966;  // PI / 2
    /** Napier's constant e, base of the natural logarithm. */
    public static final double E = 2.7182818284590452354;  //Math.E

    public static final float SQRT_2 = 1.4142135623730951F;
    public static final float SQRT_3 = 1.7320508075688772F;
    public static final float SQRT_5 = 2.2360679774997897F;
    public static final float SQRT_6 = 2.4494897427831781F;
    public static final float SQRT_7 = 2.6457513110645907F;

    public static double FLOAT_ERROR = 1.0E-5F;

    /** A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with steps of 2*PI / 65536. */
    private static final double[] SIN_TABLE;
    /**
     * Though it looks like an array, this is really more like a mapping.  Key (index of this array) is the upper 5 bits
     * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531.  Value
     * (value stored in the array) is the unique index (from the right) of the leftmost one-bit in a 32-bit unsigned
     * integer that can cause the upper 5 bits to get that value.  Used for highly optimized "find the log-base-2 of
     * this number" calculations.
     */
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    private static final double FRAC_BIAS;
    private static final double[] ASIN_TAB;
    private static final double[] COS_TAB;

    /**
     * sin looked up in a table.
     */
    public static double sin(float value) {
        return SIN_TABLE[(int)(value * 10430.378F) & 65535];
    }

    /**
     * cos looked up in the sin table with the appropriate offset.
     */
    public static double cos(float value) {
        return SIN_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
    }

    private static final double taylor0 = -4.172325134277344E-7;  // -0x7 / (double) 0x1000000
    private static final double taylor1 = 1.0000254133639503;  // 0x1922253 / (double) 0x1000000 * 2 / PI;
    private static final double taylor2 = -2.6529055565837706E-4;  // -0x2ae6 / (double) 0x1000000 * 4 / (PI * PI);
    private static final double taylor3 = -0.1656240165739414;  // -0xa45511 / (double) 0x1000000 * 8 / (PI * PI * PI);
    private static final double taylor4 = -0.0019645325977732703;  // -0x30fd3 / (double) 0x1000000 * 16 / (PI * PI * PI * PI);
    private static final double taylor5 = 0.010257509876096116;  // 0x191cac / (double) 0x1000000 * 32 / (PI * PI * PI * PI * PI);
    private static final double taylor6 = -9.580378236498403E-4;  // -0x3af27 / (double) 0x1000000 * 64 / (PI * PI * PI * PI * PI * PI);

    @SuppressWarnings("OverlyComplexArithmeticExpression")
    public static double sinTaylor(double x) {
        return ((((((((taylor6 * x) + taylor5) * x) + taylor4) * x + taylor3) * x) + taylor2) * x + taylor1) * x + taylor0;
    }

    public static double cosTaylor(double x) {
        return sinTaylor(HALF_PI - x);
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
     * Return the absolute value of an int.
     */
    public static int abs(int value) {
        return value < 0 ? -value : value;
    }

    /**
     * Return the absolute value of a float.
     */
    public static float abs(float value) {
        return value < 0.0F ? -value : value;
    }

    /**
     * Return the absolute value of a double.
     */
    public static double abs(double value) {
        return value < 0.0F ? -value : value;
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
     * BigInteger version of floorDivide()
     */
    public static BigInteger floorDivide(BigInteger value1, BigInteger value2) {
        return value1.signum() < 0 ? value1.negate().subtract(BigInteger.ONE).divide(value2).negate().subtract(BigInteger.ONE) : value1.divide(value2);
    }

    /**
     * BigDecimal version of floorDivide()
     */
    public static BigInteger floorDivide(BigDecimal value1, BigDecimal value2) {
        return value1.signum() < 0 ?
                value1.negate().subtract(BigDecimal.ONE).divide(value2, RoundingMode.FLOOR).negate().toBigInteger().subtract(BigInteger.ONE)
                : value1.divide(value2, RoundingMode.FLOOR).toBigInteger();
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
    public static boolean decimalEquals(float value1, float value2) {
        return abs(value2 - value1) < FLOAT_ERROR;
    }

    /**
     * Compare two double values.
     */
    public static boolean decimalEquals(double value1, double value2) {
        return abs(value2 - value1) < FLOAT_ERROR;
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
        int interval_ = interval;
        if (value < 0)
            interval_ = -interval_;
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
     * Compute 1/sqrt(n) using <a href="https://en.wikipedia.org/wiki/Fast_inverse_square_root">the fast inverse square
     * root</a> with a constant of 0x5FE6EB50C7B537AA.
     */
    public static double quicklyInverseSqrt(double n) {
        double d0 = 0.5D * n;
        long i = Double.doubleToRawLongBits(n);
        i = 6910469410427058090L - (i >> 1);
        double d = Double.longBitsToDouble(i);
        d = d * (1.5D - d0 * d * d);
        return d;
    }

    public static double atan2(double y, double x) {
        double a = x;
        double b = y;
        double d0 = a * a + b * b;
        if (Double.isNaN(d0))
            return Double.NaN;
        boolean flag = b < 0.0D;
        if (flag)
            b = -b;
        boolean flag1 = a < 0.0D;
        if (flag1)
            a = -a;
        boolean flag2 = b > a;
        if (flag2) {
            double d1 = a;
            a = b;
            b = d1;
        }
        double d9 = quicklyInverseSqrt(d0);
        a = a * d9;
        b = b * d9;
        double d2 = FRAC_BIAS + b;
        int i = (int)Double.doubleToRawLongBits(d2);
        double d3 = ASIN_TAB[i];
        double d4 = COS_TAB[i];
        double d5 = d2 - FRAC_BIAS;
        double d6 = b * d4 - a * d5;
        double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
        double d8 = d3 + d7;
        if (flag2)
            d8 = (Math.PI / 2.0D) - d8;
        if (flag1)
            d8 = Math.PI - d8;
        if (flag)
            d8 = -d8;
        return d8;
    }

    static {
        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] {
                 0,  1, 28,  2, 29, 14, 24,  3, 30, 22, 20, 15, 25, 17,  4,  8,
                31, 27, 13, 23, 21, 19, 16,  7, 26, 12, 18,  6, 11,  5, 10,  9};
        FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);

        SIN_TABLE = new double[65536];
        for (int i = 0; i < 65536; ++i)
            SIN_TABLE[i] = StrictMath.sin(i * Math.PI * 2.0D / 65536.0D);
        ASIN_TAB = new double[257];
        COS_TAB = new double[257];
        for (int i = 0; i < 257; ++i)
        {
            double asin = StrictMath.asin(i / 256.0D);
            COS_TAB[i] = StrictMath.cos(asin);
            ASIN_TAB[i] = asin;
        }
    }
}
