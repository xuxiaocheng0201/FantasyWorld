package HeadLibs.Registerer;

import HeadLibs.DataStructures.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Elements map registerer.
 * @param <K> The type of elements key
 * @param <V> The type of elements value
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HMapRegisterer<K, V> implements Serializable, Iterable<Map.Entry<K, V>> {
    @Serial
    private static final long serialVersionUID = -5018927767212486603L;

    /**
     * The registered elements map.
     */
    protected final Map<K, V> map;

    /**
     * Can null key be registered?
     */
    protected final boolean nullKeyAllowed;

    /**
     * Can null value be registered?
     */
    protected final boolean nullValueAllowed;

    /**
     * Can same value be registered with different keys?
     */
    protected final boolean sameValueAllowed;

    /**
     * Construct a new map registerer.
     */
    public HMapRegisterer() {
        this(false, true, true);
    }

    /**
     * Construct a new map registerer.
     * @param sameValueAllowed {@link #sameValueAllowed}
     */
    public HMapRegisterer(boolean sameValueAllowed) {
        this(false, true, sameValueAllowed);
    }

    /**
     * Construct a new map registerer.
     * @param nullKeyAllowed {@link #nullKeyAllowed}
     * @param nullValueAllowed {@link #nullValueAllowed}
     */
    public HMapRegisterer(boolean nullKeyAllowed, boolean nullValueAllowed) {
        this(nullKeyAllowed, nullValueAllowed, true);
    }

    /**
     * Construct a new map registerer.
     * @param nullKeyAllowed {@link #nullKeyAllowed}
     * @param nullValueAllowed {@link #nullValueAllowed}
     * @param sameValueAllowed {@link #sameValueAllowed}
     */
    public HMapRegisterer(boolean nullKeyAllowed, boolean nullValueAllowed, boolean sameValueAllowed) {
        super();
        this.nullKeyAllowed = nullKeyAllowed;
        this.nullValueAllowed = nullValueAllowed;
        this.sameValueAllowed = sameValueAllowed;
        this.map = new HashMap<>();
    }

    /**
     * For inheritance.
     */
    protected HMapRegisterer(boolean nullKeyAllowed, boolean nullValueAllowed, boolean sameValueAllowed, @NotNull Map<K, V> map) {
        super();
        this.nullKeyAllowed = nullKeyAllowed;
        this.nullValueAllowed = nullValueAllowed;
        this.sameValueAllowed = sameValueAllowed;
        this.map = map;
    }

    public boolean isNullKeyAllowed() {
        return this.nullKeyAllowed;
    }

    public boolean isNullValueAllowed() {
        return this.nullValueAllowed;
    }

    public boolean isSameValueAllowed() {
        return this.sameValueAllowed;
    }

    /**
     * Register a new element pair.
     * @param pair the element pair
     * @throws HElementRegisteredException Key or value has been registered.
     */
    public void register(@NotNull Pair<? extends K, ? extends V> pair) throws HElementRegisteredException {
        this.register(pair.getKey(), pair.getValue());
    }

    /**
     * Register a new element pair.
     * @param key element pair key
     * @param value element pair value
     * @throws HElementRegisteredException Key or value has been registered.
     */
    public void register(@Nullable K key, @Nullable V value) throws HElementRegisteredException {
        if (!this.nullKeyAllowed && key == null)
            throw new HElementRegisteredException("Null key.", null, value);
        if (!this.nullValueAllowed && value == null)
            throw new HElementRegisteredException("Null value.", key, null);
        if (this.map.containsKey(key))
            throw new HElementRegisteredException("Registered key.", key, value);
        if (!this.sameValueAllowed && this.map.containsValue(value))
            throw new HElementRegisteredException("Registered value.", key, value);
        this.map.put(key, value);
    }

    /**
     * Reset a new element pair.
     * @param pair the element pair
     */
    public void reset(@NotNull Pair<? extends K, ? extends V> pair) throws HElementRegisteredException {
        this.reset(pair.getKey(), pair.getValue());
    }

    /**
     * Reset a new element pair.
     * @param key element pair key
     * @param value element pair value
     */
    public void reset(@Nullable K key, @Nullable V value) throws HElementRegisteredException {
        if (!this.nullKeyAllowed && key == null)
            throw new HElementRegisteredException("Null key.", null, value);
        if (!this.nullValueAllowed && value == null)
            throw new HElementRegisteredException("Null value.", key, null);
        this.map.put(key, value);
    }

    /**
     * Deregister element pair by key.
     * @param key element pair key
     */
    public void deregisterKey(@Nullable K key) {
        this.map.remove(key);
    }

    /**
     * Deregister element pair by value.
     * @param value element pair value
     */
    public void deregisterValue(@Nullable V value) {
        if (!this.map.containsValue(value))
            return;
        if (value == null) {
            if (!this.nullValueAllowed)
                return;
            for (Map.Entry<K, V> entry: this.map.entrySet())
                if (entry.getValue() == null) {
                    this.map.remove(entry.getKey());
                    if (!this.sameValueAllowed)
                        break;
                }
            return;
        }
        for (Map.Entry<K, V> entry: this.map.entrySet())
            if (value.equals(entry.getValue())) {
                this.map.remove(entry.getKey());
                if (!this.sameValueAllowed)
                    break;
            }
    }

    /**
     * Deregister all element pair.
     */
    public void deregisterAll() {
        this.map.clear();
    }

    /**
     * If an element pair key has been registered.
     * @param key element pair key
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredKey(@Nullable K key) {
        if (!this.nullKeyAllowed && key == null)
            return true;
        return this.map.containsKey(key);
    }

    /**
     * If an element pair value has been registered.
     * @param value element pair value
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredValue(@Nullable V value) {
        if (!this.nullValueAllowed && value == null)
            return true;
        return this.map.containsValue(value);
    }

    /**
     * Get a registered element value by key.
     * @param key element pair key
     * @return element pair value
     * @throws HElementNotRegisteredException No element key registered.
     */
    public @Nullable V getElement(@Nullable K key) throws HElementNotRegisteredException {
        if (!this.nullKeyAllowed && key == null)
            throw new HElementNotRegisteredException("Null key.");
        if (!this.map.containsKey(key))
            throw new HElementNotRegisteredException(null, key);
        return this.map.get(key);
    }

    /**
     * Get a registered element value by key without exception.
     * @param key element pair key
     * @return element pair value
     */
    public @Nullable V getElementNullable(@NotNull K key) {
        if (this.map.containsKey(key))
            return this.map.get(key);
        return null;
    }

    /**
     * Get the count of registered elements.
     * @return the count of registered elements.
     */
    public int getRegisteredCount() {
        return this.map.size();
    }

    @Override
    public @NotNull String toString() {
        return "HMapRegisterer:" + this.map;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HMapRegisterer<?, ?> that)) return false;
        return this.sameValueAllowed == that.sameValueAllowed && this.map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.map, this.sameValueAllowed);
    }

    @Override
    public @NotNull Iterator<Map.Entry<K, V>> iterator() {
        return this.map.entrySet().iterator();
    }

    public @NotNull Collection<K> keys() {
        return Collections.unmodifiableCollection(this.map.keySet());
    }

    public @NotNull Collection<V> values() {
        return Collections.unmodifiableCollection(this.map.values());
    }
}
