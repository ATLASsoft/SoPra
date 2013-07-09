package de.atlassoft.ai;


class Edge {

	/**
	 * Distance from start to end in km.
	 */
	private double distance;
	private double topSpeed;
	private Vertex start;
	private Vertex end;
	private boolean blocked;
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param distance
	 * @param topSpeed
	 */
	Edge(Vertex start, Vertex end, double distance, double topSpeed) {
		this.start = start;
		this.end = end;
		this.distance = distance;
		this.topSpeed = topSpeed;
	}
	
	boolean isBlocked() {
		return blocked;
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
	
	void setBlocked(boolean blocked) {
		this.blocked = blocked;
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
