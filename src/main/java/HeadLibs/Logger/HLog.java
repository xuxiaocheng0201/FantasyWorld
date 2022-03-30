package HeadLibs.Logger;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HLog {
    public static String DATE_FORMAT = "HH:mm:ss";
    private static final List<Pair<Pair<Date, Integer>, String>> logs = new ArrayList<>();

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

    public HLog(String name, HLog parent) {
        if (parent == null) {
            this.name = name;
            return;
        }
        this.name = HStringHelper.merge(parent.getName(), "/", name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized void log(HELogLevel level, String message) {
        if (level == null)
            level = HELogLevel.DEBUG;
        Date date = new Date();
        String log = HStringHelper.merge("[", HStringHelper.getDate(DATE_FORMAT, date), "]",
                "[", this.name, "]",
                "[", level.getName(), "]",
                message);
        logs.add(new Pair<>(new Pair<>(date, level.getPriority()), log));
        if (System.console() != null)
            System.out.println(log);
        else
            System.out.println(level.getPrefix() + log + "\033[0m");
    }

    public void log(String message) {
        log(HELogLevel.DEBUG, message);
    }

    public void log(Object message) {
        log(HELogLevel.DEBUG, message);
    }

    public void log(String ...message) {
        log(HELogLevel.DEBUG, message);
    }

    public void log(Object ...message) {
        log(HELogLevel.DEBUG, message);
    }

    public void log(HELogLevel level, String ...message) {
        log(level, HStringHelper.merge(message));
    }

    public void log(HELogLevel level, Object message) {
        if (message == null)
            log(level, "Object null!");
        else
            log(level, HStringHelper.merge(message));
    }

    public void log(HELogLevel level, Object ...message) {
        log(level, HStringHelper.merge(message));
    }

    public static void logger(String message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    public static void logger(HELogLevel level, String message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    public static void logger(Object message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    public static void logger(HELogLevel level, Object message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    public static void logger(String ...message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    public static void logger(HELogLevel level, String ...message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    public static void logger(Object ...message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    public static void logger(HELogLevel level, Object ...message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    public static synchronized void saveLogs(String path, boolean needSort) {
        if (needSort)
            logs.sort((a, b) ->
            {
                int compare_date = a.getKey().getKey().compareTo(b.getKey().getKey());
                if (compare_date == 0)
                    return a.getKey().getValue().compareTo(b.getKey().getValue());
                return compare_date;
            });
        try {
            if (!HFileHelper.createNewFile(path))
                throw new IOException("Creating log file failed.");
            FileWriter writer = new FileWriter(new File(path).getAbsoluteFile(), true);
            while (!logs.isEmpty()) {
                writer.write(logs.remove(0).getValue());
                writer.write(System.getProperty("line.separator"));
            }
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void saveLogs(String path) {
        saveLogs(path, false);
    }
}
