import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    // node that stores an item in the deque
    private class Node<Item> {
        private final Item item;
        private Node<Item> next;
        private Node<Item> prev;

        // node constructor takes in the item
        private Node(Item item) {
            this.item = item;
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // check if deque is empty
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front of deque
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Must add a valid item!");

        final Node<Item> newNode = new Node<Item>(item);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            first.prev = newNode;
            newNode.next = first;
            first = newNode;
        }
        size++;
    }

    // add the item to the back of deque
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Must add a valid item!");

        final Node<Item> newNode = new Node<Item>(item);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty!");

        final Item item = first.item;
        first = first.next; // new first node
        if (first == null) {
            last = null; // if removing last item, must ensure last is reset as well
        } else {
            first.prev = null; // new first node does not have previous nodes
        }

        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty!");

        final Item item = last.item;
        last = last.prev; // new last node
        if (last == null) {
            first = null; // if empty queue, must ensure last is reset as well
        } else {
            last.next = null; // new first node does not have previous nodes
        }

        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedListIterator() {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            // this should never happen as long as callback loop is implemented correctly
            if (!hasNext())
                throw new NoSuchElementException("No items left in iterator!");

            final Item item = current.item;
            current = current.next; // move cursor to next item in queue
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation unsupported.");
        }
    }

    // prints the current queue and its size
    private void print() {
        Iterator<Item> it = this.iterator();

        System.out.print("[");
        while (it.hasNext()) {
            System.out.print(it.next() + ", ");
        }
        System.out.print("]");

        System.out.println();
        System.out.println("size: " + this.size());
        System.out.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> queue = new Deque<>();

        System.out.println("Adding 1 to empty queue...");
        queue.addFirst(1);
        queue.print();

        System.out.println("Adding 2, 3 to front...");
        queue.addFirst(2);
        queue.addFirst(3);
        queue.print();

        System.out.println("Adding 0 to back...");
        queue.addLast(0);
        queue.print();

        System.out.println("Removing first item...");
        queue.removeFirst();
        queue.print();

        System.out.println("Removing last item...");
        queue.removeLast();
        queue.print();

        System.out.println("Removing all items...");
        for (int i = 0; i < queue.size; i++) {
            queue.removeFirst();
        }
        queue.print();
    }
}