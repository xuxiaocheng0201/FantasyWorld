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
 * @param <K> The type of key
 * @param <V> The type of value
 */
@SuppressWarnings("unused")
public class Pair<K, V> implements Serializable, Map.Entry<K, V> {
    @Serial
    private static final long serialVersionUID = -321522862154374460L;

    /**
     * The key.
     */
    private @Nullable K Key;
    /**
     * The value.
     */
    private @Nullable V Value;

    /**
     * Get key.
     * @return The key.
     */
    public @Nullable K getKey() {
        return this.Key;
    }

    /**
     * Set key.
     * @param key The key to be set.
     */
    public void setKey(@Nullable K key) {
        this.Key = key;
    }

    /**
     * Get value.
     * @return The value.
     */
    public @Nullable V getValue() {
        return this.Value;
    }

    /**
     * Set value.
     * @param value The value to be set.
     * @return old value
     */
    public V setValue(@Nullable V value) {
        V oldValue = this.Value;
        this.Value = value;
        return oldValue;
    }

    /**
     * Construct an empty pair.
     */
    public Pair() {
        super();
        this.Key = null;
        this.Value = null;
    }

    /**
     * Construct a pair with <key, value>.
     * @param key The key to be set.
     * @param value The value to be set.
     */
    public Pair(@Nullable K key, @Nullable V value) {
        super();
        this.Key = key;
        this.Value = value;
    }

    /**
     * Construct a pair with <key, value> by a static method.
     * @param key The key to be set.
     * @param value The value to be set.
     * @param <K> The type of key.
     * @param <V> The type of value.
     * @return The new pair.
     */
    public static <K, V> @NotNull Pair<K, V> makePair(@Nullable K key, @Nullable V value) {
        return new Pair<>(key, value);
    }

    /**
     * Construct a pair with <key, value> by a static method from {@link Map.Entry}
     * @param entry Map entry to be set.
     * @param <K> The type of key.
     * @param <V> The type of value.
     * @return The new pair.
     */
    public static <K, V> @NotNull Pair<K, V> makePair(@NotNull Map.Entry<? extends K, ? extends V> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("Pair{",
                "Key=", this.Key,
                ", Value=", this.Value,
                '}');
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(this.Key, pair.Key) && Objects.equals(this.Value, pair.Value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.Key, this.Value);
    }
}
