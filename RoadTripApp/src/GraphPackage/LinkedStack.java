package GraphPackage;
import java.util.EmptyStackException;

public final class LinkedStack<T> implements StackInterface<T> {

    private Node topNode;

    public LinkedStack() {
        topNode = null;
    }

    /**
     * Adds a new entry to the top of this stack.
     *
     * @param newEntry an object to be added to the stack.
     */
    @Override
    public void push(T newEntry) {
        topNode = new Node(newEntry, topNode);
    }

    /**
     * Removes and returns this stack's top entry.
     *
     * @return The object at the top of the stack.
     * @throws EmptyStackException if the stack is empty before
     *                             the operation.
     */
    @Override
    public T pop() {
        if (topNode != null) {
            T top = topNode.getData();
            topNode = topNode.getNextNode();
            return top;
        } else
            throw new EmptyStackException();
    }

    /**
     * Retrieves this stacks top entry.
     *
     * @return the object at the top of the stack.
     * @throws EmptyStackException if the stack is empty.
     */
    @Override
    public T peek() {
        if (isEmpty())
            throw new EmptyStackException();
        else
            return topNode.getData();
    }

    /**
     * Detects whether this stack is empty.
     *
     * @return True if the stack is empty.
     */
    @Override
    public boolean isEmpty() {
        return topNode == null;
    }

    /**
     * Removes all entries from this stack.
     */
    @Override
    public void clear() {
        topNode = null;
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

        private T getData() {
            return data;
        }

        private void setData(T newData) {
            data = newData;
        }

        private Node getNextNode() {
            return next;
        }

        private void setNextNode(Node nextNode) {
            next = nextNode;
        }
    }
}
