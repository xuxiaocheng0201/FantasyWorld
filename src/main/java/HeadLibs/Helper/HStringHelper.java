package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Some tools about {@link String}
 */
@SuppressWarnings({"unused", "OverloadedVarargsMethod"})
public class HStringHelper {
    /**
     * No more null string.
     * @param a source string
     * @return a == null - "null".  a != null - a
     */
    public static @NotNull String noNull(@Nullable String a) {
        if (a == null)
            return "null";
        return a;
    }

    /**
     * No more null strings.
     * @param a source strings
     * @return fixed strings
     */
    public static @NotNull String[] noNull(String @NotNull [] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = (a[i] == null) ? "null" : a[i];
        return b;
    }

    /**
     * Strip string array.
     * @param a source strings
     * @return fixed strings
     */
    public static @NotNull String[] strip(@NotNull String [] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = a[i].strip();
        return b;
    }

    /**
     * Unify string.
     * @param a source string
     * @return fixed string
     */
    public static @NotNull String noNullStrip(@Nullable String a) {
        if (a == null || a.isBlank())
            return "";
        return a.strip();
    }

    /**
     * Does the String have meaning?
     * @param a the string
     * @return true - meaningful. false - meaningless.
     */
    public static boolean hasMeaning(@Nullable String a) {
        if (a == null)
            return false;
        String s = noNullStrip(a);
        if (s.isEmpty())
            return false;
        return !"null".equalsIgnoreCase(s);
    }

    /**
     * Concat objects to string.
     * @param objects source objects
     * @return concatenate string
     */
    public static @NotNull String concat(Object @NotNull ... objects) {
        if (objects.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(3 * objects.length);
        for (Object i: objects)
            builder.append(i);
        return builder.toString();
    }

    /**
     * Concat strings to string
     * @param strings source strings
     * @return concatenate string
     */
    public static @NotNull String concat(String @NotNull ... strings) {
        if (strings.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(5 * strings.length);
        for (String i: strings)
            builder.append(i);
        return builder.toString();
    }

    /**
     * Get current date formatted
     * @param format date format
     * @return current date
     */
    public static @NotNull String getDate(@NotNull String format) {
        return (new SimpleDateFormat(format)).format(new Date());
    }

    /**
     * Get string format of the date
     * @param format date format
     * @param date date to format
     * @return date formatted
     */
    public static @NotNull String getDate(@NotNull String format, @Nullable Date date) {
        return (new SimpleDateFormat(format)).format(date == null ? new Date() : date);
    }
}
