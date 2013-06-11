package de.hohenheim.view.map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;


/**
 * @author Marc Fernandes
 * 
 * This class extends a LayeredPane to draw a grid and a vertical gradient in the background. 
 */
public class BackgroundLayerdPane extends LayeredPane {

	
	/**
	 * This method overwrites the paint method of LayeredPane @see LayeredPane.
	 */
	@Override
	public void paint(Graphics graphics) {
		Rectangle r = this.getBounds();
		graphics.setForegroundColor(ColorConstants.titleGradient);
		graphics.setBackgroundColor(ColorConstants.white);
		
		graphics.fillRectangle(r);
		
		graphics.fillGradient(r, true);		
		for(int i=25; i<r.width; i+=25) {
			Point p1= new Point(i,0);
			Point p2= new Point(i,r.height);
			graphics.drawLine(p1, p2);
			graphics.setForegroundColor(ColorConstants.red);
			graphics.drawText(i+"", p1);
			graphics.setForegroundColor(ColorConstants.titleGradient);
		}
		for(int i=25; i<r.height; i+=25) {
			Point p1= new Point(0,i);
			Point p2= new Point(r.width,i);
			graphics.drawLine(p1, p2);
			graphics.setForegroundColor(ColorConstants.red);
			graphics.drawText(i+"", p1);
			graphics.setForegroundColor(ColorConstants.titleGradient);
		}
		
		//#2269B5
		Color c = new Color(null, new RGB(22, 69, 120));
		graphics.setForegroundColor(c);
		//graphics.setAlpha(50);
		
		super.paint(graphics);
	}	
}
