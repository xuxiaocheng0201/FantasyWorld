package CraftWorld.Exception;

public class RegisteredException extends Exception{
    public RegisteredException() {
        super();
    }

    public RegisteredException(String message) {
        super(message);
    }

    public RegisteredException(Throwable cause) {
        super(cause);
    }

    public RegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}
