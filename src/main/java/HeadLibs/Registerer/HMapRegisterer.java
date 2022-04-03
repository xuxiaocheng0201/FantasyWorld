package HeadLibs.Registerer;

import HeadLibs.Pair;

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
     * Register a new element pair.
     * @param pair element pair
     * @throws HElementRegisteredException pair's key or value has been registered.
     */
    public void register(Pair<K, V> pair) throws HElementRegisteredException {
        this.register(pair.getKey(), pair.getValue());
    }

    /**
     * Register a new element pair.
     * @param key pair's name
     * @param value pair's value
     * @throws HElementRegisteredException pair's key or value has been registered.
     */
    public void register(K key, V value) throws HElementRegisteredException {
        if (this.map.containsKey(key))
            throw new HElementRegisteredException("Registered key.", key, value);
        if (this.map.containsValue(value))
            throw new HElementRegisteredException("Registered value.", key, value);
        this.map.put(key, value);
    }

    /**
     * Deregister element pair by key.
     * @param key element pair's key
     */
    public void deregisterKey(K key) {
        this.map.remove(key);
    }

    /**
     * Deregister element pair by value.
     * @param value element pair's value
     */
    public void deregisterValue(V value) {
        if (!this.map.containsValue(value))
            return;
        for (Map.Entry<K, V> entry : this.map.entrySet())
            if (entry.getValue().equals(value))
                this.map.remove(entry.getKey());
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
    public boolean isRegisteredKey(K key) {
        return this.map.containsKey(key);
    }

    /**
     * If a element pair's value has been registered.
     * @param value element pair's value
     * @return true - registered. false - unregistered.
     */
    public boolean isRegisteredValue(V value) {
        return this.map.containsValue(value);
    }

    /**
     * Get a registered element value by key.
     * @param key element pair's key
     * @return element pair's value
     * @throws HElementNotRegisteredException No element key registered.
     */
    public V getElement(K key) throws HElementNotRegisteredException {
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
     * Get registerer map. {@link HMapRegisterer#map}
     * @return registerer map
     */
    public Map<K, V> getMap() {
        return this.map;
    }
}
