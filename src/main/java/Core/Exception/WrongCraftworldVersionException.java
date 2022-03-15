package Core.Exception;

public class WrongCraftworldVersionException extends ModRequirementsException {
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
