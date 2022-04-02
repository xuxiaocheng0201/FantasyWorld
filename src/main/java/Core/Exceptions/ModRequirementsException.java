package Core.Exceptions;

import java.io.Serial;

public class ModRequirementsException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 9199196940721277887L;

    public ModRequirementsException() {
        super();
    }

    public ModRequirementsException(String message) {
        super(message);
    }

    public ModRequirementsException(Throwable cause) {
        super(cause);
    }

    public ModRequirementsException(String message, Throwable cause) {
        super(message, cause);
    }
}
