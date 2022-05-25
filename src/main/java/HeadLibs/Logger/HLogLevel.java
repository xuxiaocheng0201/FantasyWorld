package HeadLibs.Logger;

import HeadLibs.DataStructures.Pair;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
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
    private static final @NotNull LogRegistererMap REGISTERED_MAP = new LogRegistererMap();
    /**
     * Get registered map.
     * @return registered map.
     */
    public static @NotNull LogRegistererMap getRegisteredMap() {
        return REGISTERED_MAP;
    }

    public static final @NotNull HLogLevel FINEST = new HLogLevel("FINEST", 0, Level.FINEST);
    public static final @NotNull HLogLevel FINER = new HLogLevel("FINER", 10, Level.FINER);
    public static final @NotNull HLogLevel FINE = new HLogLevel("FINE", 20, Level.FINE);
    public static final @NotNull HLogLevel INFO = new HLogLevel("INFO", 100, Level.INFO);
    public static final @NotNull HLogLevel WARN = new HLogLevel("WARN", 200, "1;33", Level.WARNING);
    public static final @NotNull HLogLevel CONFIGURATION = new HLogLevel("CONFIGURATION", 230, "3;35", Level.CONFIG);
    public static final @NotNull HLogLevel MISTAKE = new HLogLevel("MISTAKE", 250, "1;3;34", null, Level.WARNING);
    public static final @NotNull HLogLevel ERROR = new HLogLevel("ERROR", 300, "1;4;30;41", null, Level.WARNING);
    public static final @NotNull HLogLevel BUG = new HLogLevel("BUG", 400, "1;4;41", null, Level.WARNING);
    public static final @NotNull HLogLevel FAULT = new HLogLevel("FAULT", 500, "1;3;30;41", Level.OFF);
    public static final @NotNull HLogLevel NORMAL = new HLogLevel("NORMAL", 0, "0", Level.ALL);
    public static final @NotNull HLogLevel DEBUG = new HLogLevel("DEBUG", 600, "1;4;30;44");
    static {
        try {FINEST.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {FINER.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {FINE.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {INFO.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {WARN.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {CONFIGURATION.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {MISTAKE.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {ERROR.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {BUG.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {FAULT.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {NORMAL.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {DEBUG.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
    }

    private static final @NotNull HLogLevel defaultHLogLevel = INFO;
    private static final @NotNull Level defaultLevel = Level.INFO;

    /**
     * Log level name.
     */
    private final @NotNull String name;
    /**
     * Log priority when the date is same.
     * Higher priority means higher chance to be logged when same date.
     */
    private final int priority;
    /**
     * Color options.
     */
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
     * Map {@link Level} to {@link HLogLevel}
     */
    private final @Nullable Level fromLevel;
    /**
     * Map {@link HLogLevel} to {@link Level}
     */
    private final @Nullable Level toLevel;

    /**
     * Register a new log level.
     * @param name level name
     * @param priority level priority
     * @param prefix level color option
     * @param fromLevel map from {@link Level}
     * @param toLevel map to {@link Level}
     */
    public HLogLevel(@NotNull String name, int priority, @Nullable String prefix, @Nullable Level fromLevel, @Nullable Level toLevel) {
        super();
        this.name = name;
        this.priority = priority;
        this.prefix = (prefix == null || prefix.isBlank()) ? "" : "\033[" + prefix.strip() + "m";
        this.fromLevel = fromLevel;
        this.toLevel = toLevel;
    }

    /**
     * Register a new log level.
     * @param name level name
     * @param priority level priority
     * @param prefix level color option
     * @param equalLevel map from and to {@link Level}
     */
    public HLogLevel(@NotNull String name, int priority, @Nullable String prefix, @Nullable Level equalLevel) {
        this(name, priority, prefix, equalLevel, equalLevel);
    }

    /**
     * Register a new log level.
     * @param name level name
     * @param priority level priority
     * @param prefix level color option
     */
    public HLogLevel(@NotNull String name, int priority, @Nullable String prefix) {
        this(name, priority, prefix, null, null);
    }

    /**
     * Register a new log level.
     * @param name level name
     * @param priority level priority
     * @param fromLevel map from {@link Level}
     * @param toLevel map to {@link Level}
     */
    public HLogLevel(@NotNull String name, int priority, @Nullable Level fromLevel, @Nullable Level toLevel) {
        this(name, priority, null, fromLevel, toLevel);
    }

    /**
     * Register a new log level.
     * @param name level name
     * @param priority level priority
     * @param equalLevel map from and to {@link Level}
     */
    public HLogLevel(@NotNull String name, int priority, @Nullable Level equalLevel) {
        this(name, priority, null, equalLevel, equalLevel);
    }

    /**
     * Register a new log level.
     * @param name level name
     * @param priority level priority
     */
    public HLogLevel(@NotNull String name, int priority) {
        this(name, priority, null, null, null);
    }

    /**
     * Register this log level to registerer map.
     * @throws HElementRegisteredException Level has been registered.
     */
    public void register() throws HElementRegisteredException {
        REGISTERED_MAP.register(this.name, this);
    }

    /**
     * Get level name.
     * @return level name
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Get level priority.
     * @return level priority
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Get level color option.
     * @return level color option
     */
    public @NotNull String getPrefix() {
        return this.prefix;
    }

    /**
     * Get level map form {@link Level}
     * @return {@link Level} mapped from
     */
    public @Nullable Level getFromLevel() {
        return this.fromLevel;
    }

    /**
     * Get level map to {@link Level}
     * @return {@link Level} mapped to
     */
    public @Nullable Level getToLevel() {
        return this.toLevel;
    }

    @Override
    public @NotNull String toString() {
        return "HLogLevel:" + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HLogLevel that)) return false;
        return this.priority == that.priority && this.name.equals(that.name) && Objects.equals(this.fromLevel, that.fromLevel) && Objects.equals(this.toLevel, that.toLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.priority);
    }

    /**
     * Get {@code HLogLevel} mapped from {@code Level}
     * @param level from {@code Level}
     * @return mapped {@code HLogLevel}
     */
    public static @NotNull HLogLevel mapFromLevel(@NotNull Level level) {
        try {
            String resultName = REGISTERED_MAP.fromLevelMap.getElement(level);
            if (resultName == null)
                return defaultHLogLevel;
            HLogLevel result = REGISTERED_MAP.getElement(resultName);
            if (result == null)
                return defaultHLogLevel;
            return result;
        } catch (HElementNotRegisteredException exception) {
            return defaultHLogLevel;
        }
    }

    /**
     * Get {@code Level} mapped to {@code HLogLevel}
     * @param level from {@code HLogLevel}
     * @return mapped {@code Level}
     */
    public static @NotNull Level mapToLevel(@NotNull HLogLevel level) {
        return level.toLevel == null ? defaultLevel : level.toLevel;
    }

    /**
     * Log level registerer.
     */
    public static class LogRegistererMap extends HMapRegisterer<String, HLogLevel> {
        @Serial
        private static final long serialVersionUID = 4656771989896754933L;

        /**
         * Map {@link Level} to {@link HLogLevel} registerer.
         */
        private final @NotNull HMapRegisterer<Level, String> fromLevelMap = new HMapRegisterer<>(true);
        /**
         * Map {@link HLogLevel} to {@link Level} registerer.
         */
        private final @NotNull HMapRegisterer<String, Level> toLevelMap = new HMapRegisterer<>(true);

        /**
         * Register a new log level.
         * @param element log level
         * @throws HElementRegisteredException Registered.
         */
        public void register(@NotNull HLogLevel element) throws HElementRegisteredException {
            this.register(element.name, element);
        }

        /**
         * Use {@link HLogLevel.LogRegistererMap#register(HLogLevel)}
         */
        @Override
        @Deprecated
        public void register(@NotNull Pair<? extends String, ? extends HLogLevel> pair) throws HElementRegisteredException {
            this.register(pair.getKey(), pair.getValue());
        }

        /**
         * Use {@link HLogLevel.LogRegistererMap#register(HLogLevel)}
         */
        @Override
        @Deprecated
        public void register(@Nullable String name, @Nullable HLogLevel element) throws HElementRegisteredException {
            super.register(name, element);
            if (element != null) {
                if (element.fromLevel != null)
                    this.fromLevelMap.register(element.fromLevel, name);
                if (element.toLevel != null)
                    this.toLevelMap.register(name, element.toLevel);
            }
        }

        @Override
        public void deregisterKey(@Nullable String key) {
            if (key == null)
                return;
            this.map.remove(key);
            this.fromLevelMap.deregisterValue(key);
            this.toLevelMap.deregisterKey(key);
        }

        @Override
        public void deregisterValue(@Nullable HLogLevel value) {
            if (value == null) {
                super.deregisterValue(null);
                return;
            }
            this.deregisterKey(value.getName());
        }

        @Override
        public void deregisterAll() {
            super.deregisterAll();
            this.fromLevelMap.deregisterAll();
            this.toLevelMap.deregisterAll();
        }

        /**
         * Get {@link Level} to {@link HLogLevel} map registerer.
         * @return {@link Level} to {@link HLogLevel} map registerer.
         */
        public @NotNull HMapRegisterer<Level, String> getFromLevelMap() {
            return this.fromLevelMap;
        }

        /**
         * Get {@link HLogLevel} to {@link Level} map registerer.
         * @return {@link HLogLevel} to {@link Level} map registerer.
         */
        public @NotNull HMapRegisterer<String, Level> getToLevelMap() {
            return this.toLevelMap;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LogRegistererMap that)) return false;
            if (!super.equals(o)) return false;
            return super.equals(o) && this.fromLevelMap.equals(that.fromLevelMap) && this.toLevelMap.equals(that.toLevelMap);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), this.fromLevelMap, this.toLevelMap);
        }
    }
}
