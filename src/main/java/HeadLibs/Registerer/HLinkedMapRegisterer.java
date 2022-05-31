package HeadLibs.Registerer;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.LinkedHashMap;

/**
 * Elements linked map registerer.
 * @param <K> The type of elements key
 * @param <V> The type of elements value
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HLinkedMapRegisterer<K, V> extends HMapRegisterer<K, V> {
    @Serial
    private static final long serialVersionUID = 4210339898718983379L;

    /**
     * Construct a new linked map registerer.
     */
    public HLinkedMapRegisterer() {
        this(false, true, true);
    }

    /**
     * Construct a new linked map registerer.
     * @param sameValueAllowed {@link #sameValueAllowed}
     */
    public HLinkedMapRegisterer(boolean sameValueAllowed) {
        this(false, true, sameValueAllowed);
    }

    /**
     * Construct a new linked map registerer.
     * @param nullKeyAllowed {@link #nullKeyAllowed}
     * @param nullValueAllowed {@link #nullValueAllowed}
     */
    public HLinkedMapRegisterer(boolean nullKeyAllowed, boolean nullValueAllowed) {
        this(nullKeyAllowed, nullValueAllowed, true);
    }

    /**
     * Construct a new linked map registerer.
     * @param nullKeyAllowed {@link #nullKeyAllowed}
     * @param nullValueAllowed {@link #nullValueAllowed}
     * @param sameValueAllowed {@link #sameValueAllowed}
     */
    public HLinkedMapRegisterer(boolean nullKeyAllowed, boolean nullValueAllowed, boolean sameValueAllowed) {
        super(nullKeyAllowed, nullValueAllowed, sameValueAllowed, new LinkedHashMap<>());
    }

    @Override
    public @NotNull String toString() {
        return "HLinkedMapRegisterer:" + this.map;
    }
}
