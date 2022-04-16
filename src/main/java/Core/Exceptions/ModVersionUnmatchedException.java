package Core.Exceptions;

import java.io.Serial;

public class ModVersionUnmatchedException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -5074629509213313717L;

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
