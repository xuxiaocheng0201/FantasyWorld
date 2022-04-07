package HeadLibs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Pair is a two-tuple with key and value.
 * @param <K> the type of key
 * @param <V> the type of value
 */
@SuppressWarnings("unused")
public class Pair<K, V> implements Map.Entry<K, V>, Serializable {
    @Serial
    private static final long serialVersionUID = -321522862154374460L;

    private @Nullable K key;
    private @Nullable V value;

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

    public static <K, V> @NotNull Pair<K, V> makePair(@Nullable Map.Entry<? extends K, ? extends V> entry) {
        if (entry == null)
            return new Pair<>();
        return new Pair<>(entry.getKey(), entry.getValue());
    }
}
