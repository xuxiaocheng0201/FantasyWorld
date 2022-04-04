package HeadLibs;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Pair is a two-tuple. <Key, Value>
 * @param <K> the type of key
 * @param <V> the type of value
 */
@SuppressWarnings("unused")
public class Pair<K, V> implements Map.Entry<K, V>, Serializable {
    @Serial
    private static final long serialVersionUID = -321522862154374460L;

    /**
     * The key.
     */
    private @Nullable K key;
    /**
     * The value.
     */
    private @Nullable V value;

    /**
     * Construct an empty pair.
     */
    public Pair() {
        super();
        this.key = null;
        this.value = null;
    }

    /**
     * Construct a pair with <key, value>.
     * @param key the key to be set
     * @param value the value to be set
     */
    public Pair(@Nullable K key, @Nullable V value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * Get key.
     * @return the key
     */
    public @Nullable K getKey() {
        return this.key;
    }

    /**
     * Set key.
     * @param key the key to be set
     */
    public void setKey(@Nullable K key) {
        this.key = key;
    }

    /**
     * Get value.
     * @return the value
     */
    public @Nullable V getValue() {
        return this.value;
    }

    /**
     * Set value.
     * @param value the value to be set
     * @return old value
     */
    public V setValue(@Nullable V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("Pair{",
                "Key=", this.key,
                ", Value=", this.value,
                '}');
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

    /**
     * Construct a pair with <key, value> by a static method.
     * @param key the key to be set
     * @param value the value to be set
     * @param <K> the type of key
     * @param <V> the type of value
     * @return the new pair
     */
    public static <K, V> @NotNull Pair<K, V> makePair(@Nullable K key, @Nullable V value) {
        return new Pair<>(key, value);
    }

    /**
     * Construct a pair with <key, value> by a static method from {@link Map.Entry}
     * @param entry map entry to be set
     * @param <K> the type of key
     * @param <V> the type of value
     * @return the new pair
     */
    public static <K, V> @NotNull Pair<K, V> makePair(@NotNull Map.Entry<? extends K, ? extends V> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }
}
