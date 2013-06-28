package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * This class creates the Tab Folder in which the train types
 * and the schedules are shown.
 * 
 * @author Tobias Ilg, Silvan Haeussermann 
 *
 */
public class ScheduleAndTrainTypeComposite {
	
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
	public ScheduleAndTrainTypeComposite(TabFolder tabFolder, ApplicationService applicationService) {
		I18N = I18NSingleton.getInstance();
		this.applicationService = applicationService;
		this.scheduleAndTrainTypeComposite = new Composite (tabFolder, SWT.BORDER);
		scheduleAndTrainTypeComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Composite scheduleComposite = new Composite(scheduleAndTrainTypeComposite, SWT.BORDER);
		Composite trainTypeComposite = new Composite (scheduleAndTrainTypeComposite, SWT.BORDER);
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
	private void initTrainTypeUI(Composite mainComposite) {
		// TODO: Implementieren
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
