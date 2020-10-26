/**
 * @author: Adrian Popa, adrian.popa.box@gmail.com, adrian.popa@aepeak.com
 * 
 * Coursera, Algorithms, Part I, Princeton
 * Assignment: Kd-Tree, Grade 97/100, https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 */

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final RectHV XY = new RectHV(0.0, 0.0, 1.0, 1.0);

    private static class Node {
        private Node leftBottom;
        private Node rightUp;
        private Point2D point;
        private RectHV rect;
        private int size;
        private boolean isVerticalCut;

//        private static int gorder;
//        private int order;
//        private boolean isVisited;

        public Node(Node parent, Point2D point, int level, int size) {
            this.point = point;
            this.isVerticalCut = level % 2 == 0;
            this.size = size;
            computeRect(parent, point);

//            order = gorder + 1;
//            gorder++;
        }

        @Override
        public String toString() {
//            return String.format("x=%.2f y=%.2f", point.x(), point.y());
            return String.format("%.2f;%.2f", point.x(), point.y());
        }

//        public String toString2() {
//            return String.format("o=%d, s=%d", order, size);
//        }

        private RectHV leftBottomRect() {
            if (isVerticalCut) {
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            }
        }

        private RectHV rightUpRect() {
            if (isVerticalCut) {
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
        }

        private void computeRect(Node parent, Point2D point) {
            double xmin = XY.xmin(), ymin = XY.ymin(), xmax = XY.xmax(), ymax = XY.ymax();

            if (isVerticalCut) {
                // draw on | x
                if (parent != null) {
                    xmin = parent.rect.xmin();
                    xmax = parent.rect.xmax();

                    if (point.y() >= parent.point.y()) {
                        ymin = parent.point.y();
                        ymax = parent.rect.ymax();
                    } else {
                        ymin = parent.rect.ymin();
                        ymax = parent.point.y();
                    }
                }
            } else {
                if (parent != null) {
                    ymin = parent.rect.ymin();
                    ymax = parent.rect.ymax();

                    if (point.x() >= parent.point.x()) {
                        xmin = parent.point.x();
                        xmax = parent.rect.xmax();
                    } else {
                        xmin = parent.rect.xmin();
                        xmax = parent.point.x();
                    }
                }
            }

            this.rect = new RectHV(xmin, ymin, xmax, ymax);
        }
    }

    private Node root;

    /**
     * construct an empty set of points
     */
    public KdTree() {
        // do nothing
    }

    /**
     * @return true if set is empty, false otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return the number of points in the set
     */
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        } else {
            return node.size;
        }
    }

    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param point the point
     */
    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }

        root = insert(null, root, point, 0);
    }

    /**
     * 1. at the root use the x-coordinate (if the point to be inserted has a
     * smaller x-coordinate than the point at the root, go left; otherwise go
     * right); 2. then at the next level, we use the y-coordinate (if the point to
     * be inserted has a smaller y-coordinate than the point in the node, go left;
     * otherwise go right); 3. then at the next level the x-coordinate, and so
     * forth.
     * 
     * @param node
     * @param point
     * @param level
     */
    private Node insert(Node parent, Node node, Point2D point, int level) {
        if (node == null) {
            node = new Node(parent, point, level, 1);
            return node;
        }

        // the tree already contains this point ?
        if (point.compareTo(node.point) == 0) {
            return node;
        }

        // explore according to level and point value
        if (level % 2 == 0) {
            // use x
            if (point.x() < node.point.x()) {
                // go left
                node.leftBottom = insert(node, node.leftBottom, point, ++level);
            } else {
                // go right
                node.rightUp = insert(node, node.rightUp, point, ++level);
            }
        } else {
            // use y
            if (point.y() < node.point.y()) {
                // go left
                node.leftBottom = insert(node, node.leftBottom, point, ++level);
            } else {
                // go right
                node.rightUp = insert(node, node.rightUp, point, ++level);
            }
        }

        node.size = 1 + size(node.leftBottom) + size(node.rightUp);
        return node;
    }

    /**
     * @param point the point
     * @return true if the set contains the given point p, false otherwise
     */
    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }

        return get(root, point, 0) != null;
    }

    private Node get(Node node, Point2D point, int level) {
        // no node found
        if (node == null) {
            return null;
        }

        // are equal?
        if (point.compareTo(node.point) == 0) {
            return node;
        }

        // explore according to level and point value
        if (level % 2 != 0) {
            // use y
            if (point.y() < node.point.y()) {
                // go left
                return get(node.leftBottom, point, ++level);
            } else {
                // go right
                return get(node.rightUp, point, ++level);
            }
        } else {
            // use x
            if (point.x() < node.point.x()) {
                // go left
                return get(node.leftBottom, point, ++level);
            } else {
                // go right
                return get(node.rightUp, point, ++level);
            }
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        draw(new Point2D(XY.xmin(), XY.ymax()), root);
    }

    private void draw(Point2D parent, Node node) {
        if (node == null) {
            return;
        }

        Point2D p = node.point;
        Color color = node.isVerticalCut ? new Color(255, 102, 102) : new Color(51, 204, 255);

        double x0, y0, x1, y1 = 0.0;
        if (node.isVerticalCut) {
            // draw on |
            x0 = node.point.x();
            y0 = parent.y();
            x1 = node.point.x();
            y1 = node.point.y() < parent.y() ? node.rect.ymin() : node.rect.ymax();
        } else {
            // draw on --
            x0 = parent.x();
            y0 = node.point.y();
            x1 = node.point.x() < parent.x() ? node.rect.xmin() : node.rect.xmax();
            y1 = node.point.y();
        }

        // draw
        {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(color);
            StdDraw.line(x0, y0, x1, y1);

            // draw the bounding rectangle
            double x, y, hw, hh;
            x = node.rect.xmin() + (node.rect.xmax() - node.rect.xmin()) / 2;
            y = node.rect.ymin() + (node.rect.ymax() - node.rect.ymin()) / 2;
            hw = node.rect.width() / 2.0;
            hh = node.rect.height() / 2.0;
            StdDraw.rectangle(x, y, hw, hh);

            StdDraw.setFont(new Font("Verdana", Font.PLAIN, 9));
            if (node.isVerticalCut) {
                StdDraw.text(p.x(), p.y() + 0.02, node.toString()); // p2s(p)
            } else {
                StdDraw.text(p.x(), p.y() - 0.02, node.toString()); // p2s(p)
            }

            StdDraw.setPenColor(Color.LIGHT_GRAY);
            StdDraw.setFont(new Font("Verdana", Font.BOLD, 10));
//            StdDraw.text(p.x() + 0.035, p.y(), String.format("%d %s", node.order, node.isVerticalCut ? "V" : "H"));
//            StdDraw.text(p.x(), p.y() - 0.03, node.toString2()); // p2s(p)

//            if (node.isVisited) {
//                StdDraw.setPenColor(new Color(204, 204, 204));
//                StdDraw.filledCircle(p.x(), p.y(), 0.015);
//            } else {
//                StdDraw.setPenColor(StdDraw.BLACK);
//                StdDraw.filledCircle(p.x(), p.y(), 0.007);
//            }
        }

        Point2D parentPoint = new Point2D(node.point.x(), node.point.y());
        draw(parentPoint, node.leftBottom);
        draw(parentPoint, node.rightUp);
    }

    /**
     * @param rect
     * @return all points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        range(root, XY, rect, points);
        return points;
    }

    private void range(Node node, RectHV partialRect, RectHV queryRect, ArrayList<Point2D> result) {
        if (node == null) {
            return;
        }

        if (queryRect.intersects(node.rect)) {
            if (queryRect.contains(node.point)) {
                result.add(new Point2D(node.point.x(), node.point.y()));
            }

            range(node.leftBottom, node.leftBottomRect(), queryRect, result);
            range(node.rightUp, node.rightUpRect(), queryRect, result);
        }
    }

    /**
     * @param querypoint
     * @return a nearest neighbor in the set to point qpoint; null if the set is
     *         empty
     */
    public Point2D nearest(Point2D querypoint) {
        if (querypoint == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }

//        resetVisited(root);
        Node n = nearest(root, root, querypoint, 0, new Champion());
//        StdOut.println(String.format("champion is n: %s", n));

        return n.point;
    }

//    private void resetVisited(Node node) {
//        if (node == null) {
//            return;
//        }
//
////        node.isVisited = false;
//        resetVisited(node.leftBottom);
//        resetVisited(node.rightUp);
//    }

    private static class Champion {
//        Node parent = null;
        Node node = null;
        double distance = Double.POSITIVE_INFINITY;
    }

    private Node nearest(Node parent, Node n, Point2D q, int level, Champion c) {
        if (n == null) {
            // draw
//            {
//                StdDraw.setPenRadius(0.004);
//                StdDraw.setPenColor(StdDraw.MAGENTA);
//                StdDraw.line(q.x(), q.y(), c.node.point.x(), c.node.point.y());
//            }
//            {
//                Point2D minPoint = !c.node.isVerticalCut //
//                        ? new Point2D(q.x(), c.node.point.y())
//                        : new Point2D(c.node.point.x(), q.y());
//
//                StdDraw.setPenRadius(0.004);
//                StdDraw.setPenColor(StdDraw.MAGENTA);
//                StdDraw.line(q.x(), q.y(), minPoint.x(), minPoint.y());
//            }

            return c.node;
        }

        // for debugging
//        n.isVisited = true;

        double distance = n.point.distanceSquaredTo(q);
//        StdOut.println(String.format("evaluate %s, d = %.2f", n, distance));

        if (distance < c.distance) {
            c.distance = distance;
            c.node = n;
//            c.parent = parent;
//            StdOut.println(String.format("* new champ %s, d = %.2f", c.node, c.distance));
        }

        Node good = null;
        Node bad = null;
        // now where to explore next ? left or right | ? up or down -- ?
        if (n.isVerticalCut) {
            // left
            if (q.x() < n.point.x()) {
//                StdOut.println(String.format("\u2190 go left to good %s", n.leftBottom));
                good = n.leftBottom;
                bad = n.rightUp;
            }
            // right
            else {
//                StdOut.println(String.format("\u2193 go right to good %s", n.rightUp));
                good = n.rightUp;
                bad = n.leftBottom;
            }
        } else {
            // up
            if (q.y() > n.point.y()) {
//                StdOut.println(String.format("\u2191 go up to good %s", n.rightUp));
                good = n.rightUp;
                bad = n.leftBottom;
            }
            // down
            else {
//                StdOut.println(String.format("\u2192 go bottom to good %s", n.leftBottom));
                good = n.leftBottom;
                bad = n.rightUp;
            }
        }

        c.node = nearest(n, good, q, ++level, c);

        // we have a champion! is it global or local?
        // is global
        // if the radius of the circle between the champion and
        // the query point does not intersect with other cuts
        // otherwise
        // might be just local

        // does it make sense to explore the bad side ?
        {
//            double mdist = n.rect.distanceTo(q);
            Point2D m = !n.isVerticalCut //
                    ? new Point2D(q.x(), n.point.y())
                    : new Point2D(n.point.x(), q.y());
            double mdist = m.distanceSquaredTo(q);

            if (c.distance > mdist) {
                c.node = nearest(n, bad, q, ++level, c);
            } 
//            else {
//                StdOut.println(String.format("PRUNE %s, d = %.2f", bad, mdist));
//            }
        }

        return c.node;
    }

    /**
     * unit testing of the methods (optional)
     * 
     * @param args
     */
    public static void main(String[] args) {

    }
}
