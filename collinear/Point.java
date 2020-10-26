/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Collinear, Grade 87/100, https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php
 */

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
	private int x;
	private int y;

	// constructs the point (x, y)
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

//	private static int count = 2;
	// draws this point
	public void draw() {
//		if (count % 2 == 0) {
//			StdDraw.text(x, y + 700, this.toString());
//		} else {
//			StdDraw.text(x, y - 700, this.toString());
//		}
//		count++;

//		StdDraw.text(x, y + 700, this.toString());
//		StdDraw.circle(x, y, 100);
		StdDraw.circle(x, y, 100);
	}

	// draws the line segment from this point to that point
	public void drawTo(Point that) {
		int x0 = this.x;
		int y0 = this.y;
		int x1 = that.x;
		int y1 = that.y;
		
		StdDraw.line(x0, y0, x1, y1);
	}

	// string representation
	public String toString() {
		//--return String.format("(%s, %s)", x/1000, y/1000);
		return String.format("(%s, %s)", x, y);
	}

	// compare two points by y-coordinates, breaking ties by x-coordinates
	public int compareTo(Point that) {
		if (that == null || !(that instanceof Point)) {
			throw new IllegalArgumentException();
		}

		if (this == that) {
			return 0;
		}

		int x0 = this.x;
		int y0 = this.y;
		int x1 = that.x;
		int y1 = that.y;

		if (y0 < y1) {
			return -1;
		} else if (y0 == y1) {
			if (x0 < x1) {
				return -1;
			} else if (x0 == x1) {
				return 0;
			} else {
				return 1;
			}
		} else if (y0 > y1) {
			return 1;
		}

		return 0;
	}

	// the slope between this point and that point
	public double slopeTo(Point that) {
		double x0 = this.x;
		double y0 = this.y;
		double x1 = that.x;
		double y1 = that.y;

		if (this.compareTo(that) == 0) {
			return Double.NEGATIVE_INFINITY;
		} else if (x1 == x0) {
			return Double.POSITIVE_INFINITY;
		} else if (y1 == y0) {
			return 0;
		}

		return (y1 - y0) / (x1 - x0);
	}

	// compare two points by slopes they make with this point
	public Comparator<Point> slopeOrder() {
		return new Comparator<Point>() {

			@Override
			public int compare(Point p1, Point p2) {
				double s1 = slopeTo(p1);
				double s2 = slopeTo(p2);
				if (s1 < s2) {
					return -1;
				} else if (s1 > s2) {
					return 1;
				}

				return 0;
			}
		};
	}
}