package CraftWorld;

import Core.FileTreeStorage;
import HeadLibs.Annotations.ByteRange;
import HeadLibs.Annotations.CharRange;

public class ConstantStorage {
    public static final String SAVED_WORLD_PATH = FileTreeStorage.RUNTIME_PATH + "saves\\";
    public static final String WORLD_PATH = FileTreeStorage.RUNTIME_PATH + "world";
    public static final @CharRange(minimum = Character.MIN_RADIX, maximum = Character.MAX_RADIX) char SAVE_NUMBER_RADIX = 32;
    public static final @ByteRange(minimum = 1) byte CALCULATE_DECIMAL_DEGREE = 5;
}
