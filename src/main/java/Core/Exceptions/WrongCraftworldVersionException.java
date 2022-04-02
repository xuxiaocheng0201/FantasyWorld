package Core.Exceptions;

import java.io.Serial;

public class WrongCraftworldVersionException extends ModRequirementsException {
    @Serial
    private static final long serialVersionUID = -8851833450852889381L;

    public WrongCraftworldVersionException() {
        super();
    }

    public WrongCraftworldVersionException(String message) {
        super(message);
    }

    public WrongCraftworldVersionException(Throwable cause) {
        super(cause);
    }

    public WrongCraftworldVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
