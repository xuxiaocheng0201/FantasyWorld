package HeadLibs.Registerer;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

/**
 * Throw when registering a registered element.
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class ElementRegisteredException extends ArrayStoreException{
    @Serial
    private static final long serialVersionUID = 5758033473623251615L;

    public ElementRegisteredException() {
        super();
    }

    public ElementRegisteredException(@Nullable String message) {
        super((message == null) ? "Type has been registered!" : message);
    }

    public ElementRegisteredException(@Nullable String message, @NotNull Object element) {
        super(HStringHelper.merge((message == null) ? "Type has been registered!" : message, " element=", element));
    }

    public ElementRegisteredException(@Nullable String message, @NotNull String name, @NotNull Object element) {
        super(HStringHelper.merge((message == null) ? "Type has been registered!" : message, " name='", name, "' element=", element));
    }
}
