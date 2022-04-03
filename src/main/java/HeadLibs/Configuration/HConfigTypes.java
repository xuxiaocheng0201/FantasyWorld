package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Registerer.HMapRegisterer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Configuration type.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HConfigTypes {
    private static final HMapRegisterer<HConfigTypes> REGISTERED_MAP = new HMapRegisterer<>();
    public static HMapRegisterer<HConfigTypes> getRegisteredMap() {
        return REGISTERED_MAP;
    }

    public static final HConfigTypes BOOLEAN = new HConfigTypes("BOOLEAN", HConfigTypes::fixValueBoolean);
    public static final HConfigTypes BOOLEAN_ARRAY = new HConfigTypes("BOOLEAN_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueBoolean));
    public static final HConfigTypes BYTE = new HConfigTypes("BYTE", HConfigTypes::fixValueByte);
    public static final HConfigTypes BYTE_ARRAY = new HConfigTypes("BYTE_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueByte));
    public static final HConfigTypes SHORT = new HConfigTypes("SHORT", HConfigTypes::fixValueShort);
    public static final HConfigTypes SHORT_ARRAY = new HConfigTypes("SHORT_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueShort));
    public static final HConfigTypes INT = new HConfigTypes("INT", HConfigTypes::fixValueInt);
    public static final HConfigTypes INT_ARRAY = new HConfigTypes("INT_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueInt));
    public static final HConfigTypes LONG = new HConfigTypes("LONG", HConfigTypes::fixValueLong);
    public static final HConfigTypes LONG_ARRAY = new HConfigTypes("LONG_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueLong));
    public static final HConfigTypes FLOAT = new HConfigTypes("FLOAT", HConfigTypes::fixValueFloat);
    public static final HConfigTypes FLOAT_ARRAY = new HConfigTypes("FLOAT_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueFloat));
    public static final HConfigTypes DOUBLE = new HConfigTypes("DOUBLE", HConfigTypes::fixValueDouble);
    public static final HConfigTypes DOUBLE_ARRAY = new HConfigTypes("DOUBLE_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueDouble));
    public static final HConfigTypes STRING = new HConfigTypes("STRING", HConfigTypes::fixValueString);
    public static final HConfigTypes STRING_ARRAY = new HConfigTypes("STRING_ARRAY", value -> fixValueInList(value, HConfigTypes::fixValueString));

    private final @NotNull String name;
    private final @NotNull FixConfigurationValueMethod check;

    public HConfigTypes(@NotNull String name, @NotNull FixConfigurationValueMethod check) {
        super();
        this.name = name.toUpperCase();
        this.check = check;
        REGISTERED_MAP.register(this.name, this);
    }

    public HConfigTypes(@NotNull String name) {
        this(name, value -> value);
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @Nullable String fix(@Nullable String value) {
        return this.check.fix(value);
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HConfigTypes that = (HConfigTypes) o;
        return this.name.equals(that.name);
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
        String valueWithoutBrackets = HStringHelper.noNull(HStringHelper.delBlankHeadAndTail(value));
        if (valueWithoutBrackets.isEmpty())
            return "[]";
        if (valueWithoutBrackets.charAt(0) == '[')
            valueWithoutBrackets = valueWithoutBrackets.substring(1);
        if (valueWithoutBrackets.charAt(valueWithoutBrackets.length() - 1) == ']')
            valueWithoutBrackets = valueWithoutBrackets.substring(0, valueWithoutBrackets.length() - 1);
        String[] elements = HStringHelper.delBlankHeadAndTail(valueWithoutBrackets.split(","));
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
