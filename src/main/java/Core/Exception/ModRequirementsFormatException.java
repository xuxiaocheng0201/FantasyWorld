package Core.Exception;

public class ModRequirementsFormatException extends ModRequirementsException {
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
