package Core.Exception;

public class NoCommonConstructorException extends NoSuchMethodException {
    public NoCommonConstructorException() {
        super();
    }

    public NoCommonConstructorException(String message) {
        super(message);
    }

    public NoCommonConstructorException(Throwable cause) {
        super((cause == null) ? null : cause.toString());
        super.fillInStackTrace();
        super.initCause(cause);
    }

    public NoCommonConstructorException(String message, Throwable cause) {
        super(message);
        super.fillInStackTrace();
        super.initCause(cause);
    }
}
