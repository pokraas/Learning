package roadgraph;

import geography.GeographicPoint;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * A class to represent a single intersection (graph node)
 * and its neighbors with an adjacency list
 * @author Oleksandr Pokras
 */
	class MapGraphNode implements Comparable<MapGraphNode> {
	private GeographicPoint location;
	private LinkedList<MapGraphNode> adjList;
	private HashMap<MapGraphNode,Double> neighborsDistMap;
	double cost;
	double distanceFromStart;
	
	/**
	 * Create a new MapGraphNode object
	 * @param location The location of the node as GeographicPoint object
	 */
	public MapGraphNode (GeographicPoint location) throws NullPointerException {
		if (location==null) throw new NullPointerException("Can't create a node with a null location");
		this.location = location;
		adjList = new LinkedList<MapGraphNode>();
		neighborsDistMap = new HashMap<MapGraphNode,Double>();
		cost=Integer.MAX_VALUE;
		distanceFromStart=Integer.MAX_VALUE;
	}
	
	/**
	 * Creates a new edge between this and another node
	 * @param road the edge to be added
	 */
	public void addRoad(MapGraphEdge road) throws NullPointerException {
		if (road==null) throw new NullPointerException("Can't add a null edge");
		neighborsDistMap.put(road.getEndNode(),road.getWeightedLength());
		adjList.add(road.getEndNode());
	}
	
	/**
	 * Get node's neighbors
	 * @return list of node's neighbors
	 */
	public LinkedList<MapGraphNode> getNeighbors(){
		return adjList;
	}
	

	/**
	 * Get neighbors and distances to them
	 * @return HashMap mapping node's neighbors to distances to them
	 */
	public HashMap<MapGraphNode, Double> getNeighborsDistanceMap(){
		return neighborsDistMap;
	}
	
	/**
	 * Calculate straight-line distance to other node
	 * @param other node to calculate distance to
	 * @return straight-line distance to other node
	 */
	Double distanceTo(MapGraphNode other) {
		return location.distance(other.getLocation());
	}
	
	/**
	 * Get road distance to neighbor node
	 * @return distance to neighbor
	 */
	public double getDistanceToNeighbor(MapGraphNode neighbor) {
		if (!neighborsDistMap.containsKey(neighbor)) 
			throw new IllegalArgumentException("Neighbor not found");
		return neighborsDistMap.get(neighbor);
	}
	
	/**
	 * Get the location of the node
	 * @return node's location
	 */
	public GeographicPoint getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		return "N"+(int)location.getX()+"/"+(int)location.getY();
	}
	
	@Override
	public int compareTo(MapGraphNode other) {
		return (int) (1000000*(this.cost-other.cost));
	}
}
