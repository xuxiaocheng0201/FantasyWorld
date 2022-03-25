package Core.Exceptions;

public class ModRequirementsException extends IllegalArgumentException {
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
