package de.atlassoft.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates the Composite for viewing a simulation.
 * 
 * @author Silvan Haeussermann
 */
public class SimulationComposite implements Observer {
	
	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite, buttonComposite,
					  simulationComposite, timeComposite;
	private I18NService I18N;
	private ApplicationService applicationService;
	private boolean simulationActive, simulationPaused;
	private Combo startDayCombo, startHourCombo, startMinuteCombo;
	private Label time;
	private SimpleDateFormat dateFormat;
	private Button playButton, stopButton;
	private Scale speedScale;
	
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
		simulationActive = false;
		dateFormat = new SimpleDateFormat("EEEE hh:mm");
		initUI();
	}
	
	/**
	 * Builds the elements of the UI of the simulation.
	 */
	private void initUI() {
		
		//Overall Composite
		simulationComposite = new Composite(mainComposite, SWT.BORDER);		
	    GridLayout simulationCompositeLayout = new GridLayout();
	    simulationCompositeLayout.numColumns = 2;
	    simulationCompositeLayout.horizontalSpacing = 100;
	    simulationComposite.setLayout(simulationCompositeLayout);
	    
	    //Composite for the control elements
	    Composite controllerComposite = new Composite(simulationComposite, SWT.BORDER);
	    controllerComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
	    controllerComposite.setLayout(new GridLayout());
	    
	    /*
	     * Composite for the selection of the starting time
	     */
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
		
		/*
		 * Composite for the controller buttons
		 */
		buttonComposite = new Composite(controllerComposite, SWT.BORDER);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		buttonComposite.setLayout(new GridLayout(1, false));
		
		//The play button
		playButton = new Button(buttonComposite, SWT.PUSH);
		playButton.setText("Simulation starten");
		playButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//checks if the simulation is active
				if (simulationActive) {
					//checks if the simulation is paused
					if (!simulationPaused) {
						simulationPaused = true;
						applicationService.pauseSimulation();
						switchButton();
					}
					else {
						simulationPaused = false;
						switchButton();
					}
				}
				else {
					Calendar startTime = new GregorianCalendar();
					int startHour = Integer.parseInt(startHourCombo.getItem(startHourCombo.getSelectionIndex()));
					int startMinute = Integer.parseInt(startMinuteCombo.getItem(startMinuteCombo.getSelectionIndex()));
					startTime.clear();
					startTime.set(0, 0, getDay(), startHour, startMinute);
					applicationService.startSimulation(startTime, SimulationComposite.this);
					disposeComposite(timeComposite);
					createTimeDisplay();
					switchButton();
					simulationActive = true;
					stopButton.setEnabled(true);
				}
			}
		});
		
		//The stop simulation button
		stopButton = new Button(buttonComposite, SWT.PUSH);
		stopButton.setText("Simulation stoppen");
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				applicationService.quitSimulation();
				simulationActive = false;
				playButton.setText("Simulation starten");
				buttonComposite.layout();
				stopButton.setEnabled(false);
			}
		});
		stopButton.setEnabled(false);
	    
		//The speed scale
		Composite speedScaleComposite = new Composite(controllerComposite, SWT.BORDER);
		speedScaleComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		speedScaleComposite.setLayout(new GridLayout());
		
		Label speedLabel = new Label(speedScaleComposite, SWT.NONE);
		speedLabel.setText("Geschwindigkeit:");
		
		speedScale = new Scale(speedScaleComposite, SWT.HORIZONTAL);
		speedScale.setMinimum(1);
		speedScale.setMaximum(10);
		speedScale.setIncrement(1);
		speedScale.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println(speedScale.getSelection());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		//The cancel button
		Button cancelButton = new Button(controllerComposite, SWT.PUSH);
		cancelButton.setText("Abbrechen");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//quits the simulation if it is active
				if (simulationActive) {
					//TODO: Statistics einfügen
					applicationService.quitSimulation();
				}
				
				HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, applicationService);		
	    		layout.topControl = homeScreenComposite.getComposite();
	    		mainComposite.layout();
	    		simulationComposite.dispose();
			}
		});
		
		/*
		 * The map on the right side
		 */
	    Canvas c = new Canvas(simulationComposite, SWT.FILL);
	    c.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
	    c.setBackground(ColorConstants.white);
	    c.setBounds(0, 0, 600, 500);
	    GridData gridData = new GridData();
	    gridData.horizontalSpan = 1;
	    gridData.widthHint = 600;
	    gridData.heightHint = 500;
	    c.setLayoutData(gridData);
	    
	    NodeMap map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
	    map.paintNodeMap(c);
	}
	
	/**
	 * Switches between the play and the pause button.
	 */
	private void switchButton() {
		if (simulationActive) {
			if (!simulationPaused) {
				playButton.setText("Simulation pausieren");
				buttonComposite.layout();
			}
			else {
				playButton.setText("Simulation fortsetzen");
				buttonComposite.layout();
			}
		}
		else {
			playButton.setText("Simulation pausieren");
			buttonComposite.layout();
		}
	}
	
	/**
	 * Updates the timeComposite so that the user could see the current
	 * time of the simulation
	 */
	private void createTimeDisplay() {
		Label timeDescription = new Label(timeComposite, SWT.NONE);
		timeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		timeDescription.setText("Uhrzeit:");
		
		time = new Label(timeComposite, SWT.NONE);
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

	/**
	 * Updater for the real time notification
	 */
	@Override
	public void update(Observable arg0, final Object arg1) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Calendar cal = (Calendar) arg1;
				time.setText(dateFormat.format(cal.getTime()));	
				timeComposite.layout();
			}
		}); 
	}
}
