package de.atlassoft.model;

/**
 * This class defines a path from one node to another.
 * 
 * @author Silvan Haeussermann
 */
public class Path {
	
	private int pathFigure;
	private double topSpeed;
	private State state;
	//TODO: auch start und ziel ??
	
	/**
	 * The constructor for the Path.
	 * 
	 * @param map
	 * 		The map in which the path should be saved.
	 * @param start
	 * 		The start node of the path.
	 * @param end
	 * 		The end node of the path.
	 * @param topSpeed
	 * 		The top speed that is allowed while driving
	 * 		on this path.
	 */
	public Path(int map, Node start, Node end, double topSpeed){
		//TODO: fertig machen
		this.topSpeed = topSpeed;
		state = new State();
		state.setState(State.UNBLOCKED);
	}
	
	/**
	 * Returns the top speed that is allowed in this path.
	 * 
	 * @return
	 * 		The top speed.
	 */
	public double getTopSpeed(){
		return topSpeed;
	}

	/**
	 * Sets the top speed that is allowed in this path.
	 * 
	 * @param topSpeed
	 * 		The top speed which should be set.
	 */
	public void setTopSpeed(double topSpeed){
		this.topSpeed = topSpeed;
	}
	
	/**
	 * Returns the path figure of the path.
	 * 
	 * @return
	 * 		The path figure.
	 */
	public int getPathFigure() {
		return pathFigure;
	}
	
	/**
	 * Returns the state in order to see whether the path is blocked
	 * or not blocked.
	 * 
	 * @return
	 * 		The current state of the path.
	 */
	public State getState(){
		return state;
	}
	//TODO: auch noch setState ??
}
