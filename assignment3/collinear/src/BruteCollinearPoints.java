import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    /**
     * Finds all line segments containing 4 points.
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("'points' cannot be null!");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("'point' cannot be null!");
        }
        
        // sort input array by Point's natural order and checks for duplicates
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy); // this guarantees we always start from the bottom of the line segment
        for (int i = 0; i < pointsCopy.length - 1; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate points are not allowed!");
            }
        }
        
        // stores our LineSegments temporarily
        List<LineSegment> tmpSegments = new ArrayList<>();

        // for each point, iterate through every other possible point to check if collinear
        for (int i = 0; i < pointsCopy.length - 3; i++) {
            final Point p1 = pointsCopy[i];
            for (int j = i + 1; j < pointsCopy.length - 2; j++) {
                final Point p2 = pointsCopy[j];
                for (int k = j + 1; k < pointsCopy.length - 1; k++) {
                    final Point p3 = pointsCopy[k];
                    for (int m = k + 1; m < pointsCopy.length; m++) {
                        final Point p4 = pointsCopy[m];

                        if (p1.slopeTo(p2) == p1.slopeTo(p3) && p1.slopeTo(p2) == p1.slopeTo(p4)) {
                            tmpSegments.add(new LineSegment(p1, p4));
                        }
                    }
                }
            }
        }

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
            throw new IllegalArgumentException("Usage: java BruteCollinearPoints data/input.txt");
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
