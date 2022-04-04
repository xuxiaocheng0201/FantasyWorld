package HeadLibs.Configuration;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementNotRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfigurations {
    /**
     * Configuration file path.
     */
    private File file;
    /**
     * Saved configuration elements.
     * @see HConfigElement
     */
    public final Map<String, HConfigElement> data = new HashMap<>();

    /**
     * Construct configuration in file path
     * @param path file path
     * @throws IllegalArgumentException Wrong file path.
     */
    public HConfigurations(@NotNull String path) throws IllegalArgumentException {
        super();
        this.setPath(path);
    }

    /**
     * Get configuration file path.
     * @return configuration file path
     */
    public @NotNull String getPath() {
        return this.file.getPath();
    }

    /**
     * Set configuration file path and read it.
     * @param path configuration file path.
     * @throws IllegalArgumentException File path invalid.
     */
    public void setPath(@Nullable String path) throws IllegalArgumentException {
        if (path == null)
            throw new IllegalArgumentException("Argument path is null.");
        try {
            HFileHelper.createNewFile(path);
        } catch (IOException exception) {
            throw new IllegalArgumentException(HStringHelper.concat("Can't create the new file in path='", path, '\''), exception);
        }
        this.file = (new File(path)).getAbsoluteFile();
        this.read();
    }

    /**
     * Is a config element's name exists?
     * @param config the configuration
     * @return true - exists. false - not exist.
     */
    public boolean isExists(@Nullable HConfigElement config) {
        if (config == null)
            return true;
        return this.data.containsKey(config.getName());
    }

    /**
     * Add new configuration element to cache.
     * @param config new configuration element
     * @throws IllegalArgumentException configuration's name has exist.
     */
    public void add(@NotNull HConfigElement config) throws IllegalArgumentException {
        if (this.isExists(config))
            throw new IllegalArgumentException(HStringHelper.concat("Configuration name has existed. [name='", config.getName(), "', path='", this.getPath(), "']."));
        this.data.put(config.getName(), config);
    }

    /**
     * Add new configuration element to cache.
     * @param config new configuration element
     * @param overwrite overwrite when configuration exists
     * @return true - {@code data} change. false - not change
     */
    public boolean add(@NotNull HConfigElement config, boolean overwrite) {
        if (this.isExists(config)) {
            if (!overwrite)
                return false;
            this.deleteByName(config.getName());
        }
        this.data.put(config.getName(), config);
        return true;
    }

    /**
     * Get configuration element by name.
     * @param name configuration name
     * @return null - can't find. notNull - the element.
     */
    public @Nullable HConfigElement getByName(@NotNull String name) {
        return this.data.get(name);
    }

    /**
     * Delete configuration element by name.
     * @param name configuration name
     */
    public void deleteByName(@Nullable String name) {
        this.data.remove(name);
    }

    /**
     * Remove all configuration element by value
     * @param value configuration value
     */
    public void deleteAllByValue(@NotNull String value) {
        for (Map.Entry<String, HConfigElement> entry: this.data.entrySet())
            if (value.equals(entry.getValue().getValue()))
                this.data.remove(entry.getKey());
    }

    /**
     * Remove all configuration element
     */
    public void clear() {
        this.data.clear();
    }

    /**
     * Read configurations from file.
     */
    public void read() {
        this.data.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.file));
            String temp = reader.readLine();
            HConfigElement config = new HConfigElement(null, null);
            while (temp != null) {
                if (temp.startsWith("name:"))
                    config.setName(temp.substring(6));
                if (temp.startsWith("note:"))
                    config.setNote(temp.substring(6));
                try {
                    if (temp.startsWith("type:")) {
                        HConfigType type;
                        try {
                            type = HConfigType.getRegisteredMap().getElement(temp.substring(6));
                        } catch (HElementNotRegisteredException exception) {
                            type = null;
                        }
                        if (type == null) {
                            HLog.logger(HLogLevel.CONFIGURATION, HStringHelper.concat("Unregistered configuration type! [type='", temp, "'name='", config.getName(), "', path='", this.getPath(), "']. Use STRING!"));
                            type = HConfigType.STRING;
                        }
                        config.setType(type);
                    }
                    if (temp.startsWith("value:")) {
                        config.setValue(temp.substring(7));
                        HConfigElement check = this.getByName(config.getName());
                        if (check != null)
                            if (check.equals(config))
                                HLog.logger(HLogLevel.CONFIGURATION, HStringHelper.concat("The completely same Configuration! [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                            else
                                HLog.logger(HLogLevel.CONFIGURATION, HStringHelper.concat("The same Configuration name! But different Configuration value [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                        else
                            this.add(config);
                        config = new HConfigElement(null, null);
                    }
                } catch (HWrongConfigValueException exception) {
                    HLog.logger(HLogLevel.ERROR, exception);
                }
                temp = reader.readLine();
            }
            reader.close();
        } catch (IOException | HWrongConfigValueException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    /**
     * Write configurations to file.
     */
    public void write() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
            for (HConfigElement i: this.data.values()) {
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
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("HConfigurations{",
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
