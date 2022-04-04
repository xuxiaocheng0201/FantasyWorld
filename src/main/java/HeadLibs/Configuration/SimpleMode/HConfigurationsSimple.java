package HeadLibs.Configuration.SimpleMode;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple configuration.
 * {@link HeadLibs.Configuration.HConfigurations} without note and type is {@link HeadLibs.Configuration.HConfigType#STRING}
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfigurationsSimple {
    /**
     * Configuration file path.
     */
    private File file;
    /**
     * Saved configuration elements.
     * @see HConfigElementSimple
     */
    public final Map<String, HConfigElementSimple> data = new HashMap<>();

    /**
     * Construct configuration in file path
     * @param path file path
     * @throws IllegalArgumentException Wrong file path.
     */
    public HConfigurationsSimple(String path) throws IllegalArgumentException {
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
public boolean isExists(@Nullable HConfigElementSimple config) {
        if (config == null)
            return true;
        return this.data.containsKey(config.getName());
    }


    /**
     * Add new configuration element to cache.
     * @param config new configuration element
     * @throws IllegalArgumentException configuration's name has exist.
     */
    public void add(@NotNull HConfigElementSimple config) throws IllegalArgumentException {
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
    public boolean add(@NotNull HConfigElementSimple config, boolean overwrite) {
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
    public @Nullable HConfigElementSimple getByName(@NotNull String name) {
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
        for (Map.Entry<String, HConfigElementSimple> entry: this.data.entrySet())
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
            HConfigElementSimple config = new HConfigElementSimple(null, null);
            while (temp != null) {
                if (temp.contains("=")) {
                    String[] s = temp.split("=");
                    config.setName(s[0]);
                    config.setValue(temp.substring(s[0].length() + 1));
                    HConfigElementSimple check = this.getByName(s[0]);
                    if (check != null)
                        if (check.equals(config))
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.concat("The completely same Configuration! [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                        else
                            HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.concat("The same Configuration name! But different Configuration value [name='", config.getName(), "', path='", this.getPath(), "']. Drop the second!"));
                    else
                        this.add(config);
                    config = new HConfigElementSimple(null, null);
                } else
                    HLog.logger(HELogLevel.CONFIGURATION, HStringHelper.concat("Illegal configuration format! [line='", temp, "']"));
                temp = reader.readLine();
            }
            reader.close();
        } catch (IOException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    /**
     * Write configurations to file.
     */
    public void write() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
            for (HConfigElementSimple i: this.data.values()) {
                writer.write(i.getName());
                writer.write("=");
                writer.write(i.getValue());
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
