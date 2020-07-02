package Week3;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import edu.princeton.cs.algs4.In;

/**
 * A class to determine which teams have been mathematically eliminated from winning their division,
 * given the standings in a sports division at some point during the season.
 * A team is mathematically eliminated if it cannot possibly finish the season in (or tied for) first place.
 * Assumption: no games end in a tie or are cancelled.
 * @author Alex Pokras
 */
public class BaseballElimination {
	/** 
	 * A class to represent a flow edge from one vertex to another with a predetermined capacity and a 
	 * changeable flow <= capacity. <br>
	 * <i>Residual flow</i> can be added in the direction of the edge (from-->to) increasing the flow (loading the edge)
	 * or in the opposite direction, decreasing the flow (unloading the edge).
	 * @author Alex Pokras
	 */
	private class FlowEdge {
		final int from, to;
		final double capacity;
		/** flow on the edge from-->to */
		private double flow;
		
		public FlowEdge (int from, int to, double capacity){
			this.from=from;
			this.to=to;
			this.capacity=capacity; //<0 ? 0 : capacity;
		}
		
		/** Returns a vertex other than the parameter to which the edge is connected */
		public int other(int v) {
			if (v==from) return to;
			if (v==to) return from;
			throw new IllegalArgumentException();
		}
		
		/** Increase flow towards <i>to</i> or decrease flow towards <i>from</i> by delta */
		public void addResidualFlowTo(int v, double delta) {
			if (v==to) flow+=delta;
			if (v==from) flow-=delta;
		}
		
		/** How much more flow can be added towards <i>to</i> or removed towards <i>from</i> */
		public double residualCapacityTo(int v) {
			if (v==to) return capacity-flow;
			if (v==from) return flow;
			throw new IllegalArgumentException();
		}
		
		@Override
		public String toString() {
			int flow = (int)this.flow;
			int capacity = this.capacity==Double.POSITIVE_INFINITY? Integer.MAX_VALUE : (int)this.capacity;
			return from+"---"+ (capacity==Integer.MAX_VALUE?"-"+flow+"-" : flow+"/"+capacity) +"-->"+to;
		}
	}
	
	private HashMap<String,  Integer> teams; //maps teams' names to their numbers
	private final int[] w,l,r; //wins, losses and remaining games of each team
	private final int[][] g; //g[i][j] - how many more games the ith team has to play against the jth team 
	private final int n; //number of teams
	private final int source, sink; //source and sink vertices
	
	/** Parse the file with the game schedule */
	public BaseballElimination (String filename) {
		In in = new In(filename);
		n = in.readInt();
		//Initialize data structures
		source=n;
		sink=n+1;
				
		teams = new HashMap<String, Integer>(n);
		w = new int[n];
		l = new int[n];
		r = new int[n];
		g = new int[n][n];
		
		//Parse the data from the file
		in.readLine();
		for (int i=0;i<n;i++) {
			String[] line = in.readLine().split("\\s+");
			if (line[0].equals("")) line = Arrays.copyOfRange(line,1,line.length);
			teams.put(line[0], i);
			w[i]=Integer.parseInt(line[1]);
			l[i]=Integer.parseInt(line[2]);
			r[i]=Integer.parseInt(line[3]);
			for (int j=4; j<n+4;j++) {
				g[i][j-4]=Integer.parseInt(line[j]);
			}
		}
		in.close();
	}
	
	public int numberOfTeams() {
		return n;
	}
	
	public Iterable<String> teams(){
		return teams.keySet();
	}
	
	public int wins (String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		return w[teams.get(team)];
	}
	
	public int losses (String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		return l[teams.get(team)];
	}
	
	public int remaining (String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		return r[teams.get(team)];
	}
	
	public int against (String team1, String team2) {
		if (!teams.containsKey(team1) || !teams.containsKey(team2)) throw new IllegalArgumentException();
		return g[teams.get(team1)][teams.get(team2)];
	}
	
	private int factorial (int x) {
		int f=1;
		while (x>0) f*=x--;
		return f;
	}
	
	private int gameVertex(int team1, int team2) {
//		if (team1>=n || team2>=n) throw new IllegalArgumentException();
		return n+2+n*team1+team2;
	}
	
	/** Indices 0 ... n-1 : team vertices,
	 * 			 source=n : source vertex,
	 * 			 sink=n+1 :	sink vertex,
	 * n+1 ... n+1+(n-1)! : game vertices
	 * */
	private LinkedList<FlowEdge>[] vertices;
	
	private void addEdge (FlowEdge e) {
		if (vertices==null) throw new IllegalStateException("Call createNetwork() first"); 
		vertices[e.from].add(e);
		vertices[e.to].add(e);
	}
	
	/** Create a network of flow edges, where an artificial source vertex is connected to
	 * vertices representing each of the games,
	 * each game vertex is connected to two teams to show how many times a team has won,
	 * and each team vertex is connected to the sink.
	 * The network is created with respect to the team x.
	 * 
	 * Weights: s --games left between 1 and 2--> game -∞-> team i --how many times x can win - i's wins--> sink
	 * 					                               -∞-> team j --how many times x can win - j's wins--> 
	 * 
	 * Weight on the first edge:
	 * if all the edges from s are filled, it means all the games have been completed.
	 * 
	 * Weight on the last edge:
	 * we want to know if there is some way of completing all the games 
	 * so that team x ends up winning at least as many games as team i. 
	 * Since team x can win as many as w[x] + r[x] games, we prevent team i 
	 * from winning more than that many games in total by including an edge 
	 * from team vertex i to the sink vertex with capacity w[x] + r[x] - w[i].
	 * */
	private void createNetwork(int x) {
		//int length = n+2+factorial(n-1);
		int length = gameVertex(n, 0);
		vertices = (LinkedList<FlowEdge>[]) new LinkedList[length];
		for (int i=0; i<length; i++) vertices[i]=new LinkedList<FlowEdge>();
		//Arrays.fill(vertices, new LinkedList<FlowEdge>());
		//create connections between the source, game vertices, and team vertices 
		for (int i=0; i<n; i++) {
			for (int j=0; j<=i; j++) {				
				if (g[i][j]!=0 && i!=x && j!=x) {
					//consider a vertex for each game
					int v = gameVertex(i,j);
					//create a FlowEdge from source to v
					//with capacity = number of games left to play between i and j
					FlowEdge eSource = new FlowEdge(source,v,g[i][j]);
					addEdge(eSource);
					//create two FlowEdges from the game vertex to the team vertices i and j
					FlowEdge ei = new FlowEdge(v,i,Double.POSITIVE_INFINITY);
					addEdge(ei);
					FlowEdge ej = new FlowEdge(v,j,Double.POSITIVE_INFINITY);
					addEdge(ej);
				}
			}
		}
		
		//create connections between the team vertices and the sink 
		for (int i=0; i<n; i++) {
			//maximum number of games that x can win: w[x]+r[x] victories
			//subtract w[i] games from that so that no one team can win more than x
			if (i==x) continue;
			FlowEdge eSink = new FlowEdge(i,sink, w[x]+r[x]-w[i]);
			addEdge(eSink);
		}
		//don't create a vertex for x
		vertices[x] = new LinkedList<FlowEdge>();
	}
	
	/** edgeTo[v] is the edge from source to v through which the path goes */
	private FlowEdge[] edgeTo;
	
	/** Use BFS to determine if there is an augmenting path (i.e., a path that increases the flow)
	 * through the flow network. Write the path into the <i>edgeTo</i> array.
	 */
	private boolean hasAugmentingPath() {
		if (vertices==null) throw new IllegalStateException("Call createNetwork() first"); 
//		////System.out.print("\nhasAugmentingPath() called");
		Queue<Integer> q = new LinkedList<Integer>();
		edgeTo = new FlowEdge[gameVertex(n,0)];
		boolean[] visited = new boolean[edgeTo.length];
		q.add(source);
		while (!q.isEmpty() && q.size()<=gameVertex(n,n)) {
			int v = q.poll();
//			////System.out.print("\nwhile "+v);
			for (FlowEdge e : vertices[v]) {
				int w = e.other(v);
//				////System.out.print(" for "+w);
				if (visited[w]) {
//					////System.out.print(" Continuing because "+w+" has been visited");
					continue;
				}
				//An augmenting path is a path from source to sink through the edges with
				//nonzero residual capacity.
				if (e.residualCapacityTo(w)!=0) {
					q.add(w);
					edgeTo[w] = e;
					visited[w] = true;
					//if found an augmenting path to the sink
					if (w==sink) {
//						////System.out.println("...returning true");
						return true;
					}
				}
			}
		}
//		////System.out.println("...returning false");
		return false;
	}
	
	/** Find the maximum flow through the flow network:
	 * find all augmenting paths in the flow network and fill them to their bottleneck capacity (augment them).  
	 */
	private void FordFulkerson() {
//		printGraph();
		while (hasAugmentingPath()) {
//			System.out.println("\nFinding bottleneck capacity of path "+Arrays.toString(edgeTo));
			//compute bottleneck capacity of the path
			double bottleneckCapacity = Double.POSITIVE_INFINITY;
			for (int v=sink; v!= source; v=edgeTo[v].other(v)) {
				bottleneckCapacity = Math.min(bottleneckCapacity, edgeTo[v].residualCapacityTo(v));
			}
//			System.out.println("Found path with bottleneck capacity "+bottleneckCapacity+" Filling path...");
			
			boolean[] visited = new boolean[edgeTo.length];
			//fill the path to its bottleneck capacity
			for (int v=sink; v!=source && !visited[v]; v=edgeTo[v].other(v)) {
//				//System.out.print(v+">"+edgeTo[v].other(v)+" ~ ");
				visited[v]=true;
				edgeTo[v].addResidualFlowTo(v, bottleneckCapacity);
//				System.out.print(edgeTo[v]+" ");
			}
//			System.out.println("\nFilled path: "+Arrays.toString(edgeTo));
		}
	}
	
	/** Name of the team at a given team vertex */
	private String teamAtVertex(int v) {
		for (String s : teams.keySet())
			if (teams.get(s)==v) return s;
		return null;
	}
	
	/** All teams that eliminate the given team */
	private HashSet<Integer> eliminators;
	private boolean trivial=false;
	
	/** Is the given team eliminated? */
	public boolean isEliminated (String team) {
		if (!teams.containsKey(team)) throw new IllegalArgumentException();
		int x = teams.get(team);
		eliminators = new HashSet<Integer>();
		
		//Trivial elimination: one team has already won more games than x possibly can
		for (int i=0; i<n; i++) {
			if (w[i] > w[x]+r[x]) {
				eliminators.add(i);
			}
		}
		if (!eliminators.isEmpty()) {
			trivial = true;
			System.out.print("    trivial ");
			return true;
		}
		
		//Nontrivial elimination with Ford-Fulkerson algorithm
		boolean eliminated=false;
		createNetwork(x);
		FordFulkerson();
		
		for (FlowEdge e : vertices[source]) {
			int game = e.other(source); //game vertex that is the reason for elimination
			if (e.residualCapacityTo(game)!=0) {
				eliminated=true;
				
				for (FlowEdge f : vertices[game]) {
					int eliminator = f.other(game);
					if (eliminator<n) {
						eliminators.add(eliminator);
					}
				}
			}
		}
		System.out.print(" nontrivial "+vertices[source]);
		return eliminated;
	}
	
	/** Subset of teams that eliminates the given team; null if given team not eliminated */
	public Iterable<String> certificateOfElimination(String query) {		
		if (!isEliminated(query)) return null;
		HashSet<String> certificate = new HashSet<String>();
		
		//In case of trivial elimination
		if (trivial) {
			for (int i : eliminators) certificate.add(teamAtVertex(i));
			return certificate;
		}
		
		HashSet<Integer> S = new HashSet<Integer>(); //vertices connected to the source
		HashSet<Integer> T = new HashSet<Integer>(); //vertices connected to the sink
		
		S.add(source);
		for (int i=0; i<vertices.length; i++) {
			if (i==source || vertices[i].isEmpty()) continue;
			T.add(i);
		}
		
		//Perform BFS to add all vertices connected to the source (via edges with residual flow !=0) to S
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(source);
		while (!q.isEmpty()) {
			int v = q.poll();
			for (FlowEdge e : vertices[v]) {
				int w = e.other(v);
				if (e.residualCapacityTo(w)==0 || S.contains(w)) continue;
				//if (!T.remove(w)) throw new IllegalStateException(v+" "+w);
				T.remove(w);
				S.add(w);
				q.add(w);
			}
		}
		//System.out.println("\nConnected to the source: "+S+", unconnected: "+T);
		
		//For each team vertex from S: if it has a connection to a vertex from T, this team vertex is on the
		//minimum cut, so, add this team to the certificate
		for (int team : S) {
			if (team>=n) continue;
			
			//if found a team vertex
			for (FlowEdge e : vertices[team]) {
				if (T.contains(e.other(team))) certificate.add(teamAtVertex(team));
			}
		}
		
		return certificate;
	}
	
	/** Print FlowEdges adjacent to each vertex */
	private void printGraph() {
		int i=0;
		for (LinkedList<FlowEdge> list : vertices) {
			System.out.println(i++ + (i>10?": ":":  ") + list);
		}
	}
	
	/** Prove that sum of inflows = sum of outflows */
	private void proveFlowInVertex(int v) {
		if (v==source || v==sink) return;
		
		double in=0,out=0;
		for (FlowEdge e : vertices[v]) {
			if (e.from==v) out+= e.flow;
			else if (e.to==v) in+= e.flow;
			else throw new IllegalArgumentException();
		}
		if (in!=out) throw new IllegalStateException(v+": inflow "+in+" outflow "+out);
	}
	
	/** Prove that no two game vertices point at the same pair of teams */
	private void proveGameVertices() {
		boolean[][] teamPairs = new boolean[n][n];
		for (int g = sink+1; g<vertices.length; g++) { //iterate through all game vertices
			int t1=-1,t2=-1; //two team vertices to which the game vertex g is connected
			for (FlowEdge e : vertices[g]) {
				int t = e.other(g);
				if (t==source || t==sink) continue;
				if (t1==-1) t1=t;
				else if (t2==-1) t2=t;
				else throw new IllegalStateException("More than two edges from game vertex "+g);
			}
			if (t1==-1 || t2==-1) continue;
			if (teamPairs[t1][t2] || teamPairs[t2][t1]) throw 
					new IllegalStateException("Two game vertices built for "+t1+" "+t2);
			teamPairs[t1][t2]=true;
			teamPairs[t2][t1]=true;
		}
	}
	
	public static void main(String[] args) {
		BaseballElimination be = new BaseballElimination("https://coursera.cs.princeton.edu/algs4/assignments/baseball/files/teams30.txt");
		System.out.println(be.teams.toString());

		System.out.printf("source: %d, sink: %d\n", be.source,be.sink);		
		
//		for (String team : be.teams.keySet()) {
//			System.out.println(" "+team+": "+be.isEliminated(team)+" "+be.certificateOfElimination(team));
//			System.out.print("        ");
//			//for (int i=0; i<21; i++) System.out.print("       "+i+"        ");
//			System.out.println();
//			be.proveGameVertices();
//			be.printGraph();
//		}
		
		
		String team = "Team18";
		System.out.println(" "+team+": "+be.isEliminated(team)+" "+be.certificateOfElimination(team));
		System.out.println("\n\n");
		for (int i=0; i<be.vertices.length; i++) be.proveFlowInVertex(i);
		be.proveGameVertices();
		System.out.println("\n\n");
		be.printGraph();
	}
}
