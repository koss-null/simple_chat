package Chat.Database;

import Chat.Utils.Concurrent.Tree.KeyTree;

import java.io.*;


public class PersistentDB implements Database {

    private String path = "/Users/d.kossovich/learning/simple_chat/var/persistent.db";
    private KeyTree<String, String> storage = new KeyTree<>();

    public void init() {
        try (FileInputStream fileIn = new FileInputStream(path);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            var obj = in.readObject();
            if (obj instanceof KeyTree) {
                this.storage = (KeyTree<String, String>) obj;
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

    public void set(String[] key, String val) {
        this.storage.set(key, val);
    }

    public String get(String[] key) {
        return this.storage.get(key);
    }

    public String[] getKeys(String[] key) {
        return this.storage.getKeys(key);
    }

    public void remove(String[] key) {
        // todo: tbd
    }

    public void change(String[] key, String val) {
        // todo: need to check if the key is present
        this.storage.set(key,val);
    }
}
