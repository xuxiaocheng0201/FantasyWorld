package HeadLibs.Configuration.SimpleMode;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HConfigurationsSimple {
    public static final String CURRENT_VERSION = "1.0.0";

    private File file;
    public final List<HConfigSimple> date = new ArrayList<>();

    public HConfigurationsSimple(String path) throws IllegalArgumentException {
        this.setPath(path);
        this.read();
    }

    public String getPath() {
        return this.file.getPath();
    }

    public void setPath(String path) throws IllegalArgumentException {
        if (path == null)
            throw new IllegalArgumentException("Argument path is null.");
        if (!HFileHelper.createNewFile(path))
            throw new IllegalArgumentException(HStringHelper.merge("Can't create the new file in path='", path, '\''));
        this.file = (new File(path)).getAbsoluteFile();
        this.read();
    }

    public void add(HConfigSimple config) throws IllegalArgumentException {
        if (this.getByName(config.getName()) != null)
            throw new IllegalArgumentException("Configuration name has existed.");
        this.date.add(config);
    }

    public HConfigSimple getByName(String name) {
        for (HConfigSimple i: this.date) {
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

    public void deleteByName(String name) {
        for (int i = 0; i < this.date.size(); ++i) {
            if (this.date.get(i).getName() == null) {
                if (name == null) {
                    this.date.remove(i);
                    return;
                }
                continue;
            }
            if (this.date.get(i).getName().equals(name)) {
                this.date.remove(i);
                return;
            }
        }
    }

    public void deleteByValue(String value) {
        for (int i = 0; i < this.date.size(); ++i) {
            if (this.date.get(i).getValue() == null) {
                if (value == null) {
                    this.date.remove(i);
                    return;
                }
                continue;
            }
            if (this.date.get(i).getValue().equals(value)) {
                this.date.remove(i);
                return;
            }
        }
    }

    public void deleteAllByValue(String value) {
        for (int i = 0; i < this.date.size(); ++i) {
            if (this.date.get(i).getValue() == null) {
                if (value == null) {
                    this.date.remove(i);
                    --i;
                }
                continue;
            }
            if (this.date.get(i).getValue().equals(value))
                this.date.remove(i);
        }
    }

    public void read() {
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
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("The completely same Configuration! [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                        else
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("The same Configuration name! But different Configuration value [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                    else
                        this.add(config);
                    config = new HConfigSimple(null, null);
                } else
                    HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("Illegal configuration format! [line='", temp, "']"));
                temp = reader.readLine();
            }
            reader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void write() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
            for (HConfigSimple i: this.date) {
                writer.write(i.getName());
                writer.write("=");
                writer.write(i.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return HStringHelper.merge("HConfigurationsSimple{",
                "file=", file,
                ", date=", date,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigurationsSimple))
            return false;
        return this.getPath().equals(((HConfigurationsSimple) a).getPath()) && this.date.equals(((HConfigurationsSimple) a).date);
    }

    @Override
    public int hashCode() {
        return this.getPath().hashCode();
    }
}
