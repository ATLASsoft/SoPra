package de.hohenheim.view.path;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class CenterAnchor extends ChopboxAnchor {

	public CenterAnchor(IFigure fig) {
		super(fig);
	}
	
	protected Rectangle getBox() {
		Rectangle tmp = getOwner().getBounds();
		Rectangle r = new Rectangle(tmp.x+tmp.width/2,tmp.y+tmp.height/2,0,0);
		return r;
	}
	
	public Point getReferencePoint() {
		Rectangle r = this.getOwner().getBounds();
		return new Point(r.x+r.width/2,r.y+r.height/2);
	}
}
