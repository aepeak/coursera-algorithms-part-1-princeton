/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Collinear, Grade 87/100, https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	private Point[] points;
	private LineSegment[] segments;

	// finds all line segments containing 4 or more points
	public FastCollinearPoints(Point[] points) {
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
		// --System.out.println("OG: " + Arrays.toString(points));
		this.segments = getSegments(points).toArray(new LineSegment[0]);
		return segments;
	}

	private List<LineSegment> getSegments(Point[] points) {
		final List<LineSegment> line_segments = new ArrayList<>();

		Point[] points_n_sorted = Arrays.copyOf(points, points.length);
		Arrays.sort(points_n_sorted);

		// --System.out.println("NS: " + Arrays.toString(points_n_sorted));
		// --System.out.println();

		for (int i = 0; i < points_n_sorted.length; i++) {
			// Think of p as the origin.
			Point p = points_n_sorted[i];

			Point[] points_p_slope_sorted = Arrays.copyOf(points_n_sorted, points.length);
			Arrays.sort(points_p_slope_sorted, p.slopeOrder());
			// --System.out.println("SS: " + Arrays.toString(points_p_slope_sorted));

			ArrayList<Point> colls = new ArrayList<>();
			for (int j = 1; j < points_p_slope_sorted.length; j++) {
				Point q_prev = points_p_slope_sorted[j - 1];
				Point q = points_p_slope_sorted[j];
				Point q_next = (j + 1 < points_p_slope_sorted.length) ? points_p_slope_sorted[j + 1] : p;

				// For each other point q, determine the slope it makes with p.
				double pq_slope_prev = p.slopeTo(q_prev);
				double pq_slope = p.slopeTo(q);
				double pq_slope_next = p.slopeTo(q_next);
				// --System.out.println(String.format("[%.2f] %s, %s", pq_slope, p, q));

				// same slope ?
				if (pq_slope == pq_slope_prev) {
					if (colls.size() == 0) {
						colls.add(q_prev);
					}

					// add up
					colls.add(q);
				} else {
					// if slope changed reset the colls to avoid intersection ghosts like
					// a point that is part of multiple collinear points (e.g. /\, |-, +, _|, >,
					// ...)
					if (colls.size() > 1) {
						colls.clear();
					}
				}
				pq_slope_prev = pq_slope;

				
				// there are multiple collinear candidates, but we should choose the one where
				// the p is the smallest point in the array
				if (colls.size() >= 3) {
					// --System.out.println("CC: " + colls);
					// 1. founded collinear points colls should be sorted as q1 < q2 < ... < qn
					// 2. choose the collinear points colls where p (the origin) is the smallest
					// as p < q1 < q2 < ... < qn
					// so => p,[q1, q2, q3 ... qn], where the points are sorted naturally
					Point q1 = colls.get(0); // min
					Point qn = colls.get(colls.size() - 1); // max

					if (p.compareTo(q1) == -1) {
						// is it the max segment? if not continue
						if (pq_slope == pq_slope_next) {
							continue;
						}
						
						// --System.out.print("valid segments: " + p + ", " + colls);
						// --System.out.println(String.format(" as %s.compareTo(%s) = %d", p, q1,
						// p.compareTo(q1)));
						Point min = p;
						Point max = qn;

						LineSegment ls = new LineSegment(min, max);
						// --System.out.println("max segment: " + ls);
						line_segments.add(ls);

						// remove the last coll point to avoid reporting duplicate segments
						// if there will be another coll point added it (the last removed) will not be
						// the max
						colls.remove(qn);
					} else {
						// --System.out.print("invalid segments: " + colls);
						// --System.out.println(String.format(" as %s.compareTo(%s) = %d", p, q1,
						// p.compareTo(q1)));
						colls.clear();
					}
				}
			}
			// --System.out.println();
		}

		// --System.out.println("max segments: " + line_segments);
		return line_segments;
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
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}

		StdDraw.show();
	}
}
