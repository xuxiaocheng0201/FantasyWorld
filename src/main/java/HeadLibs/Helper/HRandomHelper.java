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

    /**
     * @see UUID#randomUUID()
     */
    @SuppressWarnings("MagicNumber")
    public static UUID getRandomUUID(RandomGenerator random) {
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        randomBytes[6]  &= 0x0f;  /* clear version        */
        randomBytes[6]  |= 0x40;  /* set to version 4     */
        randomBytes[8]  &= 0x3f;  /* clear variant        */
        randomBytes[8]  |= 0x80;  /* set to IETF variant  */
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
