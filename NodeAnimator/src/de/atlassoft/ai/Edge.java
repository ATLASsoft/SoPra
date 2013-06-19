package de.atlassoft.ai;


class Edge {

	private double distance;
	private double topSpeed;
	private Vertex start;
	private Vertex end;
	private boolean blocked;
	
	Edge(Vertex start, Vertex end, double distance, double topSpeed) {
		this.start = start;
		this.end = end;
		this.distance = distance;
		this.topSpeed = topSpeed;
	}
	
	boolean isBlocked() {
		return blocked;
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
