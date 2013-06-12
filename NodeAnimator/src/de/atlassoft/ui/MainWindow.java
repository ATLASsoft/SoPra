package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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

	private ApplicationService applicationService;
	private Shell shell;
	private Display display;
	private Composite mainComposite;
	private Composite borderComposite;
	private StackLayout layout;
	private RowLayout rowLayout;
	private I18NService I18N;
	//TODO: Konstruktor sollte so aussehen: MainWindow(ApplicationService)
	/**
	 * The constructor for the main window of the application
	 * 
	 * @param display
	 */
	public MainWindow(ApplicationService applicationService) {
		
		createAllImages();
		this.applicationService = applicationService;
		I18N = I18NSingleton.getInstance();
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setText(I18NSingleton.getInstance().getMessage("MainWindow.ProgramName"));
		shell.setSize(960, 620);
		shell.setImage(ImageHelper.getImage("trainIcon"));
		GridLayout shellLayout = new GridLayout();
		shell.setLayout(shellLayout);
		GridData shellGridData = new GridData();
		shellGridData.grabExcessHorizontalSpace = true;
		shellGridData.grabExcessVerticalSpace = true;
		shell.setLayoutData(shellGridData);
		
		//Creates the overall composite
		borderComposite = new Composite(shell, SWT.BORDER);
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
		mainComposite = new Composite(borderComposite, SWT.BORDER);
		GridData mainCompositeGridData = new GridData();
		mainCompositeGridData.horizontalAlignment = SWT.FILL;
		mainCompositeGridData.grabExcessHorizontalSpace = true;
		mainCompositeGridData.verticalAlignment = SWT.FILL;
		mainCompositeGridData.grabExcessVerticalSpace = true;
		mainComposite.setLayoutData(mainCompositeGridData);
	    layout = new StackLayout();
	    mainComposite.setLayout(layout);
	    HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, layout);
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

		ToolBar toolBar = new ToolBar(borderComposite, SWT.BORDER);
		GridData toolbarGridData = new GridData();
		toolbarGridData.grabExcessHorizontalSpace = true;
		toolbarGridData.horizontalAlignment = SWT.FILL;
		toolBar.setLayoutData(toolbarGridData);
		
		//Create schedule item
		ToolItem createScheduleItem = new ToolItem(toolBar, SWT.PUSH);
		Image createScheduleIcon = new Image (null, "img/scheduleIcon.png");
		createScheduleItem.setImage(createScheduleIcon);
		createScheduleItem.setText(I18N.getMessage("MainWindow.CreateSchedule"));
		createScheduleItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				ScheduleComposite scheduleComposite = new ScheduleComposite(shell, mainComposite, layout);
				layout.topControl = scheduleComposite.getComposite();
				mainComposite.layout();
			}
		});
		
		//Create railway system item
		ToolItem createRailSystemItem = new ToolItem(toolBar, SWT.PUSH);
		Image createRailSysIcon = new Image (null, "img/railSysIcon.png");
		createRailSystemItem.setImage(createRailSysIcon);
		createRailSystemItem.setText(I18N.getMessage("MainWindow.CreateRailwaysys"));
		createRailSystemItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        layout.topControl = RailSysComposite.createRailSysComposite(shell, mainComposite);
		        mainComposite.layout();
            }
        });
		
		//Load RailSystem item
		ToolItem loadRailSystemItem = new ToolItem(toolBar, SWT.PUSH);
		Image loadImage = new Image(null, "img/loadButton.png");
		loadRailSystemItem.setImage(loadImage);
		loadRailSystemItem.setText(I18N.getMessage("MainWindow.LoadRailwaysys"));
		loadRailSystemItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogMode load = DialogMode.LOAD;
				new ListDialog(load);
            }
        });
		
		//Create train type item
		ToolItem createTrainTypeItem = new ToolItem(toolBar, SWT.PUSH);
		createTrainTypeItem.setImage(ImageHelper.getImage("trainIcon"));
		createTrainTypeItem.setText(I18N.getMessage("MainWindow.CreateTrainType"));
		createTrainTypeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new TrainTypeDialog(null); 	// TODO: ApplicationService �bergeben
            }
        });
		
		//Start simulation item
		Image startSimulation = new Image(null, "img/startSimulationIcon.png");
		ToolItem simulationItem = new ToolItem(toolBar, SWT.PUSH);
		simulationItem.setImage(startSimulation);
		simulationItem.setText(I18N.getMessage("MainWindow.StartSimulation"));
		simulationItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				SimulationComposite simComp = new SimulationComposite(shell, mainComposite, layout);
				layout.topControl = simComp.getComposite();
				mainComposite.layout();
            }
        });
		
		//About item
		Image questionMark = new Image(null, "img/question_mark.png");
		ToolItem aboutItem = new ToolItem(toolBar, SWT.PUSH);	
		aboutItem.setImage(questionMark);
		aboutItem.setText(I18N.getMessage("MainWindow.About"));
		aboutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new AboutDialog(display);
			}
		});
	}
	
	//TODO: Im finalen Programm l�schen
	/**
	 * Creates the menuBar for the main window
	 * 
	 */
	private void createMenubar() {
		
		I18NService I18N = I18NSingleton.getInstance();
		//creates the menubar
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu2 = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu3 = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu4 = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu5 = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu6 = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText(I18N.getMessage("MainWindow.File"));
		cascadeFileMenu2.setText(I18N.getMessage("MainWindow.Railsystem"));
		cascadeFileMenu3.setText(I18N.getMessage("MainWindow.Schedule"));
		cascadeFileMenu4.setText(I18N.getMessage("MainWindow.Traintype"));
		cascadeFileMenu5.setText(I18N.getMessage("MainWindow.Simulation"));
		cascadeFileMenu6.setText("?");
		
		//creates the file menu
		Menu fileMenu = new Menu (shell, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);
		
		//creates the railsystem menu
		Menu railsysMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu2.setMenu(railsysMenu);
		
		//creaters the schedule menu
		Menu scheduleMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu3.setMenu(scheduleMenu);
		
		//creates the traintype menu
		Menu traintypeMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu4.setMenu(traintypeMenu);
		
		//creates the simulation menu
		Menu simulationMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu5.setMenu(simulationMenu);
		
		//creates the ? menu
		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu6.setMenu(helpMenu);
		
		//the dropdown items of the file menu
			//quit button
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText(I18N.getMessage("MainWindow.Quit"));
		
		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
		
		//the dropdown items of the railsystem menu
			//new railsystem button
		MenuItem newRailSysItem = new MenuItem(railsysMenu, SWT.PUSH);
		newRailSysItem.setText(I18N.getMessage("MainWindow.CreateRailwaysys"));
		
		newRailSysItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        layout.topControl = RailSysComposite.createRailSysComposite(shell, mainComposite);
		        mainComposite.layout();
            }
        });
		
			//load railsystem button
		MenuItem loadRailSysItem = new MenuItem(railsysMenu, SWT.PUSH);
		loadRailSysItem.setText(I18N.getMessage("MainWindow.LoadRailwaysys"));
		
		loadRailSysItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogMode load = DialogMode.LOAD;
				new ListDialog(load);
            }
        });
		
		//the dropdown items of the schedule menu
			//create schedule
		MenuItem newScheduleItem = new MenuItem(scheduleMenu, SWT.PUSH);
		newScheduleItem.setText(I18N.getMessage("MainWindow.CreateSchedule"));
		
		newScheduleItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				ScheduleComposite scheduleComposite = new ScheduleComposite(shell, mainComposite, layout);
				layout.topControl = scheduleComposite.getComposite();
//				layout.topControl = ScheduleComposite.createScheduleComposite(shell, mainComposite);
				mainComposite.layout();
			}
		});

		//the dropdown items of the traintype menu
			//create traintype
		MenuItem newTraintypeItem = new MenuItem(traintypeMenu, SWT.PUSH);
		newTraintypeItem.setText(I18N.getMessage("MainWindow.CreateTrainType"));
		
		newTraintypeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new TrainTypeDialog(null); 	// TODO: ApplicationService �bergeben
            }
        });
		
		//the dropdown items of the simulation menu
			//start simulation
		MenuItem startSimulation = new MenuItem(simulationMenu, SWT.PUSH);
		startSimulation.setText(I18N.getMessage("MainWindow.StartSimulation"));
		
		startSimulation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				SimulationComposite simComp = new SimulationComposite(shell, mainComposite, layout);
				layout.topControl = simComp.getComposite();
				mainComposite.layout();
            }
        });
		
		//the dropdown items of the ? menu
			//about button
		MenuItem about = new MenuItem(helpMenu, SWT.PUSH);
		about.setText(I18N.getMessage("MainWindow.About"));
		
		about.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				new AboutDialog(display);
			}
		});
				
		shell.setMenuBar(menuBar);
	}	
	
	private void createAllImages(){
		ImageHelper.createImage("loadButton", "img/loadButton.png");
		ImageHelper.createImage("trainIcon", "img/trainIcon.png");
		ImageHelper.createImage("cancelIcon", "img/redX.png");
	}
	
	public void close() {
		Display display = Display.getDefault();
		display.dispose();
	}
}
