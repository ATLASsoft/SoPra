package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

/**
 * This class creates a composite in which the current railway
 * system is shown.
 * 
 * @author Silvan Haeussermnn
 */
public class CurrentRailSysComposite {
	
	private Composite currentRailSysComposite;
	
	/**
	 * Constructor for the class CurrentRailSysComposite
	 * 
	 * @param tabFolder
	 * 		The tab folder in which the composite should be placed.
	 */
	public CurrentRailSysComposite(TabFolder tabFolder) {
		currentRailSysComposite = new Composite(tabFolder, SWT.BORDER);
	}
	
	/**
	 * Returns the currentRailSysComposite.
	 * 
	 * @return
	 * 		The composite.
	 */
	public Composite getComposite(){
		return currentRailSysComposite;
	}	
}
