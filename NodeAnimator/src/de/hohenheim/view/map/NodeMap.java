package de.hohenheim.view.map;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import de.hohenheim.view.mobile.AnimationFigure;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.PathFigure;

public class NodeMap {
	
	/**
	 * This HashMap Contains all mobile objects, such as fork trucks, ....
	 */
	HashMap<String, AnimationFigure> mobile_objects = new HashMap<String, AnimationFigure>();
	
	/**
	 * This HashMap Contains all paths of all rooms in the RoomMap. The paths are added automatically, when the methods of FigureFactory
	 * are used to create the Figures in the Map.
	 */
	HashMap<NodeFigure, ArrayList<PathFigure>> paths = new HashMap<NodeFigure, ArrayList<PathFigure>>();
	/**
	 * This HashMap Contains all rooms in the NodeMap. The paths are added automatically, when the methods of FigureFactory
	 * are used to create the Figures in the Map.
	 */
	HashMap<String, NodeFigure>  nodes = new HashMap<String, NodeFigure>();
	
	/**
	 * On this figure all rooms are drawn.
	 */
	NodeLayerFigure nodeLayer;
	/**
	 * On this figure all figures which can be animated must be drawn.
	 * It is a transparent layer for the animations.
	 */
	AnimationLayerFigure animationLayer;
		
	/**
	 * 
	 * @return Figure - The Layer where the rooms are drawn onto. 
	 */
	public Figure getNodeLayer() {
		return this.nodeLayer;
	}
	
	/**
	 * 
	 * @return Figure - The Figure where the animations are drawn (AnimationLayerFigure).
	 */
	public Figure getAnimationLayer() {
		return this.animationLayer;
	}
	
	/**
	 * Returns a HasMap with all movable objects inside this RoomMap. 
	 * @return HashMap
	 */
	public HashMap<String, AnimationFigure> getMobileObjects() {
		return this.mobile_objects;
	}
	
	/**
	 * Returns the HashMap which contains all paths of all rooms in this RoomMap.
	 * Key of the {@link HashMap} is the room (RoomFigure). Value is an {@link ArrayList} with all paths (PathFigure)
	 * which are connected to this room.
	 * @return {@link HashMap}
	 */
	public HashMap<NodeFigure, ArrayList<PathFigure>> getPaths() {
		return this.paths;
	}
	/**
	 * Returns the HashMap which contains all rooms in this RoomMap.
	 * Key of the {@link HashMap} is the name of the room as {@link String}. Value is an {@link ArrayList} with all paths ({@link PathFigure})
	 * which are connected to this room.
	 * @return {@link HashMap}
	 */
	public HashMap<String, NodeFigure> getNodes() {
		return this.nodes;
	}
	/**
	 * Constructor to create a new RoomMap	
	 * @param {@link String} roomName - the name of the RoomMap. The name is displayed in the titlebar. 
	 */
	public NodeMap(Canvas c) {
		nodeLayer = new NodeLayerFigure();
	    animationLayer = new AnimationLayerFigure();
	    createNodeContents(c);
	}
	
	/**
	 * Returns the default display. Perhaps we must use another later, 
	 * that is why we let it stay here...
	 * @return {@link Display}
	 */
	public Display getDisplay() {
		return Display.getDefault();
	}
	
	/**O
	 * Initializes the lightweightsystem and the layers for drawing.
	 * @param {@link Canvas} c - the canvas where the lightweight system runs on.  
	 */
	private void createNodeContents(Canvas c) {		
		LightweightSystem lws = new LightweightSystem(c);
		BackgroundLayerdPane layers = new BackgroundLayerdPane();
	    lws.setContents(layers);
	    layers.add(nodeLayer);
	    layers.add(animationLayer);
	}
}