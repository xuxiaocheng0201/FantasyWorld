package Core.Exception;

public class ModVersionUnmatchedException extends ModRequirementsException {
    public ModVersionUnmatchedException() {
        super();
    }

    public ModVersionUnmatchedException(String message) {
        super(message);
    }

    public ModVersionUnmatchedException(Throwable cause) {
        super(cause);
    }

    public ModVersionUnmatchedException(String message, Throwable cause) {
        super(message, cause);
    }
}
