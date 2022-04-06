package HeadLibs.Registerer;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when registering an element that has been registered.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HElementRegisteredException extends Exception{
    @Serial
    private static final long serialVersionUID = 5758033473623251615L;

    private static final String DEFAULT_MESSAGE = "Type has been registered!";

    public HElementRegisteredException() {
        super();
    }

    public HElementRegisteredException(@Nullable String message) {
        super((message == null) ? DEFAULT_MESSAGE : message);
    }

    public HElementRegisteredException(@Nullable String message, @Nullable Object element) {
        super(HStringHelper.concat((message == null) ? DEFAULT_MESSAGE : message, " element=", element));
    }

    public HElementRegisteredException(@Nullable String message, @Nullable  Object key, @Nullable Object value) {
        super(HStringHelper.concat((message == null) ? DEFAULT_MESSAGE : message, " key=", value, " element=", value));
    }
}
