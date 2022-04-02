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

@SuppressWarnings("unused")
public class HLog {
    private static @NotNull String DATE_FORMAT = "HH:mm:ss";
    private static final List<@NotNull Pair<@NotNull Pair<@NotNull Date, @NotNull Integer>, @NotNull String>> logs = new ArrayList<>();

    private String name;

    public HLog() {
        this("main");
    }

    public HLog(String name) {
        this.name = name;
    }

    public HLog(String name, String parent) {
        this.name = HStringHelper.merge(parent, "/", name);
    }

    public HLog(String name, @Nullable HLog parent) {
        if (parent == null) {
            this.name = name;
            return;
        }
        this.name = HStringHelper.merge(parent.name, "/", name);
    }

    public static @NotNull String getDateFormat() {
        return DATE_FORMAT;
    }

    public static void setDateFormat(@NotNull String dateFormat) {
        DATE_FORMAT = dateFormat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String name, String parent) {
        this.name = HStringHelper.merge(parent, "/", name);
    }

    public void setName(String name, @Nullable HLog parent) {
        if (parent == null) {
            this.name = name;
            return;
        }
        this.name = HStringHelper.merge(parent.name, "/", name);
    }

    public void log(@Nullable HELogLevel level, String message) {
        if (level == null)
            level = HELogLevel.DEBUG;
        Date date = new Date();
        String log = HStringHelper.merge("[", HStringHelper.getDate(DATE_FORMAT, date), "]",
                "[", this.name, "]",
                "[", level.getName(), "]",
                message);
        synchronized (logs) {
            logs.add(Pair.makePair(Pair.makePair(date, level.getPriority()), log));
            if (System.console() != null)
                System.out.println(log);
            else
                System.out.println(level.getPrefix() + log + "\033[0m");
        }
    }

    public void log(@Nullable HELogLevel level, @Nullable Throwable throwable) {
        if (throwable == null) {
            log(level, "Object null!");
            return;
        }
        if (level == null)
            level = HELogLevel.ERROR;
        Date date = new Date();
        StringBuilder builder = new StringBuilder("[");
        builder.append(HStringHelper.getDate(DATE_FORMAT, date));
        builder.append("][");
        builder.append(this.name);
        builder.append("][");
        builder.append(level.getName());
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
        synchronized (logs) {
            logs.add(Pair.makePair(Pair.makePair(date, level.getPriority()), log));
            if (System.console() != null)
                System.out.println(log);
            else
                System.out.println(level.getPrefix() + log + "\033[0m");
        }
    }

    public void log(HELogLevel level, @Nullable Object message) {
        if (message == null) {
            log(level, "Object null!");
            return;
        }
        log(level, HStringHelper.merge(message));
    }

    public void log(HELogLevel level, String ...messages) {
        log(level, HStringHelper.merge(messages));
    }

    public void log(HELogLevel level, Throwable @NotNull ...throwable) {
        for (Throwable t: throwable)
            log(level, t);
    }

    public void log(HELogLevel level, Object ...messages) {
        log(level, HStringHelper.merge(messages));
    }

    public void log(String message) {
        log(HELogLevel.DEBUG, message);
    }

    public void log(Throwable throwable) {
        log(HELogLevel.ERROR, throwable);
    }

    public void log(Object message) {
        log(HELogLevel.DEBUG, message);
    }

    public void log(String ...messages) {
        log(HELogLevel.DEBUG, messages);
    }

    public void log(Throwable ...throwable) {
        log(HELogLevel.ERROR, throwable);
    }

    public void log(Object ...messages) {
        log(HELogLevel.DEBUG, messages);
    }

    public static void logger(HELogLevel level, String message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    public static void logger(HELogLevel level, Throwable throwable) {
        (new HLog(Thread.currentThread().getName())).log(level, throwable);
    }

    public static void logger(HELogLevel level, Object message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    public static void logger(HELogLevel level, String ...messages) {
        (new HLog(Thread.currentThread().getName())).log(level, messages);
    }

    public static void logger(HELogLevel level, Throwable ...throwable) {
        (new HLog(Thread.currentThread().getName())).log(level, throwable);
    }

    public static void logger(HELogLevel level, Object ...messages) {
        (new HLog(Thread.currentThread().getName())).log(level, messages);
    }

    public static void logger(String message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    public static void logger(Throwable throwable) {
        (new HLog(Thread.currentThread().getName())).log(throwable);
    }

    public static void logger(Object message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    public static void logger(String ...messages) {
        (new HLog(Thread.currentThread().getName())).log(messages);
    }

    public static void logger(Throwable ...throwable) {
        (new HLog(Thread.currentThread().getName())).log(throwable);
    }

    public static void logger(Object ...messages) {
        (new HLog(Thread.currentThread().getName())).log(messages);
    }

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
                if (HFileHelper.createNewFile(path))
                    throw new IOException("Creating log file failed.");
                FileWriter writer = new FileWriter(new File(path).getAbsoluteFile(), true);
                for (Pair<Pair<Date, Integer>, String> pair: logs) {
                    writer.write(HStringHelper.noNull(pair.getValue()));
                    writer.write(System.getProperty("line.separator"));
                }
                writer.close();
            } catch (IOException exception) {
                HLog.logger(HELogLevel.ERROR, exception);
            }
        }
    }

    public static void saveLogs(@NotNull String path) {
        saveLogs(path, false);
    }
}
