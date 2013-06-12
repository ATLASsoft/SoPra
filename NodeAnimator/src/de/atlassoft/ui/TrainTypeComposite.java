package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

/**
 * This class creates the composite where you can see
 * all existing traintypes.
 * 
 * @author Silvan Haeussermann
 */
public class TrainTypeComposite {

	private Composite trainTypeComposite;
	
	/**
	 * Constructor for the TrainTypeComposite class
	 * 
	 * @param tabFolder
	 * 		The tab folder in which the composite should be placed.
	 */
	public TrainTypeComposite(TabFolder tabFolder){
		trainTypeComposite = new Composite (tabFolder, SWT.BORDER);
	}
	
	/**
	 * Returns the trainTypeComposite
	 * 
	 * @return
	 * 		The composite
	 */
	public Composite getComposite(){
		return trainTypeComposite;
	}	
}
