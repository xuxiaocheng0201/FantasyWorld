package CraftWorld.Utils;

import HeadLibs.Helper.HRandomHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

public class WorldSystemUtils {
    public static final int MIN_RANDOM_SEED_SIZE = 1;
    public static final int MAX_RANDOM_SEED_SIZE = 50;

    @SuppressWarnings("NumericCastThatLosesPrecision")
    public static @NotNull String getRandomSeed(@Nullable RandomGenerator randomGenerator) {
        RandomGenerator random = randomGenerator == null ? HRandomHelper.RANDOM : randomGenerator;
        int size = HRandomHelper.nextInt(random, MIN_RANDOM_SEED_SIZE, MAX_RANDOM_SEED_SIZE);
        char[] chars = new char[size];
        for (int i = 0; i < size; ++i)
            switch (HRandomHelper.nextInt(random, 0, 2)) {
                case 0 -> chars[i] = (char) HRandomHelper.nextInt(random, 'a', 'z');
                case 1 -> chars[i] = (char) HRandomHelper.nextInt(random, 'A', 'Z');
                case 2 -> chars[i] = (char) HRandomHelper.nextInt(random, '0', '9');
            }
        return new String(chars);
    }

    public static @NotNull RandomGenerator getRandom(@Nullable String randomSeed) {
        if (randomSeed == null)
            return HRandomHelper.RANDOM;
        return new Random(HRandomHelper.getSeed(randomSeed));
    }

    public static @Nullable String getDimensionSeed(@Nullable String worldRandomSeed, @NotNull UUID dimensionUUID) {
        if (worldRandomSeed == null)
            return null;
        return getRandomSeed(getRandom(worldRandomSeed + " in Dimension:" + dimensionUUID));
    }
}
