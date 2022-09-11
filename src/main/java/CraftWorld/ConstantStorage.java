package CraftWorld;

import Core.FileTreeStorage;
import HeadLibs.Annotations.ByteRange;
import HeadLibs.Annotations.CharRange;
import HeadLibs.Annotations.IntRange;

public class ConstantStorage {
    public static final String SAVED_WORLD_PATH = FileTreeStorage.RUNTIME_PATH + "saves\\";
    public static final String WORLD_PATH = FileTreeStorage.RUNTIME_PATH + "world";
    public static final @CharRange(minimum = Character.MIN_RADIX, maximum = Character.MAX_RADIX) char SAVE_NUMBER_RADIX = 32;
    public static final @ByteRange(minimum = 1) byte CALCULATE_DECIMAL_DEGREE = 5;
    /** All the following needs to be compressed by {@code GZIPStream}
     *   In buffer 0~15: Packet UUID. Each packet of sent information is unique.    (long(8), long(8))
     *   In buffer 16~23: Packet index and size.    (int(4), int(4))
     *   In buffer 24~1024: User defined sent information.
     */
    public static final @IntRange(minimum = 18) int NETWORK_BYTEBUFFER_SIZE = 1024;
}
