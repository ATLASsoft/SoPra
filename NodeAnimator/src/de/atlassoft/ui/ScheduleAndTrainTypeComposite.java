package de.atlassoft.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
		
		scheduleComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scheduleComposite.setLayout(new GridLayout(4, false));
		
		//Shows the information about the schedule
		informationComposite = new Composite(scheduleComposite, SWT.BORDER);
		informationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		informationComposite.setLayout(new GridLayout(2, false));
		
		Label noSelection = new Label(informationComposite, SWT.NONE);
		noSelection.setText("Kein Fahrplan ausgewählt");
		
		//Shows the two lists and the buttons to switch
		Composite listComposite = new Composite(scheduleComposite, SWT.BORDER);
		listComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		listComposite.setLayout(new GridLayout());
		
		//Active schedules list
		Label activeSchedulesLabel = new Label(listComposite, SWT.NONE);
		activeSchedulesLabel.setText("Aktive Fahrpläne:");
		
		activeSchedules = new List(listComposite,  SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
		activeSchedules.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
	    		if (passiveSchedules.getItemCount() == 0) {	    			
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
	    			activeSchedules.select(activeSchedules.getItemCount()-1);
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
	    		if (activeSchedules.getItemCount() == 0) {
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
		    		passiveSchedules.select(passiveSchedules.getItemCount()-1);
	    		}    		
	    	}
	    });
		
		//Passive schedules list
	    Label passiveSchedulesLabel = new Label(listComposite, SWT.NONE);
	    passiveSchedulesLabel.setText("Passive Fahrpläne:");
	    
		passiveSchedules = new List(listComposite,  SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
		passiveSchedules.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
		scrolledTrainTypeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		
		// This is the composite where we generate the content of mainComposite (scrolledTrainTypeComposite)
		trainTypeComposite = new Composite (scrolledTrainTypeComposite, SWT.NULL);
		trainTypeComposite.setLayout(new GridLayout (1, true));
		
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
			departureInformation.setText(activeSchedule.getFirstRide().getTime().toString());
		}
		
		Label days = new Label(informationComposite, SWT.NONE);
		days.setLayoutData(new GridData(SWT.NULL, SWT.UP, false, false));
		days.setText("Tage:");
		
		Composite allDays = new Composite(informationComposite, SWT.NONE);
		allDays.setLayout(new RowLayout(SWT.VERTICAL));
		for (Integer temp : activeSchedule.getDays()) {
			if (temp == Calendar.MONDAY) {
				Label monday = new Label(allDays, SWT.NONE);
				monday.setText("Montag");
			}
			else if (temp == Calendar.TUESDAY) {
				Label tuesday = new Label(allDays, SWT.NONE);
				tuesday.setText("Dienstag");
			}
			else if (temp == Calendar.WEDNESDAY) {
				Label wednesday = new Label(allDays, SWT.NONE);
				wednesday.setText("Mittwoch");
			}
			else if (temp == Calendar.THURSDAY) {
				Label thursday = new Label(allDays, SWT.NONE);
				thursday.setText("Donnerstag");
			}
			else if (temp == Calendar.FRIDAY) {
				Label friday = new Label(allDays, SWT.NONE);
				friday.setText("Freitag");
			}
			else if (temp == Calendar.SATURDAY) {
				Label saturday = new Label(allDays, SWT.NONE);
				saturday.setText("Samstag");
			}
			else {
				Label sunday = new Label(allDays, SWT.NONE);
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
			stopOverComposite.setLayout(new RowLayout(SWT.VERTICAL));
			
			for (int i=1; i<activeSchedule.getStations().size()-1; i++) {
				Label station = new Label(stopOverComposite, SWT.NONE);
				station.setText(activeSchedule.getStations().get(i).getName());
			}
		}
		
		Label lastStation = new Label(informationComposite, SWT.NONE);
		lastStation.setText("Letzte Station:");
		
		Label lastStationInformation = new Label(informationComposite, SWT.NONE);
		int last = activeSchedule.getStations().size();
		lastStationInformation.setText(activeSchedule.getStations().get(last-1).getName());
		
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
								I18N.getMessage("ScheduleAndTrainTypeComposite.TrainTypeComposite.topSpeed") + "\t" + type.getTopSpeed() + " km/h\t\n" +
								I18N.getMessage("ScheduleAndTrainTypeComposite.TrainTypeComposite.priority") + "\t\t" + type.getPriority()); 
			
			Button delete = new Button(trainType, SWT.PUSH);
			delete.setImage(ImageHelper.getImage("trashIcon"));			 
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
