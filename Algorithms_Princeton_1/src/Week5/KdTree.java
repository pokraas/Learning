package Week5;
import java.util.ArrayList;
import java.util.Comparator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A Kd-Tree implementation to solve the problem of finding 2D points in a certain range
 * and finding a 2D point closest to a query 2D point.
 * <br>
 * In a Kd-Tree, each node divides the K-dimensional space into two parts based on the node's Kth-dimension
 * coordinate.<br>
 * This is a 2d-tree, so the nodes divide the 2-dimensional plane vertically and horizontally in an alternating
 * manner, <br> using the node's x- and y-coordinate respectively. The draw() method visualizes how it's done.
 * @author Alex Pokras
 */
public class KdTree {
	private static final boolean HORIZONTAL = true;
	private static final boolean VERTICAL = false;
	
	private class Node {
		final Point2D p;
		final boolean orientation;
		Node left,right;
		RectHV rect;	//part of the plane containing this node
		
		private Node(Point2D p, boolean orientation, RectHV rect){
			this.p = p;
			this.orientation = orientation;
			this.rect=rect;
		}
		Double x(){
			return p.x();
		}
		Double y() {
			return p.y();
		}
		@Override
		public String toString() {
			return x()+"_"+y();
		}
		
		/** Draw this Node's left and right children and their lines*/
		public void drawChildren() {
			StdDraw.setPenRadius(0.003);
			if(orientation==HORIZONTAL) {
				//draw vertical lines for both children
				StdDraw.setPenColor(StdDraw.RED);
				if(left!=null) StdDraw.line(left.x(), rect.ymin(), left.x(), y());
				if(right!=null) StdDraw.line(right.x(), y(), right.x(), rect.ymax());
			} else {
				//draw horizontal lines for both children
				StdDraw.setPenColor(StdDraw.BLUE);
				if(left!=null) StdDraw.line(rect.xmin(), left.y(), x(), left.y());
				if(right!=null) StdDraw.line(x(), right.y(), rect.xmax(), right.y());
			}
			
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.01);
			if (left!=null) {
				left.p.draw();
				left.drawChildren();
			}
			if (right!=null) {
				right.p.draw();
				right.drawChildren();
			}
		}
	}
	
	private Node root;
	private int size;
	
	public KdTree() {
		size=0;
	}
	
	public boolean isEmpty() {
		return root==null;
	}
	
	/** is the point q to be found in the left subtree of Node n? */
	private boolean isLeft(Node n, Point2D q) {
		if (n==null || q==null) throw new IllegalArgumentException();
		Comparator<Point2D> comp = n.orientation==VERTICAL ? Point2D.X_ORDER : Point2D.Y_ORDER;
		return comp.compare(q,n.p)<0;
	}
	
	/** find the child of the Node n whose rectangle contains the Point q <br>
	 * (whose tree might contain the Point2D q) */
	private Node child(Node n, Point2D q) {
		return isLeft(n,q) ? n.left:n.right;
	}
	
	/** find the child of the Node n who is not n.child <br>
	 * (whose tree does not contain the Point2D q	 */
	private Node secondChild(Node n, Point2D q) {
		return isLeft(n,q) ? n.right:n.left;
	}
	
	/**add the point to the set (if it is not already in the set)*/
	public void insert(Point2D q) {
		if (q==null) throw new IllegalArgumentException();
		if (this.contains(q)) return; //don't insert an already inserted point
		size++;
		if (root==null) {
			root = new Node(q,VERTICAL, new RectHV(0,0,1,1));
			return;
		}
		insert(root,q);
	}
	
	/**
	 * Recursive insertion function
	 * @param p Point2D to be inserted
	 * @param curr Node after which to insert 
	*/
	private void insert(Node curr, Point2D q) {
		Node next = child(curr,q);
		
		if (next==null) {
			//create a rectangle for the new node
			double  xmin=curr.rect.xmin(),
					xmax=curr.rect.xmax(),
					ymin=curr.rect.ymin(),
					ymax=curr.rect.ymax();
			if (curr.orientation==VERTICAL) {
				if (isLeft(curr, q)) {
					//new node is in curr's left rectangle
					xmax=curr.x();
				} else {
					//new node is in curr's right rectangle
					xmin=curr.x();
				}
			} else {
				if (isLeft(curr, q)) {
					//new node is in curr's bottom rectangle
					ymax=curr.y();
				} else {
					//new node is in curr's top rectangle
					ymin=curr.y();
				}
			}
			//create the new node
			next = new Node(q,!curr.orientation,new RectHV(xmin,ymin,xmax,ymax));
			if (isLeft(curr,q)) curr.left=next;
			else curr.right=next;
			
		} else {
			//insert after the next node (curr's child)
			insert(next,q);
		}
	}
	
	/** draw all points and the divided plane to standard draw,
	 * calls the recursive method Node.drawChildren()*/
	public void draw() {
		if (this.isEmpty()) return;
		//draw the root
		StdDraw.setPenRadius(0.003);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.line(root.x(), 0, root.x(), 1);
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		root.p.draw();
		//call the recursive method Node.drawChildren() to draw the whole tree
		root.drawChildren();
	}
	
	/** does the set contain point p?*/
	public boolean contains(Point2D q) {
		if (q==null) throw new IllegalArgumentException();
		if (this.isEmpty()) return false;
		return contains(root,q);
	}
	
	/** Recursive search function*/
	private boolean contains (Node n, Point2D q) {
		if (n==null) return false;
		if (n.p.equals(q)) return true;
		return contains(child(n,q), q);
	}
	
	/** number of points in the set*/
	public int size() {
		return size;
	}
	
	/** all points that are inside the rectangle (or on the boundary); null if the set is empty*/
	public Iterable<Point2D> range(RectHV rect){
		if (rect==null) throw new IllegalArgumentException();
		if (this.isEmpty()) return null;
		ArrayList<Point2D> pointsInRectangle = new ArrayList<Point2D>();
		range(root,rect, pointsInRectangle);
		return pointsInRectangle;
	}
	
	/** Recursive range search function based on the following pruning rule:<br>
	 * if the node's rectangle does not intersect with the query rectangle, 
	 * there is no need to explore that node.*/
	private void range(Node n, RectHV rect, ArrayList<Point2D> pointsInRectangle) {
		//add a new point to pointsInRectangle
		if (rect.contains(n.p)) pointsInRectangle.add(n.p);
		//search in the left subtree of n
		if (n.left != null) if (n.left.rect.intersects(rect)) range(n.left,rect, pointsInRectangle);
		//search in the right subtree of n
		if (n.right != null) if (n.right.rect.intersects(rect)) range(n.right,rect, pointsInRectangle);
	}
	
	private Point2D champion;
	/** a nearest neighbor in the set to point p; null if the set is empty*/
	public Point2D nearest (Point2D q) {
		if (q==null) throw new IllegalArgumentException();
		if (this.isEmpty()) return null;
		champion = root.p; //closest point to the query point found so far
		nearest(root,q);
		return champion;
	}
	
	/** Recursive nearest neighbor search function based on the following pruning rules:<br>
	 * 1. A node whose rectangle contains the query point is searched first.<br>
	 * 2. If a node's rectangle is further away from the query point than the champion,
	 * there is no need to explore that node.*/
	private void nearest(Node n, Point2D q) {
//TODO		System.out.println("Searching for "+q+" from "+n+", champion: "+champion);
		//if found a point equal to the query point
		if (n.p.equals(q)) {
			champion = n.p;
			return;
		}
		//compare the node's point to champion
		if (n.p.distanceTo(q)<champion.distanceTo(q)) champion=n.p;
		//search for nearest point
		Node child = child(n,q); //a child of n whose rectangle contains q
		Node secondChild = secondChild(n,q); //a child of n whose rectangle does not contain q
//TODO		System.out.println(child+" "+secondChild);
		if (child!=null) {
			//1st pruning rule
			nearest(child,q);
		}
		if (secondChild!=null) {
			//2nd pruning rule
			if (champion.distanceTo(q)>secondChild.rect.distanceTo(q)) nearest(secondChild,q);
		}
	}

	public static void main(String[] args) {
		KdTree tree = new KdTree();
		tree.insert(new Point2D(0.5,0.5));
		tree.insert(new Point2D(0.6,0.6));
		tree.insert(new Point2D(0.4,0.7));
		tree.insert(new Point2D(0.4,0.2));
		
//		System.out.println(tree.root.left.left);
//		System.out.println(tree.root.left.right);
//		System.out.println(tree.contains(new Point2D(0.4,0.2)));
		System.out.println(tree.nearest(new Point2D(1,1)));
		tree.draw();
		
	}
}
