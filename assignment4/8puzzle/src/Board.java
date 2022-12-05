import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;

public class Board {
    private final int n;
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];

        // creates a local copy of tiles 2D array
        for (int i = 0; i < n; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], this.n);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder finalString = new StringBuilder();

        // first line is the board size
        finalString.append(n).append("\n");

        // add n by n grid
        for (int i = 0; i < n; i++) {
            finalString.append(" ");
            for (int j = 0; j < n; j++) {
                finalString.append(tiles[i][j]).append("  ");
            }
            finalString.append("\n");
        }
        return finalString.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int k = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if (tile != 0 && tile != (i * n + j + 1)) {
                    k++;
                }
            }
        }
        return k;
    }

    // sum of Manhattan distances between current tiles and goal
    public int manhattan() {
        int m = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];

                // break if tile is 0 or tile is already in the correct position
                if (tile == 0 || tile == (i * n + j + 1))
                    break;

                // correct positions of this tile
                int newI = (tile - 1) / n;
                int newJ = (tile - 1) % n;

                // add Manhattan distance for this tile
                m += Math.abs(newI - i) + Math.abs(newJ - j);
            }
        }
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    // must fulfill all requirements of `equals` from Java.util.Object
    public boolean equals(Object y) {
        // reflexive
        if (this == y)
            return true;

        // non-null
        if (y == null)
            return false;

        // same object types
        if (this.getClass() != y.getClass())
            return false;

        // cast to a Board and check for dimensions
        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;

        // since same dimensions, check individidual tiles
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final List<Board> neighbors = new ArrayList<>();
        final int[][] tilesCopy = new int[n][n];
        int zeroI = 0;
        int zeroJ = 0;

        // make a local copy of tiles board to make local changes for each neighbor
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tilesCopy[i][j] = this.tiles[i][j];
                if (this.tiles[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }

        // add blank tile's neighbors depending on its location
        if (zeroI - 1 >= 0) {
            int tmp = tilesCopy[zeroI - 1][zeroJ];
            tilesCopy[zeroI][zeroJ] = tmp;
            tilesCopy[zeroI - 1][zeroJ] = 0;
            neighbors.add(new Board(tilesCopy));
            tilesCopy[zeroI][zeroJ] = 0;
            tilesCopy[zeroI - 1][zeroJ] = tmp;
        }
        if (zeroI + 1 < this.n) {
            int tmp = tilesCopy[zeroI + 1][zeroJ];
            tilesCopy[zeroI][zeroJ] = tmp;
            tilesCopy[zeroI + 1][zeroJ] = 0;
            neighbors.add(new Board(tilesCopy));
            tilesCopy[zeroI][zeroJ] = 0;
            tilesCopy[zeroI + 1][zeroJ] = tmp;
        }
        if (zeroJ - 1 >= 0) {
            int tmp = tilesCopy[zeroI][zeroJ - 1];
            tilesCopy[zeroI][zeroJ] = tmp;
            tilesCopy[zeroI][zeroJ - 1] = 0;
            neighbors.add(new Board(tilesCopy));
            tilesCopy[zeroI][zeroJ - 1] = 0;
            tilesCopy[zeroI][zeroJ - 1] = tmp;
        }
        if (zeroJ + 1 < this.n) {
            int tmp = tilesCopy[zeroI][zeroJ + 1];
            tilesCopy[zeroI][zeroJ] = tmp;
            tilesCopy[zeroI][zeroJ + 1] = 0;
            neighbors.add(new Board(tilesCopy));
            tilesCopy[zeroI][zeroJ] = 0;
            tilesCopy[zeroI][zeroJ + 1] = tmp;
        }

        return neighbors;
    }

    // a board that is obtained by swapping any pairs of tiles
    public Board twin() {
        int[][] twinall = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinall[i][j] = this.tiles[i][j];
            }
        }
        if (this.tiles[0][0] != 0 && this.tiles[0][1] != 0) {
            twinall[0][0] = this.tiles[0][1];
            twinall[0][1] = this.tiles[0][0];
        } else {
            twinall[1][0] = this.tiles[1][1];
            twinall[1][1] = this.tiles[1][0];
        }
        return new Board(twinall);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("Usage: java Board data/puzzle00.txt");

        // read in the board specified in the filename
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // test Board methods
        Board board = new Board(tiles);
        System.out.print(board.toString());
    }
}