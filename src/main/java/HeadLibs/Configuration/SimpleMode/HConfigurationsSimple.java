package HeadLibs.Configuration.SimpleMode;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HConfigurationsSimple {
    public static final String CURRENT_VERSION = "1.0.0";

    private File file;
    public final List<HConfigSimple> data = new ArrayList<>();

    public HConfigurationsSimple(String path) throws IllegalArgumentException {
        super();
        this.setPath(path);
    }

    public @NotNull String getPath() {
        return this.file.getPath();
    }

    public void setPath(@Nullable String path) throws IllegalArgumentException {
        if (path == null)
            throw new IllegalArgumentException("Argument path is null.");
        if (HFileHelper.createNewFile(path))
            throw new IllegalArgumentException(HStringHelper.concat("Can't create the new file in path='", path, '\''));
        this.file = (new File(path)).getAbsoluteFile();
        this.read();
    }

    public void add(@NotNull HConfigSimple config) {
        if (this.getByName(config.getName()) != null) {
            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.concat("Configuration name has existed. [name='", config.getName(), "', path='", this.getPath(), "']. Drop the first!"));
            this.deleteByName(config.getName());
        }
        this.data.add(config);
    }

    public @Nullable HConfigSimple getByName(@Nullable String name) {
        for (HConfigSimple i: this.data) {
            if (i.getName() == null)
                if (name == null)
                    return i;
                else
                    continue;
            if (i.getName().equals(name))
                return i;
        }
        return null;
    }

    public void deleteByName(@Nullable String name) {
        this.data.removeIf(config -> Objects.equals(config.getName(), name));
    }

    public void deleteAllByValue(@Nullable String value) {
        this.data.removeIf(config -> Objects.equals(config.getValue(), value));
    }

    public void read() {
        this.data.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            String temp = reader.readLine();
            HConfigSimple config = new HConfigSimple(null, null);
            while (temp != null) {
                if (temp.contains("=")) {
                    String[] s = temp.split("=");
                    config.setName(s[0]);
                    config.setValue(temp.substring(s[0].length() + 1));
                    HConfigSimple check = this.getByName(s[0]);
                    if (check != null)
                        if (check.equals(config))
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.concat("The completely same Configuration! [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                        else
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.concat("The same Configuration name! But different Configuration value [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                    else
                        this.add(config);
                    config = new HConfigSimple(null, null);
                } else
                    HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.concat("Illegal configuration format! [line='", temp, "']"));
                temp = reader.readLine();
            }
            reader.close();
        } catch (IOException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    public void write() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
            for (HConfigSimple i: this.data) {
                writer.write(i.getName() == null ? "null" : i.getName());
                writer.write("=");
                writer.write(i.getValue() == null ? "null" : i.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("HConfigurationsSimple{",
                "file=", this.file,
                ", date=", this.data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigurationsSimple))
            return false;
        return this.getPath().equals(((HConfigurationsSimple) a).getPath()) && this.data.equals(((HConfigurationsSimple) a).data);
    }

    @Override
    public int hashCode() {
        return this.getPath().hashCode();
    }
}
