package de.hohenheim.view.map;

import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;

/**
 * This class is a container to draw the rooms in a RoomMap.
 * @author Marc Fernandes
 *
 */
class NodeLayerFigure extends FreeformLayeredPane {
	  public NodeLayerFigure() {
	    setLayoutManager(new FreeformLayout());
	    setBorder(new MarginBorder(5));	    
	  }
	}