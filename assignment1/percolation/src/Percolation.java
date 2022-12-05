import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[][] grid; // true if open
    private int openCount;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF extrauf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("'n' must be greater than 0!");
        }

        this.n = n;
        this.grid = new boolean[n][n]; // does not include virtual sites
        this.openCount = 0;
        this.uf = new WeightedQuickUnionUF(n * n + 2); // +2 for top and bottom virtual sites
        this.extrauf = new WeightedQuickUnionUF(n * n + 1); // +1 for top virtual site
    }

    // opens the site (row, col) if not already open
    // updates class variables and makes call to uf.union()
    public void open(int row, int col) {
        // if already open, return
        if (isOpen(row, col))
            return;
        assertRange(row, col);

        this.grid[row - 1][col - 1] = true;
        this.openCount++;

        // Top & bottom rows with virtual sites
        if (row == 1) {
            this.uf.union(toIndex(row, col), 0);
            this.extrauf.union(toIndex(row, col), 0);
        } else if (row == this.n) {
            this.uf.union(toIndex(row, col), this.n * this.n + 1);
        }

        // Rows
        if (row > 1 && isOpen(row - 1, col)) {
            this.uf.union(toIndex(row, col), toIndex(row - 1, col));
            this.extrauf.union(toIndex(row, col), toIndex(row - 1, col));
        }
        if (row < this.n && isOpen(row + 1, col)) {
            this.uf.union(toIndex(row, col), toIndex(row + 1, col));
            this.extrauf.union(toIndex(row, col), toIndex(row + 1, col));
        }

        // Columns
        if (col > 1 && isOpen(row, col - 1)) {
            this.uf.union(toIndex(row, col), toIndex(row, col - 1));
            this.extrauf.union(toIndex(row, col), toIndex(row, col - 1));
        }
        if (col < this.n && isOpen(row, col + 1)) {
            this.uf.union(toIndex(row, col), toIndex(row, col + 1));
            this.extrauf.union(toIndex(row, col), toIndex(row, col + 1));
        }
    }

    // convert 2D grid index to 1D flattened index
    // index 0 is the top virtual site, index (n*n + 1) is the bottom virtual site
    private int toIndex(int row, int col) {
        return (row - 1) * this.n + col;
    }

    // throws an exception if given site is out of grid's range
    private void assertRange(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            throw new IllegalArgumentException("Site is out of range!");
        }
    }

    // checks if a site is open
    public boolean isOpen(int row, int col) {
        assertRange(row, col);
        return this.grid[row - 1][col - 1];
    }

    // checks if a site is full -- an open site that can be connected to an open
    // site in the top row
    public boolean isFull(int row, int col) {
        assertRange(row, col);
        return this.uf.find(toIndex(row, col)) == this.uf.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openCount;
    }

    // checks if the system percolates - top and bottom virtual sites have same
    // canonical element
    public boolean percolates() {
        return this.uf.find(0) == this.uf.find(this.n * this.n + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        // empty main
    }
}
