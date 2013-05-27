package de.atlassoft.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
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
	
	/**
	 * The constructor for the main window of the application
	 * 
	 * @param display
	 */
	public MainWindow(Display display){
		
		//this.display = display;
		shell = new Shell(display);
		shell.setText("Choo Choo Motherfucker");
		shell.setSize(640, 480);
		
		center(shell);
		createToolbar();
		initUI();
		
		shell.open();
				
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}		
	}
	
	/**
	 * Creates the UI elements of the main window.
	 * The main window consists of the railsystem, the schedule,
	 * the traintypes and the schedule selector.
	 */
	private void initUI(){
		
		Composite mainComposite = new Composite(shell, SWT.BORDER);
		mainComposite.setLayout(new FillLayout(4));
		Rectangle frameRect = shell.getMonitor().getBounds();
		mainComposite.setBounds(frameRect);
		
		
	}
	
	/**
	 * Centers the given shell in the middle of the screen
	 * 
	 * @param shell
	 */
	public void center(Shell shell){ 
		
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
                System.out.println("Streckennetz erstellen");
                //TODO: implement function
            }
        });
		
			//load railsystem button
		MenuItem loadRailSysItem = new MenuItem(railsysMenu, SWT.PUSH);
		loadRailSysItem.setText("Streckennetz laden");
		
		loadRailSysItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Streckennetz laden");
				//TODO: implement function
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
                System.out.println("Simulation starten");
                //TODO: implement function
            }
        });
		
		
		shell.setMenuBar(menuBar);
	}
			
	public static void main(String[] args){
		Display display = new Display();
		new MainWindow(display);
		display.dispose();
	}
}
