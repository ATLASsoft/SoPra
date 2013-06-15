package de.hohenheim.view.map;

import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;

/**
 * This class is a transparent {@link FreeformLayeredPane}.
 * It is used to draw the animations in a RoomMap.
 * @author Marc Fernandes
 *
 */
class AnimationLayerFigure extends FreeformLayeredPane {
	/**
	 * Constructor sets the background of the layer transparent.  
	 */
	public AnimationLayerFigure() {
	    setLayoutManager(new FreeformLayout());
	    setBorder(new MarginBorder(5));	    
	    setOpaque(false);
	  }
}
