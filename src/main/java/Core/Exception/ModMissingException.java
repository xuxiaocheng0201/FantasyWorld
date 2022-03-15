package Core.Exception;

public class ModMissingException extends ModRequirementsException {
    public ModMissingException() {
        super();
    }

    public ModMissingException(String message) {
        super(message);
    }

    public ModMissingException(Throwable cause) {
        super(cause);
    }

    public ModMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
