package Week1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {
	private final Digraph g;
	
	public SAP(Digraph g) {
		this.g = g;
	}
	
	private HashSet<Integer> adj(Iterable<Integer> vertices){
		HashSet<Integer> ret = new HashSet<Integer>();
		for (int v : vertices) {
			for (int w : g.adj(v)) {
				ret.add(w);
			}
		}
		return ret;
	}
	
	/** An automatically resizing 2D list, where many integers can be placed
	 * at one Y-coordinate without regard for their x-coordinate. <br>
	 * If an integer is added lower than it had already been, it will be moved to the lower Y. <br>
	 * If an integer is added higher than it had already been, it won't be inserted. <br>
	 * Thus, the 2D list acts like a PriorityQueue, preferring to add an element at a lowest possible Y.
	 */
	private static class List2D {
		/** A list indexing HashSets of Integers to their y-coordinate */
		private ArrayList<HashSet<Integer>> list = new ArrayList<HashSet<Integer>>();
		/** Maps members to their y-coordinates */
		private HashMap<Integer, Integer> members = new HashMap<Integer, Integer>();
		
		/** Get all integers on a specified y-coordinate: */
		public HashSet<Integer> getAll(int y){
			return list.get(y);
		}
		/** Get the y-coordinate of an element or -1 if element not found (in constant time) */
		public int getY(int element) {
			if (!contains(element)) return -1;
			return members.get(element);
		}
		/** Check if an element is in the list (in constant time) */
		public boolean contains(int element) {
			return members.containsKey(element);
		}
		
		/** Add an integer to a specified y-coordinate in the list or moves it to
		 * min(yBefore, y)
		 */
		public void add(int y, int toAdd) {
			if (contains(toAdd)) {  //if toAdd has already been in the list
				int yBefore = getY(toAdd);
				if (y<yBefore) { //move toAdd from yBefore to y
					list.get(yBefore).remove(toAdd);
				} else return;
			}
			
			members.put(toAdd,y);
			
			if (y>maxY()) { //resize list
				HashSet<Integer> newY = new HashSet<Integer>();
				newY.add(toAdd);
				list.add(newY);
			} else {
				list.get(y).add(toAdd);
			}
		}
		/** Add all integers from toAdd to a specified y-coordinate the list */
		public void add(int y, Iterable<Integer> toAdd) {
			for (Integer i : toAdd) {
				if (i==null) throw new IllegalArgumentException();
				add(y,(int)i);
			}
		}
		
		/** Maximum y-coordinate */
		public int maxY() {
			return list.size()-1;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = maxY();y>=0;y--) {
				sb.append("-"+y+"-");
				sb.append(getAll(y).toString());
				sb.append("\n");
			}
			return sb.toString();
		}
	}
	
	/** finds all ancestors of the vertices in startingSet that are N hops away from them
	 *  and keeps them on y=N position in the List2D */
	private List2D mapAncestors(Iterable<Integer> startingSet) {
		List2D ancestorMap = new List2D();
		ancestorMap.add(0, startingSet);
		int currDist=0;
		HashSet<Integer> curr = adj(startingSet);	//vertices on distance currDist from startingSet

		while (!curr.isEmpty()) {
			//System.out.println("Adding "+curr);
			ancestorMap.add(++currDist, curr);
			//System.out.println(ancestorMap);
			if (currDist>ancestorMap.maxY()) break;
			curr = adj(ancestorMap.getAll(currDist));
		}
		return ancestorMap;
	}
	
	/** length of shortest ancestral path between v and w; -1 if no such path */
	public int length(int v, int w) {
		return length(Arrays.asList(v),Arrays.asList(w));
	}

	/** a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path */
	public int ancestor(int v, int w) {
		return ancestor(Arrays.asList(v),Arrays.asList(w));
	}

	/** length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path */
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		ancestor(v,w);
		return minLength;
	}

	private int minLength;
	/** a common ancestor that participates in shortest ancestral path; -1 if no such path */
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v==null || w==null) throw new IllegalArgumentException();
		List2D a = mapAncestors(v);
		List2D b = mapAncestors(w);
		minLength = Integer.MAX_VALUE;	//a.maxY()+b.maxY()+1;
		int champion = -1; //lowest common ancestor found so far
		
		for (int ay=0; ay<=a.maxY() && ay<=minLength; ay++) {
			for (int vertex : a.getAll(ay)) {
				if (!b.contains(vertex)) continue;
				int length = ay + b.getY(vertex);
				if (length<minLength) {	//if found a lower common ancestor
					minLength = length;
					champion = vertex;
				}
			}
		}
		
		if (minLength == Integer.MAX_VALUE) minLength=-1; 
		return champion;
	}
	
	public static void main(String[] args) {
		Digraph digraph = new Digraph(new In("Week1/graph1"));
		System.out.println(digraph);
		SAP sap = new SAP(digraph);
		ArrayList<Integer> startingSet = new ArrayList<Integer>();
		startingSet.add(1);
		startingSet.add(7);
		//sap.mapAncestors(startingSet);
		System.out.println(sap.ancestor(Arrays.asList(0,4),Arrays.asList(3))+" "+sap.length(0,4));
	}

}
