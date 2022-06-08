package HeadLibs.Helper;

public class HCharHelper {
    public static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isBigLetter(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isSmallLetter(char ch) {
        return ch >='a' && ch <= 'z';
    }

    public static boolean isLetter(char ch) {
        return isSmallLetter(ch) || isBigLetter(ch);
    }
}
