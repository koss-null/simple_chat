package Chat.Utils.Concurrent.Tree;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class BinaryTree<K extends Comparable<K>, V> implements Tree<K, V> {
    private class TreeElement {
        V val;
        K key;
        TreeElement left;
        TreeElement right;

        TreeElement(K key, V val) {
            this.key = key;
            this.val = val;
            this.left = this.right = null;
        }
    }

    private Semaphore semaphore = new Semaphore(1);
    private TreeElement head = null;

    // TODO: think about more convenient way to manage mutexes

    // if this key is already in use, we'r replacing the value
    public void set(K key, V val) {
        var curHead = this.head;
        boolean found = false;
        boolean wasAquired = false;

        try {
            this.semaphore.acquire();
            wasAquired = true;

            while (!found) {
                switch (curHead.key.compareTo(key)) {
                    case -1:
                        if (curHead.left == null) {
                            found = true;
                            curHead.left = new TreeElement(key, val);
                        } else {
                            curHead = curHead.left;
                        }
                        break;
                    case 0:
                        curHead.val = val;
                        break;
                    case 1:
                        if (curHead.right == null) {
                            found = true;
                            curHead.right = new TreeElement(key, val);
                        } else {
                            curHead = curHead.right;
                        }
                        break;
                }
            }
        } catch (InterruptedException e) {
            var errorLogger = Logger.getLogger("ERROR");
            errorLogger.log(
                    new LogRecord(
                            Level.WARNING,
                            "Caught an interrupt exception while setting the key into a tree"
                    )
            );
        } finally {
            if (wasAquired) {
                this.semaphore.release();
            }
        }
    }

    // return null if didn't found anything
    public V get(K key) {
        TreeElement curHead = this.head;
        boolean wasAquired = false;

        try {
            this.semaphore.acquire();
            wasAquired = true;

            while (curHead != null) {
                switch (curHead.key.compareTo(key)) {
                    case -1:
                        curHead = curHead.left;
                        break;
                    case 0:
                        return curHead.val;
                    case 1:
                        curHead = curHead.right;
                        break;
                }
            }
        } catch (InterruptedException e) {
            var errorLogger = Logger.getLogger("ERROR");
            errorLogger.log(
                    new LogRecord(
                            Level.WARNING,
                            "Caught an interrupt exception while getting the key from the tree"
                    )
            );
            return null;
        } finally {
            if (wasAquired) {
                this.semaphore.release();
            }
        }

        return null;
    }
}
