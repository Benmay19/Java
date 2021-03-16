package GraphPackage;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedDictionary<K, V> implements DictionaryInterface<K, V> {

    private Node firstNode;
    private int currentSize;

    public LinkedDictionary() {
        firstNode = null;
        currentSize = 0;
    }

    /**
     * Adds a new entry to this dictionary. If the given search key already
     * exists in the dictionary, replaces the corresponding value.
     *
     * @param key   An object search key of the new entry.
     * @param value An object associated with the search key.
     * @return Either null if the new entry was added to the dictionary
     * or the value that was associated with key if that value was replaced.
     */
    @Override
    public V add(K key, V value) {
        Node currentNode = firstNode;
        while ((currentNode != null) && (!key.equals(currentNode.getKey()))) {
            currentNode = currentNode.getNextNode();
        }
        if (currentNode == null) {
            Node newEntry = new Node(key, value);
            newEntry.setNextNode(firstNode);
            firstNode = newEntry;
            return null;
        } else {
            V oldValue = currentNode.getValue();
            currentNode.setValue(value);
            return oldValue;
        }
    }

    /**
     * Removes a specific entry from this dictionary.
     *
     * @param key An object search key of the entry to be removed.
     * @return Either the value that was associated with the search key
     * or null if no such object exists.
     */
    @Override
    public V remove(K key) {
        V result = null;
        if (!isEmpty()) {
            Node currentNode = firstNode;
            Node previousNode = null;

            while ((currentNode != null) && (!key.equals(currentNode.getKey()))) {
                previousNode = currentNode;
                currentNode = currentNode.getNextNode();
            }

            if (currentNode != null) {
                Node followingNode = currentNode.getNextNode();

                if (previousNode == null)
                    firstNode = followingNode;
                else
                    previousNode.setNextNode(followingNode);

                result = currentNode.getValue();
                currentSize--;
            }
        }
        return result;
    }

    /**
     * Retrieves from this dictionary the value associated with a given search key.
     *
     * @param key An object search key of the entry to be retrieved.
     * @return Either the value that is associated with the search key
     * or null if no such object exists.
     */
    @Override
    public V getValue(K key) {
        V result = null;
        Node currentNode = firstNode;

        while ((currentNode != null) && (!key.equals(currentNode.getKey()))) {
            currentNode = currentNode.getNextNode();
        }
        if (currentNode != null)
            result = currentNode.getValue();
        return result;
    }

    /**
     * Sees whether a specific entry is in this dictionary.
     *
     * @param key An object search key of the desired entry.
     * @return True if key is associated with an entry in the dictionary.
     */
    @Override
    public boolean contains(K key) {
        return getValue(key) != null;
    }

    /**
     * Creates an iterator that traverses all search keys in this dictionary.
     *
     * @return An iterator that provides sequential access to the search
     * keys in the dictionary.
     */
    @Override
    public Iterator<K> getKeyIterator() {
        return new KeyIterator();
    }

    /**
     * Creates an iterator that traverses all values in this dictionary.
     *
     * @return An iterator that provides sequential access to the values
     * in this dictionary.
     */
    @Override
    public Iterator<V> getValueIterator() {
        return new ValueIterator();
    }

    /**
     * Sees whether this dictionary is empty.
     *
     * @return True if the dictionary is empty.
     */
    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Gets the size of this dictionary.
     *
     * @return The number of entries (key-value pairs) currently
     * in the dictionary.
     */
    @Override
    public int getSize() {
        return currentSize;
    }

    /**
     * Removes all entries from this dictionary.
     */
    @Override
    public void clear() {
        firstNode = null;
        currentSize = 0;
    }

    private class KeyIterator implements Iterator<K> {
        private Node nextNode;

        private KeyIterator() {nextNode = firstNode;}

        public boolean hasNext() {return nextNode != null;}

        public K next() {
            K result;
            if (hasNext()) {
                result = nextNode.getKey();
                nextNode = nextNode.getNextNode();
            } else
                throw new NoSuchElementException("Illegal call to next(); " +
                        "iterator is after end of list.");
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() is not supported " +
                    "by this iterator");
        }
    }

    private class ValueIterator implements Iterator<V> {
        private Node nextNode;

        private ValueIterator() {nextNode = firstNode;}

        public boolean hasNext() {return nextNode != null;}

        public V next() {
            V result;
            if (hasNext()) {
                result = nextNode.getValue();
                nextNode = nextNode.getNextNode();
            } else
                throw new NoSuchElementException("Illegal call to next(); " +
                        "iterator is after end of list.");
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() is not supported " +
                    "by this iterator");
        }
    }

    private class Node {
        private K key;
        private V value;
        private Node next;

        private Node(K newKey, V newValue) { this(newKey, newValue, null);}

        private Node(K newKey, V newValue, Node nextNode) {
            key = newKey;
            value = newValue;
            next = nextNode;
        }

        private K getKey() {return key;}
        private void setKey(K newKey) {key = newKey;}
        private V getValue() {return value;}
        private void setValue(V newValue) {value = newValue;}
        private Node getNextNode() {return next;}
        private void setNextNode(Node nextNode) {next = nextNode;}
    }
}
