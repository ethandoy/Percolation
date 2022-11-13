/*
Completed by Ethan Doyle
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    double threshold[];
    double openCount;
    int size;
    double mean;
    double stdDev;
    int T;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Input must be greater than zero");
        }
        T = trials;
        size = n;
        threshold = new double[trials];
        Percolation percolation;
        for (int i = 0; i < trials; i++) {
            // reset variables for this trial
            openCount = 0;
            percolation = new Percolation(n);

            // open sites until it percolates
            while (!percolation.percolates()) {
                // generate two random integers
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);
                // check if its already open
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    openCount++;
                }
            }
            threshold[i] = openCount / (size * size);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(threshold);
        return mean;

    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        stdDev = StdStats.stddev(threshold);
        return stdDev;
    }


    // low endpoint of 95% confidence interval
    public double confidenceLow() {

        return mean - ((1.96 * stdDev) / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean + ((1.96 * stdDev) / Math.sqrt(T));
    }

    // test client (see below)
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int Trials = Integer.parseInt(args[1]);
        Stopwatch start = new Stopwatch();
        PercolationStats ps = new PercolationStats(N, Trials);
        double time = start.elapsedTime();
        StdOut.println("Mean: " + ps.mean());
        StdOut.println("StdDev: " + ps.stddev());
        StdOut.println("ConfidenceLow: " + ps.confidenceLow());
        StdOut.println("ConfidenceHigh: " + ps.confidenceHigh());
        StdOut.println("Time: " + time);
    }

}
