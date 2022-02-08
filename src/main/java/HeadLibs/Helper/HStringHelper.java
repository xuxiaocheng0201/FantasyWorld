package HeadLibs.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HStringHelper {
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
