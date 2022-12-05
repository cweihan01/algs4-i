import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue; // each item in queue is simply the item itself
    private int size;
    private int capacity = 2;

    // construct an empty deque
    public RandomizedQueue() {
        queue = (Item[]) new Object[capacity];
        size = 0;
    }

    // check if deque is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Must add a valid item!");

        // double capacity of queue if it is full
        if (size == capacity) {
            resize(capacity * 2);
        }

        // add item to end of array
        queue[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0)
            throw new NoSuchElementException("Queue is empty!");

        // half capacity of queue if it is a quarter full
        if (size > 0 && size <= (capacity / 4)) {
            resize(capacity / 2);
        }

        // remove random item
        final int idx = StdRandom.uniform(size);
        final Item item = queue[idx];
        queue[idx] = queue[--size]; // swap last item in queue with the one being removed
        queue[size] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0)
            throw new NoSuchElementException("Queue is empty!");

        // sample random item
        return queue[StdRandom.uniform(size)];
    }

    private void resize(int newCapacity) {
        this.capacity = newCapacity; // update local variables
        final Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < this.size; i++) {
            copy[i] = this.queue[i];
        }
        this.queue = copy;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int currentIdx = 0;
        private final Item[] queueIterator;

        public ArrayIterator() {
            queueIterator = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                queueIterator[i] = queue[i];
            }
            // randomise each iterator so each iterator has a unique order
            StdRandom.shuffle(queueIterator);
        }

        public boolean hasNext() {
            return currentIdx < size;
        }

        public Item next() {
            // this should never happen as long as callback loop is implemented correctly
            if (!hasNext())
                throw new NoSuchElementException("No items left in iterator!");

            // move cursor to next item after returning current item in randomised array
            return queueIterator[currentIdx++];
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
        System.out.println("capacity: " + this.capacity);
        System.out.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        System.out.println("Adding 1 to empty queue...");
        queue.enqueue(1);
        queue.print();

        System.out.println("Adding 2, 3 (array capacity should double)...");
        queue.enqueue(2);
        queue.enqueue(3);
        queue.print();

        System.out.println("Adding 4, 5 (array capacity should double again)...");
        queue.enqueue(4);
        queue.enqueue(5);
        queue.print();

        System.out.println("Sampling random item...");
        System.out.println("item: " + queue.sample());
        queue.print();

        System.out.println("Removing random item...");
        System.out.println("item: " + queue.dequeue());
        queue.print();

        System.out.println("Removing 2 items (array capacity should half)...");
        queue.dequeue();
        queue.dequeue();
        queue.print();

        System.out.println("Removing all items...");
        for (int i = 0; i < queue.size; i++) {
            queue.dequeue();
        }
        queue.print();
    }
}
