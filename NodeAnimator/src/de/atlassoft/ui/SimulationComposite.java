package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This class creates the Composite for viewing a simulation.
 * 
 * @author Silvan Haeussermann
 */
public class SimulationComposite {

	/**
	 * Creates the composite for running a simulation
	 * 
	 * @param shell
	 * 			The main shell of the application.
	 * @param mainComposite
	 * 			The main composite of the application.
	 * @return
	 * 			The simulation composite.
	 */
	public static Composite createSimulationComposite(Shell shell, Composite mainComposite){
		//TODO: Ausprogrammieren
		
	    Composite simualtionComposite = new Composite(mainComposite, SWT.NONE);
	    simualtionComposite.setLayout(new RowLayout());
	    Label label = new Label(simualtionComposite, SWT.NONE);
	    label.setText("simulationcomp");
	    label.pack();
		
		return simualtionComposite;
	}
}
