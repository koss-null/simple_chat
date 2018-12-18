package Chat.Database;

import Chat.Utils.Concurrent.Tree.KeyTree;

import java.io.*;


public class PersistentDB<K extends Comparable<K>, V> implements Database<K, V> {

    private String path;
    private KeyTree<K, V> storage = new KeyTree<>();

    public void init(String path) {
        this.path = path;

        try (FileInputStream fileIn = new FileInputStream(path);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            var obj = in.readObject();
            if (obj instanceof KeyTree) {
                this.storage = (KeyTree<K, V>) obj;
            }
        } catch (IOException e) {
            //todo handle
            System.out.println("Creating new database");
        } catch (ClassNotFoundException e) {
            //todo handle
            System.out.println("Can't find the database");
        }
    }

    public void store() {
        try (FileOutputStream fileOut = new FileOutputStream(path);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this.storage);
        } catch (IOException e) {
            // todo: handle
            e.printStackTrace();
        }
    }

    public void set(K[] key, V val) {
        this.storage.set(key, val);
    }

    public V get(K[] key) {
        return this.storage.get(key);
    }

    public K[] getKeys(K[] key) {
        return this.storage.getKeys(key);
    }

    public void remove(K[] key) {
        // todo: tbd
    }

    public void change(K[] key, V val) {
        // todo: need to check if the key is present
        this.storage.set(key, val);
    }
}
