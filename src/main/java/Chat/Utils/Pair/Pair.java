package Chat.Utils.Pair;

/**
 * Pair represents a simple immutable pair of a key and a value
 * @param <K> Key of the pair
 * @param <V> Value of the pair
 */

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K Key() { return this.key; }
    public V Value() { return this.value; }
}
