package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;

/**
 * Contains the main window of the application.
 * 
 * @author Silvan Haeussermann
 */
public class MainWindow {

	//TODO: Schließen Button überarbeiten
	//TODO: Fenstergröße verändern
	private ApplicationService applicationService;
	private Shell shell;
	private Display display;
	private Composite mainComposite;
	private Composite borderComposite;
	private StackLayout layout;
	private I18NService I18N;
	
	/**
	 * The constructor for the main window of the application
	 * 
	 * @param display
	 */
	public MainWindow(ApplicationService applicationService) {
				
		this.applicationService = applicationService;
		I18N = I18NSingleton.getInstance();
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setText(I18NSingleton.getInstance().getMessage("MainWindow.ProgramName"));
		shell.setSize(1050, 670);
		shell.setImage(ImageHelper.getImage("trainIcon"));
		GridLayout shellLayout = new GridLayout();
		shell.setLayout(shellLayout);
		GridData shellGridData = new GridData();
		shellGridData.grabExcessHorizontalSpace = true;
		shellGridData.grabExcessVerticalSpace = true;
		shell.setLayoutData(shellGridData);
		
		//Creates the overall composite
		borderComposite = new Composite(shell, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		borderComposite.setLayout(gridLayout);
		GridData borderGridData = new GridData();
		borderGridData.grabExcessHorizontalSpace = true;
		borderGridData.grabExcessVerticalSpace = true;
		borderGridData.verticalAlignment = SWT.FILL;
		borderGridData.horizontalAlignment = SWT.FILL;
		borderComposite.setLayoutData(borderGridData);
		
		createToolbar();
		
		//Creates the main screen with the different tabs
		mainComposite = new Composite(borderComposite, SWT.NONE);
		GridData mainCompositeGridData = new GridData();
		mainCompositeGridData.horizontalAlignment = SWT.FILL;
		mainCompositeGridData.grabExcessHorizontalSpace = true;
		mainCompositeGridData.verticalAlignment = SWT.FILL;
		mainCompositeGridData.grabExcessVerticalSpace = true;
		mainComposite.setLayoutData(mainCompositeGridData);
	    layout = new StackLayout();
	    mainComposite.setLayout(layout);
	    HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, layout, applicationService);
	    layout.topControl = homeScreenComposite.getComposite();
	    mainComposite.layout();	    
		
	    borderComposite.layout();
		center(shell);		
		shell.open();
				
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}		
	}
	
	/**
	 * Centers the given shell in the middle of the screen
	 * 
	 * @param shell
	 */
	public static void center(Shell shell) { 
		
		//Rectangle bdc = shell.getDisplay().getBounds();
		Rectangle bdc = shell.getMonitor().getBounds();
		
		Point p = shell.getSize();
		
		int Left = (bdc.width - p.x) / 2;
		int Top = (bdc.height - p.y) / 2;
		
		shell.setBounds(Left, Top, p.x, p.y);
	}	
	
	/**
	 * Creates the toolbar for the main window
	 * 
	 */
	private void createToolbar() {

		ToolBar toolBar = new ToolBar(borderComposite, SWT.NONE);
		GridData toolbarGridData = new GridData();
		toolbarGridData.grabExcessHorizontalSpace = true;
		toolbarGridData.horizontalAlignment = SWT.FILL;
		toolBar.setLayoutData(toolbarGridData);
		
		//Create schedule item
		ToolItem createScheduleItem = new ToolItem(toolBar, SWT.PUSH);
		createScheduleItem.setImage(ImageHelper.getImage("scheduleIcon"));
		createScheduleItem.setText(I18N.getMessage("MainWindow.CreateSchedule"));
		createScheduleItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				ScheduleComposite scheduleComposite = new ScheduleComposite(shell, mainComposite, layout, applicationService);
				layout.topControl = scheduleComposite.getComposite();
				mainComposite.layout();
			}
		});
		
		//Create railway system item
		ToolItem createRailSystemItem = new ToolItem(toolBar, SWT.PUSH);
		createRailSystemItem.setImage(ImageHelper.getImage("railwaySysIcon"));
		createRailSystemItem.setText(I18N.getMessage("MainWindow.CreateRailwaysys"));
		createRailSystemItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        layout.topControl = RailSysComposite.createRailSysComposite(shell, mainComposite);
		        mainComposite.layout();
            }
        });
		
		//manage RailSystem item
		ToolItem manageRailSystemItem = new ToolItem(toolBar, SWT.PUSH);
		manageRailSystemItem.setImage(ImageHelper.getImage("settingsIcon"));
		manageRailSystemItem.setText(I18N.getMessage("MainWindow.ManageRailwaysys"));
		manageRailSystemItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new RailSysDialog(applicationService);
			}
		});
		
		//Create train type item
		ToolItem createTrainTypeItem = new ToolItem(toolBar, SWT.PUSH);
		createTrainTypeItem.setImage(ImageHelper.getImage("trainIcon"));
		createTrainTypeItem.setText(I18N.getMessage("MainWindow.CreateTrainType"));
		createTrainTypeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new TrainTypeDialog(applicationService);
            }
        });
		
		//Start simulation item
		ToolItem simulationItem = new ToolItem(toolBar, SWT.PUSH);
		simulationItem.setImage(ImageHelper.getImage("playIcon"));
		simulationItem.setText(I18N.getMessage("MainWindow.StartSimulation"));
		simulationItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				SimulationComposite simComp = new SimulationComposite(shell, mainComposite, layout, applicationService);
				layout.topControl = simComp.getComposite();
				mainComposite.layout();
            }
        });
		
		//About item
		ToolItem aboutItem = new ToolItem(toolBar, SWT.PUSH);	
		aboutItem.setImage(ImageHelper.getImage("questionMarkIcon"));
		aboutItem.setText(I18N.getMessage("MainWindow.About"));
		aboutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new AboutDialog(display);
			}
		});
	}
	
	public void close() {
		Display display = Display.getDefault();
		display.dispose();
	}
}
