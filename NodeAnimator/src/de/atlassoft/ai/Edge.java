package de.atlassoft.ai;

import de.atlassoft.model.Path;
import de.atlassoft.model.State;


class Edge {

	/**
	 * Distance from start to end in km.
	 */
	private double distance;
	private double topSpeed;
	private Vertex start;
	private Vertex end;
	private Path modelObject;
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param distance
	 * @param topSpeed
	 */
	Edge(Vertex start, Vertex end, double distance, double topSpeed, Path modelObject) {
		this.start = start;
		this.end = end;
		this.distance = distance;
		this.topSpeed = topSpeed;
		this.modelObject = modelObject;
	}
	
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
	
	public double getDistance() {
		return distance;
	}

	public double getTopSpeed() {
		return topSpeed;
	}

	public Vertex getStart() {
		return start;
	}

	public Vertex getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return "start: '" + start + "' end: '" + end + "' dist: " + distance + " topSpeed: " + topSpeed;
	}
}
