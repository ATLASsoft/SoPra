package de.atlassoft.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;
import de.atlassoft.model.TrainType;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates a composite for creating a new schedule.
 * 
 * @author Silvan Haeussermann
 */
public class ScheduleComposite {

	//TODO: internationalisieren
	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite, scheduleComposite, spinnerComposite;
	private I18NService I18N;
	private NodeMap map;
	private ApplicationService applicationService;
	private Spinner spinner;
	private Button monday, tuesday, wednesday, thursday, friday,
			saturday, sunday;
	private Boolean addStationState = false;
	private List stationList;
	private Combo trainTypeCombo, hourCombo, minuteCombo;
	
	/**
	 * Constructor for the ScheduleComposite class.
	 * 
	 * @param shell
	 * 		The current shell.
	 * @param mainComposite
	 * 		The composite of the main window.
	 * @param layout
	 * 		The stack layout of the main window.
	 */
	public ScheduleComposite(Shell shell, Composite mainComposite, StackLayout layout, ApplicationService applicationService){
	
		this.shell = shell;
		this.layout = layout;
		this.mainComposite = mainComposite;
		I18N = I18NSingleton.getInstance();
		this.applicationService = applicationService;
		map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
		initUI();
	}
	
	/**
	 * Creates the UI elements of the ScheduleComposite
	 */
	private void initUI(){
		
		//the overall composite
		scheduleComposite = new Composite (mainComposite, SWT.BORDER);
		GridLayout scheduleCompositeLayout = new GridLayout();
		scheduleCompositeLayout.numColumns = 2;
		scheduleCompositeLayout.marginRight =  15;
		scheduleComposite.setLayout(scheduleCompositeLayout);
		
		//The composite for the controls
		Composite buttonComposite = new Composite(scheduleComposite, SWT.BORDER);
		GridData buttonCompositeData = new GridData();
		buttonCompositeData.horizontalAlignment = GridData.FILL;
		buttonCompositeData.grabExcessHorizontalSpace = true;
		buttonCompositeData.grabExcessVerticalSpace = true;
		buttonCompositeData.verticalAlignment = GridData.FILL;
		buttonComposite.setLayoutData(buttonCompositeData);
		GridLayout buttonCompositeLayout = new GridLayout();
		buttonCompositeLayout.numColumns = 2;
		buttonComposite.setLayout(buttonCompositeLayout);
		
		/*
		 * 1st row (name of the schedule)
		 */
		Label nameLabel = new Label(buttonComposite, SWT.NONE);
		nameLabel.setText("Name: ");
		
		Text nameField = new Text(buttonComposite, SWT.SINGLE);
		nameField.setToolTipText("Name des Fahrplans");
		
		/*
		 * 2nd row (traintype of the schedule)
		 */
		Label trainTypeLabel = new Label(buttonComposite, SWT.NONE);
		trainTypeLabel.setText("Zugtyp: ");
		
		trainTypeCombo = new Combo(buttonComposite, SWT.NULL);
		for (TrainType temp: applicationService.getModel().getTrainTypes()) {
			trainTypeCombo.add(temp.getName());
		}
		trainTypeCombo.select(0);
		
		/*
		 * 3rd row (departure time of the schedule)
		 */
		Label departureLabel = new Label(buttonComposite, SWT.NONE);
		departureLabel.setText("Abfahrtszeit: ");
		
		Composite timeComposite = new Composite(buttonComposite, SWT.NONE);
		timeComposite.setLayout(new RowLayout());
		
		hourCombo = new Combo(timeComposite, SWT.NULL);
		for (int i=0; i<25; i++) {
			hourCombo.add(Integer.toString(i));
		}
		hourCombo.select(0);
		
		Label pointLabel = new Label(timeComposite, SWT.NONE);
		pointLabel.setText(":");
		
		minuteCombo = new Combo(timeComposite, SWT.NULL);
		for (int i=0; i<=60; i=i+5) {
			minuteCombo.add(Integer.toString(i));
		}
		minuteCombo.select(0);
		
		/*
		 * 4th row (station selection)
		 */
		Composite stationButtonComposite = new Composite(buttonComposite, SWT.BORDER);
		RowLayout stationButtonCompositeLayout = new RowLayout();
		stationButtonCompositeLayout.type = SWT.VERTICAL;
		stationButtonComposite.setLayout(stationButtonCompositeLayout);
		
		Button addStation = new Button(stationButtonComposite, SWT.TOGGLE);
		addStation.setText("Station hinzufügen");
		addStation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (addStationState == true) {
					addStationState = false;
				} else {
					addStationState = true;
				}
			}
		});
		
		Button deleteStation = new Button(stationButtonComposite, SWT.PUSH);
		deleteStation.setText("Station entfernen");
		deleteStation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (stationList.getSelectionIndex() >= 0) {
					stationList.remove(stationList.getSelectionIndex());
				}
			}
		});
		
		stationList = new List(buttonComposite, SWT.BORDER|SWT.V_SCROLL);
		
		
		/*
		 * 5th row (repeat type of the schedule)
		 */
		Label repeatLabel = new Label (buttonComposite, SWT.NONE);
		repeatLabel.setText("Wiederholung :");
		
		final Combo repeatCombo = new Combo(buttonComposite, SWT.NULL);
		repeatCombo.add("Einmalig");
		repeatCombo.add("Minütlich");
		repeatCombo.add("Stündlich");
		repeatCombo.add("Täglich");
		repeatCombo.select(0);
		repeatCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//minutely ride
				if (repeatCombo.getSelectionIndex() == 1) {
					disposeComposite();
					createMinutelyRide();
				} 
				//hourly ride
				else if (repeatCombo.getSelectionIndex() == 2) {
					disposeComposite();
					createHourlyRide();
				}
				//daily ride
				else if (repeatCombo.getSelectionIndex() == 3) {
					disposeComposite();
//					createDailyRide();
					monday.setSelection(true);
					tuesday.setSelection(true);
					wednesday.setSelection(true);
					thursday.setSelection(true);
					friday.setSelection(true);
					saturday.setSelection(true);
					sunday.setSelection(true);
				} 
				//singular ride
				else {
					disposeComposite();
					createSingularRide();
				}
			}
		});
		
		/*
		 * 6th row (spinner for the repetition)
		 */
		spinnerComposite = new Composite(buttonComposite, SWT.NONE);
		spinnerComposite.setLayout(new RowLayout());
		GridData spinnerData = new GridData();
		spinnerData.horizontalSpan = 2;
		spinnerData.horizontalAlignment = GridData.FILL;
		spinnerComposite.setLayoutData(spinnerData);
		spinner = new Spinner (spinnerComposite, SWT.BORDER);
		spinner.setEnabled(false);
		
		/*
		 * 7th row (day selection)
		 */
		Label daysLabel = new Label(buttonComposite, SWT.NONE);
		daysLabel.setText("Tage :");
		
		Composite dayComposite = new Composite(buttonComposite, SWT.BORDER);
		GridLayout dayCompositeLayout = new GridLayout(4, false);
		dayComposite.setLayout(dayCompositeLayout);
		
		monday = new Button(dayComposite, SWT.CHECK);
		monday.setText("Mo");
		tuesday = new Button(dayComposite, SWT.CHECK);
		tuesday.setText("Di");
		wednesday = new Button(dayComposite, SWT.CHECK);
		wednesday.setText("Mi");
		thursday = new Button(dayComposite, SWT.CHECK);
		thursday.setText("Do");
		friday = new Button(dayComposite, SWT.CHECK);
		friday.setText("Fr");
		saturday = new Button(dayComposite, SWT.CHECK);
		saturday.setText("Sa");
		sunday = new Button(dayComposite, SWT.CHECK);
		sunday.setText("So");
		
		/*
		 * 8th row (save and cancel Buttons)
		 */
		Button save = new Button(buttonComposite, SWT.PUSH);
		save.setText("Speichern");
//		save.setImage(ImageHelper.getImage("loadButton"));
		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Get the schedule type
				ScheduleType scheduleType;
				if (repeatCombo.getSelectionIndex() == 0) {
					scheduleType = ScheduleType.SINGLE_RIDE;
				} else {
					scheduleType = ScheduleType.INTERVALL;
				}
				
				//Get the train type
				TrainType trainType = null;
				for (TrainType temp: applicationService.getModel().getTrainTypes()) {
					if (trainTypeCombo.getItem(trainTypeCombo.getSelectionIndex()).equals(temp.getName())) {
						trainType = temp;
					}
				}
				
				//Get the days
				java.util.List<Integer> days = new ArrayList<Integer>();
				if (monday.getSelection() == true) {
					days.add(Calendar.MONDAY);
				}
				if (tuesday.getSelection() == true) {
					days.add(Calendar.TUESDAY);
				}
				if (wednesday.getSelection() == true) {
					days.add(Calendar.WEDNESDAY);
				}
				if (thursday.getSelection() == true) {
					days.add(Calendar.THURSDAY);
				}
				if (friday.getSelection() == true) {
					days.add(Calendar.FRIDAY);
				}
				if (saturday.getSelection() == true) {
					days.add(Calendar.SATURDAY);
				}
				if (sunday.getSelection() == true) {
					days.add(Calendar.SUNDAY);
				} //TODO: Fehler abfangen
				
				//Get first ride
				//TODO: implementieren
				Calendar firstRide = new GregorianCalendar();
				int hour = Integer.parseInt(hourCombo.getItem(hourCombo.getSelectionIndex()));
				int minute = Integer.parseInt(minuteCombo.getItem(minuteCombo.getSelectionIndex()));
				firstRide.set(0, 0, 0, hour, minute);
				
				//Get the rail sys id
				String railSysID = applicationService.getModel().getActiveRailwaySys().getID();
				
				ScheduleScheme schedule = new ScheduleScheme(scheduleType, trainType, days, firstRide, railSysID);
				applicationService.addScheduleScheme(schedule);
				
				HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, layout, applicationService);		
	    		layout.topControl = homeScreenComposite.getComposite();
	    		mainComposite.layout();
			}
		});
		
		Button cancel = new Button(buttonComposite, SWT.PUSH);
		cancel.setText("Abbrechen");
//		cancel.setImage(ImageHelper.getImage("cancelIcon"));
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, layout, applicationService);		
	    		layout.topControl = homeScreenComposite.getComposite();
	    		mainComposite.layout();
			}
		});
		
		//the map on the right side of the screen
		Canvas c = new Canvas(scheduleComposite, SWT.FILL);
		GridData canvasData = new GridData();
		canvasData.horizontalAlignment = GridData.END;
		canvasData.grabExcessHorizontalSpace = true;
		canvasData.grabExcessVerticalSpace = true;
		canvasData.widthHint = 600;
		canvasData.heightHint = 500;
		c.setLayoutData(canvasData);
	    c.setBackground(ColorConstants.white);
	    c.setBounds(0, 0, 600, 500);
	    c.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				
			}

			@Override
			public void mouseDown(MouseEvent arg0) {

			}

			@Override
			public void mouseUp(MouseEvent e) {
				if (addStationState == true) {
					for (Node temp : applicationService.getModel().getActiveRailwaySys().getNodes()) {
						if (temp.getNodeFigure().getBounds().contains(new Point(e.x, e.y))) {
							stationList.add(temp.getName());
						}
					}
				}
			}	    	
	    });
	    map.paintNodeMap(c);
	}
	
	/**
	 * Creates the singular ride spinner.
	 */
	private void createSingularRide() {
		spinner = new Spinner(spinnerComposite, SWT.NONE);
		spinner.setEnabled(false);
		
		spinnerComposite.layout();
	}
	
	/**
	 * Creates the minutely ride spinner.
	 */
	private void createMinutelyRide() {
		Label label = new Label(spinnerComposite, SWT.NONE);
		label.setText("Alle ");
		
		spinner = new Spinner(spinnerComposite, SWT.BORDER);
		spinner.setMaximum(60);
		spinner.setMinimum(5);
		spinner.setIncrement(5);
		spinner.setSelection(5);
		
		Label label2 = new Label(spinnerComposite, SWT.NONE);
		label2.setText(" Minuten");
		
		spinnerComposite.layout();
	}
	
	/**
	 * Creates the hourly ride spinner.
	 */
	private void createHourlyRide() {
		Label label = new Label(spinnerComposite, SWT.NONE);
		label.setText("Alle ");
		
		spinner = new Spinner(spinnerComposite, SWT.BORDER);
		spinner.setMaximum(24);
		spinner.setMinimum(1);
		spinner.setIncrement(1);
		spinner.setSelection(1);
		
		Label label2 = new Label(spinnerComposite, SWT.NONE);
		label2.setText(" Stunden");
		
		spinnerComposite.layout();
	}
	
	//TODO: ggf löschen, falls nicht mehr gebraucht
	/**
	 * Creates the daily ride spinner.
	 */
	private void createDailyRide() {
		Label label = new Label(spinnerComposite, SWT.NONE);
		label.setText("Alle ");
		
		spinner = new Spinner(spinnerComposite, SWT.BORDER);
		spinner.setMaximum(7);
		spinner.setMinimum(1);
		spinner.setIncrement(1);
		spinner.setSelection(1);
		
		Label label2 = new Label(spinnerComposite, SWT.NONE);
		label2.setText(" Tage");
		
		spinnerComposite.layout();
	}
	
	/**
	 * Disposes all elements of the given composite.
	 */
	private void disposeComposite() {
		for (int i = 0; i < spinnerComposite.getChildren().length; i++) {
			spinnerComposite.getChildren()[i].dispose();
		}
		spinner.dispose();
	}
	
	/**
	 * Returns the scheduleComposite of this class.
	 * 
	 * @return
	 * 		The schedule Composite.
	 */
	public Composite getComposite(){
		return scheduleComposite;
	}
}
