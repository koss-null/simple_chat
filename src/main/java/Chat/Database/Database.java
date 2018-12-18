package Chat.Database;

import Chat.Utils.Pair.Pair;

public interface Database<K extends Comparable<K>, V> {
    // reads all info from the disk
    void init(String storagePath);
    // encrypts and stores all info
    void store();

    void set(K[] key, V val);
    V get(K[] key);
    K[] getKeys(K[] key);
    void remove(K[] key);
    void change(K[] key, V val);
}
