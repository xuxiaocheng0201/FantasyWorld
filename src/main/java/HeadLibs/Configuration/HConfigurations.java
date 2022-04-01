package HeadLibs.Configuration;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HConfigurations {
    public static final String CURRENT_VERSION = "1.0.0";

    private File file;
    public final List<HConfig> data = new ArrayList<>();

    public HConfigurations(String path) throws IllegalArgumentException {
        this.setPath(path);
    }

    public String getPath() {
        return this.file.getPath();
    }

    public void setPath(String path) throws IllegalArgumentException {
        if (path == null)
            throw new IllegalArgumentException("Argument path is null.");
        if (HFileHelper.createNewFile(path))
            throw new IllegalArgumentException(HStringHelper.merge("Can't create the new file in path='", path, '\''));
        this.file = (new File(path)).getAbsoluteFile();
        this.read();
    }

    public void add(HConfig config) {
        if (this.getByName(config.getName()) != null) {
            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("Configuration name has existed. [name='", config.getName(), "', path='", this.getPath(), "']. Drop the first!"));
            this.deleteByName(config.getName());
        }
        this.data.add(config);
    }

    public void clear() {
        this.data.clear();
    }

    public HConfig getByName(String name) {
        for (HConfig i: this.data) {
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
        for (int i = 0; i < this.data.size(); ++i) {
            if (this.data.get(i).getName() == null) {
                if (name == null) {
                    this.data.remove(i);
                    return;
                }
                continue;
            }
            if (this.data.get(i).getName().equals(name)) {
                this.data.remove(i);
                return;
            }
        }
    }

    public void deleteByValue(String value) {
        for (int i = 0; i < this.data.size(); ++i) {
            if (this.data.get(i).getValue() == null) {
                if (value == null) {
                    this.data.remove(i);
                    return;
                }
                continue;
            }
            if (this.data.get(i).getValue().equals(value)) {
                this.data.remove(i);
                return;
            }
        }
    }

    public void deleteAllByValue(String value) {
        for (int i = 0; i < this.data.size(); ++i) {
            if (this.data.get(i).getValue() == null) {
                if (value == null) {
                    this.data.remove(i);
                    --i;
                }
                continue;
            }
            if (this.data.get(i).getValue().equals(value))
                this.data.remove(i);
        }
    }

    public void read() {
        this.data.clear();
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
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("The completely same Configuration! [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                        else
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("The same Configuration name! But different Configuration value [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                    else
                        this.add(config);
                    config = new HConfig(null, null);
                }
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
            for (HConfig i: this.data) {
                writer.write("name: ");
                writer.write(i.getName());
                writer.newLine();
                writer.write("note: ");
                writer.write(i.getNote());
                writer.newLine();
                writer.write("type: ");
                writer.write(i.getType().toString());
                writer.newLine();
                writer.write("value: ");
                writer.write(i.getValue());
                writer.newLine();
                writer.newLine();
            }
            writer.close();
        } catch (IOException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    @Override
    public String toString() {
        return HStringHelper.merge("HConfigurations{",
                "file=", file,
                ", date=", data,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof HConfigurations))
            return false;
        return this.getPath().equals(((HConfigurations) a).getPath()) && this.data.equals(((HConfigurations) a).data);
    }

    @Override
    public int hashCode() {
        return this.getPath().hashCode();
    }
}
