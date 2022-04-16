package Core.Exceptions;

import java.io.Serial;

public class ModRequirementFormatException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -4542217647636150904L;

    public ModRequirementFormatException() {
        super();
    }

    public ModRequirementFormatException(String message) {
        super(message);
    }

    public ModRequirementFormatException(Throwable cause) {
        super(cause);
    }

    public ModRequirementFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
