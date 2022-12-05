import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Usage: java Permutation k");
        
        final int k = Integer.parseInt(args[0]);
        final RandomizedQueue<String> queue = new RandomizedQueue<>();

        // read input from stdin and add to queue
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }

        // remove k items from queue at random
        // System.out.println(k + " random items returned:");
        for (int i = 0; i < k; i++) {
            // System.out.println((i + 1) + ": " + queue.dequeue());
            System.out.println(queue.dequeue());
        }
    }
}
