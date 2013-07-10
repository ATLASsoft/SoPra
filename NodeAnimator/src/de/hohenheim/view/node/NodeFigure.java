package de.hohenheim.view.node;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import de.atlassoft.model.Node;

/**
 * This class is for drawing of a room.
 * @author Marc Fernandes
 *
 */
public class NodeFigure extends Figure {
	private String name = new String();
	
	private Node modellObject;
	
	private Image backgroundImage  =null;
	
	/**
	 * Constructor. Creates a room with a name.
	 * The Name will be displayed in the center of the figure.
	 * @param String roomName 
	 */
	public NodeFigure(Node modellObject) {
		setModelObject(modellObject);
	}
	
	public NodeFigure(Node modellObject, Image backgroundImg) {
		this(modellObject);
		this.backgroundImage=backgroundImg;
	}
	
	/**
	 * Returns the model object of this RoomFigure.
	 * @return {@link Object}
	 */
	public Node getModellObject() {
		return this.modellObject;
	}
	/**
	 * Sets the Object which is the model part of this view object .
	 * @param modellObject - The model object which belongs to this RoomFigure.
	 */
	public void setModelObject(Node modellObject) {
		this.modellObject = modellObject;
	}
	
	/**
	 * Sets the name of the room, which then is beeing displayed.
	 * @param roomName - the name of the room.
	 */
	public void setName(String roomName) {
	    name = roomName;
	    setToolTip(new Label(name));
	    repaint();
	}	
	/**
	 * Returns the name of the room.
	 * 
	 * @return String name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Draws a rectangle which represents a room in a RoomMap.
	 * The name of the room will be placed in the middle of the rectangle.
	 */
	public void paintFigure(Graphics g) {
      Rectangle r = bounds;
      
      Rectangle r_tmp = r.getCopy();
      g.setBackgroundColor(ColorConstants.lightGray);
      g.fillRectangle(r_tmp);
      
      if(backgroundImage!=null) {
        g.drawImage(backgroundImage, r_tmp.x,r_tmp.y);
      }
      g.setForegroundColor(ColorConstants.darkGray);
      g.drawRectangle(r.x, r.y, r.width-1, r.height-1);
      
      g.setForegroundColor(ColorConstants.white);
      
      String nam = name;
      if (nam.length() > 2) {
    	  nam = nam.substring(0, 2);
      }
      Font f = g.getFont();
      Dimension dim = FigureUtilities.getStringExtents(nam, f);
      g.drawText(nam, r.x+r.width/2-dim.width/2 , r.y+r.height/2-dim.height/2);
  }  
}