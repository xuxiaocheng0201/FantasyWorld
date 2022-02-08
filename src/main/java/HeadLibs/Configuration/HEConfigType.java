package HeadLibs.Configuration;

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
        switch (this) {
            case BOOLEAN_ARRAY:
                return BOOLEAN;
            case BYTE_ARRAY:
                return BYTE;
            case SHORT_ARRAY:
                return SHORT;
            case INT_ARRAY:
                return INT;
            case LONG_ARRAY:
                return LONG;
            case FLOAT_ARRAY:
                return FLOAT;
            case DOUBLE_ARRAY:
                return DOUBLE;
            default:
                return STRING;
        }
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
                        throw new NumberFormatException("Array value error in " + i + "(th) char.");
                    }
                    part += value.charAt(i);
                }
                if (!"]".equals(part) && !type.checkString(part))
                    throw new NumberFormatException("Array value error in " + (value.length() - 2) + "(th) char.");
            }
            return true;
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static HEConfigType getTypeByName(String name) {
        switch (name) {
            case "BOOLEAN":
                return BOOLEAN;
            case "BOOLEAN_ARRAY":
                return BOOLEAN_ARRAY;
            case "BYTE":
                return BYTE;
            case "BYTE_ARRAY":
                return BYTE_ARRAY;
            case "SHORT":
                return SHORT;
            case "SHORT_ARRAY":
                return SHORT_ARRAY;
            case "INT":
                return INT;
            case "INT_ARRAY":
                return INT_ARRAY;
            case "LONG":
                return LONG;
            case "LONG_ARRAY":
                return LONG_ARRAY;
            case "FLOAT":
                return FLOAT;
            case "FLOAT_ARRAY":
                return FLOAT_ARRAY;
            case "DOUBLE":
                return DOUBLE;
            case "DOUBLE_ARRAY":
                return DOUBLE_ARRAY;
            case "STRING_ARRAY":
                return STRING_ARRAY;
            default:
                return STRING;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case BOOLEAN:
                return "BOOLEAN";
            case BOOLEAN_ARRAY:
                return "BOOLEAN_ARRAY";
            case BYTE:
                return "BYTE";
            case BYTE_ARRAY:
                return "BYTE_ARRAY";
            case SHORT:
                return "SHORT";
            case SHORT_ARRAY:
                return "SHORT_ARRAY";
            case INT:
                return "INT";
            case INT_ARRAY:
                return "INT_ARRAY";
            case LONG:
                return "LONG";
            case LONG_ARRAY:
                return "LONG_ARRAY";
            case FLOAT:
                return "FLOAT";
            case FLOAT_ARRAY:
                return "FLOAT_ARRAY";
            case DOUBLE:
                return "DOUBLE";
            case DOUBLE_ARRAY:
                return "DOUBLE_ARRAY";
            case STRING:
                return "STRING";
            case STRING_ARRAY:
                return "STRING_ARRAY";
        }
        return "UNDEFINED";
    }
}
