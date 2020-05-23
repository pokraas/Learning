package Week3;
import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdDraw;
/**
 * A class to find line segments formed by exactly 4 collinear points.
 * Uses brute-force algorithm in O(n^4) time.
 * @author alexanderpokras
 *
 */
public class BruteCollinearPoints {
	private ArrayList<LineSegment> segments;
	private int nSegm;
   public BruteCollinearPoints(Point[] array) {
	   if (array==null) throw new IllegalArgumentException();
	   for (Point p : array) {
		   if (p==null) throw new IllegalArgumentException();
	   }
	   Point[] points = array.clone();
	   
	   Arrays.sort(points);
	   segments = new ArrayList<LineSegment>(); 
	   nSegm=0;
	   	   
	   // finds all line segments containing 4 points
	   for (int i0=0;i0<points.length;i0++) {
		   Point p0 = points[i0];
		   if (p0==null) throw new IllegalArgumentException(); 
		   
		   for (int i1=i0+1;i1<points.length;i1++) {
			   Point p1 = points[i1];
			
			   double s1 = p0.slopeTo(p1);
			   //if two different points have the same coordinates
			   if (s1==Double.NEGATIVE_INFINITY) 
				   		throw new IllegalArgumentException(p0.toString()+p1.toString());
			   
			   for (int i2=i1+1;i2<points.length;i2++) {
				   Point p2 = points[i2];
				   
				   double s2 = p1.slopeTo(p2);
				   if (s2!=s1) continue;
				   
				   for (int i3=i2+1;i3<points.length;i3++) {
					   Point p3 = points[i3];
					   
					   double s3 = p2.slopeTo(p3);
					   if (s1==s3 && s1==s2) {
						   //System.out.println("From "+p0+" to "+p1+s1+"  "+p2+s2+"  "+p3+s3);
						   segments.add(new LineSegment(p0,p3));
						   nSegm++;
					   }
				   }
			   }
		   }
	   }
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
    			new Point(3,6),
    	};
    	for (Point p:points) p.draw();
    	
    	LineSegment[] arr = new BruteCollinearPoints(points).segments();
    	//System.out.println(Arrays.toString(arr));
    	//System.out.println(arr.length);
    	StdDraw.setPenRadius(0.005);
    	StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
    	for (LineSegment ls : arr) {
    		ls.draw();
    	}
	}

}
