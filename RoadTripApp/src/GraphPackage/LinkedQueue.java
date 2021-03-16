package GraphPackage;

public class LinkedQueue<T> implements QueueInterface<T> {

    private Node firstNode;
    private Node lastNode;
    private int numberOfEntries;  // NOT original, added for RoadTrip Project

    public LinkedQueue() {
        firstNode = null;
        lastNode = null;
        numberOfEntries = 0;  // NOT original, added for RoadTrip Project

    }

    /**
     * Adds a new entry to the back of this queue.
     *
     * @param newEntry An object to be added.
     */
    @Override
    public void enqueue(T newEntry) {
        Node newNode = new Node(newEntry, null);
        if (isEmpty()) {
            firstNode = newNode;
        } else {
            lastNode.setNextNode(newNode);
        }
        lastNode = newNode;
        numberOfEntries++;  // NOT original, added for RoadTrip Project
    }

    /**
     * Removes and returns the entry at the front of this queue.
     *
     * @return The object at the front of the queue.
     * @throws EmptyQueueException if the queue is empty before the operation.
     */
    @Override
    public T dequeue() {
        T front = getFront();  // might throw EmptyQueueException
        firstNode.setData(null);
        firstNode = firstNode.getNextNode();
        if (firstNode == null)
            lastNode = null;
        numberOfEntries--;  // NOT original, added for RoadTrip Project
        return front;
    }

    /**
     * Retrieves the entry at the front of this queue.
     *
     * @return The object at the front of the queue.
     * @throws EmptyQueueException if the queue is empty.
     */
    @Override
    public T getFront() {
        if (isEmpty())
            throw new EmptyQueueException("The queue is empty.");
        else
            return firstNode.getData();
    }

    /**
     * Detects whether this queue is empty.
     *
     * @return True if the queue is empty, or false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return (firstNode == null) && (lastNode == null);
    }

    /**
     * Removes all entries from this queue.
     */
    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
    }

    // NOT original, added for RoadTrip Project
    /** Returns the Number of entries currently in the queue. */
    public int getSize() {
        return numberOfEntries;
    }

    private class Node {
        private T data;
        private Node next;

        private Node(T dataPortion) {
            this(dataPortion, null);
        }
        private Node(T dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        }

        private T getData() {return data;}

        private void setData(T newData) {data = newData;}

        private Node getNextNode() {return next;}

        private void setNextNode(Node nextNode) {next = nextNode;}
    }
}
