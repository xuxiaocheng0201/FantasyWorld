package HeadLibs.Registerer;

import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Elements registerer map.
 * @param <K> The type of elements key
 * @param <V> The type of elements value
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HMapRegisterer<K, V> {
    /**
     * The registered elements' map.
     */
    protected final Map<K, V> map = new HashMap<>();

    /**
     * Can be registered with different keys and same value?
     */
    private final boolean sameValueAllowed;

    /**
     * Construct a new map registerer.
     */
    public HMapRegisterer() {
        this(true);
    }

    /**
     * Construct a new map registerer.
     * @param sameValueAllowed {@link HMapRegisterer#sameValueAllowed}
     */
    public HMapRegisterer(boolean sameValueAllowed) {
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
     * @throws HElementRegisteredException pair's key or value has been registered.
     */
    public void register(@NotNull Pair<? extends K, ? extends V> pair) throws HElementRegisteredException {
        this.register(pair.getKey(), pair.getValue());
    }

    /**
     * Register a new element pair.
     * @param key pair's name
     * @param value pair's value
     * @throws HElementRegisteredException pair's key or value has been registered.
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
     * Deregister element pair by key.
     * @param key element pair's key
     */
    public void deregisterKey(@Nullable K key) {
        if (key == null)
            return;
        this.map.remove(key);
    }

    /**
     * Deregister element pair by value.
     * @param value element pair's value
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
     * If a element pair's key has been registered.
     * @param key element pair's key
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredKey(@Nullable K key) {
        if (key == null)
            return true;
        return this.map.containsKey(key);
    }

    /**
     * If a element pair's value has been registered.
     * @param value element pair's value
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredValue(@Nullable V value) {
        return this.map.containsValue(value);
    }

    /**
     * Get a registered element value by key.
     * @param key element pair's key
     * @return element pair's value
     * @throws HElementNotRegisteredException No element key registered.
     */
    public @Nullable V getElement(@NotNull K key) throws HElementNotRegisteredException {
        if (!this.map.containsKey(key))
            throw new HElementNotRegisteredException(null, key);
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
     * Get registerer map. {@link HMapRegisterer#map} (for iterator)
     * @return registerer map
     */
    public @NotNull Map<K, V> getMap() {
        return this.map;
    }

    @Override
    public @NotNull String toString() {
        return HStringHelper.concat("MapRegisterer", this.map.toString());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        HMapRegisterer<?, ?> that = (HMapRegisterer<?, ?>) o;
        return this.sameValueAllowed == that.sameValueAllowed && this.map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}
