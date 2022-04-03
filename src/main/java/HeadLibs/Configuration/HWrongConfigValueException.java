package HeadLibs.Configuration;

import HeadLibs.Helper.HStringHelper;

import java.io.Serial;

/**
 * Throw when Configuration value isn't suitable for the type.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HWrongConfigValueException extends NumberFormatException {
    @Serial
    private static final long serialVersionUID = -4695385007029343114L;

    /**
     * Construct a HWrongConfigValueException with no detail message.
     */
    public HWrongConfigValueException() {
        super();
    }

    public HWrongConfigValueException(String s) {
        super(s);
    }

    public HWrongConfigValueException(HConfigTypes type) {
        super(HStringHelper.merge("Value is no longer suitable for type: ", type.getName()));
    }
}
