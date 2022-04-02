package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration type.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HEConfigType {
    //                       name         element       array_type
    private static final Map<String, Pair<HEConfigType, HEConfigType>> ALL_TYPES = new HashMap<>();
    public static boolean register(@NotNull HEConfigType type) {
        Pair<HEConfigType, HEConfigType> pair;
        if (ALL_TYPES.containsKey(type.name))
            pair = ALL_TYPES.get(type.name);
        else
            pair = new Pair<>();
        if (type.array)
            if (pair.getValue() == null)
                pair.setValue(type);
            else
                return pair.getValue().equals(type);
        else
        if (pair.getKey() == null)
            pair.setKey(type);
        else
            return pair.getKey().equals(type);
        if (ALL_TYPES.containsKey(type.name))
            ALL_TYPES.replace(type.name, pair);
        else
            ALL_TYPES.put(type.name, pair);
        return true;
    }
    public static HEConfigType getTypeByName(String name, boolean array) {
        if (ALL_TYPES.containsKey(name))
            if (array)
                return ALL_TYPES.get(name).getValue();
            else
                return ALL_TYPES.get(name).getKey();
        return null;
    }

    public static final HEConfigType BOOLEAN = new HEConfigType("BOOLEAN", false, HEConfigType::fixValueBoolean);
    public static final HEConfigType BOOLEAN_ARRAY = new HEConfigType("BOOLEAN", true, value -> fixValueInList(value, HEConfigType::fixValueBoolean));
    public static final HEConfigType BYTE = new HEConfigType("BYTE", false, HEConfigType::fixValueByte);
    public static final HEConfigType BYTE_ARRAY = new HEConfigType("BYTE", true, value -> fixValueInList(value, HEConfigType::fixValueByte));
    public static final HEConfigType SHORT = new HEConfigType("SHORT", false, HEConfigType::fixValueShort);
    public static final HEConfigType SHORT_ARRAY = new HEConfigType("SHORT", true, value -> fixValueInList(value, HEConfigType::fixValueShort));
    public static final HEConfigType INT = new HEConfigType("INT", false, HEConfigType::fixValueInt);
    public static final HEConfigType INT_ARRAY = new HEConfigType("INT", true, value -> fixValueInList(value, HEConfigType::fixValueInt));
    public static final HEConfigType LONG = new HEConfigType("LONG", false, HEConfigType::fixValueLong);
    public static final HEConfigType LONG_ARRAY = new HEConfigType("LONG", true, value -> fixValueInList(value, HEConfigType::fixValueLong));
    public static final HEConfigType FLOAT = new HEConfigType("FLOAT", false, HEConfigType::fixValueFloat);
    public static final HEConfigType FLOAT_ARRAY = new HEConfigType("FLOAT", true, value -> fixValueInList(value, HEConfigType::fixValueFloat));
    public static final HEConfigType DOUBLE = new HEConfigType("DOUBLE", false, HEConfigType::fixValueDouble);
    public static final HEConfigType DOUBLE_ARRAY = new HEConfigType("DOUBLE", true, value -> fixValueInList(value, HEConfigType::fixValueDouble));
    public static final HEConfigType STRING = new HEConfigType("STRING", false, value -> value);
    public static final HEConfigType STRING_ARRAY = new HEConfigType("STRING", true, value -> fixValueInList(value, value1 -> value1));

    private final String name;
    private final boolean array;
    private final FixConfigurationValueMethod check;

    public HEConfigType(String name, boolean isArray, FixConfigurationValueMethod check) {
        super();
        this.name = name.toUpperCase();
        this.array = isArray;
        this.check = check;
        register(this);
    }

    public HEConfigType(String name, boolean isArray) {
        this(name, isArray, value -> value);
    }

    public String getName() {
        return this.name;
    }

    public boolean isArray() {
        return this.array;
    }

    public String fix(String value) {
        return this.check.fix(value);
    }

    @Override
    public String toString() {
        if (this.array)
            return HStringHelper.merge(this.name, "_ARRAY");
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HEConfigType that = (HEConfigType) o;
        return this.array == that.array && this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public interface FixConfigurationValueMethod {
        /**
         * Fix configuration value is right for type.
         * @param value Configuration value.
         * @return Null means unfixable. Others mean fixed.
         */
        @Nullable String fix(@Nullable String value);
    }

    public static @Nullable String fixValueBoolean(@Nullable String value) {
        try {
            return String.valueOf(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueByte(@Nullable String value) {
        if (value == null)
            return "0";
        try {
            return String.valueOf(Byte.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueShort(@Nullable String value) {
        if (value == null)
            return "0";
        try {
            return String.valueOf(Short.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueInt(@Nullable String value) {
        if (value == null)
            return "0";
        try {
            return String.valueOf(Integer.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueLong(@Nullable String value) {
        if (value == null)
            return "0";
        try {
            return String.valueOf(Long.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueFloat(@Nullable String value) {
        if (value == null)
            return "0.0";
        try {
            return String.valueOf(Float.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static @Nullable String fixValueDouble(@Nullable String value) {
        if (value == null)
            return "0.0";
        try {
            return String.valueOf(Double.valueOf(value));
        } catch (NumberFormatException exception) {
            return null;
        }
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
