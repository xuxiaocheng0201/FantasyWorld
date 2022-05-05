package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HTimeHelper {
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

    public static int getHour() {
        return Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
    }
}
