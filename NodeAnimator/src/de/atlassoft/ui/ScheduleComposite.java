package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This class creates a composite for creating a new schedule.
 * 
 * @author Silvan
 */
public class ScheduleComposite {

	/**
	 * Creates the Composite for creating a schedule.
	 * 
	 * @param shell
	 * 			The main shell of the application.
	 * @param mainComposite
	 * 			The main composite of the application
	 * @return
	 * 			The schedule composite.
	 */
	public static Composite createScheduleComposite(Shell shell, Composite mainComposite){
		//TODO: Ausprogrammieren
		
	    Composite scheduleComposite = new Composite(mainComposite, SWT.NONE);
	    scheduleComposite.setLayout(new RowLayout());
	    Label label = new Label(scheduleComposite, SWT.NONE);
	    label.setText("scheduleComposite");
	    label.pack();
		
		return scheduleComposite;
	}
}
