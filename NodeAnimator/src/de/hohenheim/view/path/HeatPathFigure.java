package de.hohenheim.view.path;

import org.eclipse.swt.graphics.LineAttributes;

import de.atlassoft.util.ColorHelper;


/**
 * This subclass is for drawing the paths in the heatmap.
 *
 * @author Silvan Haussermann
 *
 */
public class HeatPathFigure extends PathFigure {

	private LineAttributes attr;
	
	/**
	 * The constructor for the class HeatPathFigure.
	 * Defines the line thickness and the color
	 * depending on the relative workload.
	 * 
	 * @param workload
	 * 		The relative workload.
	 */
	public HeatPathFigure(float workload) {
		super();
		
		if (workload <= 0.0) {
			attr = new LineAttributes(2);
			super.setForegroundColor(ColorHelper.getColor("yellow"));
		}
		else if (workload > 0.0 && workload <= 0.25) {
			attr = new LineAttributes(4);
			super.setForegroundColor(ColorHelper.getColor("orange"));
		}
		else if (workload > 0.25 && workload <= 0.5) {
			attr = new LineAttributes(6);
			super.setForegroundColor(ColorHelper.getColor("darkOrange"));
		}
		else if (workload > 0.5 && workload <= 0.75) {
			attr = new LineAttributes(8);
			super.setForegroundColor(ColorHelper.getColor("red"));
		}
		else {
			attr = new LineAttributes(10);
			super.setForegroundColor(ColorHelper.getColor("darkRed"));
		}
		
		super.setLineAttributes(attr);
	}
  
}