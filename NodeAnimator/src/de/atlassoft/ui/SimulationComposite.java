package de.atlassoft.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.hohenheim.controller.ControllerCanvas;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates the Composite for viewing a simulation.
 * 
 * @author Silvan Haeussermann
 */
public class SimulationComposite {
	
	private Shell shell;
	private StackLayout layout;
	private Composite mainComposite;
	private Composite simulationComposite;
	private I18NService I18N;
	private Image cancelBild;
	private Image playImage;
	private Image pauseImage;
	private ApplicationService applicationService;
	
	/**
	 * Constructor for the SimulationComposite class.
	 * 
	 * @param shell
	 * 		The current shell.
	 * @param mainComposite
	 * 		The composite of the main window.
	 * @param layout
	 * 		The stack layout of the main window.
	 */
	public SimulationComposite (Shell shell, Composite mainComposite, StackLayout layout, ApplicationService applicationService) {
		//TODO: Ausprogrammieren
		this.applicationService = applicationService;
		this.shell = shell;
		this.layout = layout;
		this.mainComposite = mainComposite;
		I18N = I18NSingleton.getInstance();
		initUI();
	}
	
	/**
	 * Builds the elements of the UI of the simulation.
	 */
	private void initUI() {
		
		simulationComposite = new Composite(mainComposite, SWT.NONE);		
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.horizontalSpacing = 20;
	    gridLayout.numColumns = 2;
	    simulationComposite.setLayout(gridLayout);
	    
	    
	    Canvas c = new Canvas(simulationComposite, SWT.FILL);
	    c.setBackground(ColorConstants.white);
	    c.setBounds(0, 0, 600, 500);
	    GridData gridData = new GridData();
	    gridData.horizontalSpan = 1;
	    gridData.widthHint = 600;
	    gridData.heightHint = 500;
	    c.setLayoutData(gridData);
	    
	    Composite controllerComposite = new Composite(simulationComposite, SWT.NONE);
	    GridData controllerGridData = new GridData();
	    controllerGridData.verticalAlignment = SWT.BEGINNING;
	    controllerGridData.verticalIndent = 30 ;
	    controllerGridData.horizontalAlignment = SWT.CENTER;
	    controllerComposite.setLayoutData(controllerGridData);
	    RowLayout rowLayout = new RowLayout();
	    rowLayout.spacing = 30;
	    rowLayout.type = SWT.VERTICAL;	    
	    controllerComposite.setLayout(rowLayout);
	    
	    //play button
	    Button play = new Button(controllerComposite, SWT.PUSH);
	    playImage = new Image(null, "img/playButton.png");
	    play.setImage(playImage);
	    play.setText(I18N.getMessage("SimulationComposite.SimulationStart"));
	    RowData playRowData = new RowData(180, 60);
	    play.setLayoutData(playRowData);
	   // GridData playGridData = new GridData();
	   // playGridData.heightHint = 40;
	    //play.setLayoutData(playGridData);	  
	    
	    //pause button
	    Button pause = new Button(controllerComposite, SWT.PUSH);
	    pauseImage = new Image(null, "img/pauseButton.png");
	    pause.setToolTipText(I18N.getMessage("SimulationComposite.PauseTooltip"));
	    pause.setImage(pauseImage);
	    pause.setText(I18N.getMessage("SimulationComposite.SimulationPause"));
	    pause.setLayoutData(new RowData(180, 60));
//	    GridData pauseGridData = new GridData();
//	    pauseGridData.heightHint = 40;
//	    pause.setLayoutData(pauseGridData);
	    
	    
	    Composite scaleComposite = new Composite(controllerComposite, SWT.NONE);
	    scaleComposite.setLayoutData(new RowData(180, 75));
	    FillLayout fillLayout2 = new FillLayout();
	    fillLayout2.type = SWT.VERTICAL;
	    fillLayout2.spacing = 0;
	    scaleComposite.setLayout(fillLayout2);
	    
	    //Speed scale
	    Label scaleDesc = new Label(scaleComposite, SWT.NONE);
	    scaleDesc.setText(I18N.getMessage("SimulationComposite.SimulationSpeed"));
//	    GridData scaleDescGridData = new GridData();
//	    scaleDesc.setLayoutData(scaleDescGridData);
	    
	    Scale speedScale = new Scale(scaleComposite, SWT.HORIZONTAL);
	    speedScale.setMinimum(1);
	    speedScale.setMaximum(5);
	    speedScale.setIncrement(1);	    
//	    GridData scaleGridData = new GridData();
//	    speedScale.setLayoutData(scaleGridData);	    
	    
	    //Cancel Button
	    Button cancel = new Button(controllerComposite, SWT.PUSH);
	    cancel.setText(I18N.getMessage("SimulationComposite.CancelSimulation"));
	    cancelBild = new Image(null, "img/redX.png");
	    cancel.setImage(cancelBild);
	    cancel.setLayoutData(new RowData(180, 60));
//	    GridData cancelGridData = new GridData();
//	    cancel.setLayoutData(cancelGridData);
	    cancel.addSelectionListener(new SelectionAdapter(){
	    	@Override
	    	public void widgetSelected(SelectionEvent e){
	    		cancelBild.dispose();
	    		pauseImage.dispose();
	    		playImage.dispose();
	    		HomeScreenComposite homeScreenComposite = new HomeScreenComposite(shell, mainComposite, layout, applicationService);		
	    		layout.topControl = homeScreenComposite.getComposite();
	    		mainComposite.layout();
	    	}
	    });
	    
	    
	    //TODO: richtige nodemap holen: applicationService.getModel().getActiveRailwaySys().getNodeMap();
	    NodeMap map = new NodeMap();
	    map.paintNodeMap(c);
	    
	    new ControllerCanvas (mainComposite, SWT.FILL, map);		
	}
	
	/**
	 * Returns the composite of the simulation screen.
	 * 
	 * @return
	 * 		The simulation composite.
	 */
	public Composite getComposite() {
		return simulationComposite;
	}
}
