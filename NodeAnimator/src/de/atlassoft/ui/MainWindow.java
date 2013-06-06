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
	
	/**
	 * The constructor for the main window of the application
	 * 
	 * @param display
	 */
	public MainWindow(Display display){
		
		shell = new Shell(display);
		shell.setText("Choo Choo Motherfucker");
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
		
		//creates the menubar
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu2 = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu3 = new MenuItem(menuBar, SWT.CASCADE);
		MenuItem cascadeFileMenu4 = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText("Datei");
		cascadeFileMenu2.setText("Streckennetz");
		cascadeFileMenu3.setText("Zugtyp");
		cascadeFileMenu4.setText("Simulation");
		
		//creates the file menu
		Menu fileMenu = new Menu (shell, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);
		
		//creates the railsystem menu
		Menu railsysMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu2.setMenu(railsysMenu);
		
		//creates the traintype menu
		Menu traintypeMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu3.setMenu(traintypeMenu);
		
		//creates the simulation menu
		Menu simulationMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu4.setMenu(simulationMenu);
		
		//the dropdown items of the file menu
			//exit button
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("Beenden");
		
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
		newRailSysItem.setText("Streckennetz erstellen");
		
		newRailSysItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        layout.topControl = RailSysComposite.createRailSysComposite(shell, mainComposite);
		        mainComposite.layout();
            }
        });
		
			//load railsystem button
		MenuItem loadRailSysItem = new MenuItem(railsysMenu, SWT.PUSH);
		loadRailSysItem.setText("Streckennetz laden");
		
		loadRailSysItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogMode load = DialogMode.LOAD;
				new ListDialog(display, load);
            }
        });

		//the dropdown items of the traintype menu
			//create traintype
		MenuItem newTraintypeItem = new MenuItem(traintypeMenu, SWT.PUSH);
		newTraintypeItem.setText("Zugtyp erstellen");
		
		newTraintypeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new TrainTypeDialog(display);
            }
        });
		
		//the dropdown items of the simulation menu
			//start simulation
		MenuItem startSimulation = new MenuItem(simulationMenu, SWT.PUSH);
		startSimulation.setText("Simulation starten");
		
		startSimulation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 layout.topControl = SimulationComposite.createSimulationComposite(shell, mainComposite);
			     mainComposite.layout();
            }
        });
		
		
		shell.setMenuBar(menuBar);
	}
	
	//TODO: delete in final program
	public static void main(String[] args){
		Display display = new Display();
		new MainWindow(display);
		display.dispose();
	}
}
