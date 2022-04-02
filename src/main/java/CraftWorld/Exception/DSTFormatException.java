package CraftWorld.Exception;

import java.io.IOException;
import java.io.Serial;

public class DSTFormatException extends IOException{
    @Serial
    private static final long serialVersionUID = -8277448868946723580L;

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
