package Chat.Database;

import Chat.Utils.Pair.Pair;

public interface Database {
    // reads all info from the disk
    void Init();
    // encrypts and stores all info
    void Store();

    void Set(String key, String val);
    void Remove(String key);
    void Change(String key, String val);
}
