package HeadLibs.DataStructures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class DoubleHashMap<K, V> implements Map<K, V>, Serializable {
    @Serial
    private static final long serialVersionUID = -7426549795044952622L;

    private final HashMap<K, V> kv;
    private final HashMap<V, K> vk;

    public DoubleHashMap(@Range(from = 0, to = Integer.MAX_VALUE) int initialCapacity, float loadFactor) {
        super();
        this.kv = new HashMap<>(initialCapacity, loadFactor);
        this.vk = new HashMap<>(initialCapacity, loadFactor);
    }

    public DoubleHashMap(@Range(from = 0, to = Integer.MAX_VALUE) int initialCapacity) {
        super();
        this.kv = new HashMap<>(initialCapacity);
        this.vk = new HashMap<>(initialCapacity);
    }

    public DoubleHashMap() {
        super();
        this.kv = new HashMap<>();
        this.vk = new HashMap<>();
    }

    public DoubleHashMap(Map<? extends K, ? extends V> m) {
        super();
        this.kv = new HashMap<>(m);
        this.vk = new HashMap<>(m.size());
        for (Map.Entry<? extends K, ? extends V> entry: m.entrySet())
            this.vk.put(entry.getValue(), entry.getKey());
    }

    @Override
    public int size() {
        return this.kv.size();
    }

    @Override
    public boolean isEmpty() {
        return this.kv.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        try {
            return this.getValue((K) key);
        } catch (ClassCastException ignore) {
        }
        return null;
    }

    public V getValue(K key) {
        return this.kv.get(key);
    }

    public K getKey(V value) {
        return this.vk.get(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return this.kv.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.kv.containsValue(value);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        this.vk.put(value, key);
        return this.kv.put(key, value);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        this.kv.putAll(m);
        for (Map.Entry<? extends K, ? extends V> entry: m.entrySet())
            this.vk.put(entry.getValue(), entry.getKey());
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        try {
            return this.removeKey((K) key);
        } catch (ClassCastException ignore) {
        }
        return null;
    }

    public V removeKey(K key) {
        Set<Map.Entry<V, K>> entries = this.vk.entrySet();
        for (Map.Entry<? extends V, ? extends K> entry: entries)
            if (key.equals(entry.getValue()))
                this.vk.remove(entry.getKey(), entry.getValue());
        return this.kv.remove(key);
    }

    public K removeValue(V value) {
        Set<Map.Entry<K, V>> entries = this.kv.entrySet();
        for (Map.Entry<? extends K, ? extends V> entry: entries)
            if (value.equals(entry.getValue()))
                this.kv.remove(entry.getKey(), entry.getValue());
        return this.vk.remove(value);
    }

    @Override
    public void clear() {
        this.kv.clear();
        this.vk.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return this.kv.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return this.kv.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.kv.entrySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        try {
            return this.getValueOrDefault((K) key, defaultValue);
        } catch (ClassCastException ignore) {
        }
        return defaultValue;
    }

    public V getValueOrDefault(K key, V defaultValue) {
        V value = this.getValue(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public K getKeyOrDefault(V value, K defaultKey) {
        K key = this.getKey(value);
        if (key == null)
            return defaultKey;
        return key;
    }
    
    @Override
    public V putIfAbsent(K key, V value) {
        this.vk.putIfAbsent(value, key);
        return this.kv.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        this.vk.remove(value, key);
        return this.kv.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        this.vk.remove(oldValue, key);
        this.vk.put(newValue, key);
        return this.kv.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        this.vk.remove(this.kv.get(key), key);
        this.vk.put(value, key);
        return this.kv.replace(key, value);
    }

    @Override
    public String toString() {
        return "DoubleHashMap:" + this.kv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        DoubleHashMap<?, ?> that = (DoubleHashMap<?, ?>) o;
        return this.kv.equals(that.kv) && this.vk.equals(that.vk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.kv);
    }
}
