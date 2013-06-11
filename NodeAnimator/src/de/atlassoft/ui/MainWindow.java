package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;

/**
 * Contains the main window of the application.
 * 
 * @author Silvan Haeussermann
 */
public class MainWindow {

	private Shell shell;
	private Display display;
	private Composite mainComposite;
	private StackLayout layout;
	//TODO: Konstruktor sollte so aussehen: MainWindow(ApplicationService), display in der klasse selber verwalten und swt javadoc einfügen
	/**
	 * The constructor for the main window of the application
	 * 
	 * @param display
	 */
	public MainWindow(){
		
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setText(I18NSingleton.getInstance().getMessage("MainWindow.ProgramName"));
		shell.setSize(640, 480);
		
		mainComposite = new Composite(shell, SWT.BORDER);
		Rectangle frameRect = shell.getMonitor().getBounds();
		mainComposite.setBounds(frameRect);
	    layout = new StackLayout();
	    mainComposite.setLayout(layout);
	    layout.topControl = HomeScreenComposite.createHomeScreenComposite(shell, mainComposite);
	    mainComposite.layout();	    
		
		center(shell);
		createToolbar();
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
	public static void center(Shell shell){ 
		
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
	private void createToolbar(){
		
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
				new ListDialog(display, load);
            }
        });
		
		//the dropdown items of the schedule menu
			//create schedule
		MenuItem newScheduleItem = new MenuItem(scheduleMenu, SWT.PUSH);
		newScheduleItem.setText(I18N.getMessage("MainWindow.CreateSchedule"));
		
		newScheduleItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				layout.topControl = ScheduleComposite.createScheduleComposite(shell, mainComposite);
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
				new TrainTypeDialog(display);
            }
        });
		
		//the dropdown items of the simulation menu
			//start simulation
		MenuItem startSimulation = new MenuItem(simulationMenu, SWT.PUSH);
		startSimulation.setText(I18N.getMessage("MainWindow.StartSimulation"));
		
		startSimulation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 layout.topControl = SimulationComposite.createSimulationComposite(shell, mainComposite);
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
	
	//TODO: delete in final program
	public static void main(String[] args){
		new MainWindow();
		Display display = Display.getDefault();
		display.dispose();
	}
}
