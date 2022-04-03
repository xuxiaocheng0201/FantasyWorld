package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;

import java.io.Serial;

/**
 * Throw when Configuration value isn't suitable for the type.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HWrongConfigValueException extends Exception {
    @Serial
    private static final long serialVersionUID = -4695385007029343114L;

    /**
     * Construct a HWrongConfigValueException with no detail message.
     */
    public HWrongConfigValueException() {
        super();
    }

    /**
     * Construct a HWrongConfigValueException with wrong type.
     * @param type wrong type
     */
    public HWrongConfigValueException(HConfigType type) {
        super(HStringHelper.merge("Value is no longer suitable. type=", type.getName()));
    }

    /**
     * Construct a HWrongConfigValueException with wrong type and value.
     * @param type wrong type
     * @param value wrong value
     */
    public HWrongConfigValueException(HConfigType type, String value) {
        super(HStringHelper.merge("Value is no longer suitable. type=", type.getName(), " value='", value, "'"));
    }

    /**
     * Construct a HWrongConfigValueException with message.
     * @param message warning message
     */
    public HWrongConfigValueException(String message) {
        super(message);
    }

    /**
     * Construct a HWrongConfigValueException with message and wrong type.
     * @param message warning message
     * @param type wrong type
     */
    public HWrongConfigValueException(String message, HConfigType type) {
        super(HStringHelper.merge(message, " type=", type.getName()));
    }

    /**
     * Construct a HWrongConfigValueException with message and wrong type and value.
     * @param message warning message
     * @param type wrong type
     * @param value wrong value
     */
    public HWrongConfigValueException(String message, HConfigType type, String value) {
        super(HStringHelper.merge(message, " type=", type.getName(), " value='", value, "'"));
    }
}
