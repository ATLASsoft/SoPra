package de.atlassoft.model;

import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.path.PathFigure;
//TODO: Klasse ummodellieren insbesondere das eingügen in die NodeMap
/**
 * This class defines a path from one node to another.
 * 
 * @author Silvan Haeussermann
 */
public class Path {
	
	private PathFigure pathFigure;
	private double topSpeed;
	private State state;
	
	/**
	 * The constructor for the Path.
	 * 
	 * @param map
	 *            The map in which the path should be saved.
	 * @param start
	 *            The start node of the path.
	 * @param end
	 *            The end node of the path.
	 * @param topSpeed
	 *            The top speed that is allowed while driving on this path.
	 */
	public Path(NodeMap map, Node start, Node end, double topSpeed) {
		if (topSpeed < 0) {
			throw new IllegalArgumentException(
					"topSpeed must not be a negative number");
		}

		this.topSpeed = topSpeed;
		pathFigure = FigureFactory.createPath(map, start.getNodeFigure(),
				end.getNodeFigure(), this);
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
		if (topSpeed < 0) {
			throw new IllegalArgumentException(
					"topSpeed must not be a negative number");
		}
		this.topSpeed = topSpeed;
	}
	
	/**
	 * Returns the path figure of the path.
	 * 
	 * @return
	 * 		The path figure.
	 */
	public PathFigure getPathFigure() {
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
	
}
