package HeadLibs;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Pair is a two-tuple. <Key, Value>
 * @param <K> The type of key
 * @param <V> The type of value
 */
@SuppressWarnings("unused")
public class Pair<K, V> implements Serializable {
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
        return Key;
    }

    /**
     * Set key.
     * @param key The key to be set.
     */
    public void setKey(@Nullable K key) {
        Key = key;
    }

    /**
     * Get value.
     * @return The value.
     */
    public @Nullable V getValue() {
        return Value;
    }

    /**
     * Set value.
     * @param value The value to be set.
     */
    public void setValue(@Nullable V value) {
        Value = value;
    }

    /**
     * Construct an empty pair.
     */
    public Pair() {
        this.Key = null;
        this.Value = null;
    }

    /**
     * Construct a pair with <key, value>.
     * @param key The key to be set.
     * @param value The value to be set.
     */
    public Pair(@Nullable K key, @Nullable V value) {
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

    @Override
    public @NotNull String toString() {
        return HStringHelper.merge("Pair{",
                "Key=", Key,
                ", Value=", Value,
                '}');
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(Key, pair.Key) && Objects.equals(Value, pair.Value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Key, Value);
    }
}
