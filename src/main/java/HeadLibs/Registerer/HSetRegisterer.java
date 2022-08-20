package HeadLibs.Registerer;

import HeadLibs.DataStructures.IImmutable;
import HeadLibs.DataStructures.IUpdatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Elements set registerer.
 * @param <T> the type of elements
 * @author xuxiaocheng
 */
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

    public @NotNull ImmutableSetRegisterer<T> toImmutable() {
        return new ImmutableSetRegisterer<>(this);
    }

    public @NotNull UpdatableSetRegisterer<T> toUpdatable() {
        return new UpdatableSetRegisterer<>(this);
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

    public static class ImmutableSetRegisterer<T> extends HSetRegisterer<T> implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(HSetRegisterer.serialVersionUID);

        protected void init() {
            Collection<T> list = new ArrayList<>(this.set.size());
            for (T element: this.set) {
                T immutableElement = null;
                try {
                    //noinspection unchecked
                    immutableElement = (T) IImmutable.getImmutableVersion(element);
                } catch (ClassCastException ignore) {
                }
                if (immutableElement != null)
                    list.add(immutableElement);
                else
                    list.add(element);
            }
            this.set.clear();
            this.set.addAll(list);
        }

        public ImmutableSetRegisterer() {
            super();
        }

        public ImmutableSetRegisterer(boolean nullAllowed) {
            super(nullAllowed);
        }

        public ImmutableSetRegisterer(boolean nullAllowed, @NotNull Set<T> set) {
            super(nullAllowed, set);
            this.init();
        }

        public ImmutableSetRegisterer(@NotNull HSetRegisterer<T> setRegisterer) {
            super(setRegisterer.nullAllowed, setRegisterer.set);
            this.init();
        }

        @Override
        public void register(@Nullable T element) throws HElementRegisteredException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reset(@Nullable T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deregister(@Nullable T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deregisterAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutableSetRegisterer<T> toImmutable() {
            return this;
        }
    }

    public static class UpdatableSetRegisterer<T> extends HSetRegisterer<T> implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(HSetRegisterer.serialVersionUID);

        protected boolean updated = true;

        protected void init() {
            Collection<T> list = new ArrayList<>(this.set.size());
            for (T element: this.set) {
                T updatableElement = null;
                try {
                    //noinspection unchecked
                    updatableElement = (T) IUpdatable.getUpdatableVersion(element);
                } catch (ClassCastException ignore) {
                }
                if (updatableElement != null)
                    list.add(updatableElement);
                else
                    list.add(element);
            }
            this.set.clear();
            this.set.addAll(list);
        }

        public UpdatableSetRegisterer() {
            super();
        }

        public UpdatableSetRegisterer(boolean nullAllowed) {
            super(nullAllowed);
        }

        public UpdatableSetRegisterer(boolean nullAllowed, @NotNull Set<T> set) {
            super(nullAllowed, set);
            this.init();
        }

        public UpdatableSetRegisterer(@NotNull HSetRegisterer<T> setRegisterer) {
            super(setRegisterer.nullAllowed, setRegisterer.set);
            this.init();
        }

        @Override
        public void register(@Nullable T element) throws HElementRegisteredException {
            super.register(element);
            this.updated = true;
        }

        @Override
        public void reset(@Nullable T element) {
            super.reset(element);
            this.updated = true;
        }

        @Override
        public void deregister(@Nullable T element) {
            super.deregister(element);
            this.updated = true;
        }

        @Override
        public void deregisterAll() {
            super.deregisterAll();
            this.updated = true;
        }

        @Override
        public @NotNull UpdatableSetRegisterer<T> toUpdatable() {
            return this;
        }

        @Override
        public boolean getUpdated() {
            for (T element: this.set)
                if (element instanceof IUpdatable && ((IUpdatable) element).getUpdated())
                    return true;
            return this.updated;
        }

        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
            for (T element: this.set)
                if (element instanceof IUpdatable)
                    ((IUpdatable) element).setUpdated(updated);
        }
    }
}
