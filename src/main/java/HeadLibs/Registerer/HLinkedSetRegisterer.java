package HeadLibs.Registerer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Elements registerer linked set.
 * @param <T> the type of elements
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HLinkedSetRegisterer<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7067717630174449685L;

    /**
     * The registered elements set.
     */
    protected final Set<T> set = new LinkedHashSet<>();

    /**
     * Register a new element.
     * @param element the element
     * @throws HElementRegisteredException Element has been registered.
     */
    public void register(@Nullable T element) throws HElementRegisteredException {
        if (this.set.contains(element))
            throw new HElementRegisteredException(null, element);
        this.set.add(element);
    }

    /**
     * Reset a new element.
     * @param element the element
     */
    public void reset(@Nullable T element) {
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
     * If a element has been registered.
     * @param element the element
     * @return true - registered. false - unregistered.
     */
    public boolean isRegistered(@NotNull T element) {
        return this.set.contains(element);
    }

    /**
     * Get the count of registered elements.
     * @return the count of registered elements
     */
    public int getRegisteredCount() {
        return this.set.size();
    }

    /**
     * Get registerer set. {@link HLinkedSetRegisterer#set}
     * @return registerer set
     */
    public @NotNull Set<T> getSet() {
        return this.set;
    }

    @Override
    public @NotNull String toString() {
        return "HSetRegisterer:" + this.set;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HLinkedSetRegisterer<?> that = (HLinkedSetRegisterer<?>) o;
        return this.set.equals(that.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.set);
    }
}
