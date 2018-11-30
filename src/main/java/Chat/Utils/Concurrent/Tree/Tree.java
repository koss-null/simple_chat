package Chat.Utils.Concurrent.Tree;

public interface Tree <K extends Comparable<K>, V> {
    void set(K key, V value);
    V get(K key);
}
