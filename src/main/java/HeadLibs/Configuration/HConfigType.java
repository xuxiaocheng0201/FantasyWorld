package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Registerer.HMapRegistererWithName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Configuration type.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfigType {
    /**
     * All Registered configuration types.
     */
    private static final HMapRegistererWithName<HConfigType> REGISTERED_MAP = new HMapRegistererWithName<>();
    /**
     * Get registered map.
     * @return registered map.
     */
    public static HMapRegistererWithName<HConfigType> getRegisteredMap() {
        return REGISTERED_MAP;
    }

    public static final HConfigType BOOLEAN = new HConfigType("BOOLEAN", HConfigType::fixValueBoolean);
    public static final HConfigType BOOLEAN_ARRAY = new HConfigType("BOOLEAN_ARRAY", value -> fixValueInList(value, HConfigType::fixValueBoolean));
    public static final HConfigType BYTE = new HConfigType("BYTE", HConfigType::fixValueByte);
    public static final HConfigType BYTE_ARRAY = new HConfigType("BYTE_ARRAY", value -> fixValueInList(value, HConfigType::fixValueByte));
    public static final HConfigType SHORT = new HConfigType("SHORT", HConfigType::fixValueShort);
    public static final HConfigType SHORT_ARRAY = new HConfigType("SHORT_ARRAY", value -> fixValueInList(value, HConfigType::fixValueShort));
    public static final HConfigType INT = new HConfigType("INT", HConfigType::fixValueInt);
    public static final HConfigType INT_ARRAY = new HConfigType("INT_ARRAY", value -> fixValueInList(value, HConfigType::fixValueInt));
    public static final HConfigType LONG = new HConfigType("LONG", HConfigType::fixValueLong);
    public static final HConfigType LONG_ARRAY = new HConfigType("LONG_ARRAY", value -> fixValueInList(value, HConfigType::fixValueLong));
    public static final HConfigType FLOAT = new HConfigType("FLOAT", HConfigType::fixValueFloat);
    public static final HConfigType FLOAT_ARRAY = new HConfigType("FLOAT_ARRAY", value -> fixValueInList(value, HConfigType::fixValueFloat));
    public static final HConfigType DOUBLE = new HConfigType("DOUBLE", HConfigType::fixValueDouble);
    public static final HConfigType DOUBLE_ARRAY = new HConfigType("DOUBLE_ARRAY", value -> fixValueInList(value, HConfigType::fixValueDouble));
    public static final HConfigType STRING = new HConfigType("STRING", HConfigType::fixValueString);
    public static final HConfigType STRING_ARRAY = new HConfigType("STRING_ARRAY", value -> fixValueInList(value, HConfigType::fixValueString));

    /**
     * Configuration type id/name
     */
    private final @NotNull String name;
    /**
     * The method to check {@link HConfigElement#setValue(String)} is suitable for this type
     */
    private final @NotNull FixConfigurationValueMethod check;

    /**
     * Register a new ConfigType.
     * @param name type's name
     * @param check type's checkMethod
     */
    public HConfigType(@NotNull String name, @NotNull FixConfigurationValueMethod check) {
        super();
        this.name = name.toUpperCase();
        this.check = check;
        REGISTERED_MAP.register(this.name, this);
    }

    /**
     * Register a new ConfigType with {@link HConfigType#fixValueString(String)} checkMethod.
     * @param name type's name
     */
    public HConfigType(@NotNull String name) {
        this(name, HConfigType::fixValueString);
    }

    /**
     * Get type's name
     * @return type's name
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Call checkMethod.
     * @param value config's value to set
     * @return null - unfixable. notNull - fixed
     */
    public @Nullable String fix(@Nullable String value) {
        return this.check.fix(value);
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HConfigType that = (HConfigType) o;
        return this.name.equals(that.name) && this.check.equals(that.check);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public interface FixConfigurationValueMethod {
        /**
         * Fix configuration value is right for type.
         * @param value Configuration value.
         * @return Null - unfixable. NotNull - fixed.
         */
        @Nullable String fix(@Nullable String value);
    }

    public static @Nullable String fixValueBoolean(@Nullable String value) {
        if (value == null || "null".equals(value))
            return "false";
        try {
            return String.valueOf(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueByte(@Nullable String value) {
        if (value == null || "null".equals(value))
            return "0";
        try {
            return String.valueOf(Byte.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueShort(@Nullable String value) {
        if (value == null || "null".equals(value))
            return "0";
        try {
            return String.valueOf(Short.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueInt(@Nullable String value) {
        if (value == null || "null".equals(value))
            return "0";
        try {
            return String.valueOf(Integer.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueLong(@Nullable String value) {
        if (value == null || "null".equals(value))
            return "0";
        try {
            return String.valueOf(Long.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueFloat(@Nullable String value) {
        if (value == null || "null".equals(value))
            return "0.0";
        try {
            return String.valueOf(Float.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueDouble(@Nullable String value) {
        if (value == null || "null".equals(value))
            return "0.0";
        try {
            return String.valueOf(Double.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @NotNull String fixValueString(@Nullable String value) {
        return HStringHelper.noNull(value);
    }

    public static @Nullable String fixValueInList(@Nullable String value, @NotNull FixConfigurationValueMethod method) {
        String valueWithoutBrackets = HStringHelper.noNullStrip(value);
        if (valueWithoutBrackets.isEmpty())
            return "[]";
        if (valueWithoutBrackets.charAt(0) == '[')
            valueWithoutBrackets = valueWithoutBrackets.substring(1);
        if (valueWithoutBrackets.charAt(valueWithoutBrackets.length() - 1) == ']')
            valueWithoutBrackets = valueWithoutBrackets.substring(0, valueWithoutBrackets.length() - 1);
        String[] elements = HStringHelper.strip(valueWithoutBrackets.split(","));
        StringBuilder fixed = new StringBuilder("[");
        for (String element: elements) {
            String single = method.fix(element);
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
