package Chat.Utils.Concurrent.Tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class KeyTree<K extends Comparable<K>, V> implements Tree<K, V>, Serializable {
    private class TreeElement implements Serializable {
        V val;
        K key;
        List<TreeElement> nextLevel;

        TreeElement(K key) {
            this.key = key;
            this.val = null;
            this.nextLevel = null;
        }

        TreeElement(K key, V val) {
            this.key = key;
            this.val = val;
            this.nextLevel = null;
        }
    }

    private List<TreeElement> zeroLevel = new ArrayList<>();

    // TODO: think about more convenient way to manage mutexes
    private transient Semaphore semaphore = new Semaphore(1);
    private void checkSemaphore() {
        if (this.semaphore == null) {
            this.semaphore = new Semaphore(1);
        }
    }

    // if this key is already in use, we'r replacing the value
    public void set(K[] key, V val) {
        checkSemaphore();

        var curHead = this.zeroLevel;
        int level = 0;
        boolean leafFound = false;
        boolean wasAquired = false;

        try {
            this.semaphore.acquire();
            wasAquired = true;

            if (this.zeroLevel.size() == 0) {
                if (key.length == 1) {
                    this.zeroLevel.add(new TreeElement(key[0], val));
                    return;
                }
                this.zeroLevel.add(new TreeElement(key[level++]));
            }

            while (!leafFound) {
                boolean isFinal = key.length-1 == level;

                for (TreeElement elem: curHead) {
                    if (elem.key.equals(key[level])) {
                        if (isFinal) {
                            elem.val = val;
                            return;
                        }

                        if (elem.nextLevel == null) {
                            // we finished going down existing tree
                            leafFound = true;
                            level++;

                            while (level < key.length) {
                                elem.nextLevel = new ArrayList<>();
                                elem.nextLevel.add(new TreeElement(key[level++]));
                                elem = elem.nextLevel.get(0);
                            }
                            elem.val = val;
                            break;
                        }

                        curHead = elem.nextLevel;
                    }
                }
                if (!leafFound) {
                    curHead.add(new TreeElement(key[level]));
                // it makes one more useless iteration but simplifies the code
                } else {
                    level++;
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
        } catch (NullPointerException npe) {
            // impossible in case of wright implementation
            var errorLogger = Logger.getLogger("ERROR");
            errorLogger.log(
                    new LogRecord(
                            Level.WARNING,
                            "Caught an Null Pointer exception"
                    )
            );
        } finally {
            if (wasAquired) {
                this.semaphore.release();
            }
        }
    }

    // return null if didn't found anything or there is no value
    public V get(K []key) {
        checkSemaphore();

        List<TreeElement> curHead = this.zeroLevel;
        int level = 0;
        boolean wasAquired = false;

        try {
            this.semaphore.acquire();
            wasAquired = true;

            while (level < key.length) {
                if (curHead == null) {
                    return null;
                }

                boolean found = false;
                for  (TreeElement elem: curHead) {
                    if (elem.key.equals(key[level])) {
                        if  (key.length - 1 == level) {
                            return elem.val;
                        }

                        curHead = elem.nextLevel;
                        found = true;
                        level++;
                        break;
                    }
                }
                if (!found) {
                    return null;
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
