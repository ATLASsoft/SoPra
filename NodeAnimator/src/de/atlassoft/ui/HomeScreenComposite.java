package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This class creates a composite for the home screen.
 * 
 * @author Silvan Haeussermann
 */
public class HomeScreenComposite {
	
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
	public static Composite createHomeScreenComposite(Shell shell, Composite mainComposite){
		//TODO: Ausprogrammieren

	    Composite homeScreenComposite = new Composite(mainComposite, SWT.NONE);
	    homeScreenComposite.setLayout(new RowLayout());
	    Label label = new Label(homeScreenComposite, SWT.NONE);
	    label.setText("homescreen");
	    label.pack();
		
		return homeScreenComposite;
	}
}
