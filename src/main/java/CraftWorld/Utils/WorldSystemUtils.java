package CraftWorld.Utils;

import HeadLibs.Helper.HRandomHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

public class WorldSystemUtils {
    public static @NotNull RandomGenerator getRandom(@Nullable String randomSeed) {
        if (randomSeed == null)
            return HRandomHelper.RANDOM;
        return new Random(HRandomHelper.getSeed(randomSeed));
    }

    @SuppressWarnings("MagicNumber")
    public static @Nullable String getDimensionSeed(@Nullable String worldRandomSeed, @NotNull UUID dimensionUUID) {
        if (worldRandomSeed == null)
            return null;
        return HRandomHelper.nextString(getRandom(worldRandomSeed + " in Dimension:" + dimensionUUID), 1, 50);
    }
}
