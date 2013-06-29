 package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * This class creates a composite for the home screen.
 * 
 * @author Silvan Haeussermann
 */
public class HomeScreenComposite {
	
	private Shell shell;
	private Composite mainComposite;
	private Composite homeScreenComposite;
	private I18NService I18N;
	private ApplicationService applicationService;
	
	/**
	 * The constructor for the HomeScreenComposite class.
	 * 	
	 * @param shell
	 * 			The main shell of the application.
	 * @param mainComposite
	 * 			The main composite of the application
	 * @return
	 * 			The homescreen composite.
	 */
	public HomeScreenComposite(Shell shell, Composite mainComposite, ApplicationService applicationService) {
		
		this.applicationService = applicationService;
		I18N = I18NSingleton.getInstance();
		this.shell = shell;
		this.mainComposite = mainComposite;
		
		initUI();
	}
	
	/**
	 * Creates the UI elements.
	 */
	private void initUI() {
		
		homeScreenComposite = new Composite(mainComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		homeScreenComposite.setLayout(gridLayout);
		
		TabFolder tabFolder = new TabFolder (homeScreenComposite, SWT.NONE);
		GridData folderGridData = new GridData();
		folderGridData.grabExcessHorizontalSpace = true;
		folderGridData.grabExcessVerticalSpace = true;
		folderGridData.horizontalAlignment = SWT.FILL;
		folderGridData.verticalAlignment = SWT.FILL;
		tabFolder.setLayoutData(folderGridData);
		
		//Railway system tab
		TabItem railSysItem = new TabItem(tabFolder, SWT.NONE);
		railSysItem.setText(I18N.getMessage("HomeScreenComposite.RailwaySystemTab"));
		CurrentRailSysComposite currentRailSys = new CurrentRailSysComposite(tabFolder, applicationService);
		railSysItem.setControl(currentRailSys.getComposite());
		
		//Schedule and TrainType tab
		TabItem scheduleItem = new TabItem(tabFolder, SWT.NONE);
		scheduleItem.setText(I18N.getMessage("HomeScreenComposite.ScheduleTab"));
		ScheduleAndTrainTypeComposite scheduleAndTrainTypeComposite = new ScheduleAndTrainTypeComposite(shell, tabFolder, applicationService);
		scheduleItem.setControl(scheduleAndTrainTypeComposite.getComposite());
	}
	
	/**
	 * Returns the home screen composite
	 * 
	 * @return
	 * 		The composite
	 */
	public Composite getComposite() {
		return homeScreenComposite;
	}	
}
