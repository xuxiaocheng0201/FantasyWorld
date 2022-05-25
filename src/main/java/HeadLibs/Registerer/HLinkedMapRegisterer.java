package HeadLibs.Registerer;

import HeadLibs.DataStructures.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Elements registerer linked map.
 * @param <K> The type of elements key
 * @param <V> The type of elements value
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HLinkedMapRegisterer<K, V> implements Serializable {
    @Serial
    private static final long serialVersionUID = 4210339898718983379L;

    /**
     * The registered elements map.
     */
    protected final @NotNull Map<K, V> map = new LinkedHashMap<>();

    /**
     * Can be registered with different keys and same value?
     */
    protected final boolean sameValueAllowed;

    /**
     * Construct a new map registerer.
     */
    public HLinkedMapRegisterer() {
        this(true);
    }

    /**
     * Construct a new map registerer.
     * @param sameValueAllowed {@link HLinkedMapRegisterer#sameValueAllowed}
     */
    public HLinkedMapRegisterer(boolean sameValueAllowed) {
        super();
        this.sameValueAllowed = sameValueAllowed;
    }

    /**
     * Get same value allowed.
     * @return true - allowed. false - not allowed.
     */
    public boolean isSameValueAllowed() {
        return this.sameValueAllowed;
    }

    /**
     * Register a new element pair.
     * @param pair element pair
     * @throws HElementRegisteredException Key or value has been registered.
     */
    public void register(@NotNull Pair<? extends K, ? extends V> pair) throws HElementRegisteredException {
        this.register(pair.getKey(), pair.getValue());
    }

    /**
     * Register a new element pair.
     * @param key pair name
     * @param value pair value
     * @throws HElementRegisteredException Key or value has been registered.
     */
    public void register(@Nullable K key, @Nullable V value) throws HElementRegisteredException {
        if (key == null)
            throw new HElementRegisteredException("Null key.", null, value);
        if (this.map.containsKey(key))
            throw new HElementRegisteredException("Registered key.", key, value);
        if (this.map.containsValue(value) && !this.sameValueAllowed)
            throw new HElementRegisteredException("Registered value.", key, value);
        this.map.put(key, value);
    }

    /**
     * Reset a new element pair.
     * @param pair element pair
     * @return old value
     */
    public @Nullable V reset(@NotNull Pair<? extends K, ? extends V> pair) {
        return this.map.put(pair.getKey(), pair.getValue());
    }

    /**
     * Reset a new element pair.
     * @param key pair name
     * @param value pair value
     * @return old value
     */
    public @Nullable V reset(@Nullable K key, @Nullable V value) {
        return this.map.put(key, value);
    }

    /**
     * Deregister element pair by key.
     * @param key element pair key
     */
    public void deregisterKey(@Nullable K key) {
        if (key == null)
            return;
        this.map.remove(key);
    }

    /**
     * Deregister element pair by value.
     * @param value element pair value
     */
    public void deregisterValue(@Nullable V value) {
        if (!this.map.containsValue(value))
            return;
        for (Map.Entry<K, V> entry : this.map.entrySet())
            if (entry.getValue().equals(value)) {
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
        if (key == null)
            return true;
        return this.map.containsKey(key);
    }

    /**
     * If a element pair value has been registered.
     * @param value element pair value
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredValue(@Nullable V value) {
        return this.map.containsValue(value);
    }

    /**
     * Get a registered element value by key.
     * @param key element pair key
     * @return element pair value
     * @throws HElementNotRegisteredException No element key registered.
     */
    public @Nullable V getElement(@NotNull K key) throws HElementNotRegisteredException {
        if (!this.map.containsKey(key))
            throw new HElementNotRegisteredException(null, key);
        return this.map.get(key);
    }

    /**
     * Get a registered element value by key.
     * @param key element pair key
     * @return element pair value
     */
    public @Nullable V getElementNullable(@NotNull K key) {
        if (!this.map.containsKey(key))
            return null;
        return this.map.get(key);
    }

    /**
     * Get the count of registered elements.
     * @return the count of registered elements.
     */
    public int getRegisteredCount() {
        return this.map.size();
    }

    /**
     * Get registerer map. {@link HLinkedMapRegisterer#map} (for iterator)
     * @return registerer map
     */
    public @NotNull Map<K, V> getMap() {
        return this.map;
    }

    @Override
    public @NotNull String toString() {
        return "MapRegisterer:" + this.map;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HLinkedMapRegisterer<?, ?> that)) return false;
        return this.sameValueAllowed == that.sameValueAllowed && this.map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.map, this.sameValueAllowed);
    }
}
