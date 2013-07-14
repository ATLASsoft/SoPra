package de.atlassoft.ai;

import de.atlassoft.model.Path;
import de.atlassoft.model.State;

/**
 * Representation of a {@link Path} within the AI component. Optimized structure
 * for graph algorithm.
 * 
 * @author Alexander Balogh
 *
 */
class Edge {

	/**
	 * Distance from start to end in km.
	 */
	private double distance;
	
	/**
	 * Allowed top speed in km/h
	 */
	private double topSpeed;
	
	/**
	 * Start node of this edge
	 */
	private Vertex start;
	
	/**
	 * End node of this edge
	 */
	private Vertex end;
	
	/**
	 * Model object of this edge
	 */
	private Path modelObject;
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param distance
	 * @param topSpeed
	 */
	Edge(Vertex start, Vertex end, double distance, Path modelObject) {
		this.start = start;
		this.end = end;
		this.distance = distance;
		this.topSpeed = modelObject.getTopSpeed();
		this.modelObject = modelObject;
	}
	
	/**
	 * Returns true when this edge is currently blocked
	 * 
	 * @return true if blocked, otherwise false
	 */
	boolean isBlocked() {
		if (modelObject.getState().getState() == State.BLOCKED) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the actual cost of this edge. The cost is computed as the
	 * duration, a mobile object with the specified top speed needs, to drive
	 * along this path at maximal possible speed, in hours.
	 * 
	 * @param mobileTopSpeed
	 *            Top speed of the mobile object
	 * @return Cost in hours
	 */
	protected double getActualCost(double mobileTopSpeed) {
		return distance / Math.min(this.topSpeed, mobileTopSpeed);
	}
	
	/**
	 * Returns the start vertex of this edge.
	 * 
	 * @return The start vertex
	 */
	public Vertex getStart() {
		return start;
	}

	/**
	 * Returns the end vertex of this edge.
	 * 
	 * @return The end vertex
	 */
	public Vertex getEnd() {
		return end;
	}
	
	/**
	 * Return the model object of this edge.
	 * 
	 * @return The {@link Path} this edge is associated with
	 */
	public Path getModelObject() {
		return modelObject;
	}
	
	@Override
	public String toString() {
		return "start: '" + start + "' end: '" + end + "' dist: " + distance + " topSpeed: " + topSpeed;
	}
}
