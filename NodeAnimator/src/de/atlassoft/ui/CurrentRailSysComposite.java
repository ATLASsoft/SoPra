package de.atlassoft.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
import de.atlassoft.util.ImageHelper;
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates a composite in which the current railway
 * system is shown.
 * 
 * @author Silvan Haeussermnn, Tobias Ilg
 */
public class CurrentRailSysComposite {
	
	private Composite currentRailSysComposite;
	private ApplicationService applicationService;
	private I18NService I18N;
	private NodeMap map;
	private Canvas c;
	private Label name;
	private Label coordinate1;
	private Label coordinate2;
	private Table informationTable;
	private Button exportScheduleButton;
	
	/**
	 * Constructor for the class CurrentRailSysComposite
	 * 
	 * @param tabFolder
	 * 		The tab folder in which the composite should be placed.
	 */
	public CurrentRailSysComposite(TabFolder tabFolder, ApplicationService applicationService) {
		currentRailSysComposite = new Composite(tabFolder, SWT.BORDER);
		this.applicationService = applicationService;
		I18N = I18NSingleton.getInstance();
		initUI();
	}
	
	/**
	 * Builds the elements of the UI of the simulation.
	 */
	private void initUI() {
		currentRailSysComposite.setLayout(new GridLayout(2, false));
		
	    c = new Canvas(currentRailSysComposite, SWT.FILL);
	    c.setBackground(ColorConstants.white);
	    c.setBounds(0, 0, 600, 500);
	    GridData gridData = new GridData();
	    gridData.horizontalSpan = 1;
	    gridData.widthHint = 600;
	    gridData.heightHint = 500;
	    c.setLayoutData(gridData);
	    if (applicationService.getModel().getActiveRailwaySys() != null) {
		    c.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					for (Node temp : applicationService.getModel().getActiveRailwaySys().getNodes()) {
						if (temp.getNodeFigure().getBounds().contains(new Point(e.x, e.y))) {
							name.setText(temp.getName());
							int x = temp.getNodeFigure().getBounds().x;
							int y = temp.getNodeFigure().getBounds().y;
							Integer converter = new Integer(x);
					        String coordinate = converter.toString();
							coordinate1.setText("X:   " + coordinate);
							converter = new Integer(y);
					        coordinate = converter.toString();
							coordinate2.setText("Y:   " + coordinate);
							informationTable.setRedraw(false);
							informationTable.removeAll();
							final java.util.List<Path> paths = applicationService.getModel().getActiveRailwaySys().getPaths();
							for (Path path : paths) {
								if (path.getStart().equals(temp)) {
							       TableItem item = new TableItem(informationTable, SWT.NONE);
							       item.setText(0, path.getEnd().toString());
							       x = path.getEnd().getNodeFigure().getBounds().x;
							       converter = new Integer(x);
							       coordinate = converter.toString();
							       item.setText(1, coordinate);
							       y = path.getEnd().getNodeFigure().getBounds().y;
							       converter = new Integer(y);
							       coordinate = converter.toString();
							       item.setText(2, coordinate);
							       item.setText(3, String.valueOf(path.getTopSpeed()) + " km/h");
								} else if (path.getEnd().equals(temp)) {
								   TableItem item = new TableItem(informationTable, SWT.NONE);
								   item.setText(0, path.getStart().toString());
							       x = path.getStart().getNodeFigure().getBounds().x;
							       converter = new Integer(x);
							       coordinate = converter.toString();
							       item.setText(1, coordinate);
							       y = path.getStart().getNodeFigure().getBounds().y;
							       converter = new Integer(y);
							       coordinate = converter.toString();
							       item.setText(2, coordinate);
								   item.setText(3, String.valueOf(path.getTopSpeed()) + " km/h");
								}
							}
							for (int i=0; i<3; i++) {
						    	informationTable.getColumn (i).pack ();
						    } 
						    informationTable.pack(true);
						    informationTable.setSize(informationTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
						    informationTable.setRedraw(true);
						    informationTable.setVisible(true);
		        			exportScheduleButton.setVisible(true);
						}
					}
				}
		    });
	    }
	    
	    //Catches the error that occurs when there is no active railway system
	    if (applicationService.getModel().getActiveRailwaySys() != null) {
	    	map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
	    }
	    else {
	    	map = new NodeMap();
	    }
		map.paintNodeMap(c);
		currentRailSysComposite.layout();
		
		// the composite of the right side
		Composite currentRailSysInfoComposite = new Composite(currentRailSysComposite, SWT.BORDER);
		currentRailSysInfoComposite.setLayout(new GridLayout(1, true));
		currentRailSysInfoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Information title
		new Label (currentRailSysInfoComposite, SWT.NULL);
		GridData dataCenter = new GridData(GridData.FILL);
		dataCenter.horizontalAlignment = GridData.CENTER;
		Label title = new Label (currentRailSysInfoComposite, SWT.NULL);
		title.setText(I18N.getMessage("CurrentRailSysComposite.Title"));
		title.setFont(new Font(Display.getCurrent(), new FontData("Helvetica", 13, SWT.BOLD)));
		title.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		new Label (currentRailSysInfoComposite, SWT.NULL);
		
		// the composite with the Values 
		Composite informationComposite = new Composite(currentRailSysInfoComposite, SWT.BORDER);
		informationComposite.setLayout(new GridLayout(2, true));
		GridData dataValuesComposite = new GridData(SWT.FILL, SWT.NULL, true, false);
		dataValuesComposite.horizontalAlignment = SWT.CENTER;
		dataValuesComposite.widthHint = 300;
		dataValuesComposite.heightHint = 65;		
		informationComposite.setLayoutData(dataValuesComposite);
		
		// Label with Name
		new Label (informationComposite, SWT.NULL).setText(I18N.getMessage("CurrentRailSysComposite.labelName"));
		name = new Label (informationComposite, SWT.NULL);
		name.setText(I18N.getMessage("CurrentRailSysComposite.labelFirstName"));
		
		//Label with coordinates
		new Label (informationComposite, SWT.NULL).setText(I18N.getMessage("CurrentRailSysComposite.labelCoordinates"));
		coordinate1 = new Label (informationComposite, SWT.NULL);
		coordinate1.setText("X:   " + I18N.getMessage("CurrentRailSysComposite.noValue"));
		
		new Label (informationComposite, SWT.NULL);
		coordinate2 = new Label (informationComposite, SWT.NULL);
		coordinate2.setText("Y:   " + I18N.getMessage("CurrentRailSysComposite.noValue"));
		
		//Label with Table Title
		new Label (currentRailSysInfoComposite, SWT.NULL);
		new Label (currentRailSysInfoComposite, SWT.NULL);
		new Label (currentRailSysInfoComposite, SWT.NULL);
		new Label (currentRailSysInfoComposite, SWT.NULL);
		Label tableTitle = new Label (currentRailSysInfoComposite, SWT.NULL);
		tableTitle.setText(I18N.getMessage("CurrentRailSysComposite.tableCompositeTitel"));
		tableTitle.setFont(new Font(Display.getCurrent(), new FontData("Helvetica", 13, SWT.BOLD)));
		tableTitle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		new Label (currentRailSysInfoComposite, SWT.NULL);
		
		// Table Composite
		Composite tableComposite = new Composite (currentRailSysInfoComposite, SWT.NULL);
		tableComposite.setLayout(new GridLayout(1, true));
		GridData dataComposite = new GridData(SWT.FILL, SWT.NULL, true, false);
		dataComposite.horizontalAlignment = SWT.CENTER;
		dataComposite.widthHint = 300;
		dataComposite.heightHint = 130;
		tableComposite.setLayoutData(dataComposite);		
		
		//Table of Connections
		informationTable = new Table(tableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		informationTable.setLinesVisible(true);
		informationTable.setHeaderVisible(true);
		GridData dataTable = new GridData();
		dataTable.widthHint = 300;
		dataTable.heightHint = 130;
		informationTable.setLayoutData(dataTable);
		
		String[] titles = { I18N.getMessage("CurrentRailSysComposite.tableTitel1"), "X", "Y", I18N.getMessage("CurrentRailSysComposite.tableTitel2")};
		for (int i = 0; i < titles.length; i++) {
	    	TableColumn column = new TableColumn(informationTable, SWT.NONE);
	    	column.setText(titles[i]);
	    }
	    
	    for (int i=0; i<titles.length; i++) {
	    	informationTable.getColumn(i).pack ();
	    } 
	    informationTable.pack();
	    informationTable.setSize(informationTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		informationTable.setVisible(false);
		
		//PDF
		new Label (currentRailSysInfoComposite, SWT.NULL);
		new Label (currentRailSysInfoComposite, SWT.NULL);
		exportScheduleButton = new Button(currentRailSysInfoComposite, SWT.PUSH);
	    exportScheduleButton.setImage(ImageHelper.getImage("pdfIcon"));
	    exportScheduleButton.setText(I18N.getMessage("CurrenTailSysComposite.createPDF"));
	    exportScheduleButton.setLayoutData(dataCenter);
	    exportScheduleButton.setVisible(false);
	    exportScheduleButton.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	Node tempNode = null;
	        	for (Node temp : applicationService.getModel().getActiveRailwaySys().getNodes()) {
	        		if (temp.getName().equals(name.getText())) {
	        			tempNode = temp;
	        			applicationService.showDepartureBoardDoc(tempNode);
	        		}
	        	}
	        }
	    });
	}
	
	/**
	 * Returns the currentRailSysComposite.
	 * 
	 * @return
	 * 		The composite.
	 */
	public Composite getComposite(){
		return currentRailSysComposite;
	}	
}
