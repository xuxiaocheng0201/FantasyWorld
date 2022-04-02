package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class HRandomHelper {
    public static int random(@NotNull Random random, int min, int max) {
        if (min == max)
            return min;
        if (min > max)
            return random(random, max, min);
        return random.nextInt(max - min) + min;
    }
}
