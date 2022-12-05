import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private Point2D nearestPoint; // for use in the `nearest()` method

    private static class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private final boolean isVertical;
        private RectHV rect; // axis-aligned rect that encloses all points in its subtree

        // constructor
        private Node(Point2D point, boolean isVertical, Node prev) {
            this.point = point;
            this.left = null;
            this.right = null;
            this.isVertical = isVertical;

            // based on the parent node and split direction, create a new rect to represent the node
            // if root node, rect is the entire unit square
            if (prev == null) {
                this.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            } else {
                // copy over values of parent's rect
                double xmin = prev.rect.xmin();
                double ymin = prev.rect.ymin();
                double xmax = prev.rect.xmax();
                double ymax = prev.rect.ymax();

                // compare parent's point with current point
                int cmp = prev.compareTo(this.point);
                if (prev.isVertical) {
                    // parent splits vertically (comparing x-coords)
                    if (cmp > 0) {
                        // parent lies to right of curr point, we can shift the right limit of curr rect (rect shifts left)
                        xmax = prev.point.x();
                    } else {
                        // parent lies to left of curr point, we can shift the left limit of curr rect (rect shifts right)
                        xmin = prev.point.x();
                    }
                } else {
                    // parent splits horizontally (comparing y-coords)
                    if (cmp > 0) {
                        // parent lies above curr point, we can shift the top limit of curr rect (rect shifts down)
                        ymax = prev.point.y();
                    } else {
                        // parent lies below curr point, we can shift the bottom limit of curr rect (rect shifts up)
                        ymin = prev.point.y();
                    } 
                }

                this.rect = new RectHV(xmin, ymin, xmax, ymax);
            }
        }

        // compare existing node to new point based on current node's split direction
        private int compareTo(Point2D that) {
            // vertical split (compare x-coords)
            if (this.isVertical) {
                return Double.compare(this.point.x(), that.x());
            }

            // horizontal split (compare y-coords)
            else {
                return Double.compare(this.point.y(), that.y());
            }
        }

        // draws the entire tree, starting from root
        private void draw() {

            // draw vertical/horizontal splitting line
            // vertical splits are red, horizontal splits are blue
            StdDraw.setPenRadius(0.005);
            if (this.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(this.point.x(), this.rect.ymin(), this.point.x(), this.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(this.rect.xmin(), this.point.y(), this.rect.xmax(), this.point.y());
            }

            // draw point
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();

            // draw rest of tree recursively
            if (this.left != null) {
                this.left.draw();
            }
            if (this.right != null) {
                this.right.draw();
            }
        }
    }

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.root == null;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already inside)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point p cannot be null!");

        // insert the point by starting and comparing from the root
        if (root == null || !contains(p)) {
            size++;
            root = insert(root, p, true, null);
        }
    }

    private Node insert(Node curr, Point2D p, boolean isVertical, Node prev) {
        // if we have reached an empty node, return the new node to call stack (exit recursion)
        if (curr == null) {
            return new Node(p, isVertical, prev);
        }

        // compare current node's point with new point, based on current node's split direction
        int cmp = curr.compareTo(p);
        if (cmp < 0) {
            // point is less than current node's, pass on inserting to left subarray
            curr.left = insert(curr.left, p, !isVertical, curr);
        } else {
            // point is more than current node's, pass on inserting to right subarray
            curr.right = insert(curr.right, p, !isVertical, curr);
        }
        
        return curr;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point p cannot be null!");

        // begin search from root of node
        return get(root, p) == p;
    }

    private Point2D get(Node curr, Point2D p) {
        // if we reach the end of tree, point not found; return null
        if (curr == null) {
            return null;
        }

        // if we found the point, return it
        if (curr.point.equals(p)) {
            return curr.point;
        }

        // compare the node to the point, based on current node's split direction
        int cmp = curr.compareTo(p);
        if (cmp < 0) {
            return get(curr.left, p);
        } else if (cmp > 0) {
            return get(curr.right, p);
        }

        return null;
    }

    // draw all points to standard draw
    public void draw() {
        if (!isEmpty()) {
            root.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect cannot be null!");

        List<Point2D> pointsInside = new ArrayList<>();
        range(root, rect, pointsInside);
        return pointsInside;
    }

    private void range(Node curr, RectHV rect, List<Point2D> pointsInside) {
        // end of tree, return
        if (curr == null) {
            return;
        }

        // a subtree is only searched if the query rect intersects the node's rect
        // if no intersection, subtree is pruned
        if (rect.intersects(curr.rect)) {
            if (rect.contains(curr.point)) {
                pointsInside.add(curr.point);
            }
            range(curr.left, rect, pointsInside);
            range(curr.right, rect, pointsInside);
        }
    }
    
    // a nearest neighbor in the set to point p; null if empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point p cannot be null!");
        if (isEmpty()) return null;

        nearestPoint = root.point;
        nearest(root, p);
        return nearestPoint;
    }

    private void nearest(Node curr, Point2D p) {
        // end of tree, end of search of current subtree, return to call stack
        if (curr == null) {
            return;
        }

        // for any reason where nearestPoint may be null, reset to current
        if (nearestPoint == null) {
            nearestPoint = curr.point;
        }

        // only explore the node and its subtrees if the node's rect is closer to point p
        // than to the previous closest point
        if (curr.rect.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
            // update nearest point if current point is closer to point p
            if (curr.point.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                nearestPoint = curr.point;
            }

            /**
             * recursively explore subtrees, starting with the subtree that is on the same side 
             * of the splitting line as point p 
             * (may allow pruning of second subtree if a closer point is found in first subtree)
            */
            if (curr.compareTo(p) > 0) {
                // p is closer to left/top of current point so search the left subtree first
                nearest(curr.left, p);
                nearest(curr.right, p);
            } else {
                // p is closer to right/bottom of current point so search the right subtree first
                nearest(curr.right, p);
                nearest(curr.left, p);
            }
        }
    }

    // unit testing
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Usage: java KdTree data/input.txt");

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        // test various methods
        kdtree.draw();
    }
}
