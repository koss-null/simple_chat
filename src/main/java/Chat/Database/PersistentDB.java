package Chat.Database;

import Chat.Utils.Concurrent.Tree.BinaryTree;

import java.io.*;


public class PersistentDB implements Database {

    private String path = "/var/persistent.db";
    private BinaryTree<String, String> storage = new BinaryTree<>();

    public void init() {
        try (FileInputStream fileIn = new FileInputStream(path);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            var obj = in.readObject();
            if (obj instanceof  BinaryTree) {
                this.storage = (BinaryTree<String, String>) obj;
            }
        } catch (IOException e) {
            //todo handle
        } catch (ClassNotFoundException e) {
            //todo handle
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

    public void set(String key, String val) {
        this.storage.set(key, val);
    }

    public void remove(String key) {
        // todo: tbd
    }

    public void change(String key, String val) {
        // todo: need to check if the key is present
        this.storage.set(key,val);
    }
}
