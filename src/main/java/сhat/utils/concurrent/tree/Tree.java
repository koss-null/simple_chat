package сhat.utils.concurrent.tree;

public interface Tree <K extends Comparable<K>, V> {
    void set(K[] key, V value);
    V get(K[] key);
    K[] getKeys(K[] key);
}
