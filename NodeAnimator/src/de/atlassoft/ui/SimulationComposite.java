package de.atlassoft.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import de.hohenheim.controller.ControllerCanvas;
import de.hohenheim.view.map.NodeMap;

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
		
	    Composite simulationComposite = new Composite(mainComposite, SWT.NONE);
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 3;
	    simulationComposite.setLayout(gridLayout);
	    
	    
	    Canvas c = new Canvas(simulationComposite, SWT.FILL);
	    c.setBackground(ColorConstants.white);
	    c.setBounds(0, 0, 600, 500);
	    GridData gridData = new GridData();
	    gridData.horizontalSpan = 3;
	    gridData.widthHint = 600;
	    gridData.heightHint = 500;
	    c.setLayoutData(gridData);
	    
	    Button play = new Button(simulationComposite, SWT.PUSH);
	    play.setSize(40, 50);
	    play.setLayoutData(new GridData());
	    
	    Button stop = new Button(simulationComposite, SWT.PUSH);
	    stop.setLayoutData(new GridData());
	    
	    NodeMap map = new NodeMap(c);
	    
	    new ControllerCanvas (mainComposite, SWT.FILL, map);
	    
		return simulationComposite;
	}
}
