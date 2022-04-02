package HeadLibs.Configuration;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class HConfigurations {
    private File file;
    public final Set<HConfig> data = new HashSet<>();

    public HConfigurations(String path) throws IllegalArgumentException {
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
            throw new IllegalArgumentException(HStringHelper.merge("Can't create the new file in path='", path, '\''));
        this.file = (new File(path)).getAbsoluteFile();
        this.read();
    }

    public void add(@NotNull HConfig config) {
        if (this.getByName(config.getName()) != null) {
            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("Configuration name has existed. [name='", config.getName(), "', path='", this.getPath(), "']. Drop the first!"));
            this.deleteByName(config.getName());
        }
        this.data.add(config);
    }

    public void clear() {
        this.data.clear();
    }

    public @Nullable HConfig getByName(@NotNull String name) {
        for (HConfig i: this.data)
            if (i.getName().equals(name))
                return i;
        return null;
    }

    public void deleteByName(@Nullable String name) {
        this.data.removeIf(config -> config.getName().equals(name));
    }

    public void deleteAllByValue(@Nullable String value) {
        this.data.removeIf(config -> config.getValue().equals(value));
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
                if (temp.startsWith("type:")) {
                    String type_name = temp.substring(6);
                    boolean array = false;
                    if (type_name.endsWith("_ARRAY")) {
                        type_name = type_name.substring(0, type_name.length() - 6);
                        array = true;
                    }
                    HEConfigType type = HEConfigType.getTypeByName(type_name, array);
                    if (type == null) {
                        HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.merge("Unregistered configuration type! [type='", temp, "'name='", config.getName(), "', path='", this.getPath(), "']. Use STRING!"));
                        type = HEConfigType.STRING;
                    }
                    config.setType(type);
                }
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
    public @NotNull String toString() {
        return HStringHelper.merge("HConfigurations{",
                "file=", this.file,
                ", date=", this.data,
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
