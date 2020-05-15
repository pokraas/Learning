package roadgraph;

/**
 * A class to represent a road between two intersections as an edge of MapGraph.
 * @author Oleksandr Pokras
 *
 */
	class MapGraphEdge {
	private String roadName;
	private String roadType;
	private double length;
	private MapGraphNode startNode;
	private MapGraphNode endNode;
	/**
	 * Create a new road between two intersections (MapGraphNodes)
	 * and represent it as an edge in the graph.
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km 
	 */
	MapGraphEdge (MapGraphNode fromNode, MapGraphNode toNode,
			String roadName, String roadType, double length) {
		this.startNode=fromNode;
		this.endNode=toNode;
		this.roadName=roadName;
		this.roadType=roadType;
		this.length=length;		
	}
	
	//Getters for local variables
	
	/**
	 * @return the road's starting node
	 */
	public MapGraphNode getStartNode() {
		return startNode;
	}

	/**
	 * @return the road's ending node
	 */
	public MapGraphNode getEndNode() {
		return endNode;
	}

	/**
	 * @return the road's name
	 */
	public String getRoadName() {
		return roadName;
	}
	/**
	 * @return the road's type
	 */
	public String getRoadType() {
		return roadType;
	}

	/**
	 * @return the road's length in km
	 */
	public double getLength() {
		return length;
	}
	
	/**
	 * Calculate the weighted length of the road based on its type.
	 * Speeds on different road types (assumption):
	 * <ol>
	 * <li>motorway: 110 km/h
	 * <li>motorway_link: 80 km/h
	 * <li>primary or primary_link: 60 km/h
	 * <li> secondary or secondary_link: 50 km/h
	 * <li> tertiary or tertiary_link: 40 km/h
	 * <li> residential, unclassified and others: 30 km/h
	 * </ol>
	 * @return the weighted length
	 */
	public double getWeightedLength() {
		int speed=0;
		if (roadType.equals("residential")) {speed= 30;}
		if (roadType.equals("primary")) {speed= 60;}
		if (roadType.equals("secondary")) {speed= 50;}
		if (roadType.equals("unclassified")) {speed= 30;}
		if (roadType.equals("tertiary")) {speed= 40;}
		if (roadType.equals("primary_link")) {speed= 60;}
		if (roadType.equals("secondary_link")) {speed= 50;}
		if (roadType.equals("tertiary_link")) {speed= 40;}
		if (roadType.equals("motorway_link")) {speed= 80;}
		if (roadType.equals("motorway")) {speed= 110;}
		if (speed==0) speed=30;

		return length/speed;
	}
	
}
