package HeadLibs.Registerer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Elements set registerer.
 * @param <T> the type of elements
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HSetRegisterer<T> implements Serializable, Iterable<T> {
    @Serial
    private static final long serialVersionUID = 2481392558972738677L;

    /**
     * The registered elements set.
     */
    protected final @NotNull Set<T> set;

    /**
     * Is null can be registered?
     */
    protected final boolean nullAllowed;

    /**
     * Create a new set registerer.
     */
    public HSetRegisterer() {
        this(true);
    }

    /**
     * Create a new set registerer.
     * @param nullAllowed {@link #nullAllowed}
     */
    public HSetRegisterer(boolean nullAllowed) {
        super();
        this.nullAllowed = nullAllowed;
        this.set = new HashSet<>();
    }

    /**
     * For inheritance.
     */
    protected HSetRegisterer(boolean nullAllowed, @NotNull Set<T> set) {
        super();
        this.nullAllowed = nullAllowed;
        this.set = set;
    }

    /**
     * Is null can be registered?
     * @return true - allowed. false - not allowed.
     */
    public boolean isNullAllowed() {
        return this.nullAllowed;
    }

    /**
     * Register a new element.
     * @param element the element
     * @throws HElementRegisteredException Element has been registered.
     */
    public void register(@Nullable T element) throws HElementRegisteredException {
        if (!this.nullAllowed && element == null)
            throw new HElementRegisteredException("Null element");
        if (this.set.contains(element))
            throw new HElementRegisteredException(null, element);
        this.set.add(element);
    }

    /**
     * Register a new element without exception.
     * @param element the element
     */
    public void reset(@Nullable T element) {
        if (this.nullAllowed || element != null)
            this.set.add(element);
    }

    /**
     * Deregister element.
     * @param element the element
     */
    public void deregister(@Nullable T element) {
        this.set.remove(element);
    }

    /**
     * Deregister all elements.
     */
    public void deregisterAll() {
        this.set.clear();
    }

    /**
     * If an element has been registered.
     * @param element the element
     * @return true - registered. false - unregistered.
     */
    public boolean isRegistered(@Nullable T element) {
        if (!this.nullAllowed && element == null)
            return true;
        return this.set.contains(element);
    }

    /**
     * Get the count of registered elements.
     * @return the count of registered elements
     */
    public int getRegisteredCount() {
        return this.set.size();
    }

    @Override
    public @NotNull String toString() {
        return "HSetRegisterer:" + this.set;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HSetRegisterer<?> that)) return false;
        return this.nullAllowed == that.nullAllowed && this.set.equals(that.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.set, this.nullAllowed);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.set.iterator();
    }
}
