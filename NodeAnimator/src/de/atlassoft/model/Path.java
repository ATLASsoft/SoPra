package de.atlassoft.model;

import java.util.HashMap;
import java.util.Map;

import de.hohenheim.view.path.CenterAnchor;
import de.hohenheim.view.path.PathFigure;

/**
 * This class defines a path from one node to another.
 * 
 * @author Alexander Balogh
 */
public class Path implements Blockable {
	
	private PathFigure pathFigure;
	private double topSpeed;
	private State state;
	private Node start;
	private Node end;
	int id;
	private Map<TrainType, Integer> trainTypeMap;
	private Map<ScheduleScheme, Integer> schemeMap;
	
	
	/**
	 * The constructor for the Path.
	 * 
	 * @param start
	 *            The start node of the path
	 * @param end
	 *            The end node of the path
	 * @param topSpeed
	 *            The top speed that is allowed while driving on this path in
	 *            km/h
	 * @throws IllegalArgumentException
	 *             if start or end is null or topSpeed is a negative number
	 */
	public Path(Node start, Node end, double topSpeed) {
		// check constraints
		if (start == null || end == null) {
			throw new IllegalArgumentException("start and end must not be null");
		}
		if (topSpeed < 0) {
			throw new IllegalArgumentException(
					"topSpeed must not be a negative number");
		}

		// set properties
		this.topSpeed = topSpeed;
		state = new State(this);
		this.start = start;
		this.end = end;
		trainTypeMap = new HashMap<>();
		schemeMap = new HashMap<>();

		PathFigure path = new PathFigure(this);
		path.setSourceAnchor(new CenterAnchor(start.getNodeFigure())); // new ChopboxAnchor(start)
		path.setTargetAnchor(new CenterAnchor(end.getNodeFigure()));// new ChopboxAnchor(end)
		this.pathFigure = path;
	}
	
	
	/**
	 * Returns the top speed that is allowed in this path.
	 * 
	 * @return The top speed.
	 */
	public double getTopSpeed() {
		return topSpeed;
	}

	/**
	 * Sets the top speed that is allowed in this path. If topSpeed is a
	 * negative number, an {@link IllegalArgumentException} is thrown.
	 * 
	 * @param topSpeed
	 *            The top speed to be set
	 * @throws IllegalArgumentException
	 *             if topSpeed is a negative number
	 */
	public void setTopSpeed(double topSpeed) {
		if (topSpeed < 0) {
			throw new IllegalArgumentException(
					"topSpeed must not be a negative number");
		}
		this.topSpeed = topSpeed;
	}
	
	/**
	 * Returns the path figure of the path.
	 * 
	 * @return the path figure
	 */
	public PathFigure getPathFigure() {
		return pathFigure;
	}

	@Override
	public State getState() {
		return state;
	}
	
	/**
	 * Returns the start {@link Node} of this path.
	 * 
	 * @return the start node
	 */
	public Node getStart() {
		return start;
	}

	/**
	 * Returns the end {@link Node} of this path.
	 * 
	 * @return the end node
	 */
	public Node getEnd() {
		return end;
	}
	
	protected void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public Map<TrainType, Integer> getTrainTypeWorkloadMap() {
		return trainTypeMap;
	}
	
	public Map<ScheduleScheme, Integer> getScheduleSchemeWorkloadMap() {
		return schemeMap;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[{");
		sb.append(start);
		sb.append("}<-->{");
		sb.append(end);
		sb.append("}; topSpeed:");
		sb.append(topSpeed);
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Path)) {
			return false;
		} else {
			Path other = (Path) obj;
			if (this.start.equals(other.start)
					&& this.end.equals(other.end)
					&& (this.topSpeed - other.topSpeed) < 0.01) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + start.hashCode();
		result = 31 * result + end.hashCode();
		result = 31 * result + (int) topSpeed;
		return result;
	}
}
