package Core.Exceptions;

import java.io.Serial;

public class ElementRegisteredException extends Exception{
    @Serial
    private static final long serialVersionUID = 5758033473623251615L;

    public ElementRegisteredException() {
        super();
    }

    public ElementRegisteredException(String message) {
        super(message);
    }

    public ElementRegisteredException(Throwable cause) {
        super(cause);
    }

    public ElementRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}
