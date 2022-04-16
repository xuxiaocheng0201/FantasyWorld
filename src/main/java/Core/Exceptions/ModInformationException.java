package Core.Exceptions;

import java.io.Serial;

public class ModInformationException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 9199196940721277887L;

    public ModInformationException() {
        super();
    }

    public ModInformationException(String message) {
        super(message);
    }

    public ModInformationException(Throwable cause) {
        super(cause);
    }

    public ModInformationException(String message, Throwable cause) {
        super(message, cause);
    }
}
