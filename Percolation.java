/*
Completed by Ethan Doyle
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // how many tiles are open
    private int numOpen;
    // stores n to be used by other methods
    private int size;
    // array stores n by n grid of open and closed tiles
    private boolean[][] nGrid;
    // Union Find object
    // private QuickFindUF connections;
    private WeightedQuickUnionUF connections;

    // top row of nGrid is connected to virtualTop
    private int virtualTop;
    // bottom row of nGrid is connected to virtual bottom
    private int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be greater than 0");

        // initialize variables
        size = n;
        numOpen = 0;
        nGrid = new boolean[n][n];
        virtualTop = 0;
        virtualBottom = n * n + 1;
        // add two because we have Virtual Top and Virtual Bottom
        // connections = new QuickFindUF(n * n + 2);
        connections = new WeightedQuickUnionUF(n * n + 2);

        // initialize array to false to signify that it is closed
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) nGrid[j][i] = false;
        }

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // if row or col is too big throw and exception
        if (row > size - 1 || col > size - 1) throw new IllegalArgumentException("Out of Bounds");
        // it its already open do nothing
        if (nGrid[row][col]) return;

        // if we get here, we know its closed, so open it and update open count
        nGrid[row][col] = true;
        numOpen++;

        // find 1D correlation
        int current1D = to1DArray(row, col);

        // edge case
        if (size == 1) {
            connections.union(current1D, virtualTop);
            connections.union(current1D, virtualBottom);
            return;
        }

        // means were in the top row, so we connect to virtual top
        if (row == 0) {
            connections.union(current1D, virtualTop);

            // check below to see if we need to connect
            if (nGrid[row + 1][col]) {
                connections.union(to1DArray(row + 1, col), virtualTop);
            }
            // nothing else to do so its safe to return
            return;
        }

        // means were in the bottom row, so we connect to virtual bottom
        if (row == (size - 1)) {
            connections.union(current1D, virtualBottom);

            // check above to see if we need to connect
            if (nGrid[row - 1][col]) {
                connections.union(to1DArray(row - 1, col), current1D);
            }
            // nothing else to do so its safe to return
            return;
        }


        // there are a max of 4 possible surrounding squares
        // Checking the value stored in the grid will tell if tile is opened or not
        // Above
        if (nGrid[row - 1][col]) {
            unionNearby(row - 1, col, current1D);
        }
        // Below
        if (nGrid[row + 1][col]) {
            unionNearby(row + 1, col, current1D);
        }
        // Left
        if (col > 0) {
            if (nGrid[row][col - 1]) {
                unionNearby(row, col - 1, current1D);
            }
        }
        // Right
        if (col < (size - 1)) {
            if (nGrid[row][col + 1]) {
                unionNearby(row, col + 1, current1D);
            }
        }

    }

    // take coordinates and 1D representation of another tile and unions the two
    private void unionNearby(int row, int col, int current1D) {
        int nearbyTile = to1DArray(row, col);
        connections.union(current1D, nearbyTile);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // if row or col is too big throw and exception
        if (row > size - 1 || col > size - 1) throw new IllegalArgumentException("Out of Bounds");
        // return the value stored in row col of nGrid
        return nGrid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // if row or col is too big throw and exception
        if (row > size - 1 || col > size - 1) throw new IllegalArgumentException("Out of Bounds");

        // check if (row, col) is connected to virtual top
        int current1D = to1DArray(row, col);
        if (connections.find(virtualTop) == connections.find(current1D)) return true;
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        // return variable that tracks number of open sites
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        // check if virtual top is connected to virtual bottom
        if (connections.find(virtualBottom) == connections.find(virtualTop)) return true;
        // if it doesnt return true then is must be false
        return false;

    }

    private int to1DArray(int row, int col) {
        // formula I came up with to map a 2D array to 1D array
        return (row * size) + col + 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(10);
        percolation.open(3, 3);
        percolation.open(9, 9);
        percolation.open(0, 5);
        percolation.open(6, 0);

        StdOut.println(percolation.isOpen(5, 5));
        StdOut.println(percolation.isOpen(0, 5));
        StdOut.println(percolation.isFull(0, 5));
        StdOut.println(percolation.isFull(6, 0));
        StdOut.println(percolation.numberOfOpenSites());
        StdOut.println(percolation.percolates());
    }
}
