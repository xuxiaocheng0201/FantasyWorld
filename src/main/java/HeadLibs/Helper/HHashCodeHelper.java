package HeadLibs.Helper;

public class HHashCodeHelper {
    public static long getLong(int a, int size, int offset) {
        return ((long) (a & getMask(size))) << offset;
    }

    public static int getMask(int size) {
        assert (0 < size && size < 32);
        return 0xffffffff >>> (32 - size);
    }

    public static int getMinSigned(int size) {
        return - (1 << (size - 1));
    }

    public static int getMinUnsigned(int size) {
        return - (1 << size);
    }

    public static int getMaxSigned(int size) {
        return (1 << (size - 1)) - 1;
    }

    public static int getMaxUnsigned (int size) {
        return (1 << size) - 1;
    }
}
