package Week5;
import java.util.ArrayList;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

/**
 * A brute force implementation to solve the problem of finding 2D points in a certain range
 * and finding a 2D point closest to a query 2D point.
 * @author Alex Pokras
 */
public class PointSET {
	private TreeSet<Point2D> set;
	
	public PointSET() {
		set = new TreeSet<Point2D>();
	}
	
	public boolean isEmpty() {
		return set.isEmpty();
	}
	
	/** number of points in the set*/
	public int size() {
		return set.size();
	}
	
	/**add the point to the set (if it is not already in the set)*/
	public void insert(Point2D p) {
		if (p==null) throw new IllegalArgumentException();
		set.add(p);
	}
	
	/** does the set contain point p?*/
	public boolean contains(Point2D p) {
		if (p==null) throw new IllegalArgumentException();
		return set.contains(p);
	}
	
	/** draw all points to standard draw*/
	public void draw() {
		for (Point2D p:set) {
			p.draw();
		}
	}
	
	/** all points that are inside the rectangle (or on the boundary)*/
	public Iterable<Point2D> range(RectHV rect){
		if (rect==null) throw new IllegalArgumentException();
		ArrayList<Point2D> ret = new ArrayList<Point2D>();
		Point2D lo = new Point2D(rect.xmin(),rect.ymin());	//lower left corner of rect
		Point2D hi = new Point2D(rect.xmax(),rect.ymax());	//upper right corner of rect
		
		for (Point2D p : set.subSet(lo, true, hi, true)) {
			if (p.x()<lo.x() || p.x()>hi.x()) continue;
			if (p.y()<lo.y() || p.y()>hi.y()) continue;			
			ret.add(p);
		}
		return ret;
	}
	
	/** a nearest neighbor in the set to point p; null if the set is empty*/
	public Point2D nearest (Point2D p) {
		if (p==null) throw new IllegalArgumentException();
		if (this.isEmpty()) return null;
		double minDist = Double.POSITIVE_INFINITY;
		Point2D champion = null;
		for (Point2D q : set) {
			double dist = p.distanceTo(q);
			if (dist<minDist) {
				minDist = dist;
				champion = q;
			}
		}
		return champion;
	}
}
