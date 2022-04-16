package Core.Exceptions;

import java.io.Serial;

public class ModUnsupportedCraftworldVersionException extends ModInformationException {
    @Serial
    private static final long serialVersionUID = -8851833450852889381L;

    public ModUnsupportedCraftworldVersionException() {
        super();
    }

    public ModUnsupportedCraftworldVersionException(String message) {
        super(message);
    }

    public ModUnsupportedCraftworldVersionException(Throwable cause) {
        super(cause);
    }

    public ModUnsupportedCraftworldVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
