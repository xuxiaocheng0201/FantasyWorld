package HeadLibs.Logger;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public enum HELogLevel {
    FINEST("FINEST", 0),
    FINER("FINER", 10),
    FINE("FINE", 20),
    INFO("INFO", 100),
    WARN("WARN", 200, "1;33"),
    CONFIGURATION("CONFIGURATION", 230, "3;35"),
    MISTAKE("MISTAKE", 250, "1;3;34"),
    ERROR("ERROR", 300, "1;4;34;41"),
    BUG("BUG", 400, "1;4;41"),
    FAULT("FAULT", 500, "1;3;30;41"),
    NORMAL("NORMAL", 0, "0"),
    DEBUG("DEBUG", 600, "1;4;30;44");

    private final String name;
    // Higher priority means higher chance to be logged.
    private final int PRIORITY;
    /**
     * From https://www.cnblogs.com/gzj03/p/14425860.html
     * Format: "\033[number;.....;number;number m"
     * number:
     * 0 Default; 1 Wider; 2 Normal; 3 Italic; 4 underline; 9 line!
     * Foreground color 30-37; Background color 40-47.
     *
     * example. System.out.println("\033[31;0;42;30;1;2;3;4;41m" + "Hello");
     *
     * Colour meanings.
     * 0 -> Black
     * 1 -> Red
     * 2 -> Green
     * 3 -> Brown
     * 4 -> Blue
     * 5 -> Purple
     * 6 -> Cyan
     * 7 -> Gray
     */
    private final @NotNull String PREFIX;

    HELogLevel(String name, int priority) {
        this.name = name;
        this.PRIORITY = priority;
        this.PREFIX = "";
    }

    HELogLevel(String name, int priority, String prefix) {
        this.name = name;
        this.PRIORITY = priority;
        this.PREFIX = HStringHelper.merge("\033[", prefix, "m");
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return PRIORITY;
    }

    public @NotNull String getPrefix() {
        return PREFIX;
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.merge("HELogLevel{",
                "name='", name, '\'',
                '}');
    }

    public static @NotNull HELogLevel getFromLevel(@NotNull Level level) {
        if (level.equals(Level.INFO))
            return HELogLevel.INFO;
        if (level.equals(Level.FINE))
            return HELogLevel.FINE;
        if (level.equals(Level.FINER))
            return HELogLevel.FINER;
        if (level.equals(Level.FINEST))
            return HELogLevel.FINEST;
        if (level.equals(Level.CONFIG))
            return HELogLevel.CONFIGURATION;
        if (level.equals(Level.WARNING))
            return HELogLevel.WARN;
        if (level.equals(Level.SEVERE))
            return HELogLevel.NORMAL;
        if (level.equals(Level.ALL))
            return HELogLevel.INFO;
        if (level.equals(Level.OFF))
            return HELogLevel.BUG;
        return HELogLevel.DEBUG;
    }
}
