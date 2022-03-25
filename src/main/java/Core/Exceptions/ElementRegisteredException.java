package Core.Exceptions;

public class ElementRegisteredException extends Exception{
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
