package Week3;
import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdDraw;
/**
 * A class to find line segments formed by 4 or more collinear points.
 * Uses an algorithm based on sorting in O(n^2*log(n)) time.
 * @author alexanderpokras
 *
 */
public class FastCollinearPoints {
	 private ArrayList<LineSegment> segments;
	 private int nSegm;
	
	 private class PointPair {
		 Point p,q;
		 PointPair(Point p, Point q){
			 this.p=p;
			 this.q=q;
		 }
		 
		 @Override
		 public boolean equals (Object other) {
			 if (other.getClass()!=this.getClass()) return false;
			 return ((PointPair)other).p==this.p && ((PointPair)other).q==this.q; 
		 }		 
		 @Override
		 public String toString() {
			 return p+"---"+q;
		 }
	 }
	 
	 public FastCollinearPoints(Point[] array) {
		 if (array==null) throw new IllegalArgumentException();
		 Point[] points = array.clone();
		 for (Point p:points) {
			 if (p==null) throw new IllegalArgumentException();
		 }
		 Arrays.sort(points);
		 
		 segments = new ArrayList<LineSegment>();
		 nSegm=0;
		 
		 ArrayList<Point> starts = new ArrayList<Point>();
		 ArrayList<Point> ends = new ArrayList<Point>();
		 
		 ArrayList<PointPair> pairs = new ArrayList<PointPair>();
		 
		 for (int i=0;i<points.length;i++) {
			 Point p0 = points[i];
			 
			 Point[] aux = points.clone();
			 Arrays.sort(aux,p0.slopeOrder());
			 //System.out.println(p0+" -> "+Arrays.toString(aux));
			 
			 int pointsOnSegment=2; //p0 and curr form a segment of 2 points
			 Point start=p0;	//start of segment
			 Point end=p0;	//end of segment
			 //aux[0] is always p0, because p0.slope(p0) = -Infinity
			 for (int j=1;j<=aux.length;j++) {
				 Point curr = j<aux.length ? aux[j] : p0;
				 double slope = p0.slopeTo(curr);
				 
				 Point prev = aux[j-1];
				 double prevslope = p0.slopeTo(prev);
				 //if two different points have the same coordinates
				 if (slope==Double.NEGATIVE_INFINITY && j<aux.length) 
					     throw new IllegalArgumentException(p0.toString()+curr.toString());
				 
				 if (slope==prevslope) {
					 start=min(p0,curr,start);
					 end=max(p0,curr,end);	//TODO delete third argument
					 pointsOnSegment++;
				 } else {	//if segment from p0 to prev has ended
					 if (start!=end && pointsOnSegment>3) {	//if found a new 4-point or longer segment
						 //System.out.println("Ended segment: "+start+end);
						 pairs.add(new PointPair(start,end));
					 }
					 pointsOnSegment=2;
					 start=min(p0,curr);
					 end=min(p0,curr);
				 }
			 }
		 }
		 
		 //throwing away repeating segments from allSegments
		 ArrayList<PointPair> cleanPairs = new ArrayList<PointPair>();
		 outer:
		 for (PointPair pair : pairs) {
			 for (PointPair cleanPair : cleanPairs) {
				 if (cleanPair.equals(pair)) continue outer;
			 }
			 cleanPairs.add(pair);
		 }
		 for (PointPair cp : cleanPairs) {
			 segments.add(new LineSegment(cp.p,cp.q));
			 nSegm++;
		 }
	 }
	 
	 private Point min (Point a, Point b) {
		 return less(a,b) ? a : b;
	 }
	 
	 private Point min (Point a, Point b, Point c) {
		 return min(min(a,b),c);
	 }
	 
	 private Point max (Point a, Point b) {
		 return less(a,b) ? b : a;
	 }
	 
	 private Point max (Point a, Point b, Point c) {
		 return max(max(a,b),c);
	 }
	 
	 private boolean less(Point a, Point b) {
		 return a.compareTo(b)<0;
	 }
	 
	 public int numberOfSegments() { // the number of line segments	
		 return nSegm;
	 }
     public LineSegment[] segments() { // the line segments
  	   return segments.toArray(new LineSegment[0]);
     }
     public static void main(String[] args) {
    	 StdDraw.setScale(-5,6);
     	//StdDraw.setPenRadius(0.005);

     	StdDraw.line(-20, 0, 20, 0);
         StdDraw.line(0, -20, 0, 20);
     	StdDraw.setPenRadius(0.01);
         StdDraw.setPenColor(StdDraw.BLUE); 

     	Point[] points = new Point[] {
     			
     			new Point(2,5),
     			new Point(1,2),
     			new Point(-1,-2),
     			new Point(2,4),
     			new Point(0,0),
     			new Point(-2,-4),
     			new Point(3,6),
     			new Point(-1,1),
     			new Point(-2,2),
     			
     			new Point(1,-1),
     			new Point(1,0),
     			new Point(1,-4),
     			new Point(1,5),
     	};
        //System.out.println();
     	for (Point p:points) p.draw();	

   	    FastCollinearPoints fcp = new FastCollinearPoints(points);
     	LineSegment[] arr = fcp.segments();
     	
     	//System.out.println(Arrays.toString(arr));
     	//System.out.println(arr.length);
     	StdDraw.setPenRadius(0.005);
     	StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
     	for (LineSegment ls : arr) {
     		ls.draw();
     	}
     	
	 }

}
