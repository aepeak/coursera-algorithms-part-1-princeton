/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Collinear, Grade 87/100, https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
 */

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

	private final Point[] points;
	private LineSegment[] segments;

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
		if (points == null) {
			throw new IllegalArgumentException("null points");
		}
		Point[] nsorted = points.clone();
		Arrays.sort(nsorted);
		for (int i = 0; i < nsorted.length - 1; i++) {
			if (nsorted[i] == null) {
				throw new IllegalArgumentException(String.format("null point at index %d", i));
			}
			if (nsorted[i].compareTo(nsorted[i + 1]) == 0) {
				throw new IllegalArgumentException(String.format("duplicate found at index %d", i));
			}
		}

		this.points = points;
		this.segments = new LineSegment[] {};
	}

	// the number of line segments
	public int numberOfSegments() {
		return segments.length;
	}

	// the line segments
	public LineSegment[] segments() {
		Point[] cpoints = getColPoints(points);
		segments = Arrays.copyOf(segments, cpoints.length / 2);
		int j = 0;
		for (int i = 0; i < segments.length; i++) {
			segments[i] = new LineSegment(cpoints[j], cpoints[j+1]);
			j += 2;
		}
		return segments;
	}

	private Point[] getColPoints(Point[] points) {
		Point[] cpoints = new Point[] {};
		
		Arrays.sort(points);
		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
			for (int ii = i + 1; ii < points.length; ii++) {
				Point q = points[ii];
				for (int iii = ii + 1; iii < points.length; iii++) {
					Point r = points[iii];
					for (int iiii = iii + 1; iiii < points.length; iiii++) {
						Point s = points[iiii];

						if (are4Col(p, q, r, s)) {
							if (cpoints.length == 0) {
								cpoints = Arrays.copyOf(cpoints, cpoints.length + 2);
								cpoints[cpoints.length - 2] = p;
								cpoints[cpoints.length - 1] = s;
							} else {
								Point pp = cpoints[cpoints.length - 2];
								Point ps = cpoints[cpoints.length - 1];
								if (are3Col(pp, ps, s)) {
									//rpoints[rpoints.length - 2] = p; // keep the previous
									cpoints[cpoints.length - 1] = s; // replace previous s with new s
								} else {
									cpoints = Arrays.copyOf(cpoints, cpoints.length + 2);
									cpoints[cpoints.length - 2] = p;
									cpoints[cpoints.length - 1] = s;
								}
							}
						}
					}
				}
			}
		}

		return cpoints;
	}

	private boolean are3Col(Point p, Point q, Point s) {
		double spq = p.slopeTo(q);
		double sps = p.slopeTo(s);

		if (spq == sps) {
			return true;
		}

		return false;
	}

	private boolean are4Col(Point p, Point q, Point r, Point s) {
		double spq = p.slopeTo(q);
		double spr = p.slopeTo(r);
		double sps = p.slopeTo(s);

		if (spq == spr && spq == sps) {
			return true;
		}

		return false;
	}

	public static void main(String[] args) {
		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setCanvasSize(1024, 1024);
		StdDraw.setXscale(-2000, 34768);
		StdDraw.setYscale(-2000, 34768);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}