package Chat.Database;

import Chat.Utils.Pair.Pair;

public interface Database {
    // reads all info from the disk
    void init();
    // encrypts and stores all info
    void store();

    void set(String[] key, String val);
    String get(String[] key);
    void remove(String[] key);
    void change(String[] key, String val);
}
