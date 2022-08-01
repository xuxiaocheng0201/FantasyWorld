package HeadLibs.Registerer;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.LinkedHashSet;

/**
 * Elements linked set registerer.
 * @param <T> the type of elements
 * @author xuxiaocheng
 */
public class HLinkedSetRegisterer<T> extends HSetRegisterer<T> {
    @Serial
    private static final long serialVersionUID = 7067717630174449685L;

    /**
     * Construct a new linked set registerer.
     */
    public HLinkedSetRegisterer() {
        this(true);
    }

    /**
     * Construct a new linked set registerer.
     * @param nullAllowed {@link #nullAllowed}
     */
    public HLinkedSetRegisterer(boolean nullAllowed) {
        super(nullAllowed, new LinkedHashSet<>());
    }

    @Override
    public @NotNull String toString() {
        return "HLinkedSetRegisterer:" + this.set;
    }
}
