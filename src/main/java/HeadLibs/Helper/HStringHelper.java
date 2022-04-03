package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("OverloadedVarargsMethod")
public class HStringHelper {
    public static @NotNull String noNull(@Nullable String a) {
        if (a == null)
            return "null";
        return a;
    }

    public static @NotNull String[] noNull(String @NotNull [] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = noNull(a[i]);
        return b;
    }

    public static boolean isBlank(@Nullable String a) {
        if (a == null)
            return true;
        for (byte i: a.getBytes())
            if (i != (byte) ' ' && i != (byte) '\n' && i != (byte) '\t')
                return false;
        return true;
    }

    public static @Nullable String delBlankHeadAndTail(@Nullable String a) {
        if (a == null)
            return null;
        int left = a.length() - 1;
        for (int i = 0; i < a.length(); ++i)
            if (a.charAt(i) != ' ' && a.charAt(i) != '\n' && a.charAt(i) != '\t') {
                left = i;
                break;
            }
        int right = 0;
        for (int i = a.length() - 1; i > left; --i)
            if (a.charAt(i) != ' ' && a.charAt(i) != '\n' && a.charAt(i) != '\t') {
                right = i;
                break;
            }
        if (left > right)
            return "";
        if (left < 0)
            return "";
        return a.substring(left, right + 1);
    }

    public static @NotNull String [] delBlankHeadAndTail(String @NotNull [] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = delBlankHeadAndTail(a[i]);
        return b;
    }

    public static @NotNull String merge(Object @NotNull ... objects) {
        if (objects.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(3 * objects.length);
        for (Object i: objects)
            builder.append(i);
        return builder.toString();
    }

    public static @NotNull String merge(String @NotNull ... strings) {
        if (strings.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(5 * strings.length);
        for (String i: strings)
            builder.append(i);
        return builder.toString();
    }

    public static @NotNull String getDate(@NotNull String format) {
        return (new SimpleDateFormat(format)).format(new Date());
    }

    public static @NotNull String getDate(@NotNull String format, Date date) {
        return (new SimpleDateFormat(format)).format(date);
    }
}
