package Core.Exceptions;

import java.io.Serial;

public class ModRequirementsFormatException extends ModRequirementsException {
    @Serial
    private static final long serialVersionUID = -4542217647636150904L;

    public ModRequirementsFormatException() {
        super();
    }

    public ModRequirementsFormatException(String message) {
        super(message);
    }

    public ModRequirementsFormatException(Throwable cause) {
        super(cause);
    }

    public ModRequirementsFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
