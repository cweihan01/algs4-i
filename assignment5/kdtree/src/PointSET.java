import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> points;

    // construct empty set of points
    public PointSET() {
        this.points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already inside)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point p cannot be null!");
        points.add(p);
    }

    // does the set contains the point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point p cannot be null!");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect cannot be null!");

        final List<Point2D> pointsInside = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                pointsInside.add(p);
            }
        }
        return pointsInside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point p cannot be null!");
        if (isEmpty()) return null;

        Point2D nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (Point2D currPoint : points) {
            double currDistance = currPoint.distanceSquaredTo(p);
            if (currDistance < nearestDistance) {
                // override the nearest point if a new nearest distance is found
                nearestPoint = currPoint;
                nearestDistance = currDistance;
            }
        }
        return nearestPoint;
    }

    // unit testing
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Usage: java PointSET data/input.txt");

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // test various methods
        brute.draw();
    }
}
