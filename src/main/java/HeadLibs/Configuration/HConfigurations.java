package HeadLibs.Configuration;

import HeadLibs.Helper.HFileHelper;
import HeadLibs.Registerer.HElementNotRegisteredException;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HLinkedMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;

/**
 * Configuration.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfigurations implements Serializable {
    @Serial
    private static final long serialVersionUID = 5587908518491879607L;

    /**
     * Configuration file path.
     */
    private @NotNull File file = new File("config.cfg");
    /**
     * Saved configuration elements.
     * @see HConfigElement
     */
    public final @NotNull HLinkedMapRegisterer<String, HConfigElement> data = new HLinkedMapRegisterer<>(true);

    /**
     * Construct a empty configuration.
     */
    public HConfigurations() {
        super();
    }

    /**
     * Construct configuration in file path
     * @param path file path
     * @throws IOException Wrong file path.
     */
    public HConfigurations(@NotNull String path) throws IOException {
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
     * @param path configuration file path.
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
    public boolean isExists(@Nullable HConfigElement config) {
        if (config == null)
            return true;
        return this.data.isRegisteredKey(config.getName());
    }

    /**
     * Add a new configuration element to cache.
     * @param config new configuration element
     * @throws HElementRegisteredException Configuration name has existed.
     */
    public void add(@NotNull HConfigElement config) throws HElementRegisteredException {
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
    public boolean add(@NotNull HConfigElement config, boolean overwrite) {
        try {
            this.data.register(config.getName(), config);
        } catch (HElementRegisteredException exception) {
            if (!overwrite)
                return false;
            this.data.reset(config.getName(), config);
        }
        return true;
    }

    /**
     * Get configuration element by name.
     * @param name configuration name
     * @return null - can't find. notNull - the element.
     */
    public @Nullable HConfigElement getByName(@NotNull String name) {
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
        for (HConfigElement element: this.data.getMap().values())
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
    public void read() throws IOException, HElementRegisteredException, HElementNotRegisteredException {
        this.data.deregisterAll();
        HElementRegisteredException hElementRegisteredException;
        HElementNotRegisteredException hElementNotRegisteredException;
        HWrongConfigValueException hWrongConfigValueException;
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            String temp = reader.readLine();
            HConfigElement config = new HConfigElement(null, null);
            hElementRegisteredException = null;
            hElementNotRegisteredException = null;
            hWrongConfigValueException = null;
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
                        } catch (HElementNotRegisteredException elementNotRegisteredException) {
                            type = null;
                            if (hElementNotRegisteredException == null)
                                hElementNotRegisteredException = elementNotRegisteredException;
                        }
                        if (type == null)
                            type = HConfigType.STRING;
                        config.setType(type);
                    }
                    if (temp.startsWith("value:")) {
                        config.setValue(temp.substring(7));
                        try {
                            this.add(config);
                        } catch (HElementRegisteredException registeredException) {
                            if (hElementRegisteredException == null)
                                hElementRegisteredException = registeredException;
                        }
                        config = new HConfigElement(null, null);
                    }
                } catch (HWrongConfigValueException wrongConfigValueException) {
                    if (hWrongConfigValueException == null)
                        hWrongConfigValueException = wrongConfigValueException;
                }
                temp = reader.readLine();
            }
        }
        if (hElementRegisteredException != null)
            throw hElementRegisteredException;
        if (hElementNotRegisteredException != null)
            throw hElementNotRegisteredException;
        if (hWrongConfigValueException != null)
            throw hWrongConfigValueException;
    }

    /**
     * Write configurations to file.
     */
    public void write() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file))) {
            for (HConfigElement i : this.data.getMap().values()) {
                writer.write("name: ");
                writer.write(i.getName());
                writer.newLine();
                writer.write("note: ");
                writer.write(i.getNote());
                writer.newLine();
                writer.write("type: ");
                writer.write(i.getType().getName());
                writer.newLine();
                writer.write("value: ");
                writer.write(i.getValue());
                writer.newLine();
                writer.newLine();
            }
        }
    }

    @Override
    public @NotNull String toString() {
        return "HConfigurations:" + this.data;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HConfigurations that = (HConfigurations) o;
        return this.file.equals(that.file) && this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }
}
