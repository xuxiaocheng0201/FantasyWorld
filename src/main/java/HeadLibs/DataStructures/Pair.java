package HeadLibs.DataStructures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Pair is a two-tuple with key and value.
 * @param <K> the type of key
 * @param <V> the type of value
 */
public class Pair<K, V> implements Entry<K, V>, Serializable {
    @Serial
    private static final long serialVersionUID = -321522862154374460L;

    protected @Nullable K key;
    protected @Nullable V value;

    public Pair() {
        super();
        this.key = null;
        this.value = null;
    }

    public Pair(@Nullable K key, @Nullable V value) {
        super();
        this.key = key;
        this.value = value;
    }

    public Pair(@Nullable Entry<K, V> entry) {
        super();
        if (entry != null) {
            this.key = entry.getKey();
            this.value = entry.getValue();
        }
    }

    public @Nullable K getKey() {
        return this.key;
    }

    public void setKey(@Nullable K key) {
        this.key = key;
    }

    public @Nullable V getValue() {
        return this.value;
    }

    public @Nullable V setValue(@Nullable V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    public @NotNull ImmutablePair<K, V> toImmutable() {
        return new ImmutablePair<>(this);
    }

    public @NotNull UpdatablePair<K, V> toUpdatable() {
        return new UpdatablePair<>(this);
    }

    @Override
    public @NotNull String toString() {
        return "Pair{" +
                "key=" + (this.key == null ? "null" : this.key) +
                ", value=" + (this.value == null ? "null" : this.value) +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(this.key, pair.key) && Objects.equals(this.value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    public static <K, V> @NotNull Pair<K, V> makePair(@Nullable Pair<? extends K, ? extends V> pair) {
        if (pair == null)
            return new Pair<>();
        return new Pair<>(pair.key, pair.value);
    }

    public static <K, V> @NotNull Pair<K, V> makePair(@Nullable K key, @Nullable V value) {
        return new Pair<>(key, value);
    }

    public static <K, V> @NotNull Pair<K, V> makePair(@Nullable Entry<? extends K, ? extends V> entry) {
        if (entry == null)
            return new Pair<>();
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    public static class ImmutablePair<K, V> extends Pair<K, V> implements IImmutable {
        @Serial
        private static final long serialVersionUID = IImmutable.getSerialVersionUID(-321522862154374460L);

        @SuppressWarnings("unchecked")
        protected void init() {
            if (this.key != null && IImmutable.hasImmutableVersion(this.key.getClass()))
                try {
                    this.key = (K) this.key.getClass().getDeclaredMethod("toImmutable").invoke(this.key);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ignore) {
                }
            if (this.value != null && IImmutable.hasImmutableVersion(this.value.getClass()))
                try {
                    this.value = (V) this.value.getClass().getDeclaredMethod("toImmutable").invoke(this.value);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ignore) {
                }
        }

        public ImmutablePair() {
            super();
        }

        public ImmutablePair(@Nullable K key, @Nullable V value) {
            super(key, value);
            this.init();
        }

        public ImmutablePair(@Nullable Entry<K, V> entry) {
            super(entry);
            this.init();
        }

        @Override
        public void setKey(@Nullable K key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @Nullable V setValue(@Nullable V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull ImmutablePair<K, V> toImmutable() {
            return this;
        }
    }

    public static class UpdatablePair<K, V> extends Pair<K, V> implements IUpdatable {
        @Serial
        private static final long serialVersionUID = IUpdatable.getSerialVersionUID(-321522862154374460L);

        protected boolean updated;

        @SuppressWarnings("unchecked")
        protected void initKey() {
            if (this.key != null && IUpdatable.hasUpdatableVersion(this.key.getClass()))
                try {
                    this.key = (K) this.key.getClass().getDeclaredMethod("toUpdatable").invoke(this.key);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ignore) {
                }
        }

        @SuppressWarnings("unchecked")
        protected void initValue() {
            if (this.value != null && IUpdatable.hasUpdatableVersion(this.value.getClass()))
                try {
                    this.value = (V) this.value.getClass().getDeclaredMethod("toUpdatable").invoke(this.value);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException ignore) {
                }
        }

        public UpdatablePair() {
            super();
        }

        public UpdatablePair(@Nullable K key, @Nullable V value) {
            super(key, value);
            this.initKey();
            this.initValue();
        }

        public UpdatablePair(@Nullable Entry<K, V> entry) {
            super(entry);
            this.initKey();
            this.initValue();
        }

        @Override
        public @Nullable K getKey() {
            if (!(this.key instanceof IUpdatable))
                this.updated = true;
            return super.getKey();
        }

        @Override
        public void setKey(@Nullable K key) {
            super.setKey(key);
            this.initKey();
            this.updated = true;
        }

        @Override
        public @Nullable V getValue() {
            if (!(this.value instanceof IUpdatable))
                this.updated = true;
            return super.getValue();
        }

        @Override
        public @Nullable V setValue(@Nullable V value) {
            V old = super.setValue(value);
            this.initValue();
            this.updated = true;
            return old;
        }

        @Override
        public @NotNull UpdatablePair<K, V> toUpdatable() {
            return this;
        }

        @SuppressWarnings("ChainOfInstanceofChecks")
        @Override
        public boolean getUpdated() {
            if (this.key instanceof IUpdatable && ((IUpdatable) this.key).getUpdated())
                return true;
            if (this.value instanceof IUpdatable && ((IUpdatable) this.value).getUpdated())
                return true;
            return this.updated;
        }

        @SuppressWarnings("ChainOfInstanceofChecks")
        @Override
        public void setUpdated(boolean updated) {
            this.updated = updated;
            if (this.key instanceof IUpdatable)
                ((IUpdatable) this.key).setUpdated(updated);
            if (this.value instanceof IUpdatable)
                ((IUpdatable) this.value).setUpdated(updated);
        }
    }
}
