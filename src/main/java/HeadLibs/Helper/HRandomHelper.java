package HeadLibs.Helper;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

/**
 * Some tools about {@link Random}
 */
@SuppressWarnings("unused")
public class HRandomHelper {
    private static final RandomGenerator RANDOM = new SecureRandom();

    public static int nextInt(RandomGenerator random, int minimum, int maximum) {
        return minimum >= maximum ? minimum : random.nextInt(maximum - minimum + 1) + minimum;
    }

    public static int nextInt(int minimum, int maximum) {
        return nextInt(RANDOM, minimum, maximum);
    }

    public static long nextLong(RandomGenerator random, long minimum, long maximum) {
        return minimum >= maximum ? minimum : random.nextLong(maximum - minimum + 1) + minimum;
    }

    public static long nextLong(long minimum, long maximum) {
        return nextLong(RANDOM, minimum, maximum);
    }

    public static float nextFloat(RandomGenerator random, float minimum, float maximum) {
        return minimum >= maximum ? minimum : random.nextFloat() * (maximum - minimum) + minimum;
    }

    public static float nextFloat(float minimum, float maximum) {
        return nextFloat(RANDOM, minimum, maximum);
    }

    public static double nextDouble(RandomGenerator random, double minimum, double maximum) {
        return minimum >= maximum ? minimum : random.nextDouble() * (maximum - minimum) + minimum;
    }

    public static double nextDouble(double minimum, double maximum) {
        return nextDouble(RANDOM, minimum, maximum);
    }

    public static String nextString(RandomGenerator random, int size, int minimum, int maximum) {
        if (size < 1)
            return "";
        char[] chars = new char[size];
        for (int i = 0; i < size; ++i)
            //noinspection NumericCastThatLosesPrecision
            chars[i] = (char) nextInt(random, minimum, maximum);
        return new String(chars);
    }

    public static String nextString(int size, int minimum, int maximum) {
        return nextString(RANDOM, size, minimum, maximum);
    }

    public static String nextString(RandomGenerator random, int size) {
        return nextString(random, size, Character.MIN_VALUE, Character.MAX_VALUE);
    }

    public static String nextString(int size) {
        return nextString(RANDOM, size, Character.MIN_VALUE, Character.MAX_VALUE);
    }

    public static String nextString(RandomGenerator random, int minSize, int maxSize, int minimum, int maximum) {
        return nextString(random, nextInt(random, minSize, maxSize), minimum, maximum);
    }

    public static String nextString(int minSize, int maxSize, int minimum, int maximum) {
        return nextString(RANDOM, nextInt(RANDOM, minSize, maxSize), minimum, maximum);
    }

    public static String nextString(RandomGenerator random, int minSize, int maxSize) {
        return nextString(random, nextInt(random, minSize, maxSize), Character.MIN_VALUE, Character.MAX_VALUE);
    }

    public static String nextString(int minSize, int maxSize) {
        return nextString(RANDOM, nextInt(RANDOM, minSize, maxSize), Character.MIN_VALUE, Character.MAX_VALUE);
    }

    /**
     * @see UUID#randomUUID()
     */
    @SuppressWarnings("MagicNumber")
    public static UUID getRandomUUID(RandomGenerator random) {
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        randomBytes[6]  &= 0x0f;
        randomBytes[6]  |= 0x40;
        randomBytes[8]  &= 0x3f;
        randomBytes[8]  |= 0x80;
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; ++i)
            msb = (msb << 8) | (randomBytes[i] & 0xff);
        for (int i = 8; i < 16; ++i)
            lsb = (lsb << 8) | (randomBytes[i] & 0xff);
        return new UUID(msb, lsb);
    }

    public static UUID getRandomUUID() {
        return getRandomUUID(RANDOM);
    }
}
