package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

/**
 * This class creates a composite where you can see
 * all the schedules of the current railway system.
 * 
 * @author Silvan Haeussermann
 */
public class AllSchedulesComposite {
	
	private Composite allScheduleComposite;
	
	/**
	 * Constructor for the class AllScheduleComposite
	 * 
	 * @param tabFolder
	 */
	public AllSchedulesComposite(TabFolder tabFolder){
		allScheduleComposite = new Composite(tabFolder, SWT.BORDER);
	}
	
	/**
	 * Returns the allScheduleComposite.
	 * 
	 * @return
	 * 		The composite.
	 */
	public Composite getComposite(){
		return allScheduleComposite;
	}
}
