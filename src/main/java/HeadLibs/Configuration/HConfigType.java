package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HElementRegisteredException;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * Configuration type.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfigType implements Serializable {
    @Serial
    private static final long serialVersionUID = -7824027279249219691L;

    /**
     * All Registered configuration types.
     */
    private static final @NotNull HMapRegisterer<String, HConfigType> REGISTERED_MAP = new HMapRegisterer<>(false, false, true);
    /**
     * Get registered map.
     * @return registered map
     */
    public static @NotNull HMapRegisterer<String, HConfigType> getRegisteredMap() {
        return REGISTERED_MAP;
    }

    public static final @NotNull HConfigType BOOLEAN = new HConfigType("BOOLEAN", HConfigType::fixValueBoolean);
    public static final @NotNull HConfigType BOOLEAN_ARRAY = new HConfigType("BOOLEAN_ARRAY", value -> fixValueInList(value, HConfigType::fixValueBoolean));
    public static final @NotNull HConfigType BYTE = new HConfigType("BYTE", HConfigType::fixValueByte);
    public static final @NotNull HConfigType BYTE_ARRAY = new HConfigType("BYTE_ARRAY", value -> fixValueInList(value, HConfigType::fixValueByte));
    public static final @NotNull HConfigType SHORT = new HConfigType("SHORT", HConfigType::fixValueShort);
    public static final @NotNull HConfigType SHORT_ARRAY = new HConfigType("SHORT_ARRAY", value -> fixValueInList(value, HConfigType::fixValueShort));
    public static final @NotNull HConfigType INT = new HConfigType("INT", HConfigType::fixValueInt);
    public static final @NotNull HConfigType INT_ARRAY = new HConfigType("INT_ARRAY", value -> fixValueInList(value, HConfigType::fixValueInt));
    public static final @NotNull HConfigType LONG = new HConfigType("LONG", HConfigType::fixValueLong);
    public static final @NotNull HConfigType LONG_ARRAY = new HConfigType("LONG_ARRAY", value -> fixValueInList(value, HConfigType::fixValueLong));
    public static final @NotNull HConfigType FLOAT = new HConfigType("FLOAT", HConfigType::fixValueFloat);
    public static final @NotNull HConfigType FLOAT_ARRAY = new HConfigType("FLOAT_ARRAY", value -> fixValueInList(value, HConfigType::fixValueFloat));
    public static final @NotNull HConfigType DOUBLE = new HConfigType("DOUBLE", HConfigType::fixValueDouble);
    public static final @NotNull HConfigType DOUBLE_ARRAY = new HConfigType("DOUBLE_ARRAY", value -> fixValueInList(value, HConfigType::fixValueDouble));
    public static final @NotNull HConfigType STRING = new HConfigType("STRING", HConfigType::fixValueString);
    public static final @NotNull HConfigType STRING_ARRAY = new HConfigType("STRING_ARRAY", value -> fixValueInList(value, HConfigType::fixValueString));
    static {
        try {BOOLEAN.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {BOOLEAN_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {BYTE.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {BYTE_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {SHORT.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {SHORT_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {INT.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {INT_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {LONG.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {LONG_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {FLOAT.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {FLOAT_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {DOUBLE.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {DOUBLE_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {STRING.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
        try {STRING_ARRAY.register();} catch (HElementRegisteredException exception) {HLog.logger(exception);}
    }

    /**
     * Configuration type name.
     */
    private final @NotNull String name;
    /**
     * The method to check {@link HConfigElement#setValue(String)} is suitable for this type.
     * Fix configuration value is right for type.
     * T: value Configuration value.
     * R: Null - unfixable. NotNull - fixed.
     */
    private final @NotNull CheckValueFunction check;

    private interface CheckValueFunction extends Function<String, String>, Serializable {
    }

    /**
     * Register a new ConfigType.
     * @param name type name
     * @param check type's checkMethod
     */
    public HConfigType(@NotNull String name, @NotNull CheckValueFunction check) {
        super();
        this.name = name.toUpperCase();
        this.check = check;
    }

    /**
     * Register a new ConfigType with {@link HConfigType#fixValueString(String)} checkMethod.
     * @param name type name
     */
    public HConfigType(@NotNull String name) {
        this(name, HConfigType::fixValueString);
    }

    /**
     * Register this config type to registerer map.
     * @throws HElementRegisteredException Type has been registered.
     */
    public void register() throws HElementRegisteredException {
        REGISTERED_MAP.register(this.name, this);
    }

    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Call checkMethod to make value fit type.
     * @param value config's value to set
     * @return null - unfixable. notNull - fixed
     */
    public @Nullable String fix(@Nullable String value) {
        return this.check.apply(value);
    }

    @Override
    public @NotNull String toString() {
        return "HConfigType:" + this.name;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HConfigType that)) return false;
        return this.name.equals(that.name) && this.check.equals(that.check);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    public static @Nullable String fixValueBoolean(@Nullable String value) {
        if (HStringHelper.meanNull(value))
            return "false";
        try {
            return String.valueOf(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueByte(@Nullable String value) {
        if (HStringHelper.meanNull(value))
            return "0";
        try {
            return String.valueOf(Byte.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueShort(@Nullable String value) {
        if (HStringHelper.meanNull(value))
            return "0";
        try {
            return String.valueOf(Short.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueInt(@Nullable String value) {
        if (HStringHelper.meanNull(value))
            return "0";
        try {
            return String.valueOf(Integer.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueLong(@Nullable String value) {
        if (HStringHelper.meanNull(value))
            return "0";
        try {
            return String.valueOf(Long.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueFloat(@Nullable String value) {
        if (HStringHelper.meanNull(value))
            return "0.0";
        try {
            return String.valueOf(Float.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueDouble(@Nullable String value) {
        if (HStringHelper.meanNull(value))
            return "0.0";
        try {
            return String.valueOf(Double.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @NotNull String fixValueString(@Nullable String value) {
        return value == null || value.isBlank() ? "null" :value;
    }

    public static @Nullable String fixValueInList(@Nullable String value, @NotNull Function<? super String, String> method) {
        String valueWithoutBrackets = HStringHelper.notNullStrip(value);
        if (valueWithoutBrackets.isEmpty())
            return "[]";
        if (valueWithoutBrackets.charAt(0) == '[')
            valueWithoutBrackets = valueWithoutBrackets.substring(1);
        if (valueWithoutBrackets.charAt(valueWithoutBrackets.length() - 1) == ']')
            valueWithoutBrackets = valueWithoutBrackets.substring(0, valueWithoutBrackets.length() - 1);
        String[] elements = HStringHelper.strip(valueWithoutBrackets.split(","));
        StringBuilder fixed = new StringBuilder("[");
        for (String element: elements) {
            String single = method.apply(element);
            if (single == null)
                return null;
            fixed.append(single);
            fixed.append(",");
        }
        fixed.deleteCharAt(fixed.length() - 1);
        fixed.append("]");
        return fixed.toString();
    }
}
