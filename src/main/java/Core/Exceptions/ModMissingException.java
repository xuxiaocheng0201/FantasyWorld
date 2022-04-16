package Core.Exceptions;

import java.io.Serial;

public class ModMissingException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -7028339289330024072L;

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
