package de.hohenheim.view;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.mobile.TrainFigure;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.CenterAnchor;
import de.hohenheim.view.path.PathFigure;

/**
 * @author Marc Fernandes
 * 
 * This Class is the factory class for every Figure. It should be used to create Figures in a RoomMap.
 * Please use only this class to create Figures!
 *
 */
public class FigureFactory {
	
	/**
	 * This class creates a new Room in a RoomMap.
	 * 
	 * @param map The RoomMap where this room should be added.
	 * @param room_name A <b>unique</b> name for the room
	 * @param x The x_position, where the room should be located in the RoomMap.
	 * @param y The y_position, where the room should be located in the RoomMap.
	 * @param width The Width of the room.
	 * @param height The height of the room.
	 * @return Roomfigure The room after it was created and attached to a RoomMap.
	 */
	 
	public static NodeFigure createNode(NodeMap map, String room_name, int x, int y, int width, int height, Object modellObject) {    
		  NodeFigure node = new NodeFigure(modellObject);
		  node.setName(room_name);    
		  node.setBounds(new Rectangle(x, y, width, height));	  
		  map.getNodeLayer().add(node);
		  map.getPaths().put(node, new ArrayList<PathFigure>());
		  map.getNodes().put(room_name, node);
		  return node;
	}
	
	public static NodeFigure createNode(NodeMap map, String room_name, int x, int y, int width, int height, Object modellObject, Image backgroundImage) {    
		  NodeFigure node = new NodeFigure(modellObject, backgroundImage);
		  node.setName(room_name);    
		  node.setBounds(new Rectangle(x, y, width, height));	  
		  map.getNodeLayer().add(node);
		  map.getPaths().put(node, new ArrayList<PathFigure>());
		  map.getNodes().put(room_name, node);
		  return node;
	}
	
	
	
	/**
	 * 
	 * @param map The RoomMap where this fork truck should be added.
	 * @param in_room The Room where the fork truck should be positioned at first.
	 * @param a unique id for the fork truck! Developer must ensure this...
	 * @return ForkTruckFigure The fork truck after creation.
	 */
	public static TrainFigure createTrainFigure(final NodeMap map, final NodeFigure in_node, final int train_id, final Object modellObject) {
		final TrainFigure mobile = new TrainFigure(map, in_node, train_id,
				modellObject);
		Rectangle b = in_node.getBounds();
		mobile.setBounds(new Rectangle(b.x + b.width / 2 - 16, b.y + b.height
				/ 2 - 12, 37, 24));
		
		map.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				map.getAnimationLayer().add(mobile);
				map.getMobileObjects().put("" + train_id, mobile);
			}
		});
		
		return mobile;
	}
	
	/**
	 * 
	 * @param map - The RoomMap where this path should be added.
	 * @param start - The start room from which the path should be drawn.
	 * @param end - The end room to which the path should be drawn.
	 * @param modellObject - The modell object belonging to this PathFigure.
	 * @return {@link PathFigure} - The path after it is created, connected to the rooms and added to the RoomMap.
	 */
	public static PathFigure createPath(NodeMap map, NodeFigure start, NodeFigure end, Object modellObject) {
		  PathFigure path = new PathFigure(modellObject);		  
		  path.setSourceAnchor(new CenterAnchor(start)); //new ChopboxAnchor(start)		  
		  path.setTargetAnchor(new CenterAnchor(end));//new ChopboxAnchor(end)
		  map.getNodeLayer().add(path);
		  map.getPaths().get(start).add(path);
		  map.getPaths().get(end).add(path);	
		  map.getNodeLayer().remove(start);
		  map.getNodeLayer().add(start);
		  map.getNodeLayer().remove(end);
		  map.getNodeLayer().add(end);
		  
		  return path;
	}
	
}
