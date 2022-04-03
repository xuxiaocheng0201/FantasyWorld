package HeadLibs.Registerer;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.NoSuchElementException;

/**
 * Throw when getting an unregistered element.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HElementNotRegisteredException extends NoSuchElementException {
    @Serial
    private static final long serialVersionUID = -4342199817525193050L;

    public HElementNotRegisteredException() {
        super();
    }

    public HElementNotRegisteredException(@Nullable String message) {
        super((message == null) ? "Type is not registered!" : message);
    }

    public HElementNotRegisteredException(@Nullable String message, @NotNull Object key) {
        super(HStringHelper.concat((message == null) ? "Type is not registered!" : message, " key=", key));
    }
}
