package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Some tools about {@link Random}
 */
@SuppressWarnings("unused")
public class HRandomHelper {
    /**
     * Get next int in [min, max)
     * @param random Random number generator
     * @param min min border
     * @param max max border
     * @return Random next int
     */
    @SuppressWarnings("GrazieInspection")
    public static int random(@NotNull Random random, int min, int max) {
        if (min == max)
            return min;
        if (min > max)
            return random(random, max, min);
        return random.nextInt(max - min) + min;
    }
}
