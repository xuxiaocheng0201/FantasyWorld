package CraftWorld.Exception;

import java.io.IOException;

public class DSTFormatException extends IOException{
    public DSTFormatException() {
        super();
    }

    public DSTFormatException(String message) {
        super(message);
    }

    public DSTFormatException(Throwable cause) {
        super(cause);
    }

    public DSTFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
