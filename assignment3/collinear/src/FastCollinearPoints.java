import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private LineSegment[] segments;

    /**
     * Finds all line segments containing 4 points.
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("'points' cannot be null!");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("'point' cannot be null!");
        }

        // sort input array by Point's natural order and checks for duplicates
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointsCopy.length - 1; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate points are not allowed!");
            }
        }

        // declare a new array to store points sorted by slope for each origin
        Point[] pointsBySlope;

        // stores our LineSegments temporarily
        List<LineSegment> tmpSegments = new ArrayList<>();

        // set each point as the origin
        for (Point origin : pointsCopy) {
            // sort every other point by slope to origin
            pointsBySlope = pointsCopy.clone();
            Arrays.sort(pointsBySlope, origin.slopeOrder());

            // keep track of where we are in the 
            int cursor = 0;

            while (cursor < pointsBySlope.length) {
                List<Point> tmpPoints = new ArrayList<>();

                // get the slope to the next point, then add this point to stack
                double currSlope = origin.slopeTo(pointsBySlope[cursor]);
                tmpPoints.add(pointsBySlope[cursor++]);

                // keep adding new points as long as slope is the same
                while (cursor < pointsBySlope.length && origin.slopeTo(pointsBySlope[cursor]) == currSlope) {
                    tmpPoints.add(pointsBySlope[cursor++]);
                }

                // when we reach this part, all points with same slope has been added
                // ensure that there are at least 4 points (including origin) and 
                // double counting by only counting LineSegments going upwards
                if (tmpPoints.size() >= 3 && origin.compareTo(tmpPoints.get(0)) < 0) {
                    tmpSegments.add(new LineSegment(origin, tmpPoints.get(tmpPoints.size() - 1)));
                }
            }
        }

        // save LineSegments as class variable
        segments = new LineSegment[tmpSegments.size()];
        segments = tmpSegments.toArray(segments);
    }

    /**
     * Returns the number of line segments that contain 4 collinear points exactly once.
     */
    public int numberOfSegments() {
        return segments.length;
    }

    /**
     * Returns an array of `LineSegment`s that contain 4 collinear points exactly once.
     * Each line segment is unique, and should not contain any sub-segments.
     */
    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {
        
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: java FastCollinearPoints data/input.txt");
        }

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt(); // first int in file is the number of points
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
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
