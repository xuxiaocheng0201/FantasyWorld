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
public class ElementNotRegisteredException extends NoSuchElementException {
    @Serial
    private static final long serialVersionUID = -4342199817525193050L;

    public ElementNotRegisteredException() {
        super();
    }

    public ElementNotRegisteredException(@Nullable String message) {
        super((message == null) ? "Type is not registered!" : message);
    }

    public ElementNotRegisteredException(@Nullable String message, @NotNull String name) {
        super(HStringHelper.merge((message == null) ? "Type is not registered!" : message, " name='", name, "'"));
    }
}
