package HeadLibs.Logger;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A HeadLib logger just like {@link java.util.logging.Logger}
 * @author xuxiaocheng
 */
@SuppressWarnings({"unused", "OverloadedVarargsMethod"})
public class HLog {
    /**
     * Log's date format.
     */
    private static @NotNull String DATE_FORMAT = "HH:mm:ss";
    /**
     * Cache logs.
     */
    private static final List<Pair<Pair<Date, Integer>, String>> logs = new ArrayList<>();

    /**
     * Get log's date format.
     * @return log's date format
     */
    public static @NotNull String getDateFormat() {
        return DATE_FORMAT;
    }

    /**
     * Set log's date format.
     * @param dateFormat log's date format
     */
    public static void setDateFormat(@NotNull String dateFormat) {
        DATE_FORMAT = dateFormat;
    }

    /**
     * logger's name.
     */
    private @NotNull String name;

    /**
     * Construct a logger named main.
     */
    public HLog() {
        this("main");
    }

    /**
     * Construct a logger.
     * @param name logger's name
     */
    public HLog(@NotNull String name) {
        super();
        this.name = name;
    }

    /**
     * Construct a logger with parent's name.
     * @param name logger's name
     * @param parent logger's parent's name
     */
    public HLog(@NotNull String name, @Nullable String parent) {
        super();
        if (parent == null) {
            this.name = name;
            return;
        }
        this.name = HStringHelper.concat(parent, "/", name);
    }


    /**
     * Construct a logger with parent's name.
     * @param name logger's name
     * @param parent logger's parent
     */
    public HLog(@NotNull String name, @Nullable HLog parent) {
        super();
        if (parent == null) {
            this.name = name;
            return;
        }
        this.name = HStringHelper.concat(parent.name, "/", name);
    }

    /**
     * Get logger's name.
     * @return logger's name
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Set logger's name.
     * @param name logger's name
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Set logger's name with parent's name.
     * @param name logger's name
     * @param parent logger's parent's name
     */
    public void setName(@NotNull String name, @Nullable String parent) {
        if (parent == null) {
            this.name = name;
            return;
        }
        this.name = HStringHelper.concat(parent, "/", name);
    }

    /**
     * Set logger's name with parent's name.
     * @param name logger's name
     * @param parent logger's parent
     */
    public void setName(@NotNull String name, @Nullable HLog parent) {
        if (parent == null) {
            this.name = name;
            return;
        }
        this.name = HStringHelper.concat(parent.name, "/", name);
    }

    /**
     * Log message with level.
     * @param level log's level
     * @param message the message to log
     */
    @SuppressWarnings("DefaultNotLastCaseInSwitch")
    public void log(@Nullable HLogLevel level, @Nullable String message) {
        if ("null".equals(this.name))
            return;
        HLogLevel level1 = level;
        if (level1 == null)
            level1 = HLogLevel.DEBUG;
        Date date = new Date();
        String log = HStringHelper.concat("[", HStringHelper.getDate(DATE_FORMAT, date), "]",
                "[", this.name, "]",
                "[", level1.getName(), "]",
                message == null ? "Null message." : message);
        switch (this.name) {
            case "stdErr":
                if (System.console() != null)
                    System.err.println(log);
                else
                    System.err.println(level1.getPrefix() + log + "\033[0m");
                break;
            default:
                synchronized (logs) {
                    logs.add(Pair.makePair(Pair.makePair(date, level1.getPriority()), log));
                }
                //noinspection fallthrough
            case "stdOut":
                if (System.console() != null)
                    System.out.println(log);
                else
                    System.out.println(level1.getPrefix() + log + "\033[0m");
        }
    }

    /**
     * Log throwable with level.
     * @param level log's level
     * @param throwable the throwable to log
     */
    @SuppressWarnings("DefaultNotLastCaseInSwitch")
    public void log(@Nullable HLogLevel level, @NotNull Throwable throwable) {
        if ("null".equals(this.name))
            return;
        HLogLevel level1 = level;
        if (level1 == null)
            level1 = HLogLevel.ERROR;
        Date date = new Date();
        StringBuilder builder = new StringBuilder("[");
        builder.append(HStringHelper.getDate(DATE_FORMAT, date));
        builder.append("][");
        builder.append(this.name);
        builder.append("][");
        builder.append(level1.getName());
        builder.append("]");
        builder.append(throwable.getClass().getName());
        if (throwable.getMessage() != null) {
            builder.append(": ");
            builder.append(throwable.getMessage());
        } else if (throwable.getCause() != null) {
            builder.append(": ");
            builder.append(throwable.getCause().getClass().getName());
        }
        for (StackTraceElement stackTraceElement: throwable.getStackTrace()) {
            builder.append(System.getProperty("line.separator"));
            builder.append("\tat ");
            builder.append(stackTraceElement.toString());
        }
        addCausedThrowable(builder, throwable.getCause());
        String log = builder.toString();
        switch (this.name) {
            case "stdErr":
                if (System.console() != null)
                    System.err.println(log);
                else
                    System.err.println(level1.getPrefix() + log + "\033[0m");
                break;
            default:
                synchronized (logs) {
                    logs.add(Pair.makePair(Pair.makePair(date, level1.getPriority()), log));
                }
                //noinspection fallthrough
            case "stdOut":
                if (System.console() != null)
                    System.out.println(log);
                else
                    System.out.println(level1.getPrefix() + log + "\033[0m");
        }
    }

    /**
     * Log String.valueOf(object) with level.
     * @param level log's level
     * @param object the object to log
     * @see String#valueOf(Object)
     */
    public void log(@Nullable HLogLevel level, @Nullable Object object) {
        if (object == null) {
            this.log(level, "Object null!");
            return;
        }
        this.log(level, HStringHelper.concat(object));
    }

    /**
     * Log messages with level.
     * @param level log's level
     * @param messages the messages to log
     */
    public void log(@Nullable HLogLevel level, @NotNull String ...messages) {
        this.log(level, HStringHelper.concat(messages));
    }

    /**
     * Log throwable with level.
     * @param level log's level
     * @param throwable the throwable to log
     */
    public void log(@Nullable HLogLevel level, @NotNull Throwable ...throwable) {
        for (Throwable t: throwable)
            this.log(level, t);
    }

    /**
     * Log objects with level.
     * @param level log's level
     * @param objects the objects to log
     * @see String#valueOf(Object)
     */
    public void log(@Nullable HLogLevel level, @NotNull Object ...objects) {
        this.log(level, HStringHelper.concat(objects));
    }

    /**
     * Log message with DEBUG level.
     * @param message the message to log
     */
    public void log(@Nullable String message) {
        this.log(HLogLevel.DEBUG, message);
    }

    /**
     * Log throwable with ERROR level.
     * @param throwable the throwable to log
     */
    public void log(@NotNull Throwable throwable) {
        this.log(HLogLevel.ERROR, throwable);
    }

    /**
     * Log String.valueOf(object) with DEBUG level.
     * @param object the object to log
     * @see String#valueOf(Object)
     */
    public void log(@Nullable Object object) {
        this.log(HLogLevel.DEBUG, object);
    }

    /**
     * Log messages with DEBUG level.
     * @param messages the messages to log
     */
    public void log(@Nullable String ...messages) {
        this.log(HLogLevel.DEBUG, messages);
    }

    /**
     * Log throwable with DEBUG level.
     * @param throwable the throwable to log
     */
    public void log(@NotNull Throwable ...throwable) {
        this.log(HLogLevel.ERROR, throwable);
    }

    /**
     * Log objects with DEBUG level.
     * @param objects the objects to log
     * @see String#valueOf(Object)
     */
    public void log(@Nullable Object ...objects) {
        this.log(HLogLevel.DEBUG, objects);
    }

    /**
     * Statically log message with level.
     * @param level log's level
     * @param message the message to log
     */
    public static void logger(@Nullable HLogLevel level, @Nullable String message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    /**
     * Statically log throwable with level.
     * @param level log's level
     * @param throwable the throwable to log
     */
    public static void logger(@Nullable HLogLevel level, @NotNull Throwable throwable) {
        (new HLog(Thread.currentThread().getName())).log(level, throwable);
    }

    /**
     * Statically log String.valueOf(object) with level.
     * @param level log's level
     * @param object the object to log
     * @see String#valueOf(Object)
     */
    public static void logger(@Nullable HLogLevel level, @Nullable Object object) {
        (new HLog(Thread.currentThread().getName())).log(level, object);
    }

    /**
     * Statically log messages with level.
     * @param level log's level
     * @param messages the messages to log
     */
    public static void logger(@Nullable HLogLevel level, @Nullable String ...messages) {
        (new HLog(Thread.currentThread().getName())).log(level, messages);
    }

    /**
     * Statically log throwable with level.
     * @param level log's level
     * @param throwable the throwable to log
     */
    public static void logger(@Nullable HLogLevel level, @NotNull Throwable ...throwable) {
        (new HLog(Thread.currentThread().getName())).log(level, throwable);
    }

    /**
     * Statically log objects with level.
     * @param level log's level
     * @param objects the objects to log
     * @see String#valueOf(Object)
     */
    public static void logger(@Nullable HLogLevel level, @Nullable Object ...objects) {
        (new HLog(Thread.currentThread().getName())).log(level, objects);
    }

    /**
     * Statically log message with DEBUG level.
     * @param message the message to log
     */
    public static void logger(@Nullable String message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    /**
     * Statically log throwable with ERROR level.
     * @param throwable the throwable to log
     */
    public static void logger(@NotNull Throwable throwable) {
        (new HLog(Thread.currentThread().getName())).log(throwable);
    }

    /**
     * Statically log String.valueOf(object) with DEBUG level.
     * @param object the object to log
     * @see String#valueOf(Object)
     */
    public static void logger(@Nullable Object object) {
        (new HLog(Thread.currentThread().getName())).log(object);
    }

    /**
     * Statically log messages with DEBUG level.
     * @param messages the messages to log
     */
    public static void logger(@Nullable String ...messages) {
        (new HLog(Thread.currentThread().getName())).log(messages);
    }

    /**
     * Statically log throwable with DEBUG level.
     * @param throwable the throwable to log
     */
    public static void logger(@NotNull Throwable ...throwable) {
        (new HLog(Thread.currentThread().getName())).log(throwable);
    }

    /**
     * Statically log objects with DEBUG level.
     * @param objects the objects to log
     * @see String#valueOf(Object)
     */
    public static void logger(@Nullable Object ...objects) {
        (new HLog(Thread.currentThread().getName())).log(objects);
    }

    /**
     * Recursively add throwable and its cause to log builder.
     * @param builder log builder.
     * @param throwable the throwable to log
     */
    private static void addCausedThrowable(@NotNull StringBuilder builder, @Nullable Throwable throwable) {
        if (throwable == null)
            return;
        builder.append(System.getProperty("line.separator"));
        builder.append("Caused by: ");
        builder.append(throwable.getClass().getName());
        for (StackTraceElement stackTraceElement: throwable.getStackTrace()) {
            builder.append(System.getProperty("line.separator"));
            builder.append("\tat ");
            builder.append(stackTraceElement.toString());
        }
        addCausedThrowable(builder, throwable.getCause());
    }

    /**
     * Save all cached logs to file.
     * @param path log file
     * @param needSort sort logs by date and warning level
     */
    public static void saveLogs(@NotNull String path, boolean needSort) {
        synchronized (logs) {
            if (needSort)
                logs.sort((a, b) ->
                {
                    assert a.getKey() != null;
                    assert b.getKey() != null;
                    assert a.getKey().getKey() != null;
                    int compare_date = a.getKey().getKey().compareTo(b.getKey().getKey());
                    if (compare_date == 0) {
                        assert a.getKey().getValue() != null;
                        assert b.getKey().getValue() != null;
                        return a.getKey().getValue().compareTo(b.getKey().getValue());
                    }
                    return compare_date;
                });
            try {
                try {
                    HFileHelper.createNewFile(path);
                } catch (IOException exception) {
                    throw new IOException("Creating log file failed.", exception);
                }
                FileWriter writer = new FileWriter(new File(path).getAbsoluteFile(), true);
                for (Pair<Pair<Date, Integer>, String> pair: logs) {
                    writer.write(HStringHelper.notNullOrEmpty(pair.getValue()));
                    writer.write(System.getProperty("line.separator"));
                }
                writer.close();
            } catch (IOException exception) {
                HLog.logger(HLogLevel.ERROR, exception);
            }
        }
    }

    /**
     * Save all cached logs to file.
     * @param path log file
     */
    public static void saveLogs(@NotNull String path) {
        saveLogs(path, false);
    }
}
