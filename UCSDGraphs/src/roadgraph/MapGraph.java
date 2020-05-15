/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team
 * @author Oleksandr Pokras
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private  HashMap<GeographicPoint, MapGraphNode> nodes;
	private List<String> roadTypes = new LinkedList<String>();
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		nodes = new HashMap<GeographicPoint, MapGraphNode>();
	}
	
	/**
	 * Find the vertex (road intersection) on a specified location
	 * @param location
	 * @return node found on location
	 */
	private MapGraphNode getNode(GeographicPoint location) {
		return nodes.get(location);
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return nodes.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		return nodes.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		int numEdges = 0;
		//for each intersection (node in the graph)
		for (GeographicPoint location : nodes.keySet()) {
			MapGraphNode node = getNode(location);
			//add number of each node's edges (size of adjacency list)
			numEdges += node.getNeighbors().size();
		}
		return numEdges;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		if (location==null || nodes.containsKey(location)) return false;
		nodes.put(location,new MapGraphNode(location));
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		if (!nodes.containsKey(from) || !nodes.containsKey(to)
			|| from==null || to==null || length<0) throw new IllegalArgumentException();
		MapGraphNode startNode = nodes.get(from);
		MapGraphNode endNode = nodes.get(to);
		MapGraphEdge road = new MapGraphEdge(startNode,endNode,roadName,roadType,length);
		startNode.addRoad(road);
		if (!roadTypes.contains(roadType)) {
			roadTypes.add(roadType);
			//System.out.println("if (roadType.equals(\""+roadType+"\")) {speed= ;}");
		}
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
			                         throws IllegalArgumentException
	{
		if (!nodes.containsKey(start) || !nodes.containsKey(goal)
			|| start==null || goal==null) throw new IllegalArgumentException();	
		
		MapGraphNode startNode = nodes.get(start);
		MapGraphNode goalNode = nodes.get(goal);
		//Visited vertices
		HashMap<MapGraphNode,Boolean> visited = new HashMap<MapGraphNode,Boolean>(getNumVertices());
		//Maps vertices to their respective parents
		HashMap<MapGraphNode,MapGraphNode> parents = new HashMap<MapGraphNode,MapGraphNode>();
		//Search queue
		Queue<MapGraphNode> queue = new LinkedList<MapGraphNode>();
		queue.add(startNode);
		
		while (!queue.isEmpty()) {
			MapGraphNode curr = queue.remove();
			visited.put(curr, true);
			nodeSearched.accept(curr.getLocation());
			//System.out.print("Current: "+curr+" Neighbors: ");
			//System.out.println(curr.getNeighbors().toString());
			for (MapGraphNode next : curr.getNeighbors()) {
				if (visited.get(next)!=null) continue;
				parents.put(next, curr);
				queue.add(next);
				if (next==goalNode) {
					//reconstructing the way from start to goal
					//System.out.println("Found goal node");
					//System.out.println (reconstructPath(goalNode,startNode,parents).toString());
					return reconstructPath(goalNode,startNode,parents);
					}
			}
		}

		//System.out.println("Goal node not found");
		return null;
	}
	
	/**
	 * Helper: reconstruct path from goal to start
	 */
	private List<GeographicPoint> reconstructPath (MapGraphNode goalNode, 
							MapGraphNode startNode,HashMap parents){
		LinkedList<MapGraphNode> reverseOrder = new LinkedList<MapGraphNode>();
		MapGraphNode curr = goalNode;
		while (curr != startNode) {
			reverseOrder.add(curr);
			curr = (MapGraphNode) parents.get(curr);
		}
		reverseOrder.add(startNode);
		
		LinkedList<GeographicPoint> ret = new LinkedList<GeographicPoint>();
		for (int i=reverseOrder.size()-1;i>=0;i--) {
			ret.add(reverseOrder.get(i).getLocation());
		}
		return ret;
	}
	

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (!nodes.containsKey(start) || !nodes.containsKey(goal)
				|| start==null || goal==null) throw new IllegalArgumentException();	
		int iterations=1;
		MapGraphNode startNode = nodes.get(start);
		startNode.cost=0;
		MapGraphNode goalNode = nodes.get(goal);
		//Visited vertices
		//HashMap<MapGraphNode,Boolean> visited = new HashMap<MapGraphNode,Boolean>(getNumVertices());
		//Maps vertices to their respective parents
		HashMap<MapGraphNode,MapGraphNode> parents = new HashMap<MapGraphNode,MapGraphNode>();
		//Search queue
		PriorityQueue<MapGraphNode> queue = new PriorityQueue<MapGraphNode>();
		queue.add(startNode);
		//Distance so far from start
		double currDist=0.0;
		while (!queue.isEmpty()) {
			MapGraphNode curr = queue.remove();
			//System.out.println(iterations++ +" "+curr);

			if (curr==goalNode) {
				System.out.println("Found path with Dijkstra: "+iterations);
				return reconstructPath(goalNode,startNode,parents);
			}
			iterations++;
			currDist = curr.cost;
			//System.out.println(curr+" "+currDist);
			//visited.put(curr, true);
			nodeSearched.accept(curr.getLocation());
			for (MapGraphNode next:curr.getNeighborsDistanceMap().keySet()) {
				double nextDist = currDist+curr.getDistanceToNeighbor(next);
				//checking if found a more efficient path to next
				if (nextDist>next.cost) continue;
				//if found a more efficient path to next
				next.cost=nextDist;
				queue.add(next);
				parents.put(next, curr);
			}
		}
		System.out.println("Path not found with Dijkstra");
		return null;
		//nodeSearched.accept(next.getLocation());
		
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (!nodes.containsKey(start) || !nodes.containsKey(goal)
				|| start==null || goal==null) throw new IllegalArgumentException();	
		System.out.println("\nStarting A* Search...");
		MapGraphNode startNode = nodes.get(start);
		startNode.cost=0;
		startNode.distanceFromStart=0;
		MapGraphNode goalNode = nodes.get(goal);
		//Visited vertices
		//HashMap<MapGraphNode,Boolean> visited = new HashMap<MapGraphNode,Boolean>(getNumVertices());
		//Maps vertices to their respective parents
		HashMap<MapGraphNode,MapGraphNode> parents = new HashMap<MapGraphNode,MapGraphNode>();
		//Search queue
		PriorityQueue<MapGraphNode> queue = new PriorityQueue<MapGraphNode>();
		queue.add(startNode);
		//Distance so far from start
		double currDist=0.0;
		int iterations=1;
		while (!queue.isEmpty()) {
			System.out.println("\n"+queue.toString());
			MapGraphNode curr = queue.remove();
			currDist = curr.distanceFromStart;
			System.out.println(iterations++ +" "+curr+" - from start "+String.format("%.2f",curr.distanceFromStart)+" - to goal "+String.format("%.2f", curr.distanceTo(goalNode)));
			if (curr==goalNode) {
				System.out.println("Found path with AStar: "+(iterations-1));
				return reconstructPath(goalNode,startNode,parents);
			}
			//System.out.println(curr+" "+currDist);
			//visited.put(curr, true);
			nodeSearched.accept(curr.getLocation());
			for (MapGraphNode next:curr.getNeighborsDistanceMap().keySet()) {
				//distance from start to next
				double nextDist = currDist+curr.getDistanceToNeighbor(next);
				//checking if found a more efficient path to next
				if (nextDist>next.distanceFromStart) continue;
				//if found a more efficient path to next
				next.cost=nextDist+next.distanceTo(goalNode);
				queue.add(next);
				parents.put(next, curr);
				next.distanceFromStart=nextDist;
			}
		}
		System.out.println("Path not found with AStar");
		return null;
		// TODO: Implement this method in WEEK 4
		
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
	}

	
	
	public static void main(String[] args)
	{
		
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(4.0, 2.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		System.out.println("My Test 0 using simpletest");
		simpleTestMap.bfs(testStart, testEnd);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		if (testroute2!=null)
			System.out.println(testroute2.toString()+" Size: "+testroute2.size());
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println(testroute2.size());

		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		//System.out.println(testroute.size());
		
		
		// Use this code in Week 3 End of Week Quiz 
		
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		MapGraph sanDiegoMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/san_diego.map", sanDiegoMap);

		
	}
	
}
