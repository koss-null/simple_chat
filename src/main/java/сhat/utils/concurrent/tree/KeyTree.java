package сhat.utils.concurrent.tree;

import java.io.Serializable;
import java.lang.reflect.Array;
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
        boolean wasAcquired = false;

        try {
            this.semaphore.acquire();
            wasAcquired = true;

            while (true) {
                boolean foundKey = false;
                for (var elem: curHead) {
                    if (elem.key.equals(key[level])) {
                        foundKey = true;
                        if (level == key.length-1) {
                            elem.val = val;
                            return;
                        }
                        if (elem.nextLevel == null) {
                            elem.nextLevel = new ArrayList<>();
                        }
                        curHead = elem.nextLevel;
                        level++;
                    }
                }
                if (!foundKey) {
                    curHead.add(new TreeElement(key[level]));
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
            if (wasAcquired) {
                this.semaphore.release();
            }
        }
    }

    // return null if didn't found anything or there is no value
    public V get(K []key) {
        checkSemaphore();

        List<TreeElement> curHead = this.zeroLevel;
        int level = 0;
        boolean wasAcquired = false;

        try {
            this.semaphore.acquire();
            wasAcquired = true;

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
            if (wasAcquired) {
                this.semaphore.release();
            }
        }

        return null;
    }

    // return null if didn't found anything or there is no value
    public K[] getKeys(K []key) {
        checkSemaphore();

        List<TreeElement> curHead = this.zeroLevel;
        int level = 0;
        boolean wasAcquired = false;

        try {
            this.semaphore.acquire();
            wasAcquired = true;

            while (level < key.length) {
                if (curHead == null) {
                    return null;
                }

                boolean found = false;
                for  (TreeElement elem: curHead) {
                    if (elem.key.equals(key[level])) {
                        if  (key.length - 1 == level) {
                            if (elem.nextLevel == null) {
                                return null;
                            }
                            K[] keys = (K[]) Array.newInstance(elem.key.getClass(), elem.nextLevel.size());
                            for (int i = 0; i < elem.nextLevel.size(); i++) {
                                keys[i] = elem.nextLevel.get(i).key;
                            }
                            return keys;
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
            if (wasAcquired) {
                this.semaphore.release();
            }
        }

        return null;
    }
}
