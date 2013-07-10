package de.hohenheim.view.path;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.LineAttributes;

import de.atlassoft.model.Path;
import de.hohenheim.view.mobile.Utility;

/**
 * This class is used to draw paths between rooms in a RoomMap.
 * @author Marc Fernandes
 *
 */
public class PathFigure extends PolylineConnection {
	 
	  /**
	   * The thickness of the polyline which is drawn.
	   */
	  public static int LINE_WIDTH = 3; 
	  
	  /**
	   * A Object that is connected to this {@link PathFigure} as the modell.
	   */
	  private Path modellObject;
	  
	  public PathFigure() {
		BendpointConnectionRouter router = new BendpointConnectionRouter();
		setConnectionRouter(router);
		LineAttributes attr = new LineAttributes(LINE_WIDTH);
		this.setLineAttributes(attr);
	  }
	  
	  /**
	   * The constructor sets a {@link BendpointConnectionRouter} to draw the path
	   * as a line.  
	   */
	  public PathFigure(Path modellObject) {
		this();
		this.modellObject = modellObject;
	    StringBuilder toolTip = new StringBuilder();
	    toolTip.append("Höchstgeschwindigkeit: ");
	    toolTip.append(Math.round(modellObject.getTopSpeed()));
	    toolTip.append(" km/h");
//	    toolTip.append("Länge: "); //TODO: wtf
//	    toolTip.append(Math.round(this.getDistance() * 100));
//	    toolTip.append(" Meter");
	    this.setToolTip(new Label(toolTip.toString()));
	  }
	  
	  /**
	   * Sets the modellObject for this {@link PathFigure}.
	   * @param modellObject - The model object that belongs to this PathFigure.
	   */
	  public void setModellObject(Path modellObject) {
		  this.modellObject = modellObject;
	  }
	  
	  /**
	   * Returns the modellObject of this PathFigure.
	   * @return {@link Object}
	   */
	  public Path getModellObject() {
		  return this.modellObject;
	  }	  
	  
	  /**
	   * Calculates and returns the distance of this path.
	   * @return double
	   */
	  public double getDistance() {
		  return Utility.getDistance(this);
	  }
	}