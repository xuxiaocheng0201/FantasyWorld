package HeadLibs.Configuration.SimpleMode;

import HeadLibs.Configuration.HWrongConfigValueException;
import HeadLibs.Helper.HFileHelper;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HLinkedMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

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
    private @NotNull File file = new File("config.cfg");
    /**
     * Saved configuration elements.
     * @see HConfigElementSimple
     */
    public final @NotNull HLinkedMapRegisterer<String, HConfigElementSimple> data = new HLinkedMapRegisterer<>();

    /**
     * Construct a empty configuration.
     */
    public HConfigurationsSimple() {
        super();
    }

    /**
     * Construct configuration in file path
     * @param path file path
     * @throws IOException Wrong file path.
     */
    public HConfigurationsSimple(String path) throws IOException {
        super();
        this.setPath(path);
    }

    /**
     * Get configuration file absolute path.
     * @return configuration file absolute path
     */
    public @NotNull String getPath() {
        return this.file.getPath();
    }

    /**
     * Set configuration file path.
     * @param path configuration file path
     * @throws IOException File path invalid.
     */
    public void setPath(@Nullable String path) throws IOException {
        if (path == null)
            throw new IOException("Null path");
        HFileHelper.createNewFile(path);
        this.file = (new File(path)).getAbsoluteFile();
    }

    /**
     * Is a configuration name exists?
     * @param config the configuration
     * @return true - exists. false - not exist.
     */
    public boolean isExists(@Nullable HConfigElementSimple config) {
        if (config == null)
            return true;
        return this.data.isRegisteredKey(config.getName());
    }

    /**
     * Add a new configuration element to cache.
     * @param config new configuration element
     * @throws HElementRegisteredException Configuration name has existed.
     */
    public void add(@NotNull HConfigElementSimple config) throws HElementRegisteredException {
        if (this.data.isRegisteredKey(config.getName()))
            throw new HElementRegisteredException("Configuration name has been registered! name='" + config.getName() + "', path='" + this.file.getPath() + "'.");
        this.data.register(config.getName(), config);
    }

    /**
     * Add new configuration element to cache.
     * @param config new configuration element
     * @param overwrite overwrite when configuration exists
     * @return true - added. false - not added.
     */
    public boolean add(@NotNull HConfigElementSimple config, boolean overwrite) {
        try {
            this.data.register(config.getName(), config);
        } catch (HElementRegisteredException exception) {
            if (!overwrite)
                return false;
            try {
                this.data.reset(config.getName(), config);
            } catch (HElementRegisteredException ignore) {
            }
        }
        return true;
    }

    /**
     * Get configuration element by name.
     * @param name configuration name
     * @return null - can't find. notNull - the element.
     */
    public @Nullable HConfigElementSimple getByName(@NotNull String name) {
        try {
            return this.data.getElement(name);
        } catch (HElementNotRegisteredException exception) {
            return null;
        }
    }

    /**
     * Delete configuration element by name.
     * @param name configuration name
     */
    public void deleteByName(@Nullable String name) {
        this.data.deregisterKey(name);
    }

    /**
     * Remove all configuration elements by value.
     * @param value configuration value
     */
    public void deleteAllByValue(@NotNull String value) {
        for (HConfigElementSimple element: this.data.values())
            if (value.equals(element.getValue()))
                this.data.deregisterValue(element);
    }

    /**
     * Remove all configuration elements.
     */
    public void clear() {
        this.data.deregisterAll();
    }

    /**
     * Read configurations from file.
     */
    public void read() throws IOException, HElementRegisteredException {
        this.data.deregisterAll();
        HWrongConfigValueException hWrongConfigValueException;
        HElementRegisteredException hElementRegisteredException;
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            String temp = reader.readLine();
            HConfigElementSimple config = new HConfigElementSimple(null, null);
            hWrongConfigValueException = null;
            hElementRegisteredException = null;
            while (temp != null) {
                if (temp.contains("=")) {
                    int index = temp.indexOf('=');
                    config.setName(temp.substring(0, index));
                    config.setValue(temp.substring(index + 1));
                    try {
                        this.add(config);
                    } catch (HElementRegisteredException elementRegisteredException) {
                        if (hElementRegisteredException == null)
                            hElementRegisteredException = elementRegisteredException;
                    }
                    config = new HConfigElementSimple(null, null);
                } else if (hWrongConfigValueException == null)
                        hWrongConfigValueException = new HWrongConfigValueException("Illegal configuration format! [line='" + temp + "']");
                temp = reader.readLine();
            }
        }
        if (hWrongConfigValueException != null)
            throw hWrongConfigValueException;
        if (hElementRegisteredException != null)
            throw hElementRegisteredException;
    }

    /**
     * Write configurations to file.
     */
    public void write() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file))) {
            for (HConfigElementSimple i: this.data.values()) {
                writer.write(i.getName());
                writer.write("=");
                writer.write(i.getValue());
                writer.newLine();
            }
        }
    }

    @Override
    public @NotNull String toString() {
        return "HConfigurationsSimple:" + this.data;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HConfigurationsSimple that = (HConfigurationsSimple) o;
        return this.file.equals(that.file) && this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
