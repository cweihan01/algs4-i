import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] thresholds;
    private final int trials;
    private static final double confidence95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("'n' must be greater than 0!");
        if (trials <= 0) throw new IllegalArgumentException("'trials' must be greater than 0!");

        this.thresholds = new double[trials];
        this.trials = trials;
        final int totalSites = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                // open a random site
                perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);

                // end condition
                if (perc.percolates()) {
                    this.thresholds[i] = (double) perc.numberOfOpenSites() / (double) totalSites;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - ((confidence95 * this.stddev()) / Math.sqrt(this.trials));
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + ((confidence95 * this.stddev()) / Math.sqrt(this.trials));
    }

   // test client (see below)
   public static void main(String[] args) {
       if (args.length != 2) throw new IllegalArgumentException("Usage: java PercolationStats n trials");
       
       final int n = Integer.parseInt(args[0]);
       final int trials = Integer.parseInt(args[1]);

       PercolationStats percStats = new PercolationStats(n, trials);
       StdOut.println("mean                    = " + percStats.mean());
       StdOut.println("stddev                  = " + percStats.stddev());
       StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats.confidenceLo() + "]");
   }
}
