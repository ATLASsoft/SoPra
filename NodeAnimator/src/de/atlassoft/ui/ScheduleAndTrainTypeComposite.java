package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.application.ApplicationService;
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
	private Composite scheduleAndTrainTypeComposite;
	private I18NService I18N;
	private ApplicationService applicationService;
	
	/**
	 * Constructor for the class ScheduleAndTrainTypeComposite
	 * 
	 * @param tabFolder
	 * 		The tab folder in which the composite lays.
	 * @param applicationService
	 * 		The application Service of the application.
	 */
	public ScheduleAndTrainTypeComposite(Shell shell, TabFolder tabFolder, ApplicationService applicationService) {
		this.shell = shell;
		I18N = I18NSingleton.getInstance();
		this.applicationService = applicationService;
		this.scheduleAndTrainTypeComposite = new Composite (tabFolder, SWT.BORDER);
		scheduleAndTrainTypeComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Composite scheduleComposite = new Composite(scheduleAndTrainTypeComposite, SWT.BORDER);
		ScrolledComposite trainTypeComposite = new ScrolledComposite (scheduleAndTrainTypeComposite, SWT.BORDER | SWT.V_SCROLL);
		initScheduleUI(scheduleComposite);
		initTrainTypeUI(trainTypeComposite);
	}
	
	/**
	 * Creates the elements for the schedule UI.
	 * 
	 * @param
	 * 		The composite for the elements.
	 */
	private void initScheduleUI(Composite mainComposite) {
		// TODO: Implementieren
	}
	
	/**
	 * Creates the elements for the train type UI.
	 * 
	 * @param
	 * 		The composite for the elements.
	 */
	private void initTrainTypeUI(ScrolledComposite scrolledTrainTypeComposite) {
		// TODO: Implementieren
		Composite trainTypeComposite = new Composite (scrolledTrainTypeComposite, SWT.NULL);
		trainTypeComposite.setLayout(new GridLayout (3, false));
		
		java.util.List<TrainType> trainTypes = applicationService.getModel().getTrainTypes();
		for (final TrainType type : trainTypes) {
		//TODO: if-Bedinung rausnehmen, wenn fertig
		if (type.getImg() == null) {
			new Label (trainTypeComposite, SWT.NULL).setImage(ImageHelper.getImage("standardTrainIcon"));
		 	} else {
		 		new Label (trainTypeComposite, SWT.NULL).setImage(type.getImg());
		 	}
			new CLabel(trainTypeComposite, SWT.NULL).setText("Name:" + "\t\t\t" + type.getName() + "\n" +
															 "Höchstgeschwindigkeit:" + "\t" + type.getTopSpeed() + "\n" +
															 "Priorität:" + "\t\t\t" + type.getPriority()); 
			Button delete = new Button(trainTypeComposite, SWT.PUSH);
			delete.setImage(ImageHelper.getImage("trashIcon"));			 
			 	
			delete.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
		    		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION |SWT.YES | SWT.NO);
		    	    messageBox.setMessage(I18N.getMessage("TrainTypeComposite.deleteQuestion"));
		    	    int rc = messageBox.open();
		    	    if (rc == SWT.YES) {
		    	      	applicationService.deleteTrainType(type);
		            }
		    	}
			});
		}
	 	
		scrolledTrainTypeComposite.setExpandVertical(true);
		scrolledTrainTypeComposite.setExpandHorizontal(true);
		scrolledTrainTypeComposite.setMinSize(trainTypeComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledTrainTypeComposite.setContent(trainTypeComposite);
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
