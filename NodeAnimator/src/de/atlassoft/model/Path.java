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
	
	/**
	 * The graphical representation of this {@link Path}.
	 */
	private PathFigure pathFigure;
	
	/**
	 * The top speed that is allowed on this {@link Path} in kilometer per
	 * hours.
	 */
	private double topSpeed;
	
	/**
	 * Indicates the state of this {@link Path}.
	 */
	private State state;
	
	/**
	 * The start {@link Node} of this {@link Path}.
	 */
	private Node start;
	
	/**
	 * The end {@link Node} of this {@link Path}.
	 */
	private Node end;
	
	/**
	 * The id of this path. Should be unique within the {@link RailwaySystem}
	 * this path belongs to.
	 */
	int id;
	
	/**
	 * The train type workload map of this {@link Path}. Indicates how often
	 * this train has been passed over by the different {@link TrainType
	 * TrainTypes} since the instantiation of this object or the last
	 * {@link Path#clear() clear()}.
	 */
	private Map<TrainType, Integer> trainTypeMap;
	
	/**
	 * The schedule scheme workload map of this {@link Path}. Indicates how
	 * often this train has been passed over by trains of the different
	 * {@link ScheduleScheme ScheduleSchemes} since the instantiation of this
	 * object or the last {@link Path#clear() clear()}.
	 */
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
	 * Removes all data that has been collected during one or multiple
	 * simulations from this path.
	 * @see {@link Path#trainTypeMap}, {@link Path#schemeMap}, {@link Path#state}
	 */
	protected void clear() {
		state.setState(State.UNBLOCKED, null);
		trainTypeMap.clear();
		schemeMap.clear();
	}
	
	/**
	 * Sets the id. The id should be unique within the {@link RailwaySystem}
	 * this path belongs to. However, this method does not check for uniqueness.
	 * 
	 * @param id
	 *            The id
	 */
	protected void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the top speed that is allowed on this path.
	 * 
	 * @return The top speed in kilometer per hours
	 */
	public double getTopSpeed() {
		return topSpeed;
	}

	/**
	 * Sets the top speed that is allowed on this path. If topSpeed is a
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
	
	/**
	 * Returns the id of this path. The id is unique within the {@link RailwaySystem}
	 * this path belongs to.
	 * @return
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Increases the work load ,i.e. how often this {@link Node} has been passed
	 * over, by one.
	 * 
	 * @param scheme
	 *            {@link ScheduleScheme} of the train that passed over this
	 *            {@link Node}
	 */
	public void incrementWorkLoad(ScheduleScheme scheme) {
		
		// increase work load in scheme map
		if (schemeMap.containsKey(scheme)) {
			schemeMap.put(scheme, schemeMap.get(scheme) + 1);
		} else {
			schemeMap.put(scheme, new Integer(1));
		}
		
		// increase work load in train type map
		TrainType type = scheme .getTrainType();
		if (trainTypeMap.containsKey(type)) {
			trainTypeMap.put(type, trainTypeMap.get(type) + 1);
		} else {
			trainTypeMap.put(type, new Integer(1));
		}
	}
	
	/**
	 * Returns the train type workload map of this {@link Node} that indicates
	 * how often this train has been passed over by the different
	 * {@link TrainType}s within the last simulation.
	 * 
	 * @return The train type workload map
	 */
	public Map<TrainType, Integer> getTrainTypeWorkloadMap() {
		return trainTypeMap;
	}
	
	/**
	 * Returns the schedule scheme workload map of this {@link Node} that indicates
	 * how often this train has been passed over by trains of the different
	 * {@link ScheduleScheme}s within the last simulation.
	 * 
	 * @return The schedule scheme workload map
	 */
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
