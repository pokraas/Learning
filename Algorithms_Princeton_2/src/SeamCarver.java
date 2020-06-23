import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import edu.princeton.cs.algs4.Picture;

/**
 * Seam-carving is a content-aware image resizing technique where the image is reduced 
 * in size by one pixel of height (or width) at a time. 
 * A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row; 
 * a horizontal seam is a path of pixels connected from the left to the right with one pixel in each column.
 * @author Alex Pokras
 */
public class SeamCarver {
	private Picture picture;
	/** All pixels (x,y) represented as Nodes */
	private Node[][] nodes;
	private Color[][]colors;
	
	public SeamCarver(Picture picture) {
		if (picture == null) throw new IllegalArgumentException();
		this.picture=picture;
		nodes = new Node[width()][height()];
		colors = new Color[width()][height()];		
		for (int x=0;x<width();x++)
			for (int y=0;y<height();y++)
				nodes[x][y]=new Node(x,y);
	}
	
	public Picture picture() {
		return picture;
	}
	
	public int width() {
		return picture.width();
	}
	
	public int height() {
		return picture.height();
	}
	
	/** Cache colors of each pixel into the Color[] colors */
	private Color get(int x, int y) {
		if (colors[x][y]==null) colors[x][y]=picture.get(x, y);
		return colors[x][y];
	}
	
	/** Compute (a-b)^2 */
	private double differenceSquared(int a, int b) {
		return (a-b)*(a-b);
	}
	
	/** For pixels (x1,y1) and (x2,y2) compute the square of the gradient function (∆^2): <br>
	 *  add squared differences in their R, G, and B values: <br>
	 *  ∆^2 = R^2 + G^2 + B^2 
	 */
	private double deltaSquared(int x1, int y1, int x2, int y2) {
		Color c1 = get(x1, y1);
		Color c2 = get(x2, y2);
		return  differenceSquared(c1.getBlue(),c2.getBlue()) + 
				differenceSquared(c1.getGreen(),c2.getGreen()) + 
				differenceSquared(c1.getRed(),c2.getRed());
	}
	
	/** Calculate the energy of a pixel (x,y) based on a <i>dual-gradient energy function</i>: 
	 * √( ∆x^2 + ∆y^2 )
	 */
	public double energy (int x, int y) {
		if (x<0 || y<0 || x>=width() || y>= height()) throw new IllegalArgumentException();
		
		//big energy for pixels on the edges of the picture
		if (x==0 || y==0 || x==width()-1 || y==height()-1) return 1000;
		//calculate the function
		double deltaXSquared = deltaSquared(x-1,y,x+1,y);
		double deltaYSquared = deltaSquared(x,y-1,x,y+1);
		return Math.sqrt(deltaXSquared + deltaYSquared);
	}
	
	private class Node implements Comparable<Node>{
		int x,y;
		double distance;
		double energy;
		Node parent;
		
		/** Initialize a Node without the distance from the top to its parent*/
		public Node(int x, int y) {
			this.x=x;
			this.y=y;
			this.energy = energy(x,y);
			this.distance = -1; //-1 if the Node has not yet been encountered by the search
		}
		
		/** Nodes adjacent to this, i.e., (x-1,y+1), (x,y+1), (x+1,y+1) in that order */
		public ArrayList<Node> adj() {
			ArrayList<Node> adj = new ArrayList<Node>();
			if (y==height()-1) return adj;
			if (x>0) adj.add(nodes[x-1][y+1]);
			adj.add(nodes[x][y+1]);
			if (x<width()-1) adj.add(nodes[x+1][y+1]);
			return adj;
		}
		
		@Override
		public int compareTo(Node other) {
			return Double.compare(this.distance, other.distance);
		}

		@Override
		public String toString() {
			return "("+x+" "+y+") e"+energy; //: distance "+distance;
		}
	}
	
	/** Sequence of indices (one for a pixel in each row) for the vertical seam */
	public int[] findVerticalSeam() {
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		
		//corner cases
		if (height()==1) {
			if (width()==1) return new int[] {0};
			for (int x=1;x<width()-1;x++) {
				nodes[x][0].distance=nodes[x][0].energy;
				if (nodes[x][0]!=null) pq.add(nodes[x][0]);
			}
			if (pq.isEmpty()) return new int[] {0};
			return new int[] {pq.poll().x};
		}
		
		int[] seam = new int[height()];
		
		
		//add all nodes from the first row to the priority queue
		for (int x=0; x<width(); x++) {
			nodes[x][1].distance = nodes[x][1].energy;
			pq.add(nodes[x][1]);
		}
		
		//Prim's algorithm
		Node curr = pq.poll();
		while (curr.y < height()-1) {
			for (Node adj : curr.adj()) {
				if (adj.distance !=-1 && curr.distance+adj.energy > adj.distance) continue;
				//if found a better way to reach adj
				pq.remove(adj);
				adj.distance = curr.distance+adj.energy;
				adj.parent=curr;
				pq.add(adj);
			}
			curr = pq.poll();
		}

		for (int y=height()-1; curr!=null; y--) {
			seam[y] = curr.x;
			curr=curr.parent;
		}
		
		//choose the pixel from the 0th row to be (x in the 1st row)-1
		int x = seam[1]-1;
		seam[0] = x<0 ? 0:x;
		//choose the pixel from the last row to be (x in the second-to-last row)-1
		x = seam[seam.length-2]-1;
		seam[seam.length-1] = x<0 ? 0:x;
		return seam;
	}

	/** sequence of indices for horizontal seam */
	public int[] findHorizontalSeam() {
		//create newPicture whose pixel (x,y) corresponds to picture's (y,x)
		//(it looks turned clockwise and mirrored at the vertical axis)
		int newHeight = width();
		int newWidth = height();
		Picture newPicture = new Picture(newWidth, newHeight);
		for (int x=0; x<width(); x++)
			for (int y=0; y<height(); y++)
				newPicture.set(y, x, get(x, y));
		//the horizontal seam of picture is the vertical seam of newPicture
		return (new SeamCarver(newPicture)).findVerticalSeam();
	}

	/** remove horizontal seam from current picture */
	public void removeHorizontalSeam(int[] seam) {
		if (seam==null || height()==0 || seam.length<width()) throw new IllegalArgumentException();
		//check that seam is valid (no two entries differ more than by 1)
		for (int i=0; i<seam.length-1; i++) {
			if (Math.abs(seam[i]-seam[i+1]) > 1) throw new IllegalArgumentException();
		}
		
		//copy all pixels except for those on the seam to the new picture
		Picture newPicture = new Picture(width(), height()-1);
		for (int x=0; x<width(); x++)
			for (int y=0; y<height(); y++) {
				if (seam[x]<0 || seam[x]>height()-1) throw new IllegalArgumentException();
				int ny = y>seam[x] ? y-1:y;
				if (ny==height()-1 || ny==-1) continue;
				newPicture.set(x, ny, get(x, y));
			}
		//recalculate the energy of nodes and create a new array of colors
		SeamCarver newSeamCarver = new SeamCarver(newPicture);
		nodes = newSeamCarver.nodes;
		colors = newSeamCarver.colors;
		picture = newSeamCarver.picture;
	}

	/** remove vertical seam from current picture */
	public void removeVerticalSeam(int[] seam) {
		if (seam==null || width()==0 || seam.length<height()) throw new IllegalArgumentException();
		//check that seam is valid (no two entries differ more than by 1)
		for (int i=0; i<seam.length-1; i++) {
			if (Math.abs(seam[i]-seam[i+1]) > 1) throw new IllegalArgumentException();
		}
		
		//copy all pixels except for those on the seam to the new picture
		Picture newPicture = new Picture(width()-1, height());
		for (int y=0; y<height(); y++)
			for (int x=0; x<width(); x++) {
				if (seam[y]<0 || seam[y]>width()-1) throw new IllegalArgumentException();
				int nx = x>seam[y] ? x-1:x;
				if (nx==width()-1 || nx==-1) continue;
				newPicture.set(nx, y, get(x, y));
			}
		//recalculate the energy of nodes and create a new array of colors
		SeamCarver newSeamCarver = new SeamCarver(newPicture);
		nodes = newSeamCarver.nodes;
		colors = newSeamCarver.colors;
		picture = newSeamCarver.picture;
	}
	
	public static void main(String[] args) {
		Picture p3x4 = new Picture("https://coursera.cs.princeton.edu/algs4/assignments/seam/files/6x5.png");
		SeamCarver sc = new SeamCarver(p3x4);
		System.out.printf("Picture %d * %d%n", sc.width(),sc.height());
		System.out.println(sc.picture());

		System.out.print("\n\n");
		int[]seam = new int[]{ 4,4,3,2,1,0 };
		sc.removeHorizontalSeam(seam);
		System.out.println(sc.picture());
		//System.out.println(Arrays.toString(sc.findHorizontalSeam()));
	}
}
