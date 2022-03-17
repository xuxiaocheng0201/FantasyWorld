package HeadLibs.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HStringHelper {
    public static String noNull(String a) {
        if (a == null)
            return "null";
        return a;
    }

    public static String[] noNull(String[] a) {
        String[] b = new String[a.length];
        for (int i = 0; i < a.length; ++i)
            b[i] = noNull(a[i]);
        return b;
    }

    public static boolean isBlank(String a) {
        if (a == null)
            return true;
        for (byte i: a.getBytes())
            if (i != (byte) ' ' && i != (byte) '\n' && i != (byte) '\t')
                return false;
        return true;
    }

    public static String delBlankHeadAndTail(String a) {
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

    public static String[] delBlankHeadAndTail(String[] a) {
        String[] b = new String[a.length];
        for (int i = 0; i < a.length; ++i)
            b[i] = delBlankHeadAndTail(a[i]);
        return b;
    }

    public static String merge(Object... objects) {
        if (objects.length == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        for (Object i: objects)
            builder.append(i);
        return builder.toString();
    }

    public static String merge(String... strings) {
        if (strings.length == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        for (String i: strings)
            builder.append(i);
        return builder.toString();
    }

    public static String getDate(String format) {
        return (new SimpleDateFormat(format)).format(new Date());
    }

    public static String getDate(String format, Date date) {
        return (new SimpleDateFormat(format)).format(date);
    }
}
