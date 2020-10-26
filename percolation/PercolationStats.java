
/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Percolation, Grade 92/100, https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import java.lang.Math;

public class PercolationStats {

	private int trials;
	private double[] results;
	private final double CONFIDANCE_FACTOR = 1.96;

	// perform independent trials on an n-by-n grid
	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0) {
			throw new IllegalArgumentException();
		}

		this.trials = trials;
		this.results = new double[trials];

		for (int i = 0; i < trials; i++) {
			Percolation model = new Percolation(n);
			while (!model.percolates()) {
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				model.open(row, col);
			}

			this.results[i] = (double) model.numberOfOpenSites() / (n * n);
		}
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(results);
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev(results);
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean() - this.CONFIDANCE_FACTOR * (stddev() / Math.sqrt(trials));
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean() + this.CONFIDANCE_FACTOR * (stddev() / Math.sqrt(trials));
	}

	// test client (see below)
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);

		PercolationStats stats = new PercolationStats(n, trials);
		StdOut.println(String.format("%-24s= %f", "mean", stats.mean()));
		StdOut.println(String.format("%-24s= %f", "stddev", stats.stddev()));
		StdOut.println(String.format("%-24s= [%f, %f]", "95% confidence interval", stats.confidenceLo(),
				stats.confidenceHi()));
	}
}
