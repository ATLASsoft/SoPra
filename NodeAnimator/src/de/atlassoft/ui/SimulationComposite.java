package de.atlassoft.ui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.hohenheim.controller.ControllerCanvas;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates the Composite for viewing a simulation.
 * 
 * @author Silvan Haeussermann
 */
public class SimulationComposite {
	
	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite;
	private Composite simulationComposite, timeComposite;
	private I18NService I18N;
	private ApplicationService applicationService;
	private boolean simulationPaused;
	private Combo startDayCombo, startHourCombo, startMinuteCombo;
	
	/**
	 * Constructor for the SimulationComposite class.
	 * 
	 * @param shell
	 * 		The current shell.
	 * @param mainComposite
	 * 		The composite of the main window.
	 * @param layout
	 * 		The stack layout of the main window.
	 * @param applicationService
	 * 		The ApplicationService of the program.
	 */
	public SimulationComposite (Shell shell, Composite mainComposite, StackLayout layout, ApplicationService applicationService) {
		this.applicationService = applicationService;
		this.shell = shell;
		this.layout = layout;
		this.mainComposite = mainComposite;
		I18N = I18NSingleton.getInstance();
		simulationPaused = false;
		initUI();
	}
	
	/**
	 * Builds the elements of the UI of the simulation.
	 */
	private void initUI() {
		
		//Overall Composite
		simulationComposite = new Composite(mainComposite, SWT.BORDER);		
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.horizontalSpacing = 20;
	    gridLayout.numColumns = 2;
	    simulationComposite.setLayout(gridLayout);
	    
	    //Composite for the control elements
	    Composite controllerComposite = new Composite(simulationComposite, SWT.BORDER);
	    controllerComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true));
	    controllerComposite.setLayout(new GridLayout());
	    
	    //Composite for the selection of the starting time
	    timeComposite = new Composite(controllerComposite, SWT.BORDER);
	    timeComposite.setLayout(new GridLayout(3, false));
	    
	    Label departureLabel = new Label(timeComposite, SWT.NONE);
	    departureLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
	    departureLabel.setText("Beginn der Simulation");
	    
	    startDayCombo = new Combo(timeComposite, SWT.READ_ONLY);
	    startDayCombo.add("Montag");
	    startDayCombo.add("Dienstag");
	    startDayCombo.add("Mittwoch");
	    startDayCombo.add("Donnerstag");
	    startDayCombo.add("Freitag");
	    startDayCombo.add("Samstag");
	    startDayCombo.add("Sonntag");
	    startDayCombo.select(0);
	    
	    startHourCombo = new Combo(timeComposite, SWT.READ_ONLY);
	    startHourCombo.add("00");
	    startHourCombo.add("01");
	    startHourCombo.add("02");
		startHourCombo.add("03");
		startHourCombo.add("04");
		startHourCombo.add("05");
		startHourCombo.add("06");
		startHourCombo.add("07");
		startHourCombo.add("08");
		startHourCombo.add("09");
		for (int i=10; i<24; i++) {
			startHourCombo.add(Integer.toString(i));
		}
		startHourCombo.select(1);
		
		startMinuteCombo = new Combo(timeComposite, SWT.READ_ONLY);
		startMinuteCombo.add("00");
		startMinuteCombo.add("01");
		startMinuteCombo.add("02");
		startMinuteCombo.add("03");
		startMinuteCombo.add("04");
		startMinuteCombo.add("05");
		startMinuteCombo.add("06");
		startMinuteCombo.add("07");
		startMinuteCombo.add("08");
		startMinuteCombo.add("09");
		for (int i=10; i<=59; i=i+1) {
			startMinuteCombo.add(Integer.toString(i));
		}
		startMinuteCombo.select(0);
		
		//Composite for the controller buttons
		Composite buttonComposite = new Composite(controllerComposite, SWT.BORDER);
		buttonComposite.setLayout(new GridLayout(4, false));
		
		Button playButton = new Button(buttonComposite, SWT.PUSH);
		playButton.setText("Play");
		playButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!simulationPaused) {
					Calendar startTime = new GregorianCalendar();
					int startHour = Integer.parseInt(startHourCombo.getItem(startHourCombo.getSelectionIndex()));
					int startMinute = Integer.parseInt(startMinuteCombo.getItem(startMinuteCombo.getSelectionIndex()));
					startTime.clear();
					startTime.set(0, 0, getDay(), startHour, startMinute);
					applicationService.startSimulation(startTime);
					disposeComposite(timeComposite);
				}
				else {
					applicationService.continueSimulation();
					simulationPaused = false;
				}
			}
		});
		
		Button pauseButton = new Button(buttonComposite, SWT.PUSH);
		pauseButton.setText("Pause");
		pauseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationService.pauseSimulation();
				simulationPaused = true;
			}
		});
	    
	    Canvas c = new Canvas(simulationComposite, SWT.FILL);
	    c.setBackground(ColorConstants.white);
	    c.setBounds(0, 0, 600, 500);
	    GridData gridData = new GridData();
	    gridData.horizontalSpan = 1;
	    gridData.widthHint = 600;
	    gridData.heightHint = 500;
	    c.setLayoutData(gridData);
	    
	    NodeMap map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
	    map.paintNodeMap(c);
	    
	    new ControllerCanvas (mainComposite, SWT.FILL, map);		
	}
	
	/**
	 * Updates the timeComposite so that the user could see the current
	 * time of the simulation
	 */
	private void createTimeDisplay() {
		
	}
	
	/**
	 * Disposes all children of the given composite.
	 * 
	 * @param composite
	 */
	private void disposeComposite(Composite composite) {
		for (Control kid:composite.getChildren()) {
			kid.dispose();
		}
	}
	
	/**
	 * Checks the combo box with the days.
	 * 
	 * @return
	 * 		The day which is selected
	 */
	private int getDay() {
		switch (startDayCombo.getItem(startDayCombo.getSelectionIndex())) {
			case "Montag": return 5;
			case "Dienstag": return 6;
			case "Mittwoch": return 7;
			case "Donnerstag": return 8;
			case "Freitag": return 9;
			case "Samstag": return 10;
			case "Sonntag": return 11;
			default: return 5;
		}
	}
	
	/**
	 * Returns the composite of the simulation screen.
	 * 
	 * @return
	 * 		The simulation composite.
	 */
	public Composite getComposite() {
		return simulationComposite;
	}
}
