package CraftWorld;

import Core.FileTreeStorage;
import org.jetbrains.annotations.Range;

public class ConstantStorage {
    public static final String SAVED_WORLD_PATH = FileTreeStorage.RUNTIME_PATH + "saves\\";
    public static final String WORLD_PATH = FileTreeStorage.RUNTIME_PATH + "world";
    public static final @Range(from = Character.MIN_RADIX, to = Character.MAX_RADIX) int SAVE_NUMBER_RADIX = 32;
    public static final @Range(from = 1, to = Byte.MAX_VALUE) int CALCULATE_DECIMAL_DEGREE = 5;
}
