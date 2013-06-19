package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This class creates the composite for creating a new railsystem.
 * 
 * @author Silvan Haeussermann
 */
public class RailSysComposite {

	/**
	 * The method createHomeScreenComposite creates the composite for the
	 * homescreen, which is shown when the application starts.
	 * 	
	 * @param shell
	 * 			The main shell of the application.
	 * @param mainComposite
	 * 			The main composite of the application
	 * @return
	 * 			The homescreen composite.
	 */
	public static Composite createRailSysComposite(Shell shell, Composite mainComposite){
		//TODO: Ausprogrammieren
		
	    Composite railSysComposite = new Composite(mainComposite, SWT.BORDER);
	    railSysComposite.setLayout(new RowLayout());
	    Label label = new Label(railSysComposite, SWT.NONE);
	    label.setText("railsyscomp");
	    label.pack();
		
		return railSysComposite;
	}
}
