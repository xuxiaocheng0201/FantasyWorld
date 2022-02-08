package HeadLibs.Helper;

import java.util.Random;

public class HRandomHelper {
    public static int random(Random random, int min, int max) {
        if (min == max)
            return min;
        if (min > max)
            return random(random, max, min);
        return random.nextInt(max - min) + min;
    }
}
