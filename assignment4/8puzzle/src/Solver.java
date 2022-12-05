import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final List<Board> solution = new ArrayList<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Board cannot be null!");

        MinPQ<Node> pq = new MinPQ<>(new ManhattanPriorityComparator());
        MinPQ<Node> pqTwin = new MinPQ<>(new ManhattanPriorityComparator());

        Node currNode = new Node(initial, null);
        Node currTwinNode = new Node(initial.twin(), null);

        pq.insert(currNode);
        pqTwin.insert(currTwinNode);

        // loop until reach goal board
        while (!currNode.board.isGoal() && !currTwinNode.board.isGoal()) {
            currNode = pq.delMin();
            // add all neighbors of current board to pq, provided it is not the board we just came from
            for (Board neighbor : currNode.board.neighbors()) {
                if (currNode.prev != null && currNode.prev.board.equals(neighbor)) {
                    continue;
                }
                Node neighborNode = new Node(neighbor, currNode);
                pq.insert(neighborNode);
            }

            currTwinNode = pqTwin.delMin();
            // add all neighbors of current board to pq, provided it is not the board we just came from
            for (Board neighbor : currTwinNode.board.neighbors()) {
                if (currTwinNode.prev != null && currTwinNode.prev.board.equals(neighbor)) {
                    continue;
                }
                Node neighborNode = new Node(neighbor, currTwinNode);
                pqTwin.insert(neighborNode);
            }
        }

        // add solution if solvable
        if (currNode.board.isGoal() && !currTwinNode.board.isGoal()) {
            // start adding solutions, starting from goal board to initial board
            while (currNode != null) {
                this.solution.add(currNode.board);
                currNode = currNode.prev;
            }
            
            // reverse solution order
            Collections.reverse(this.solution);
        }
    }
    
    // search node of the board
    private static class Node {
        private final Board board;
        private final int moves;
        private final Node prev;
        private final int priority;

        private Node(Board board, Node prev) {
            this.board = board;
            this.prev = prev;
            if (prev == null) {
                this.moves = 0; // if no previous node, initial search node is move 0
            } else {
                this.moves = prev.moves + 1;
            }
            this.priority = board.manhattan() + this.moves;
        }
    }

    // comparator used to initialize the priority queue
    private static class ManhattanPriorityComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return n1.priority - n2.priority;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.solution != null;
    }

    // min number of moves to solve initial board, -1 if unsolvable
    public int moves() {
        return this.solution.size() - 1;
    }
    
    // sequence of boards in shortest solution, null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? this.solution : null;
    }

    // test client
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Usage: java Solver data/puzzle00.txt");

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}