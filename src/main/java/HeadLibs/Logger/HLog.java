package HeadLibs.Logger;

import HeadLibs.Helper.HFileHelper;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HLog {
    public static final String DATE_FORMAT = "HH:mm:ss";

    private String name;
    private static final List<Pair<Pair<Date, Integer>, String>> logs = new ArrayList<>();

    public HLog() {
        this("main");
    }

    public HLog(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void log(String message) {
        log(HELogLevel.INFO, message);
    }

    public void log(HELogLevel level, String message) {
        Date date = new Date();
        String log = "[" + (new SimpleDateFormat(DATE_FORMAT)).format(date) + "]" +
                "[" + this.name + "]" +
                "[" + level.getName() + "]" +
                message;
        logs.add(new Pair<>(new Pair<>(date, level.getPriority()), log));
        System.out.println(level.getPrefix() + log);
    }

    public static void logger(String message) {
        (new HLog(Thread.currentThread().getName())).log(message);
    }

    public static void logger(HELogLevel level, String message) {
        (new HLog(Thread.currentThread().getName())).log(level, message);
    }

    public static void saveLogs(String path) {
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
}
