package de.atlassoft.ui;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates the Composite for viewing a simulation.
 * 
 * @author Silvan Haeussermann
 */
public class SimulationComposite implements Observer {
	
	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite, simulationComposite, timeComposite;
	private I18NService I18N;
	private ApplicationService applicationService;
	private boolean simulationActive, simulationPaused;
	private Combo startDayCombo, startHourCombo, startMinuteCombo;
	private Label time;
	private SimpleDateFormat dateFormat;
	private Button playButton, pauseButton, stopButton;
	private Scale speedScale;
	private Text speedText;
	
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
		dateFormat = new SimpleDateFormat("EEEE HH:mm");
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
	    simulationCompositeLayout.horizontalSpacing = 50;
	    simulationComposite.setLayout(simulationCompositeLayout);
	    
	    //Composite for the control elements
	    Composite controllerComposite = new Composite(simulationComposite, SWT.NONE);
	    controllerComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
	    GridLayout controllerCompositeLayout = new GridLayout();
	    controllerCompositeLayout.verticalSpacing = 10;
	    controllerCompositeLayout.marginTop = 10;
	    controllerCompositeLayout.numColumns = 2;
	    controllerComposite.setLayout(controllerCompositeLayout);
	    
	    /*
	     * Composite for the selection of the starting time
	     */
	    createQuestionMark(controllerComposite, I18N.getMessage("SimulationComposite.Help.StartTime"));
	    
	    timeComposite = new Composite(controllerComposite, SWT.BORDER);
	    timeComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    GridLayout timeCompositeLayout = new GridLayout(3, false);
	    timeCompositeLayout.verticalSpacing = 20;
	    timeComposite.setLayout(timeCompositeLayout);
	    
	    Label departureLabel = new Label(timeComposite, SWT.NONE);
	    departureLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
	    departureLabel.setText(I18N.getMessage("SimulationComposite.BeginSimulation"));
	    
	    startDayCombo = new Combo(timeComposite, SWT.READ_ONLY);
	    startDayCombo.add(I18N.getMessage("SimulationComposite.Monday"));
	    startDayCombo.add(I18N.getMessage("SimulationComposite.Tuesday"));
	    startDayCombo.add(I18N.getMessage("SimulationComposite.Wednesday"));
	    startDayCombo.add(I18N.getMessage("SimulationComposite.Thursday"));
	    startDayCombo.add(I18N.getMessage("SimulationComposite.Friday"));
	    startDayCombo.add(I18N.getMessage("SimulationComposite.Saturday"));
	    startDayCombo.add(I18N.getMessage("SimulationComposite.Sunday"));
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
		 * The speed scale
		 */
		createQuestionMark(controllerComposite, I18N.getMessage("SimulationComposite.Help.SimulationSpeed"));
		
		Composite speedScaleComposite = new Composite(controllerComposite, SWT.BORDER);
		GridData speedScaleCompositeData = new GridData();
		speedScaleCompositeData.widthHint = 230;
		speedScaleComposite.setLayoutData(speedScaleCompositeData);
		speedScaleComposite.setLayout(new GridLayout(2, false));
		
		Label speedLabel = new Label(speedScaleComposite, SWT.NONE);
		speedLabel.setText(I18N.getMessage("SimulationComposite.SimulationSpeed"));
		
		speedText = new Text(speedScaleComposite, SWT.BORDER);
		speedText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		speedText.setEditable(false);
		speedText.setText(I18N.getMessage("SimulationComposite.RealTimeSpeed"));
		
		speedScale = new Scale(speedScaleComposite, SWT.HORIZONTAL);
		GridData speedScaleData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		speedScaleData.horizontalIndent = 5;
		speedScale.setLayoutData(speedScaleData);
		speedScale.setMinimum(1);
		speedScale.setMaximum(3000);
		speedScale.setIncrement(1);
		speedScale.setPageIncrement(2999);
		speedScale.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (speedScale.getSelection() > 1) {
					speedText.setText(String.valueOf(speedScale.getSelection()) + "x");
				}
				else {
					speedText.setText(I18N.getMessage("SimulationComposite.RealTimeSpeed"));
				}
				applicationService.setTimeLapse(speedScale.getSelection());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		speedScale.setEnabled(false);
		
		Label trueSpeed = new Label(speedScaleComposite, SWT.NONE);
		trueSpeed.setText(I18N.getMessage("SimulationComposite.RealTimeSpeed"));
		
		Label threeThousand = new Label(speedScaleComposite, SWT.NONE);
		threeThousand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		threeThousand.setText("3000x");
		
		/*
		 * Composite for the controller buttons
		 */
		createQuestionMark(controllerComposite, I18N.getMessage("SimulationComposite.Help.Buttons"));
		
		Composite buttonComposite = new Composite(controllerComposite, SWT.BORDER);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		RowLayout buttonCompositeLayout = new RowLayout();
		buttonCompositeLayout.justify = true;
		buttonComposite.setLayout(buttonCompositeLayout);
		
			//The play button
		playButton = new Button(buttonComposite, SWT.PUSH);
		playButton.setImage(ImageHelper.getImage("playIconSmall"));
		playButton.setToolTipText(I18N.getMessage("SimulationComposite.SimulationStart"));
		playButton.setLayoutData(new RowData(43, 40));
		playButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//If there is no ongoing active simulation
				if(!simulationActive) {
					//start simulation
					startSimulation();
					
					speedScale.setEnabled(true);
					pauseButton.setEnabled(true);
					stopButton.setEnabled(true);
					playButton.setEnabled(false);
					simulationActive = true;
				}
				else {
					//If the simulation is paused
					if(simulationPaused) {
						//continue simulation
						applicationService.continueSimulation();
						
						pauseButton.setSelection(false);
						playButton.setEnabled(false);
						simulationPaused = false;
					}
				}
			}
		});
		
			//The pause button
		pauseButton = new Button(buttonComposite, SWT.TOGGLE);
		pauseButton.setImage(ImageHelper.getImage("pauseIconSmall"));
		pauseButton.setToolTipText(I18N.getMessage("SimulationComposite.SimulationPause"));
		pauseButton.setLayoutData(new RowData(43, 40));
		pauseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//If the simulation is not paused
				if(!simulationPaused) {
					//pause simulation
					applicationService.pauseSimulation();
					
					playButton.setEnabled(true);
					simulationPaused = true;
				}
				//If the simulation is paused
				else {
					//continue simulation
					applicationService.continueSimulation();
					
					playButton.setEnabled(false);
					simulationPaused = false;
				}
			}
		});
		pauseButton.setEnabled(false);
		
			//The stop button
		stopButton = new Button(buttonComposite, SWT.PUSH);
		stopButton.setImage(ImageHelper.getImage("stopIconSmall"));
		stopButton.setToolTipText(I18N.getMessage("SimulationComposite.SimulationStop"));
		stopButton.setLayoutData(new RowData(43, 40));
		stopButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(simulationActive) {
					//stop simulation
					applicationService.quitSimulation();
					createTimeDisplay();
					
					speedScale.setSelection(1);
					speedScale.setEnabled(false);
					pauseButton.setSelection(false);
					pauseButton.setEnabled(false);
					playButton.setEnabled(true);
					stopButton.setEnabled(false);
					simulationActive = false;
				}
			}
		});
		stopButton.setEnabled(false);
		
		/*
		 * The cancel button
		 */
		Button cancelButton = new Button(controllerComposite, SWT.PUSH);
		cancelButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		cancelButton.setText(I18N.getMessage("SimulationComposite.Cancel"));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//quits the simulation if it is active
				if (simulationActive) {
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
	 * Starts the simulation.
	 */
	private void startSimulation() {
		Calendar startTime = new GregorianCalendar();
		int startHour = Integer.parseInt(startHourCombo.getItem(startHourCombo.getSelectionIndex()));
		int startMinute = Integer.parseInt(startMinuteCombo.getItem(startMinuteCombo.getSelectionIndex()));
		startTime.clear();
		startTime.set(0, 0, getDay(), startHour, startMinute);
		applicationService.startSimulation(startTime, SimulationComposite.this);
		createTimeDisplay();
	}
	
	/**
	 * Updates the timeComposite so that the user could see the current
	 * time of the simulation
	 */
	private void createTimeDisplay() {
		if(!simulationActive) {
			disposeComposite(timeComposite);
			
			Label timeDescription = new Label(timeComposite, SWT.NONE);
			timeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			timeDescription.setText("Uhrzeit:");
			
			time = new Label(timeComposite, SWT.NONE);
		}
		else {
			disposeComposite(timeComposite);
			
			Label departureLabel = new Label(timeComposite, SWT.NONE);
		    departureLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		    departureLabel.setText("Beginn der Simulation:");
		    
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
			
			timeComposite.layout();
		}
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
	
	/**
	 * Creates a help icon that shows some tips when hovering over it.
	 * 
	 * @param composite
	 * 			The parent composite of the icon.
	 * @param message
	 * 			The message that should be displayed.
	 */
	public void createQuestionMark(Composite composite, String message) {
		final Shell tipShell = new Shell(shell, SWT.TOOL|SWT.ON_TOP);
		tipShell.setVisible(false);
		tipShell.setLayout(new RowLayout());
		
		Label information = new Label(tipShell, SWT.NONE);
		information.setText(message);
		tipShell.layout();
		tipShell.setSize(information.getSize().x + 10, information.getSize().y + 10);
		
		final Label questionLabel = new Label(composite, SWT.NONE);
		questionLabel.setImage(ImageHelper.getImage("questionMarkSmall"));
		questionLabel.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseEnter(MouseEvent e) {
				PointerInfo a = MouseInfo.getPointerInfo();
				java.awt.Point b = a.getLocation();
				tipShell.setLocation(b.x + 15, b.y + 20);
				tipShell.setVisible(true);
			}
			
			public void mouseExit(MouseEvent e) {
				tipShell.setVisible(false);
			}
		});
	}
}
