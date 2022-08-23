package HeadLibs.Registerer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

public class HNotNullMapRegisterer<K, V> extends HMapRegisterer<K, V> {
    @Serial
    private static final long serialVersionUID = -5490817531149741171L;

    public HNotNullMapRegisterer() {
        super(false, false, true);
    }

    public HNotNullMapRegisterer(boolean sameValueAllowed) {
        super(false, false, sameValueAllowed);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull V getElement(@Nullable K key) throws HElementNotRegisteredException {
        return super.getElement(key);
    }
}
