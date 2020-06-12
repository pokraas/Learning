package Week1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import edu.princeton.cs.algs4.In;

public class WordNet_Old {
	
	private ArrayList<Node> nodes = new ArrayList<Node>();

	
	private class Node {
		final int id; //XXX remove after testing to save memory
		private LinkedList<Integer> hypernyms;
		private String[] synsetArray;
		Node(int id, String synset){
			this.id=id;
			nodes.add(this);
			synsetArray=new String[1];
			if (synset.contains(" ")) { 
				synsetArray = synset.split(" ");
			} else {
				synsetArray[0] = synset;
			}

			hypernyms = new LinkedList<Integer>();
		}

		void addHypernym(int hypernym) {
			hypernyms.add(hypernym);
		}
		
		/** Return IDs of adjacent Nodes */
		LinkedList<Integer> adjIDs(){
			return hypernyms;
		}		
		
		/** Return all adjacent nodes */
		LinkedList<Node> adj(){
			LinkedList<Node> ret = new LinkedList<Node>();
			for (int id : hypernyms) {
				ret.add(nodeAt(id));
			}
			return ret;
		}
		
		/** Does this node have adjacent nodes? */
		boolean hasAdj() {
			return !adjIDs().isEmpty();
		}
		
		/** Return this node's synset as String*/
		String synset() {
			StringBuilder sb = new StringBuilder(synsetArray[0]);
			for (int i=1; i<synsetArray.length;i++) {
				sb.append(" ");
				sb.append(synsetArray[i]);
			}
			return sb.toString();
		}
		
		boolean contains(String noun) {
			for (String s : synsetArray) {
				if (s.equals(noun)) return true;
			}
			return false;
		}
				
		@Override
		public String toString() {
			return "Node at "+id+" "+synset();
		}
	}
	
	/** Find a Node by its ID */
	private Node nodeAt(int id) {
		return nodes.get(id);
	}
	
	/** Find all nodes that contain a certain noun */
	private LinkedList<Node> nodeOfNoun (String noun) {
		LinkedList<Node> ret = new LinkedList<Node>();
		for (Node n : nodes) {
			if (n.contains(noun)) ret.add(n);
		}
		return ret;
	}
	
	/** Constructor takes the name of two input files */
	public WordNet_Old(String synsetsFile, String hypernymsFile) {
		if (synsetsFile==null || hypernymsFile==null) throw new IllegalArgumentException();
		In synsetsIn = new In(synsetsFile);
		In hypernymsIn = new In(hypernymsFile);
		
		for (String entry : synsetsIn.readAllLines()) {
			//create a Node for each synset of the WordNet
				String[] line = entry.split(",");
				int id = Integer.parseInt(line[0]);				
				new Node(id,line[1]);
		}
		
		for (String entry : hypernymsIn.readAllLines()) {
			//add hypernyms to each node
			String[] line = entry.split(",");
			int id = Integer.parseInt(line[0]);
			Node n = nodeAt(id);
			for (int i=1;i<line.length;i++) {
				n.addHypernym(Integer.parseInt(line[i]));
			}
		}
	}
	
	/** All WordNet nouns */
	public Iterable<String> nouns() {
		HashSet<String> ret = new HashSet<String>();
		for (Node n : nodes) {
			for (String noun : n.synsetArray) {
				ret.add(noun);
			}
		}
		return ret;
	}
	
	/** Is the word a WordNet noun? */
	public boolean isNoun(String word) {
		return ((HashSet<String>)nouns()).contains(word);
	}
	
	private HashMap<Node, Integer> distancesOfNodes;
	/** Returns an ArrayList where all nodes on a distance D from nodes in the list
	 * are on Dth index */
	private ArrayList<LinkedList<Node>> distanceMap(LinkedList<Node> list) {
		//maps a distance from Node n to Nodes at this distance
		ArrayList<LinkedList<Node>> ret = new ArrayList<LinkedList<Node>>();
		ret.add(0,list); //each node is at distance 0 from itself
		//for DFS from node n
		Queue<Node> q = new LinkedList<Node>();
		q.addAll(list);
		//maps a Node m to its distance from Node n
		distancesOfNodes = new HashMap<Node, Integer>();
		for (Node n : list)	distancesOfNodes.put(n, 0);
		//DFS from node n
		Node m = q.poll();
		while (m.hasAdj()) {
			int currDist=distancesOfNodes.get(m);	//distance to m from n
			//System.out.println(m+" "+currDist);
			currDist++;
			//for each node adjacent to n
			for (Node p : m.adj()) {
				if (distancesOfNodes.containsKey(p)) {	//if exploring an already visited node
					if (distancesOfNodes.get(p)>currDist) distancesOfNodes.put(p, currDist);
					continue;
				}
				distancesOfNodes.put(p, currDist);
				if (ret.size()<=currDist) ret.add(new LinkedList<Node>());
				ret.get(currDist).add(p);
				q.add(p);
			}
			//assert ret.get(currDist).contains(m);

			//next iteration
			m = q.poll();
		}
		return ret;
	}
	
	private int shortestLength; //length of the shortest ancestral path
	/** A synset that is the common ancestor of nounA and nounB in a shortest ancestral path*/
	public String sap (String nounA, String nounB) {
		if (nounA==null || nounB==null) throw new IllegalArgumentException();
		shortestLength = Integer.MAX_VALUE;
		Node ancestor = null; //shortest common ancestor
		LinkedList<Node> a = nodeOfNoun(nounA);
		LinkedList<Node> b = nodeOfNoun(nounB);
		ArrayList<LinkedList<Node>> aMap = distanceMap(a);
//		if (distancesOfNodes.containsKey(NodeInB)) {	//optimization if nounB is an ancestor of nounA
//			shortestLength = distancesOfNodes.get(NodeInB);
//			return NodeInB.synset();
//		}
		distanceMap(b);	//calculate distancesOfNodes from b
		int da=0; //distance to the shortest common ancestor from a
		for (LinkedList<Node> l : aMap) {
			for (Node n : l) {
				if (distancesOfNodes.containsKey(n)) {
					//if found a common ancestor
					int length = da+distancesOfNodes.get(n);
					if (length<shortestLength) {
						//if found a shorter ancestral path
						ancestor=n;
						shortestLength=length;
					}
				}
			}
			da++;
		}
		distancesOfNodes=null; //save memory
		return ancestor.synset();
	}
	
	/** Distance between nounA and nounB (length of the shortest ancestral path) */
	public int distance (String nounA, String nounB) {
		if (nounA==null || nounB==null) throw new IllegalArgumentException();
		sap(nounA,nounB);
		return shortestLength;
	}
	
	public static void main(String[] args) {
		WordNet_Old w = new WordNet_Old("synsets.txt","hypernyms.txt");
		String nounA = args[0];
		String nounB = args[1];
			
		ArrayList<LinkedList<Node>> distanceMap = w.distanceMap(w.nodeOfNoun(nounA));
		for (LinkedList<Node> nodesOnDistance : distanceMap) {
			System.out.println(nodesOnDistance);
		}
		System.out.println("\n");
		ArrayList<LinkedList<Node>> distanceMap2 = w.distanceMap(w.nodeOfNoun(nounB));
		for (LinkedList<Node> nodesOnDistance : distanceMap2) {
			System.out.println(nodesOnDistance);
		}
		//System.out.println(w.distancesOfNodes);
		//System.out.println(w.nodeAt(63072).distanceToParent(w.nodeAt(18840)));
		System.out.println("\n Closest ancestral node: "+w.sap(nounA,nounB)+" - distance "+
				w.distance(nounA, nounB));
		
	}

}
