package HeadLibs.Logger;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import HeadLibs.Registerer.HMapRegistererWithName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Level;

/**
 * Log type.
 * @author xuxiaocheng
 * @see HLog
 */
@SuppressWarnings("unused")
public class HLogLevel {
    /**
     * All Registered log types.
     */
    private static final LogRegistererMap REGISTERED_MAP = new LogRegistererMap();
    /**
     * Get registered map.
     * @return registered map.
     */
    public static LogRegistererMap getRegisteredMap() {
        return REGISTERED_MAP;
    }
    public static class LogRegistererMap extends HMapRegistererWithName<HLogLevel> {
        private final HMapRegisterer<Level, String> fromLevelMap = new HMapRegisterer<>();
        private final HMapRegisterer<String, Level> toLevelMap = new HMapRegisterer<>();

        @SuppressWarnings("BoundedWildcard")
        @Override
        public void register(Pair<String, HLogLevel> pair) throws HElementRegisteredException {
            this.register(pair.getKey(), pair.getValue());
        }

        @Override
        public void register(String name, HLogLevel element) throws HElementRegisteredException {
            super.register(name, element);
            this.fromLevelMap.register(element.fromLevel, name);
            this.toLevelMap.register(name, element.toLevel);
        }

        @Override
        public void deregisterKey(String key) {
            super.deregisterKey(key);
            this.fromLevelMap.deregisterValue(key);
            this.toLevelMap.deregisterKey(key);
        }

        @Override
        public void deregisterValue(HLogLevel value) {
            super.deregisterValue(value);
            this.fromLevelMap.deregisterKey(value.fromLevel);
            this.toLevelMap.deregisterValue(value.toLevel);
        }

        @Override
        public void deregisterAll() {
            super.deregisterAll();
            this.fromLevelMap.deregisterAll();
            this.toLevelMap.deregisterAll();
        }

        public HMapRegisterer<Level, String> getFromLevelMap() {
            return this.fromLevelMap;
        }

        public HMapRegisterer<String, Level> getToLevelMap() {
            return this.toLevelMap;
        }
    }

    public static final HLogLevel FINEST = new HLogLevel("FINEST", 0, Level.FINEST);
    public static final HLogLevel FINER = new HLogLevel("FINER", 10, Level.FINER);
    public static final HLogLevel FINE = new HLogLevel("FINE", 20, Level.FINE);
    public static final HLogLevel INFO = new HLogLevel("INFO", 100, Level.INFO);
    public static final HLogLevel WARN = new HLogLevel("WARN", 200, "1;33", Level.WARNING);
    public static final HLogLevel CONFIGURATION = new HLogLevel("CONFIGURATION", 230, "3;35", Level.CONFIG);
    public static final HLogLevel MISTAKE = new HLogLevel("MISTAKE", 250, "1;3;34", null, Level.WARNING);
    public static final HLogLevel ERROR = new HLogLevel("ERROR", 300, "1;4;30;41", null, Level.WARNING);
    public static final HLogLevel BUG = new HLogLevel("BUG", 400, "1;4;41", null, Level.WARNING);
    public static final HLogLevel FAULT = new HLogLevel("FAULT", 500, "1;3;30;41", Level.OFF);
    public static final HLogLevel NORMAL = new HLogLevel("NORMAL", 0, "0", Level.ALL);
    public static final HLogLevel DEBUG = new HLogLevel("DEBUG", 600, "1;4;30;44");

    private final @NotNull String name;
    // Higher priority means higher chance to be logged when same date.
    private final int priority;
    /* Prefix options explanation.
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
    private final @NotNull String prefix;
    /**
     * Map {@link HLogLevel} to {@link Level}
     */
    private final @NotNull Level fromLevel;
    private final @NotNull Level toLevel;

    public HLogLevel(@NotNull String name, int priority, @Nullable String prefix, @Nullable Level fromLevel, @Nullable Level toLevel) {
        super();
        this.name = name;
        this.priority = priority;
        this.prefix = (prefix == null || prefix.isBlank()) ? "" : HStringHelper.concat("\033[", prefix, "m");
        this.fromLevel = fromLevel == null  ? Level.INFO : fromLevel;
        this.toLevel = toLevel == null ? Level.INFO : toLevel;
        REGISTERED_MAP.register(this.name, this);
    }

    public HLogLevel(@NotNull String name, int priority, @Nullable String prefix, @Nullable Level equalLevel) {
        this(name, priority, prefix, equalLevel, equalLevel);
    }

    public HLogLevel(@NotNull String name, int priority, @Nullable String prefix) {
        this(name, priority, prefix, null, null);
    }

    public HLogLevel(@NotNull String name, int priority, @Nullable Level fromLevel, @Nullable Level toLevel) {
        this(name, priority, null, fromLevel, toLevel);
    }

    public HLogLevel(@NotNull String name, int priority, @Nullable Level equalLevel) {
        this(name, priority, null, equalLevel, equalLevel);
    }

    public HLogLevel(@NotNull String name, int priority) {
        this(name, priority, null, null, null);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public int getPriority() {
        return this.priority;
    }

    public @NotNull String getPrefix() {
        return this.prefix;
    }

    public @NotNull Level getFromLevel() {
        return this.fromLevel;
    }

    public @NotNull Level getToLevel() {
        return this.toLevel;
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("HLogLevel{",
                "name='", this.name, '\'',
                '}');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HLogLevel hLogLevel = (HLogLevel) o;
        return this.priority == hLogLevel.priority && this.name.equals(hLogLevel.name) && this.prefix.equals(hLogLevel.prefix) && Objects.equals(this.fromLevel, hLogLevel.fromLevel) && Objects.equals(this.toLevel, hLogLevel.toLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.priority, this.prefix);
    }

    public static HLogLevel mapFromLevel(Level level) {
        return REGISTERED_MAP.getElement(REGISTERED_MAP.fromLevelMap.getElement(level));
    }

    public static Level mapToLevel(HLogLevel level) {
        return level.toLevel;
    }
}
