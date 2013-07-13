package de.atlassoft.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;

import de.hohenheim.view.node.NodeFigure;

/**
 * This class provides a node for the railway system
 * of the application.
 *  
 * @author Alexander Balogh
 */
public class Node implements Blockable {

	/**
	 * Indicates the state of this {@link Node}.
	 */
	private State state;
	
	/**
	 * The graphical representation of the {@link Node}.
	 */
	private NodeFigure nodeFigure;
	
	/**
	 * The name of this {@link Node}. Must be unique within the
	 * {@link RailwaySystem} this node {@link Node} belongs to
	 */
	private String name;
	
	/**
	 * The train type workload map of this {@link Node}. Indicates how often
	 * this train has been passed over by the different {@link TrainType
	 * TrainTypes} since the instantiation of this object or the last
	 * {@link Node#clear() clear()}.
	 */
	private Map<TrainType, Integer> trainTypeMap;
	
	/**
	 * The schedule scheme workload map of this {@link Node}. Indicates how
	 * often this train has been passed over by trains of the different
	 * {@link ScheduleScheme ScheduleSchemes} since the instantiation of this
	 * object or the last {@link Node#clear() clear()}.
	 */
	private Map<ScheduleScheme, Integer> schemeMap;
	
	
	
	/**
	 * Constructor for creating a new Node with standard size (15x15)
	 * 
	 * @param name
	 *            The name of the node.
	 * @param x
	 *            The x-coordinate in the railway system. Must not be a negative
	 *            number.
	 * @param y
	 *            The y-coordinate in the railway system. Must not be a negative
	 *            number.
	 * @throws IllegalArgumentException
	 *             if name is null or one coordinate is a negative
	 *             number
	 */
	public Node(String name, int x, int y) {
		this(name, x, y, 15, 15);
	}
	
	/**
	 * Constructor for creating a new Node.
	 * 
	 * @param name
	 *            The name of the node.
	 * @param x
	 *            The x-coordinate in the railway system. Must not be a negative
	 *            number.
	 * @param y
	 *            The y-coordinate in the railway system. Must not be a negative
	 *            number.
	 * @param width
	 *            The width of the node. Must not be a negative number.
	 * @param height
	 *            The height of the node. Must not be a negative number.
	 * @throws IllegalArgumentException
	 *             if name is null or one coordinate or dimension is a negative
	 *             number
	 */
	public Node(String name, int x, int y, int width, int height) {
		// check constraints
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		if (x < 0 || y < 0 || width < 0 || height < 0) {
			throw new IllegalArgumentException(
					"coordinates and dimensions must be positive numbers");
		}
		
		// set properties
		this.name = name;
		state = new State(this);
		trainTypeMap = new HashMap<>();
		schemeMap = new HashMap<>();
		
		NodeFigure nodeFigure = new NodeFigure(this);
		nodeFigure.setName(name);    
		nodeFigure.setBounds(new Rectangle(x, y, width, height));	
		this.nodeFigure = nodeFigure;
	}
	
	
	
	/**
	 * Clears the state of this {@link Node} and removes all data that has been
	 * collected during one or multiple simulations from this node if resetData
	 * is true.
	 * 
	 * @param resetData
	 *            indicates whether all data should be reseted or only the state
	 *            should be cleared
	 * @see {@link Node#trainTypeMap}, {@link Node#schemeMap},
	 *      {@link Node#state}
	 */
	protected void clear(boolean resetData) {
		state.setState(State.UNBLOCKED, null);
		
		if (resetData) {
			trainTypeMap.clear();
			schemeMap.clear();
		}
	}
	
	/**
	 * Returns the {@link NodeFigure} of this node.
	 * 
	 * @return the NodeFigure
	 */
	public NodeFigure getNodeFigure() {
		return nodeFigure;
	}

	@Override
	public State getState() {
		return state;
	}

	/**
	 * Returns the name of this node.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
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
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node)) {
			return false;
		} else {
			return ((Node) obj).name.equals(name);
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
}
