package HeadLibs;

import HeadLibs.Helper.HStringHelper;

import java.io.Serializable;
import java.util.Objects;

public class Pair<K, V> implements Serializable {
    private K Key;
    private V Value;

    public K getKey() {
        return Key;
    }

    public void setKey(K key) {
        Key = key;
    }

    public V getValue() {
        return Value;
    }

    public void setValue(V value) {
        Value = value;
    }

    public Pair(K key, V value) {
        this.Key = key;
        this.Value = value;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("Pair{",
                "Key=", Key,
                ", Value=", Value,
                '}');
    }

    @Override
    public boolean equals(Object o) {
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
