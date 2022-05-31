package HeadLibs.Registerer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

@SuppressWarnings("unused")
public class HashCollisionsErrorException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2572968988882422778L;

    private static final String DEFAULT_MESSAGE = "Hash Collisions! Please report it to developers!";
    private static @NotNull String getHashCollisionsMessage(@Nullable String message) {
        return (message == null) ? DEFAULT_MESSAGE : message;
    }

    public HashCollisionsErrorException() {
        super(DEFAULT_MESSAGE);
    }

    public HashCollisionsErrorException(@Nullable Object a, @Nullable Object b) {
        super(DEFAULT_MESSAGE + " \"" + a + "\" with hash " + Objects.hash(a) + " and \"" + b +  "\" with hash " + Objects.hash(b));
    }

    public HashCollisionsErrorException(@Nullable String message) {
        super(getHashCollisionsMessage(message));
    }

    public HashCollisionsErrorException(@Nullable String message, @Nullable Object a, @Nullable Object b) {
        super(getHashCollisionsMessage(message) + " \"" + a + "\" with hash " + Objects.hash(a) + " and \"" + b +  "\" with hash " + Objects.hash(b));
    }

    public HashCollisionsErrorException(@Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public HashCollisionsErrorException(@Nullable Object a, @Nullable Object b, @Nullable Throwable throwable) {
        super(DEFAULT_MESSAGE + " \"" + a + "\" with hash " + Objects.hash(a) + " and \"" + b +  "\" with hash " + Objects.hash(b), throwable);
    }

    public HashCollisionsErrorException(@Nullable String message, @Nullable Throwable throwable) {
        super(getHashCollisionsMessage(message), throwable);
    }

    public HashCollisionsErrorException(@Nullable String message, @Nullable Object a, @Nullable Object b, @Nullable Throwable throwable) {
        super(getHashCollisionsMessage(message) + " \"" + a + "\" with hash " + Objects.hash(a) + " and \"" + b +  "\" with hash " + Objects.hash(b), throwable);
    }
}
