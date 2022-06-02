package HeadLibs.Registerer;

import HeadLibs.DataStructures.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Elements registerer linked map.
 * @param <K> The type of elements key
 * @param <V> The type of elements value
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HDoubleMapRegisterer<K, V> implements Serializable, Iterable<Entry<K, V>> {
    @Serial
    private static final long serialVersionUID = 4210339898718983379L;

    /**
     * The registered elements map.(key to value)
     */
    protected final @NotNull Map<K, V> kvMap = new HashMap<>();
    /**
     * The registered elements map.(value to key)
     */
    protected final @NotNull Map<V, K> vkMap = new HashMap<>();

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
        if (value == null)
            throw new HElementRegisteredException("Null value.", key, null);
        if (this.kvMap.containsKey(key))
            throw new HElementRegisteredException("Registered key.", key, value);
        if (this.kvMap.containsValue(value))
            throw new HElementRegisteredException("Registered value.", key, value);
        this.kvMap.put(key, value);
        this.vkMap.put(value, key);
    }

    /**
     * Reset a new element pair.
     * @param pair element pair
     * @return old value
     */
    public @Nullable V reset(@NotNull Pair<? extends K, ? extends V> pair) throws HElementRegisteredException {
        return this.reset(pair.getKey(), pair.getValue());
    }

    /**
     * Reset a new element pair.
     * @param key pair name
     * @param value pair value
     * @return old value
     */
    public @Nullable V reset(@Nullable K key, @Nullable V value) throws HElementRegisteredException {
        if (key == null)
            throw new HElementRegisteredException("Null key.", null, value);
        if (value == null)
            throw new HElementRegisteredException("Null value.", key, null);
        V oldValue = this.kvMap.remove(key);
        this.vkMap.remove(value);
        this.kvMap.put(key, value);
        this.vkMap.put(value, key);
        return oldValue;
    }

    /**
     * Deregister element pair by key.
     * @param key element pair key
     */
    public void deregisterKey(@Nullable K key) {
        if (key == null)
            return;
        V value = this.kvMap.remove(key);
        if (value != null)
            this.vkMap.remove(value);
    }

    /**
     * Deregister element pair by value.
     * @param value element pair value
     */
    public void deregisterValue(@Nullable V value) {
        if (value == null)
            return;
        K key = this.vkMap.remove(value);
        if (key != null)
            this.kvMap.remove(key);
    }

    /**
     * Deregister all element pair.
     */
    public void deregisterAll() {
        this.kvMap.clear();
        this.vkMap.clear();
    }

    /**
     * If an element pair key has been registered.
     * @param key element pair key
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredKey(@Nullable K key) {
        if (key == null)
            return true;
        return this.kvMap.containsKey(key);
    }

    /**
     * If a element pair value has been registered.
     * @param value element pair value
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredValue(@Nullable V value) {
        if (value == null)
            return true;
        return this.vkMap.containsKey(value);
    }

    /**
     * Get a registered element value by key.
     * @param key element pair key
     * @return element pair value
     * @throws HElementNotRegisteredException No element key registered.
     */
    public @NotNull V getValue(@NotNull K key) throws HElementNotRegisteredException {
        if (!this.kvMap.containsKey(key))
            throw new HElementNotRegisteredException(null, key);
        return this.kvMap.get(key);
    }

    /**
     * Get a registered element key by value.
     * @param value element pair value
     * @return element pair key
     * @throws HElementNotRegisteredException No element value registered.
     */
    public @NotNull K getKey(@NotNull V value) throws HElementNotRegisteredException {
        if (!this.vkMap.containsKey(value))
            throw new HElementNotRegisteredException(null, value);
        return this.vkMap.get(value);
    }

    /**
     * Get a registered element value by key without exception.
     * @param key element pair key
     * @return element pair value
     */
    public @Nullable V getValueNullable(@NotNull K key) {
        if (!this.kvMap.containsKey(key))
            return null;
        return this.kvMap.get(key);
    }

    /**
     * Get a registered element key by value without exception.
     * @param value element pair value
     * @return element pair key
     */
    public @Nullable K getKeyNullable(@NotNull V value) {
        if (!this.vkMap.containsKey(value))
            return null;
        return this.vkMap.get(value);
    }

    /**
     * Get the count of registered elements.
     * @return the count of registered elements.
     */
    public int getRegisteredCount() {
        return this.kvMap.size();
    }

    /**
     * Reverse key-value.
     * @return new {@code HDoubleMapRegisterer} with reversed pair.
     */
    public HDoubleMapRegisterer<V, K> reverse() {
        HDoubleMapRegisterer<V, K> reversed = new HDoubleMapRegisterer<>();
        for (Entry<K, V> entry: this) {
            try {
                reversed.register(entry.getValue(), entry.getKey());
            } catch (HElementRegisteredException ignore) {
            }
        }
        return reversed;
    }

    @Override
    public @NotNull String toString() {
        return "HDoubleMapRegisterer:" + this.kvMap;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof HDoubleMapRegisterer<?, ?> that)) return false;
        return this.kvMap.equals(that.kvMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.kvMap);
    }

    @Override
    public @NotNull Iterator<Entry<K, V>> iterator() {
        return this.kvMap.entrySet().iterator();
    }

    public @NotNull Collection<K> keys() {
        return Collections.unmodifiableCollection(this.kvMap.keySet());
    }

    public @NotNull Collection<V> values() {
        return Collections.unmodifiableCollection(this.kvMap.values());
    }
}
