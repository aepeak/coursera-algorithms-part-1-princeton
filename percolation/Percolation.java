
/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Percolation, Grade 92/100, https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int n;
	private int topId;
	private int bottomId;
	private int openCells;
	private boolean[] cellsState;
	private WeightedQuickUnionUF algo;
	
	// creates n-by-n grid, with all sites initially blocked
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}

		int size = n * n;

		this.n = n;
		this.topId = size;
		this.bottomId = size + 1;
		this.openCells = 0;
		this.cellsState = new boolean[size];
		this.algo = new WeightedQuickUnionUF(size + 1 + 1);
	}

	// opens the site (row, col) if it is not open already
	public void open(int row, int col) {
		if ((row < 1 || row > n) || (col < 1 || col > n)) {
			throw new IllegalArgumentException();
		}

		if (isOpen(row, col)) {
			return; // is already open
		}

		int p = toId(row, col);
		this.cellsState[p] = true;
		this.openCells += 1;

		// take the neighbors as
		//   x
		// x p x
		//   x
		int[] news = new int[] { //
				row - 1, col, //
				row, col - 1, row, col + 1, //
				row + 1, col //
		};
		for (int i = 0; i < news.length; i = i + 2) {
			int r = news[i];
			int c = news[i + 1];
			if ((r < 1 || r > n) || (c < 1 || c > n)) {
				continue;
			}

			if (isOpen(r, c)) {
				int q = toId(r, c);
				algo.union(p, q);
			}
		}

		// is on first row ? connect to the roof
		if (row == 1) {
			algo.union(p, topId);
		}
		// is it the last row ? connect it to the floor
		else if (row == n) {
			algo.union(p, bottomId);
		}
	}

	// is the site (row, col) open?
	public boolean isOpen(int row, int col) {
		if ((row < 1 || row > n) || (col < 1 || col > n)) {
			throw new IllegalArgumentException();
		}

		int id = toId(row, col);
		return cellsState[id];
	}

	// is the site (row, col) full?
	public boolean isFull(int row, int col) {
		if ((row < 1 || row > n) || (col < 1 || col > n)) {
			throw new IllegalArgumentException();
		}

		int p = toId(row, col);
		return algo.find(p) == algo.find(this.topId);
	}

	// returns the number of open sites
	public int numberOfOpenSites() {
		return this.openCells;
	}

	// does the system percolate?
	public boolean percolates() {
		return algo.find(topId) == algo.find(bottomId);
	}

	// test client (optional)
	public static void main(String[] args) {
		Percolation perc = new Percolation(4);
		perc.open(1, 2);
		perc.open(2, 2);
		perc.open(2, 3);
		perc.open(3, 3);
		perc.open(4, 3);
		StdOut.println(String.format("n = 4, has perculated = %s", perc.percolates()));
	}
	
	private int toId(int row, int col) {
		return (n * row) - (n - col) - 1;
	}
}