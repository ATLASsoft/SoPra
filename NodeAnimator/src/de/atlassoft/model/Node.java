package de.atlassoft.model;

import de.hohenheim.view.FigureFactory;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.node.NodeFigure;
//TODO: Klasse ummodellieren insbesondere das eingügen in die NodeMap
/**
 * This class provides a node for the railwaysystem
 * of the application.
 *  
 * @author Silvan
 */
public class Node {

	private State state;
	private NodeFigure nodeFigure;
	private String name;
	
	/**
	 * Constructor for creating a new Node
	 * 
	 * @param map
	 * 		The number of the railwaysytem where the node
	 * 		should appear.
	 * @param name
	 * 		The name of the node.
	 * @param x
	 * 		The x-coordinate in the railwaysystem.
	 * @param y
	 * 		The y-coordinate in the railwaysystem.
	 * @param width
	 * 		The width of the node.
	 * @param height
	 * 		The height of the node.
	 */
	public Node(NodeMap map, String name, int x, int y, int width, int height){
		this.name = name;
		nodeFigure = FigureFactory.createNode(map, name, x, y, 15, 15, this);
	}
	
	/**
	 * Returns the NodeFigure of the node.
	 * 
	 * @return
	 * 		The NodeFigure of the node.
	 */
	public NodeFigure getNodeFigure() {
		return nodeFigure;
	}
	
	/**
	 * Returns the state of the node
	 * 
	 * @return
	 * 		The state of the node.
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Returns the name of the node.
	 * 
	 * @return
	 * 		The name of the node.
	 */
	public String getName() {
		return name;
	}
}
