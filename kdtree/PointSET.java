/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Kd-Tree, Grade 97/100, https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 */

import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private SET<Point2D> set;

    /**
     * construct an empty set of points
     */
    public PointSET() {
        this.set = new SET<>();
    }

    /**
     * @return true if set is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    /**
     * @return the number of points in the set
     */
    public int size() {
        return this.set.size();
    }

    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param p the point
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        this.set.add(p);
    }

    /**
     * @param p the point
     * @return true if the set contains the given point p, false otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return this.set.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        this.set.forEach(p -> {
//            StdDraw.setPenColor(StdDraw.ORANGE);
//            StdDraw.text(p.x(), p.y() + 0.03, p.toString());
//            StdDraw.setPenColor(StdDraw.GREEN);
//            StdDraw.circle(p.x(), p.y(), 0.009);
            StdDraw.point(p.x(), p.y());
        });
    }

    /**
     * @param rect
     * @return all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        
        ArrayList<Point2D> points =  new ArrayList<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                points.add(p);
            }
        }
        
        return points;
    }

    /**
     * @param p
     * @return a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Point2D nearest = null;
        double min = Double.POSITIVE_INFINITY;
        for (Point2D point : set) {
            double distance = point.distanceSquaredTo(p);
            if (distance < min) {
                nearest = point;
                min = distance;
            }
        }

        return nearest;
    }

    /**
     * unit testing of the methods (optional)
     * 
     * @param args
     */
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        
        Point2D query = new Point2D(0.417, 0.362);
        Point2D res = brute.nearest(query);
        StdOut.println("nearest is " + res);
    }
}
