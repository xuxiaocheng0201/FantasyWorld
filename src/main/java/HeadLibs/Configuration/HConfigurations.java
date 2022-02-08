package HeadLibs.Configuration;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HConfigurations {
    public static final String CURRENT_VERSION = "1.0.0";

    private File file;
    public final List<HConfig> date = new ArrayList<>();

    public HConfigurations(String path) throws IllegalArgumentException, IOException {
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
            throw new IllegalArgumentException("Can't create the new file in path=" + path);
        this.file = (new File(path)).getAbsoluteFile();
        this.read();
    }

    public void add(HConfig config) throws IllegalArgumentException {
        if (this.getByName(config.getName()) != null)
            throw new IllegalArgumentException("Configuration name has existed.");
        this.date.add(config);
    }

    public HConfig getByName(String name) {
        for (HConfig i: this.date) {
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
        this.date.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            String temp = reader.readLine();
            HConfig config = new HConfig(null, null);
            while (temp != null) {
                if (temp.startsWith("name:"))
                    config.setName(temp.substring(6));
                if (temp.startsWith("note:"))
                    config.setNote(temp.substring(6));
                if (temp.startsWith("type:"))
                    config.setType(HEConfigType.getTypeByName(temp.substring(6)));
                if (temp.startsWith("value:")) {
                    config.setValue(temp.substring(7));
                    HConfig check = this.getByName(config.getName());
                    if (check != null)
                        if (check.equals(config))
                            HLog.logger(HELogLevel.CONFIGURATION, "The completely same Configuration! [name=" + config.getName() + ", path=" + this.getPath() + "]. Drop the second!");
                        else
                            HLog.logger(HELogLevel.CONFIGURATION, "The same Configuration name! But different Configuration value [name=" + config.getName() + ", path=" + this.getPath() + "]. Drop the second!");
                    else
                        this.add(config);
                    config = new HConfig(null, null);
                }
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
            for (HConfig i: this.date) {
                writer.write("name: " + i.getName());
                writer.newLine();
                writer.write("note: " + i.getNote());
                writer.newLine();
                writer.write("type: " + i.getType());
                writer.newLine();
                writer.write("value: " + i.getValue());
                writer.newLine();
                writer.newLine();
            }
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "HConfigurations:[" + this.date + "]";
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigurations))
            return false;
        return this.getPath().equals(((HConfigurations) a).getPath()) && this.date.equals(((HConfigurations) a).date);
    }

    @Override
    public int hashCode() {
        return this.getPath().hashCode();
    }
}
