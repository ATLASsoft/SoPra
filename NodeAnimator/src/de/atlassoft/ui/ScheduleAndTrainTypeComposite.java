package de.atlassoft.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.ModelService;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;
import de.atlassoft.model.TrainType;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * This class creates the Tab Folder in which the train types
 * and the schedules are shown.
 * 
 * @author Tobias Ilg, Silvan Haeussermann 
 *
 */
public class ScheduleAndTrainTypeComposite {
	
	private Shell shell;
	private Composite scheduleAndTrainTypeComposite, scheduleComposite, informationComposite;
	private I18NService I18N;
	private ApplicationService applicationService;
	private List activeSchedules, passiveSchedules;
	private java.util.List<ScheduleScheme> active, passive;
	private ScheduleScheme activeSchedule;
	private Composite trainTypeComposite;
	private FontData fontData;
	
	/**
	 * Constructor for the class ScheduleAndTrainTypeComposite
	 * 
	 * @param shell
	 * 		The shell in which the tabFolder lays.
	 * @param tabFolder
	 * 		The tab folder in which the composite lays.
	 * @param applicationService
	 * 		The application Service of the application.
	 */
	public ScheduleAndTrainTypeComposite(Shell shell, TabFolder tabFolder, ApplicationService applicationService) {
		this.shell = shell;
		I18N = I18NSingleton.getInstance();
		this.applicationService = applicationService;
		active = applicationService.getModel().getActiveScheduleSchemes();
		passive = applicationService.getModel().getPassiveScheduleSchemes();
		fontData = new FontData();

//		activeSchedule = null;
		
		//Overall composite of the tab folder
		scheduleAndTrainTypeComposite = new Composite (tabFolder, SWT.BORDER);
		scheduleAndTrainTypeComposite.setLayout(new GridLayout(2, false));
		
		//Composites above the mainComposite
		scheduleComposite = new Composite(scheduleAndTrainTypeComposite, SWT.BORDER);
		final ScrolledComposite scrolledTrainTypeComposite = new ScrolledComposite (scheduleAndTrainTypeComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		applicationService.getModel().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ModelService.TRAIN_TYPES_PROPNAME)) {
					Control[] children = trainTypeComposite.getChildren();
					for (int i = 0; i<= children.length - 1; i++) {
						children[i].dispose();
					}
					createContent();
					scrolledTrainTypeComposite.setContent(trainTypeComposite);
				}
			}
		});
		
		//Initiate the two composites
		initScheduleUI();
		initTrainTypeUI(scrolledTrainTypeComposite);
	}
	
	/**
	 * Creates the elements for the schedule UI.
	 * 
	 * @param
	 * 		The composite for the elements.
	 */
	private void initScheduleUI() {
		
		//Font for the titles of the schedules lists
		fontData.setStyle(SWT.BOLD);
		fontData.setHeight(9);
		
		scheduleComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scheduleComposite.setLayout(new GridLayout(4, false));
		
		//Shows the information about the schedule
		informationComposite = new Composite(scheduleComposite, SWT.BORDER);
		informationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		informationComposite.setLayout(new GridLayout(2, false));
		
		Label noSelection = new Label(informationComposite, SWT.NONE);
		noSelection.setText("Kein Fahrplan ausgewählt");
		
		//Shows the two lists and the buttons to switch
		Composite listComposite = new Composite(scheduleComposite, SWT.NONE);
		listComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		listComposite.setLayout(new GridLayout());
		
		//Active schedules list
		Label activeSchedulesLabel = new Label(listComposite, SWT.NONE);
		activeSchedulesLabel.setFont(new Font(Display.getCurrent(), fontData));
		activeSchedulesLabel.setText("Aktive Fahrpläne:");
		
		activeSchedules = new List(listComposite,  SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
		GridData activeSchedulesData = new GridData();
		activeSchedulesData.grabExcessHorizontalSpace = true;
		activeSchedulesData.horizontalAlignment = SWT.FILL;
		activeSchedulesData.heightHint = 180;
		activeSchedules.setLayoutData(activeSchedulesData);
		for (ScheduleScheme temp: applicationService.getModel().getActiveScheduleSchemes()) {
			activeSchedules.add(temp.getID());
		}
		activeSchedules.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (activeSchedules.getSelectionIndex() >= 0) {
					String name = activeSchedules.getItem(activeSchedules.getSelectionIndex());
					for (ScheduleScheme temp : active) {
						if (temp.getID().equals(name)) {
							activeSchedule = temp;
							disposeCompsite();
							createInformation();
							passiveSchedules.deselectAll();
						}
					}
				}
			}
		});
		
		//The ButtonComposite
	    Composite buttonComposite = new Composite(listComposite, SWT.NONE);
	    buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	    RowLayout buttonLayout = new RowLayout();
	    buttonLayout.justify = true;
	    buttonComposite.setLayout(buttonLayout);
	    
	    	// /\ Button
	    Button setScheduleActive = new Button(buttonComposite, SWT.PUSH);
	    setScheduleActive.setText("/\\ Aktiv setzen");
	    setScheduleActive.setToolTipText(I18N.getMessage("AllSchedulesComposite.SetActiveTooltip"));
	    setScheduleActive.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		if (activeSchedules.getSelectionIndex() >= 0 || passiveSchedules.getItemCount() == 0) {	    			
	    			MessageBox errorMessageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
	    			errorMessageBox.setText(I18N.getMessage("AllSchedulesComposite.ErrorTitle"));
	    			errorMessageBox.setMessage(I18N.getMessage("ScheduleAndTrainTypeComposite.Error.AlreadyActive"));
	    			errorMessageBox.open();	    			
	    		}
	    		else if(activeSchedules.getSelectionIndex() < 0 && passiveSchedules.getSelectionIndex() < 0) {
	    			MessageBox errorMessageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
	    			errorMessageBox.setText(I18N.getMessage("AllSchedulesComposite.ErrorTitle"));
	    			errorMessageBox.setMessage(I18N.getMessage("ScheduleAndTrainTypeComposite.Error.NoScheduleSelected"));
	    			errorMessageBox.open();
	    		}
	    		else {
	    			setActive();
	    		}
	    	}
	    });
	    
	    Button deleteSchedule = new Button(buttonComposite, SWT.PUSH);
	    deleteSchedule.setImage(ImageHelper.getImage("trashIconSmall"));
	    deleteSchedule.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent e) {
	    		ScheduleScheme schedule = null;
	    		//Check if nothing is selected
	    		if (activeSchedules.getSelectionIndex() < 0 && passiveSchedules.getSelectionIndex() < 0) {
	    			MessageBox errorMessageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
	    			errorMessageBox.setText(I18N.getMessage("AllSchedulesComposite.ErrorTitle"));
	    			errorMessageBox.setMessage(I18N.getMessage("ScheduleAndTrainTypeComposite.Error.NoScheduleSelected"));
	    			errorMessageBox.open();
	    		}
	    		//Check if a passive schedule is selected
	    		else if (activeSchedules.getSelectionIndex() < 0) {
	    			if (passiveSchedules.getSelectionIndex() >= 0) {
	    				String name = passiveSchedules.getItem(passiveSchedules.getSelectionIndex());
	    				for (ScheduleScheme temp : applicationService.getModel().getPassiveScheduleSchemes()) {
	    					if (temp.getID().equals(name)) {
	    						schedule = temp;
	    					}
	    				}
	    			}
		    		applicationService.deleteScheduleScheme(schedule);
		    		passiveSchedules.remove(passiveSchedules.getSelectionIndex());
		    		if (passiveSchedules.getItemCount() > 0) {
		    			passiveSchedules.select(0);
		    		}
	    		}
	    		//Check if an active schedule is selected
	    		else if (passiveSchedules.getSelectionIndex() < 0) {
	    			if (activeSchedules.getSelectionIndex() >= 0) {
	    				String name = activeSchedules.getItem(activeSchedules.getSelectionIndex());
	    				for (ScheduleScheme temp : applicationService.getModel().getActiveScheduleSchemes()) {
	    					if (temp.getID().equals(name)) {
	    						schedule = temp;
	    					}
	    				}
	    			}
	    			applicationService.deleteScheduleScheme(schedule);
		    		activeSchedules.remove(activeSchedules.getSelectionIndex());
		    		if (activeSchedules.getItemCount() > 0) {
		    			activeSchedules.select(0);
		    		}
	    		}
	    		
	    		
	    	}
		});
	    
	    	// \/ Button
	    Button setSchedulePassive = new Button(buttonComposite, SWT.PUSH);
	    setSchedulePassive.setText("\\/ Passiv setzen");
	    setSchedulePassive.setToolTipText(I18N.getMessage("AllSchedulesComposite.SetPassiveTooltip"));
	    setSchedulePassive.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		if (passiveSchedules.getSelectionIndex() >= 0 || activeSchedules.getItemCount() == 0) {
	    			MessageBox errorMessageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
	    			errorMessageBox.setText(I18N.getMessage("AllSchedulesComposite.ErrorTitle"));
	    			errorMessageBox.setMessage(I18N.getMessage("ScheduleAndTrainTypeComposite.Error.AlreadyPassive"));
	    			errorMessageBox.open();
	    		}
	    		else if(passiveSchedules.getSelectionIndex() < 0 && activeSchedules.getSelectionIndex() < 0) {
	    			MessageBox errorMessageBox = new MessageBox(new Shell(), SWT.ICON_ERROR);
	    			errorMessageBox.setText(I18N.getMessage("AllSchedulesComposite.ErrorTitle"));
	    			errorMessageBox.setMessage(I18N.getMessage("ScheduleAndTrainTypeComposite.Error.NoScheduleSelected"));
	    			errorMessageBox.open();
	    		}
	    		else {
		    		setPassive();
	    		}    		
	    	}
	    });
		
		//Passive schedules list
	    Label passiveSchedulesLabel = new Label(listComposite, SWT.NONE);
	    passiveSchedulesLabel.setFont(new Font(Display.getCurrent(), fontData));
	    passiveSchedulesLabel.setText("Passive Fahrpläne:");
	    
		passiveSchedules = new List(listComposite,  SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
		GridData passiveSchedulesData = new GridData();
		passiveSchedulesData.horizontalAlignment = SWT.FILL;
		passiveSchedulesData.grabExcessHorizontalSpace = true;
		passiveSchedulesData.verticalAlignment = SWT.FILL;
		passiveSchedulesData.grabExcessVerticalSpace = true;
		passiveSchedules.setLayoutData(passiveSchedulesData);
		for (ScheduleScheme temp: applicationService.getModel().getPassiveScheduleSchemes()) {
			passiveSchedules.add(temp.getID());
		}
		passiveSchedules.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (passiveSchedules.getSelectionIndex() >= 0) {
					String name = passiveSchedules.getItem(passiveSchedules.getSelectionIndex());
					for (ScheduleScheme temp : passive) {
						if (temp.getID().equals(name)) {
							activeSchedule = temp;
							disposeCompsite();
							createInformation();
							activeSchedules.deselectAll();
						}
					}
				}
			}
		});
		
		setDragDrop(activeSchedules);
		setDragDrop(passiveSchedules);
	}
	
	/**
	 * Creates the elements for the train type UI.
	 * 
	 * @param
	 * 		The composite for the elements.
	 */
	private void initTrainTypeUI(ScrolledComposite scrolledTrainTypeComposite) {
		
		// Here we fill this composite over the whole area
		scrolledTrainTypeComposite.setLayout(new GridLayout (1, true));
		GridData scrolledTrainTypeCompositeData = new GridData();
		scrolledTrainTypeCompositeData.widthHint = 340;
		scrolledTrainTypeCompositeData.verticalAlignment = SWT.FILL;
		scrolledTrainTypeCompositeData.grabExcessVerticalSpace = true;
		scrolledTrainTypeComposite.setLayoutData(scrolledTrainTypeCompositeData);

		// This is the composite where we generate the content of mainComposite (scrolledTrainTypeComposite)
		trainTypeComposite = new Composite (scrolledTrainTypeComposite, SWT.NULL);
		trainTypeComposite.setLayout(new GridLayout (1, true));
		
		// Shows the description
		Label trainTypeLabel = new Label(trainTypeComposite, SWT.NONE);
		GridData trainTypeLabelData = new GridData();
		trainTypeLabelData.heightHint = 20;
		trainTypeLabel.setLayoutData(trainTypeLabelData);
		trainTypeLabel.setFont(new Font(Display.getCurrent(), fontData));
		trainTypeLabel.setText("Zugtypen:");
		
		// create Content of the TrainTypeComposite
		createContent();
	 	
		scrolledTrainTypeComposite.setExpandVertical(true);
		scrolledTrainTypeComposite.setExpandHorizontal(true);
		scrolledTrainTypeComposite.setMinSize(trainTypeComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledTrainTypeComposite.setContent(trainTypeComposite);
	}
	
	/**
	 * Builds the composite which shows the information about the schedule.
	 */
	private void createInformation() {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm");
		
		Label name = new Label(informationComposite, SWT.NONE);
		name.setText("Name:");
		
		Label nameInformation = new Label(informationComposite, SWT.NONE);
		nameInformation.setText(activeSchedule.getID());
		
		Label trainType = new Label(informationComposite, SWT.NONE);
		trainType.setText("Zugtyp:");
		
		Label trainTypeInformation = new Label(informationComposite, SWT.NONE);
		trainTypeInformation.setText(activeSchedule.getTrainType().getName());
		
		Label repetition = new Label(informationComposite, SWT.NONE);
		repetition.setText("Wiederholungsart:");

		if (activeSchedule.getScheduleType() == ScheduleType.SINGLE_RIDE) {
			Label repetitionInformation = new Label(informationComposite, SWT.NONE);
			repetitionInformation.setText("Einmalig");
			
			Label departure = new Label(informationComposite, SWT.NONE);
			departure.setText("Abfahrt:");
			
			Label departureInformation = new Label(informationComposite, SWT.NONE);
			departureInformation.setText(dateFormat.format(activeSchedule.getFirstRide().getTime()));
		}
		else {
			Label repetitionInformation = new Label(informationComposite, SWT.NONE);
			repetitionInformation.setText("Intervall");
			
			Label firstRide = new Label(informationComposite, SWT.NONE);
			firstRide.setText("Erste Fahrt:");
			
			Label firstRideInformation = new Label(informationComposite, SWT.NONE);
			firstRideInformation.setText(dateFormat.format(activeSchedule.getFirstRide().getTime()));
			
			Label lastRide = new Label(informationComposite, SWT.NONE);
			lastRide.setText("Letzte Fahrt:");
			
			Label lastRideInformation = new Label(informationComposite, SWT.NONE);
			lastRideInformation.setText(dateFormat.format(activeSchedule.getLastRide().getTime()));
			
			Label interval = new Label(informationComposite, SWT.NONE);
			interval.setText("Intervall:");
			
			Label intervalInformation = new Label(informationComposite, SWT.NONE);
			intervalInformation.setText(String.valueOf(activeSchedule.getInterval()) + " minütig");
		}
		
		Label days = new Label(informationComposite, SWT.NONE);
		days.setLayoutData(new GridData(SWT.NULL, SWT.UP, false, false));
		days.setText("Tage:");
		
		Composite allDaysComposite = new Composite(informationComposite, SWT.NONE);
		RowLayout allDaysCompositeLayout = new RowLayout(SWT.VERTICAL);
		allDaysCompositeLayout.marginLeft = 0;
		allDaysComposite.setLayout(allDaysCompositeLayout);
		for (Integer temp : activeSchedule.getDays()) {
			if (temp == Calendar.MONDAY) {
				Label monday = new Label(allDaysComposite, SWT.NONE);
				monday.setText("Montag");
			}
			else if (temp == Calendar.TUESDAY) {
				Label tuesday = new Label(allDaysComposite, SWT.NONE);
				tuesday.setText("Dienstag");
			}
			else if (temp == Calendar.WEDNESDAY) {
				Label wednesday = new Label(allDaysComposite, SWT.NONE);
				wednesday.setText("Mittwoch");
			}
			else if (temp == Calendar.THURSDAY) {
				Label thursday = new Label(allDaysComposite, SWT.NONE);
				thursday.setText("Donnerstag");
			}
			else if (temp == Calendar.FRIDAY) {
				Label friday = new Label(allDaysComposite, SWT.NONE);
				friday.setText("Freitag");
			}
			else if (temp == Calendar.SATURDAY) {
				Label saturday = new Label(allDaysComposite, SWT.NONE);
				saturday.setText("Samstag");
			}
			else {
				Label sunday = new Label(allDaysComposite, SWT.NONE);
				sunday.setText("Sonntag");
			}
		}
		
		Label firstStation = new Label(informationComposite, SWT.NONE);
		firstStation.setText("Erste Station:");
		
		Label firstStationInformation = new Label(informationComposite, SWT.NONE);
		firstStationInformation.setText(activeSchedule.getStations().get(0).getName());
		
		if (activeSchedule.getStations().size() > 2) {
			Label stopOverStations = new Label (informationComposite, SWT.NONE);
			stopOverStations.setLayoutData(new GridData(SWT.NULL, SWT.UP, false, false));
			stopOverStations.setText("Zwischenstationen:");
			
			Composite stopOverComposite = new Composite(informationComposite, SWT.NONE);
			GridLayout stopOverCompositeLayout = new GridLayout();
			stopOverCompositeLayout.numColumns = 2;
			stopOverCompositeLayout.marginWidth = 0;
			stopOverComposite.setLayout(stopOverCompositeLayout);
			
			for (int i=1; i<activeSchedule.getStations().size()-1; i++) {
				Label station = new Label(stopOverComposite, SWT.NONE);
				station.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
				station.setText(activeSchedule.getStations().get(i).getName());
				Label delay = new Label(stopOverComposite, SWT.NONE);
				delay.setText("nach " + activeSchedule.getArrivalTimes().get(i)/60 + " mins");
			}
		}
		
		Label lastStation = new Label(informationComposite, SWT.NONE);
		lastStation.setText("Letzte Station:");
		
		Composite lastStationComposite = new Composite(informationComposite, SWT.NONE);
		GridLayout lastStationCompositeLayout = new GridLayout(2, false);
		lastStationCompositeLayout.marginWidth = 0;
		lastStationComposite.setLayout(lastStationCompositeLayout);
		
		Label lastStationInformation = new Label(lastStationComposite, SWT.NONE);
		int last = activeSchedule.getStations().size();
		lastStationInformation.setText(activeSchedule.getStations().get(last-1).getName());
		
		Label lastStationDelay = new Label(lastStationComposite, SWT.NONE);
		lastStationDelay.setText("nach " + activeSchedule.getArrivalTimes().get(last-1)/60 + " mins");
		
		informationComposite.layout();
	}
	
	/**
	 * Disposes the information composite.
	 */
	private void disposeCompsite() {
		for (Control kid: informationComposite.getChildren()) {
			kid.dispose();
		}
	}
	
	/**
	 * Adds the selected schedule to the active list.
	 */
	private void setActive() {
		int selection = passiveSchedules.getSelectionIndex();
		String name = passiveSchedules.getItem(selection);
		ScheduleScheme schedule = null;
		for (ScheduleScheme temp : passive) {
			if (temp.getID().equals(name)) {
				schedule = temp;
			}
		}
		applicationService.getModel().addActiveScheduleScheme(schedule);
		applicationService.getModel().removePassiveScheduleScheme(schedule);
		
		activeSchedules.add(passiveSchedules.getItem(selection));
		passiveSchedules.remove(selection);
		
		activeSchedules.select(activeSchedules.getItemCount()-1);
	}
	
	/**
	 * Adds the selected schedules to the passive list.
	 */
	private void setPassive() {
		int selection = activeSchedules.getSelectionIndex();
		String name = activeSchedules.getItem(selection);
		ScheduleScheme schedule = null;
		for (ScheduleScheme temp : active) {
			if (temp.getID().equals(name)) {
				schedule = temp;
			}
		}
		applicationService.getModel().addPassiveScheduleScheme(schedule);
		applicationService.getModel().removeActiveScheduleScheme(schedule);
		
		passiveSchedules.add(activeSchedules.getItem(selection));
		activeSchedules.remove(selection);
		
		passiveSchedules.select(passiveSchedules.getItemCount()-1);
	}
	
	/**
	 * Adds a drag and drop listener to the given list.
	 * 
	 * @param list
	 * 		The list that should receive the listener
	 */
	private void setDragDrop (final List list) {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		
		DragSource source = new DragSource(list, DND.DROP_MOVE);
		source.setTransfer(types);
		source.addDragListener(new DragSourceListener() {
			@Override
			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE) {
					if (activeSchedules.getSelectionIndex() < 0) {
						setActive();
						String name = activeSchedules.getItem(activeSchedules.getSelectionIndex());
						for (ScheduleScheme temp : active) {
							if (temp.getID().equals(name)) {
								activeSchedule = temp;
								disposeCompsite();
								createInformation();
								passiveSchedules.deselectAll();
							}
						}
					}
					else {
						setPassive();
						String name = passiveSchedules.getItem(passiveSchedules.getSelectionIndex());
						for (ScheduleScheme temp : passive) {
							if (temp.getID().equals(name)) {
								activeSchedule = temp;
								disposeCompsite();
								createInformation();
								activeSchedules.deselectAll();
							}
						}
					}
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = list.getItem(0);
			}

			@Override
			public void dragStart(DragSourceEvent event) {
				if (list.getItemCount() == 0) {
					event.doit = false;
				}
			}			
		});
		
		DropTarget target = new DropTarget(list, DND.DROP_MOVE);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event){
			}
		});
	}
	
	/**
	 * This method creates the Content
	 * of the TrainTypeComposite
	 * 
	 */
	private void createContent() {
		// First we will get a List of all trainTypes
		java.util.List<TrainType> trainTypes = applicationService.getModel().getTrainTypes();
		// Now we generate for each TrainTypes a Label with the picture of the TrainType,
		// an other Label with the Information of this TrainType,
		// an an Button where we can delete this TrainType
		for (final TrainType type : trainTypes) {
			Composite trainType = new Composite (trainTypeComposite, SWT.BORDER);
			trainType.setLayout(new GridLayout(3, false));
			trainType.setLayoutData(new GridData (GridData.FILL_HORIZONTAL));
			//TODO: if-Bedinung rausnehmen, wenn fertig
			if (type.getImg() == null) {
				new Label (trainType, SWT.NULL).setImage(ImageHelper.getImage("standardTrainIcon"));
			} else {
				new Label (trainType, SWT.NULL).setImage(type.getImg());
			}
			Label information = new Label(trainType, SWT.NULL);
			information.setText(I18N.getMessage("ScheduleAndTrainTypeComposite.TrainTypeComposite.name") + "\t\t\t" + type.getName() + "\n" +
								I18N.getMessage("ScheduleAndTrainTypeComposite.TrainTypeComposite.topSpeed") + "\t" + type.getTopSpeed() + " km/h   \n" +
								I18N.getMessage("ScheduleAndTrainTypeComposite.TrainTypeComposite.priority") + "\t\t" + type.getPriority()); 
			
			Button delete = new Button(trainType, SWT.PUSH);
			delete.setImage(ImageHelper.getImage("trashIcon"));
			delete.setToolTipText(I18N.getMessage("ScheduleAndTrainTypeComposite.TrainTypeComposite.deleteButton"));
			delete.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
		    		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION |SWT.YES | SWT.NO);
		    	    messageBox.setMessage(I18N.getMessage("ScheduleAndTrainTypeComposite.TrainTypeComposite.deleteQuestion"));
		    	    int rc = messageBox.open();
		    	    if (rc == SWT.YES) {
		    	      	applicationService.deleteTrainType(type);
		            }
		    	}
			});
		}
	}
	
	/**
	 * Returns the scheduleAndTrainTypeComposite
	 * 
	 * @return
	 * 		The composite
	 */
	public Composite getComposite() {
		return scheduleAndTrainTypeComposite;
	}
}
