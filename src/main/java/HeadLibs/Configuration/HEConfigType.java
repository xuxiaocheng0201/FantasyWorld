package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;

public enum HEConfigType {
    BOOLEAN, BOOLEAN_ARRAY,
    BYTE, BYTE_ARRAY,
    SHORT, SHORT_ARRAY,
    INT, INT_ARRAY,
    LONG, LONG_ARRAY,
    FLOAT, FLOAT_ARRAY,
    DOUBLE, DOUBLE_ARRAY,
    STRING, STRING_ARRAY;

    public boolean isArray() {
        return (this == BOOLEAN_ARRAY
                || this == BYTE_ARRAY
                || this == SHORT_ARRAY
                || this == INT_ARRAY
                || this == LONG_ARRAY
                || this == FLOAT_ARRAY
                || this == DOUBLE_ARRAY
                || this == STRING_ARRAY);
    }

    public HEConfigType withoutArray() {
        return switch (this) {
            case BOOLEAN_ARRAY -> BOOLEAN;
            case BYTE_ARRAY -> BYTE;
            case SHORT_ARRAY -> SHORT;
            case INT_ARRAY -> INT;
            case LONG_ARRAY -> LONG;
            case FLOAT_ARRAY -> FLOAT;
            case DOUBLE_ARRAY -> DOUBLE;
            default -> STRING;
        };
    }

    public boolean checkString(String value) {
        try {
            if ("true".equals(value))
                value = "1";
            if ("false".equals(value))
                value = "0";
            if (!this.isArray()) {
                if (this == BOOLEAN && !"1".equals(value) && !"0".equals(value))
                    throw new NumberFormatException("Value isn't true or false.");
                if (this == BYTE)
                    Byte.parseByte(value);
                if (this == SHORT)
                    Short.parseShort(value);
                if (this == INT)
                    Integer.parseInt(value);
                if (this == LONG)
                    Long.parseLong(value);
                if (this == FLOAT)
                    Float.parseFloat(value);
                if (this ==DOUBLE)
                    Double.parseDouble(value);
            }
            if (this.isArray()) {
                if (value.charAt(0) != '[' || value.charAt(value.length() - 1) != ']')
                    throw new NumberFormatException("Array format error.");
                HEConfigType type = this.withoutArray();
                String part = "";
                for (int i = 1; i < value.length() - 1; ++i) {
                    if (value.charAt(i) == ',') {
                        System.out.println(part);
                        if (type.checkString(part)) {
                            ++i;
                            while(value.charAt(i) == ' ')
                                ++i;
                            part = String.valueOf(value.charAt(i));
                            continue;
                        }
                        throw new NumberFormatException(HStringHelper.merge("Array value error in ", i, "(th) char."));
                    }
                    part += value.charAt(i);
                }
                if (!"]".equals(part) && !type.checkString(part))
                    throw new NumberFormatException(HStringHelper.merge("Array value error in ", value.length() - 2, "(th) char."));
            }
            return true;
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static HEConfigType getTypeByName(String name) {
        return switch (name) {
            case "BOOLEAN" -> BOOLEAN;
            case "BOOLEAN_ARRAY" -> BOOLEAN_ARRAY;
            case "BYTE" -> BYTE;
            case "BYTE_ARRAY" -> BYTE_ARRAY;
            case "SHORT" -> SHORT;
            case "SHORT_ARRAY" -> SHORT_ARRAY;
            case "INT" -> INT;
            case "INT_ARRAY" -> INT_ARRAY;
            case "LONG" -> LONG;
            case "LONG_ARRAY" -> LONG_ARRAY;
            case "FLOAT" -> FLOAT;
            case "FLOAT_ARRAY" -> FLOAT_ARRAY;
            case "DOUBLE" -> DOUBLE;
            case "DOUBLE_ARRAY" -> DOUBLE_ARRAY;
            case "STRING_ARRAY" ->  STRING_ARRAY;
            default -> STRING;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case BOOLEAN -> "BOOLEAN";
            case BOOLEAN_ARRAY -> "BOOLEAN_ARRAY";
            case BYTE -> "BYTE";
            case BYTE_ARRAY -> "BYTE_ARRAY";
            case SHORT -> "SHORT";
            case SHORT_ARRAY -> "SHORT_ARRAY";
            case INT -> "INT";
            case INT_ARRAY -> "INT_ARRAY";
            case LONG -> "LONG";
            case LONG_ARRAY -> "LONG_ARRAY";
            case FLOAT -> "FLOAT";
            case FLOAT_ARRAY -> "FLOAT_ARRAY";
            case DOUBLE -> "DOUBLE";
            case DOUBLE_ARRAY -> "DOUBLE_ARRAY";
            case STRING -> "STRING";
            case STRING_ARRAY -> "STRING_ARRAY";
        };
    }
}
