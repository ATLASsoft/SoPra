package de.atlassoft.ui;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
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
import de.atlassoft.util.ImageHelper;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates a composite for creating a new schedule.
 * 
 * @author Silvan Haeussermann
 */
public class ScheduleComposite {

	//TODO: evtl. Button Bilder einfügen
	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite, scheduleComposite, departureComposite, buttonComposite;
	private I18NService I18N;
	private NodeMap map;
	private ApplicationService applicationService;
	private Button monday, tuesday, wednesday, thursday, friday,
			saturday, sunday, save, deleteStation;
	private Boolean addStationState = false;
	private Spinner delaySpinner, timeSpinner;
	private List stationList, firstStationList, lastStationList;
	private Combo trainTypeCombo, hourCombo, minuteCombo;
	private Label errorField;
	private Text nameField;
	private java.util.List<Integer> idleTime, arrivalTime;
	private java.util.List<Node> nodeList;
	
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
	public ScheduleComposite(Shell shell, Composite mainComposite, StackLayout layout, ApplicationService applicationService) {
	
		this.shell = shell;
		this.layout = layout;
		this.mainComposite = mainComposite;
		I18N = I18NSingleton.getInstance();
		this.applicationService = applicationService;
		map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
		idleTime = new ArrayList<Integer>();
		arrivalTime = new ArrayList<Integer>();
		nodeList = new ArrayList<Node>();
		initUI();
	}
	
	/**
	 * Creates the UI elements of the ScheduleComposite
	 */
	private void initUI() {
		//the overall composite
		scheduleComposite = new Composite (mainComposite, SWT.BORDER);
		GridLayout scheduleCompositeLayout = new GridLayout();
		scheduleCompositeLayout.numColumns = 2;
		scheduleCompositeLayout.marginRight =  15;
		scheduleComposite.setLayout(scheduleCompositeLayout);
		
		//The composite for the controls
		buttonComposite = new Composite(scheduleComposite, SWT.BORDER);
		GridData buttonCompositeData = new GridData();
		buttonCompositeData.horizontalAlignment = GridData.FILL;
		buttonCompositeData.grabExcessHorizontalSpace = true;
		buttonCompositeData.grabExcessVerticalSpace = true;
		buttonCompositeData.verticalAlignment = GridData.FILL;
		buttonComposite.setLayoutData(buttonCompositeData);
		GridLayout buttonCompositeLayout = new GridLayout();
		buttonCompositeLayout.numColumns = 3;
		buttonComposite.setLayout(buttonCompositeLayout);
		
		/*
		 * 1st row (name of the schedule)
		 */
		createQuestionMark(buttonComposite, I18N.getMessage("ScheduleComposite.Help.Name"));
		
		Label nameLabel = new Label(buttonComposite, SWT.NONE);
		nameLabel.setText(I18N.getMessage("ScheduleComposite.NameLabel"));
		
		Composite textComposite = new Composite(buttonComposite, SWT.NONE);
		textComposite.setLayout(new RowLayout(SWT.VERTICAL));
		nameField = new Text(textComposite, SWT.SINGLE|SWT.BORDER);
		nameField.setLayoutData(new RowData(80, 15));
		nameField.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event e) {
		    	Boolean twice = false;
		    	if (nameField.getCharCount() == 0) {
		    		errorField.setText(I18N.getMessage("ScheduleComposite.ErrorField.NoName"));
		    		errorField.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		    		save.setEnabled(false);
		    		return;
		    	}
		    	if (e.keyCode != 8) {
		    		for (ScheduleScheme schedule : applicationService.getModel().getActiveScheduleSchemes()) {
		    			if (schedule.getID().toLowerCase().equals(nameField.getText().toLowerCase().trim())) {
		    				twice = true;
		    			}
		    		}
		    	}
		    	if (twice == true) {
		    		errorField.setVisible(true);
		    		errorField.setText(I18N.getMessage("ScheduleComposite.ErrorField.NameNotAvailable"));
		    		errorField.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		   		  	save.setEnabled(false);
		    	} else {
		    		errorField.setText(I18N.getMessage("ScheduleComposite.ErrorField.NameAvailable"));
		    		errorField.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		    		save.setEnabled(true);
		    	}
			}
		});
		
		
		errorField = new Label(textComposite, SWT.SINGLE);
		errorField.setText(I18N.getMessage("ScheduleComposite.ErrorField.NoName"));
		errorField.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		/*
		 * 2nd row (traintype of the schedule)
		 */
		createQuestionMark(buttonComposite, I18N.getMessage("ScheduleComposite.Help.TrainType"));
		
		Label trainTypeLabel = new Label(buttonComposite, SWT.NONE);
		trainTypeLabel.setText(I18N.getMessage("ScheduleComposite.TrainTypeLabel"));
		
		trainTypeCombo = new Combo(buttonComposite, SWT.READ_ONLY);
		for (TrainType temp: applicationService.getModel().getTrainTypes()) {
			trainTypeCombo.add(temp.getName());
		}
		trainTypeCombo.select(0);
		
		/*
		 * 3rd row (repeat type of the schedule)
		 */
		createQuestionMark(buttonComposite, I18N.getMessage("ScheduleComposite.Help.Repetition"));
		
		Label repeatLabel = new Label (buttonComposite, SWT.NONE);
		repeatLabel.setText(I18N.getMessage("ScheduleComposite.RepeatLabel"));
		
		final Combo repeatCombo = new Combo(buttonComposite, SWT.READ_ONLY);
		
		repeatCombo.add(I18N.getMessage("ScheduleComposite.RepeatComboSingular"));
		repeatCombo.add(I18N.getMessage("ScheduleComposite.RepeatComboIntervall"));
		repeatCombo.select(0);
		repeatCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//intervall ride
				if (repeatCombo.getSelectionIndex() == 1) {
					disposeComposite();
					createIntervallRide();
				} 
				//singular ride
				else {
					disposeComposite();
					createSingularRide();
				}
			}
		});
		
		/*
		 * 4th row (the selection of departure times)
		 */
		departureComposite = new Composite(buttonComposite, SWT.BORDER);
		GridLayout departureCompositeLayout = new GridLayout(2, false);
		departureComposite.setLayout(departureCompositeLayout);
		GridData departureCompositeData = new GridData();
		departureCompositeData.horizontalSpan = 3;
		departureCompositeData.horizontalAlignment = SWT.CENTER;
		departureComposite.setLayoutData(departureCompositeData);
		
		Label departureLabel = new Label(departureComposite, SWT.NONE);
		departureLabel.setText(I18N.getMessage("ScheduleComposite.Departure"));
		
		Composite timeComposite = new Composite(departureComposite, SWT.NONE);
		timeComposite.setLayout(new RowLayout());
		
		hourCombo = new Combo(timeComposite, SWT.READ_ONLY);
		hourCombo.add("00");
		hourCombo.add("01");
		hourCombo.add("02");
		hourCombo.add("03");
		hourCombo.add("04");
		hourCombo.add("05");
		hourCombo.add("06");
		hourCombo.add("07");
		hourCombo.add("08");
		hourCombo.add("09");
		for (int i=10; i<24; i++) {
			hourCombo.add(Integer.toString(i));
		}
		hourCombo.select(0);
		
		Label pointLabel = new Label(timeComposite, SWT.NONE);
		pointLabel.setText(":");
		
		minuteCombo = new Combo(timeComposite, SWT.READ_ONLY);
		minuteCombo.add("00");
		minuteCombo.add("01");
		minuteCombo.add("02");
		minuteCombo.add("03");
		minuteCombo.add("04");
		minuteCombo.add("05");
		minuteCombo.add("06");
		minuteCombo.add("07");
		minuteCombo.add("08");
		minuteCombo.add("09");
		for (int i=10; i<=59; i=i+1) {
			minuteCombo.add(Integer.toString(i));
		}
		minuteCombo.select(0);
		
		/*
		 * 5th row (day selection)
		 */
		createQuestionMark(buttonComposite, I18N.getMessage("ScheduleComposite.Help.Days"));
		
		Label daysLabel = new Label(buttonComposite, SWT.NONE);
		daysLabel.setText(I18N.getMessage("ScheduleComposite.DaysLabel"));
		
		Composite dayComposite = new Composite(buttonComposite, SWT.NONE);
		GridLayout dayCompositeLayout = new GridLayout(3, false);
		dayComposite.setLayout(dayCompositeLayout);
		
		monday = new Button(dayComposite, SWT.CHECK);
		monday.setText(I18N.getMessage("ScheduleComposite.Monday"));
		monday.setSelection(true);
		tuesday = new Button(dayComposite, SWT.CHECK);
		tuesday.setText(I18N.getMessage("ScheduleComposite.Tuesday"));
		tuesday.setSelection(true);
		wednesday = new Button(dayComposite, SWT.CHECK);
		wednesday.setText(I18N.getMessage("ScheduleComposite.Wednesday"));
		wednesday.setSelection(true);
		thursday = new Button(dayComposite, SWT.CHECK);
		thursday.setText(I18N.getMessage("ScheduleComposite.Thursday"));
		thursday.setSelection(true);
		friday = new Button(dayComposite, SWT.CHECK);
		friday.setText(I18N.getMessage("ScheduleComposite.Friday"));
		friday.setSelection(true);
		saturday = new Button(dayComposite, SWT.CHECK);
		saturday.setText(I18N.getMessage("ScheduleComposite.Saturday"));
		sunday = new Button(dayComposite, SWT.CHECK);
		sunday.setText(I18N.getMessage("ScheduleComposite.Holiday"));
		
		/*
		 * 6th row (station selection)
		 */
		createQuestionMark(buttonComposite, I18N.getMessage("ScheduleComposite.Help.Stations"));
		
		Composite stationComposite = new Composite(buttonComposite, SWT.BORDER|SWT.FULL_SELECTION);
		GridData stationCompositeData = new GridData();
		stationCompositeData.horizontalSpan = 2;
		stationComposite.setLayoutData(stationCompositeData);
		stationComposite.setLayout(new RowLayout());
		
		Composite stationListComposite = new Composite(stationComposite, SWT.NONE);
		RowLayout stationListCompositeLayout = new RowLayout(SWT.VERTICAL);
		stationListComposite.setLayout(stationListCompositeLayout);

		Label firstStation = new Label(stationListComposite, SWT.NONE);
		firstStation.setText(I18N.getMessage("ScheduleComposite.FirstStation"));
		firstStationList = new List(stationListComposite, SWT.BORDER|SWT.V_SCROLL);
		RowData firstStationListData = new RowData();
		firstStationListData.height = 15;
		firstStationList.setLayoutData(firstStationListData);
		firstStationList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lastStationList.setSelection(-1);
				stationList.setSelection(-1);
				refreshSpinner();
				timeSpinner.setEnabled(false);
				delaySpinner.setEnabled(false);
			}
		});
		
		Label stopoverStation = new Label(stationListComposite, SWT.NONE);
		stopoverStation.setText(I18N.getMessage("ScheduleComposite.StopoverStation"));
		stationList = new List(stationListComposite, SWT.BORDER|SWT.V_SCROLL);
		stationList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lastStationList.setSelection(-1);
				firstStationList.setSelection(-1);
				refreshSpinner();
				timeSpinner.setEnabled(true);
				delaySpinner.setEnabled(true);
			}
		});
		
		Label lastStation = new Label(stationListComposite, SWT.NONE);
		lastStation.setText(I18N.getMessage("ScheduleComposite.LastStation"));
		lastStationList = new List(stationListComposite, SWT.BORDER|SWT.V_SCROLL);
		RowData lastStationListData = new RowData();
		lastStationListData.height = 15;
		lastStationList.setLayoutData(lastStationListData);
		lastStationList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				firstStationList.setSelection(-1);
				stationList.setSelection(-1);
				refreshSpinner();
				timeSpinner.setEnabled(true);
				delaySpinner.setEnabled(false);
			}
		});
		
			/*
			 * delayComp
			 */
		Composite delayComposite = new Composite(stationComposite, SWT.NONE);
		RowData delayCompositeData = new RowData();
		delayCompositeData.width = 120;
		delayComposite.setLayoutData(delayCompositeData);
		GridLayout delayCompositeLayout = new GridLayout();
		delayCompositeLayout.numColumns = 2;
		delayComposite.setLayout(delayCompositeLayout);
		
		Button addStation = new Button(delayComposite, SWT.TOGGLE);
		addStation.setText(I18N.getMessage("ScheduleComposite.AddStation"));
		GridData stationButtonData = new GridData();
		stationButtonData.horizontalSpan = 2;
		addStation.setLayoutData(stationButtonData);
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
		
		deleteStation = new Button(delayComposite, SWT.PUSH);
		deleteStation.setText(I18N.getMessage("ScheduleComposite.RemoveStation"));
		deleteStation.setEnabled(false);
		deleteStation.setLayoutData(stationButtonData);
		deleteStation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// delete first station selected
				if (firstStationList.getSelectionIndex() == 0) {
					firstStationList.removeAll();
					arrivalTime.remove(0);
					idleTime.remove(0);
					nodeList.remove(0);
					if (stationList.getItemCount() > 0) {
						firstStationList.add(stationList.getItem(0));
						stationList.remove(0);
						firstStationList.select(0);
					}
					else if (stationList.getItemCount() == 0 && lastStationList.getItemCount() > 0) {
						firstStationList.add(lastStationList.getItem(0));
						lastStationList.removeAll();
						firstStationList.select(0);
						delaySpinner.setEnabled(false);
					}
					else {
						deleteStation.setEnabled(false);
						timeSpinner.setEnabled(false);
					}
				}
				// delete last station selected
				else if(lastStationList.getSelectionIndex() == 0) {
					lastStationList.removeAll();
					arrivalTime.remove(arrivalTime.size()-1);
					idleTime.remove(idleTime.size()-1);
					nodeList.remove(nodeList.size()-1);
					if (stationList.getItemCount() > 0) {
						lastStationList.add(stationList.getItem(stationList.getItemCount()-1));
						stationList.remove(stationList.getItemCount()-1);
						lastStationList.select(0);
						refreshSpinner();
					}
					else {
						firstStationList.select(0);
					}
				}
				// delete stopover station selected
				else {
					arrivalTime.remove(stationList.getSelectionIndex()+1);
					idleTime.remove(stationList.getSelectionIndex()+1);
					nodeList.remove(stationList.getSelectionIndex()+1);
					stationList.remove(stationList.getSelectionIndex());
					if (stationList.getItemCount() > 0) {
						stationList.select(0);
					}
					else {
						firstStationList.select(0);
					}
				}
			}
		});
		
		Label delayLabel = new Label(delayComposite, SWT.NONE);
		delayLabel.setText(I18N.getMessage("ScheduleComposite.WaitingTime"));
		GridData delayData = new GridData();
		delayData.horizontalSpan = 2;
		delayData.heightHint = 18;
		delayLabel.setLayoutData(delayData);
		
		delaySpinner = new Spinner(delayComposite, SWT.BORDER|SWT.READ_ONLY);
		delaySpinner.setMinimum(0);
		delaySpinner.setMaximum(60);
		delaySpinner.setIncrement(1);
		delaySpinner.setSelection(3);
		delaySpinner.setEnabled(false);
		delaySpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (stationList.getSelectionCount() >= 0) {
					idleTime.set(stationList.getSelectionIndex()+1, delaySpinner.getSelection());
				}
			}
		});
		
		Label min = new Label(delayComposite, SWT.NONE);
		min.setText("min");
		
		Label timeElapsed = new Label(delayComposite, SWT.NONE);
		timeElapsed.setText(I18N.getMessage("ScheduleComposite.TimeFromStart"));
		GridData timeElapsedData = new GridData();
		timeElapsedData.horizontalSpan = 2;
		timeElapsed.setLayoutData(timeElapsedData);
		
		timeSpinner = new Spinner(delayComposite, SWT.BORDER);
		timeSpinner.setMinimum(0);
		timeSpinner.setMaximum(1000);
		timeSpinner.setIncrement(1);
		timeSpinner.setSelection(10);
		timeSpinner.setEnabled(false);
		timeSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected (SelectionEvent e) {
				if (stationList.getSelectionIndex() >= 0) {
					arrivalTime.set(stationList.getSelectionIndex()+1, timeSpinner.getSelection());
				}
				else if (lastStationList.getSelectionIndex() >= 0) {
					arrivalTime.set(arrivalTime.size()-1, timeSpinner.getSelection());
				}
			}
		});
		
		min = new Label(delayComposite, SWT.NONE);
		min.setText("min");

		/*
		 * 8th row (save and cancel Buttons)
		 */
		Composite saveComposite = new Composite(buttonComposite, SWT.NONE);
		RowLayout saveCompositeLayout = new RowLayout();
		saveCompositeLayout.justify = true;
		saveComposite.setLayout(saveCompositeLayout);
		GridData saveCompositeData = new GridData();
		saveCompositeData.horizontalSpan = 3;
		saveCompositeData.horizontalAlignment = SWT.FILL;
		saveCompositeData.grabExcessHorizontalSpace = true;
		saveComposite.setLayoutData(saveCompositeData);
		
		save = new Button(saveComposite, SWT.PUSH);
		save.setText(I18N.getMessage("ScheduleComposite.Save"));
		save.setEnabled(false);
//		save.setImage(ImageHelper.getImage("loadButton"));
		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				//check the arrays
				if (nodeList.isEmpty()|nodeList.size()<2) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
					messageBox.setText(I18N.getMessage("ScheduleComposite.ErrorMessage.Title"));
					messageBox.setMessage(I18N.getMessage("ScheduleComposite.ErrorMessage.NoStations"));
					messageBox.open();
					return;
				}
				
				//check the days
				if (!sunday.getSelection() && !monday.getSelection()
						&& !tuesday.getSelection() && !wednesday.getSelection()
						&& !thursday.getSelection() && !friday.getSelection()) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
					messageBox.setText(I18N.getMessage("ScheduleComposite.ErrorMessage.Title"));
					messageBox.setMessage(I18N.getMessage("ScheduleComposite.ErrorMessage.NoDays"));
					messageBox.open();
					return;
				}
				
				//check the time of arrival
				for (int i = 1; i<arrivalTime.size(); i++) {
					if (arrivalTime.get(i) < arrivalTime.get(i-1)) {
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
						messageBox.setText(I18N.getMessage("ScheduleComposite.ErrorMessage.Title"));
						messageBox.setMessage(I18N.getMessage("ScheduleComposite.ErrorMessage.ImpossibleArrival"));
						messageBox.open();
						return;
					}
				}
				
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
				}
				
				//Get first ride
				Calendar firstRide = new GregorianCalendar();
				int hour = Integer.parseInt(hourCombo.getItem(hourCombo.getSelectionIndex()));
				int minute = Integer.parseInt(minuteCombo.getItem(minuteCombo.getSelectionIndex()));
				firstRide.clear();
				firstRide.set(0, 0, 0, hour, minute);
				
				//Get the rail sys id
				String railSysID = applicationService.getModel().getActiveRailwaySys().getID();
				
				for(int i=0; i<arrivalTime.size(); i++) {
					arrivalTime.set(i, arrivalTime.get(i) * 60);
				}
				for(int idle : idleTime) {
					idle = idle * 60;
				}
				idleTime.set(idleTime.size()-1, 0);
				
				ScheduleScheme schedule = new ScheduleScheme(scheduleType, trainType, days, firstRide, railSysID, nameField.getText());
				
				for (int i=0; i<arrivalTime.size(); i++) {
					schedule.addStop(nodeList.get(i), arrivalTime.get(i), idleTime.get(i));
				}
				
				applicationService.addScheduleScheme(schedule);
				
				HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, layout, applicationService);		
	    		layout.topControl = homeScreenComposite.getComposite();
	    		mainComposite.layout();
			}
		});
		
		Button cancel = new Button(saveComposite, SWT.PUSH);
		cancel.setText(I18N.getMessage("ScheduleComposite.Cancel"));
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
							// add first station
							if (firstStationList.getItemCount() == 0) {
								firstStationList.add(temp.getName());
								firstStationList.select(0);
								deleteStation.setEnabled(true);
								arrivalTime.add(0);
								idleTime.add(0);
								nodeList.add(temp);
								refreshSpinner();
							}
							// add last station
							else if(firstStationList.getItemCount() == 1 && lastStationList.getItemCount() == 0) {
								if (temp.getName().equals(firstStationList.getItem(0))) {
									MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
									messageBox.setText(I18N.getMessage("ScheduleComposite.ErrorMessage.Title"));
									messageBox.setMessage(I18N.getMessage("ScheduleComposite.ErrorMessage.TwoStations"));
									messageBox.open();
								} else {
									lastStationList.add(temp.getName());
									arrivalTime.add(arrivalTime.get(arrivalTime.size()-1) + 5);
									idleTime.add(5);
									nodeList.add(temp);
								}
							}
							// add normal station
							else {
								if (temp.getName().equals(lastStationList.getItem(0))) {
									MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
									messageBox.setText(I18N.getMessage("ScheduleComposite.ErrorMessage.Title"));
									messageBox.setMessage(I18N.getMessage("ScheduleComposite.ErrorMessage.TwoStations"));
									messageBox.open();
								}
								else {
									stationList.add(lastStationList.getItem(0));
									lastStationList.removeAll();
									lastStationList.add(temp.getName());
									arrivalTime.add(arrivalTime.get(arrivalTime.size()-1) + 5);
									idleTime.add(5);
									nodeList.add(temp);
								}
							}
						}
					}
				}
			}	    	
	    });
	    map.paintNodeMap(c);
	}
	
	/**
	 * Creates the elements for the singular ride.
	 */
	private void createSingularRide() {
		Label departureLabel = new Label(departureComposite, SWT.NONE);
		departureLabel.setText(I18N.getMessage("ScheduleComposite.Departure"));
		
		Composite timeComposite = new Composite(departureComposite, SWT.NONE);
		timeComposite.setLayout(new RowLayout());
		
		hourCombo = new Combo(timeComposite, SWT.READ_ONLY);
		hourCombo.add("00");
		hourCombo.add("01");
		hourCombo.add("02");
		hourCombo.add("03");
		hourCombo.add("04");
		hourCombo.add("05");
		hourCombo.add("06");
		hourCombo.add("07");
		hourCombo.add("08");
		hourCombo.add("09");
		for (int i=10; i<24; i++) {
			hourCombo.add(Integer.toString(i));
		}
		hourCombo.select(0);
		
		Label pointLabel = new Label(timeComposite, SWT.NONE);
		pointLabel.setText(":");
		
		minuteCombo = new Combo(timeComposite, SWT.READ_ONLY);
		minuteCombo.add("00");
		minuteCombo.add("01");
		minuteCombo.add("02");
		minuteCombo.add("03");
		minuteCombo.add("04");
		minuteCombo.add("05");
		minuteCombo.add("06");
		minuteCombo.add("07");
		minuteCombo.add("08");
		minuteCombo.add("09");
		for (int i=10; i<=59; i=i+1) {
			minuteCombo.add(Integer.toString(i));
		}
		minuteCombo.select(0);
		departureComposite.pack();
		departureComposite.layout();
		buttonComposite.layout();
	}
	
	/**
	 * Creates the elements for the intervall ride
	 */
	private void createIntervallRide() {
		/*
		 * 1st row (first ride)
		 */
		Label firstRideLabel = new Label(departureComposite, SWT.NONE);
		firstRideLabel.setText(I18N.getMessage("ScheduleComposite.FirstRide"));
		
		Composite timeComposite = new Composite(departureComposite, SWT.NONE);
		timeComposite.setLayout(new RowLayout());
		
		hourCombo = new Combo(timeComposite, SWT.READ_ONLY);
		hourCombo.add("00");
		hourCombo.add("01");
		hourCombo.add("02");
		hourCombo.add("03");
		hourCombo.add("04");
		hourCombo.add("05");
		hourCombo.add("06");
		hourCombo.add("07");
		hourCombo.add("08");
		hourCombo.add("09");
		for (int i=10; i<24; i++) {
			hourCombo.add(Integer.toString(i));
		}
		hourCombo.select(00);
		
		Label pointLabel = new Label(timeComposite, SWT.NONE);
		pointLabel.setText(":");
		
		minuteCombo = new Combo(timeComposite, SWT.READ_ONLY);
		minuteCombo.add("00");
		minuteCombo.add("01");
		minuteCombo.add("02");
		minuteCombo.add("03");
		minuteCombo.add("04");
		minuteCombo.add("05");
		minuteCombo.add("06");
		minuteCombo.add("07");
		minuteCombo.add("08");
		minuteCombo.add("09");
		for (int i=10; i<=59; i=i+1) {
			minuteCombo.add(Integer.toString(i));
		}
		minuteCombo.select(0);
		
		/*
		 * 2nd row (last ride) 
		 */
		Label lastRideLabel = new Label(departureComposite, SWT.NONE);
		lastRideLabel.setText(I18N.getMessage("ScheduleComposite.LastRide"));
		
		Composite timeComposite2 = new Composite(departureComposite, SWT.NONE);
		timeComposite2.setLayout(new RowLayout());
		
		hourCombo = new Combo(timeComposite2, SWT.READ_ONLY);
		hourCombo.add("00");
		hourCombo.add("01");
		hourCombo.add("02");
		hourCombo.add("03");
		hourCombo.add("04");
		hourCombo.add("05");
		hourCombo.add("06");
		hourCombo.add("07");
		hourCombo.add("08");
		hourCombo.add("09");
		for (int i=10; i<24; i++) {
			hourCombo.add(Integer.toString(i));
		}
		hourCombo.select(1);
		
		Label pointLabel2 = new Label(timeComposite2, SWT.NONE);
		pointLabel2.setText(":");
		
		minuteCombo = new Combo(timeComposite2, SWT.READ_ONLY);
		minuteCombo.add("00");
		minuteCombo.add("01");
		minuteCombo.add("02");
		minuteCombo.add("03");
		minuteCombo.add("04");
		minuteCombo.add("05");
		minuteCombo.add("06");
		minuteCombo.add("07");
		minuteCombo.add("08");
		minuteCombo.add("09");
		for (int i=10; i<=59; i=i+1) {
			minuteCombo.add(Integer.toString(i));
		}
		minuteCombo.select(0);
		
		/*
		 * 3rd row (interval)
		 */
		Label intervallLabel = new Label(departureComposite, SWT.NONE);
		intervallLabel.setText(I18N.getMessage("ScheduleComposite.Intervall"));
		
		Composite timeComposite3 = new Composite(departureComposite, SWT.NONE);
		timeComposite3.setLayout(new RowLayout());
		
		hourCombo = new Combo(timeComposite3, SWT.READ_ONLY);
		hourCombo.add("00");
		hourCombo.add("01");
		hourCombo.add("02");
		hourCombo.add("03");
		hourCombo.add("04");
		hourCombo.add("05");
		hourCombo.add("06");
		hourCombo.add("07");
		hourCombo.add("08");
		hourCombo.add("09");
		for (int i=10; i<24; i++) {
			hourCombo.add(Integer.toString(i));
		}
		hourCombo.select(0);
		
		Label pointLabel3 = new Label(timeComposite3, SWT.NONE);
		pointLabel3.setText("h");
		
		minuteCombo = new Combo(timeComposite3, SWT.READ_ONLY);
		minuteCombo.add("00");
		minuteCombo.add("01");
		minuteCombo.add("02");
		minuteCombo.add("03");
		minuteCombo.add("04");
		minuteCombo.add("05");
		minuteCombo.add("06");
		minuteCombo.add("07");
		minuteCombo.add("08");
		minuteCombo.add("09");
		for (int i=10; i<=59; i=i+5) {
			minuteCombo.add(Integer.toString(i));
		}
		minuteCombo.select(1);
		
		Label minLabel = new Label(timeComposite3, SWT.NONE);
		minLabel.setText("min");
		
		departureComposite.layout();
		departureComposite.pack();
		buttonComposite.layout();
	}
	
	/**
	 * Disposes all elements of the given composite.
	 */
	private void disposeComposite() {
		for (Control kid: departureComposite.getChildren()) {
			kid.dispose();
		}
	}
	
	/**
	 * Refreshes the values of the two spinners.
	 */
	private void refreshSpinner() {
		if (firstStationList.getSelectionIndex() >= 0) {
			timeSpinner.setSelection(0);
			delaySpinner.setSelection(0);
		}
		else if(lastStationList.getSelectionIndex() >= 0) {
			timeSpinner.setSelection(arrivalTime.get(arrivalTime.size()-1));
			delaySpinner.setSelection(0);
		}
		else if(stationList.getSelectionIndex() >= 0) {
			timeSpinner.setSelection(arrivalTime.get(stationList.getSelectionIndex()+1));
			delaySpinner.setSelection(idleTime.get(stationList.getSelectionIndex()+1));
		}
	}
	
	/**
	 * Creates a help icon that shows some tips when hovering over it.
	 * 
	 * @param composite
	 * 			The parent composite of the icon.
	 * @param message
	 * 			The message that should be displayed.
	 */
	private void createQuestionMark(Composite composite, String message) {
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
