package de.atlassoft.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import de.hohenheim.view.map.NodeMap;

/**
 * This class creates a composite in which the current railway
 * system is shown.
 * 
 * @author Silvan Haeussermnn
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
	    c.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
//				Node node = new Node("Node", e.x - 7, e.y - 7, 15, 15);
//				applicationService.getModel().getActiveRailwaySys().addNode(node);
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
						informationTable.removeAll();
						final java.util.List<Path> paths = applicationService.getModel().getActiveRailwaySys().getPaths();
						for (Path path : paths) {
							if (path.getStart().equals(temp)) {
						       TableItem item = new TableItem(informationTable, SWT.NONE);
						       item.setText(0, path.getEnd().toString());
						       item.setText(1, String.valueOf(path.getTopSpeed()) + " km/h");
							} else if (path.getEnd().equals(temp)) {
							   TableItem item = new TableItem(informationTable, SWT.NONE);
							   item.setText(0, path.getStart().toString());
							   item.setText(1, String.valueOf(path.getTopSpeed()) + " km/h");
							}
						}
						for (int i=0; i<1; i++) {
					    	informationTable.getColumn (i).pack ();
					    	informationTable.getColumn (i).setWidth(168);
					    } 
					    informationTable.setSize(informationTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					    informationTable.setVisible(true);
					}
				}
			}
	    });

		map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
		map.paintNodeMap(c);
		currentRailSysComposite.layout();
		
		// the composite with the title of Information
		Composite informationComposite = new Composite(currentRailSysComposite, SWT.BORDER);
		informationComposite.setLayout(new GridLayout(1, true));
		informationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// GridDate which center
		GridData dataCenter = new GridData(GridData.FILL);
		dataCenter.horizontalAlignment = GridData.CENTER;
		Label title = new Label (informationComposite, SWT.NULL);
		title.setText(I18N.getMessage("CurrentRailSysComposite.Title"));
		title.setFont(new Font(Display.getCurrent(), new FontData("Helvetica", 13, SWT.BOLD)));
		title.setLayoutData(dataCenter);
		new Label (informationComposite, SWT.NULL);
		
		// the composite with the Information
		Composite informationComposite2 = new Composite(informationComposite, SWT.NULL);
		informationComposite2.setLayout(new GridLayout(2, true));
//		informationComposite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Label with Name
		new Label (informationComposite2, SWT.NULL).setText(I18N.getMessage("CurrentRailSysComposite.labelName"));
		name = new Label (informationComposite2, SWT.NULL);
		name.setText(I18N.getMessage("CurrentRailSysComposite.labelFirstName"));
		
		//Label with coordinates
		new Label (informationComposite2, SWT.NULL).setText(I18N.getMessage("CurrentRailSysComposite.labelCoordinates"));
		coordinate1 = new Label (informationComposite2, SWT.NULL);
		coordinate1.setText("X:   " + I18N.getMessage("CurrentRailSysComposite.noValue"));
		
		new Label (informationComposite2, SWT.NULL);
		coordinate2 = new Label (informationComposite2, SWT.NULL);
		coordinate2.setText("Y:   " + I18N.getMessage("CurrentRailSysComposite.noValue"));
				
		//Composite for the Table
		Composite informationComposite3 = new Composite(informationComposite, SWT.NULL);
		informationComposite3.setLayout(new GridLayout(1, true));
		informationComposite3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		//Label with Table Title
		Label tableTitle = new Label (informationComposite3, SWT.NULL);
		tableTitle.setText("Verbindungen");
		tableTitle.setFont(new Font(Display.getCurrent(), new FontData("Helvetica", 13, SWT.BOLD)));
		tableTitle.setLayoutData(dataCenter);
		
		//Table of Connections
		informationTable = new Table(informationComposite3, SWT.BORDER);
		informationTable.setLinesVisible(true);
		informationTable.setHeaderVisible(true);
		informationTable.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
		
		String[] titles = { I18N.getMessage("CurrentRailSysComposite.tableTitel1"), I18N.getMessage("CurrentRailSysComposite.tableTitel2")};
		for (int i = 0; i < titles.length; i++) {
	    	TableColumn column = new TableColumn(informationTable, SWT.NONE);
	    	column.setText(titles[i]);
	    }
	    
	    for (int i=0; i<titles.length; i++) {
	    	informationTable.getColumn (i).pack ();
	    	informationTable.getColumn (i).setWidth(168);
	    } 
	    informationTable.setSize(informationTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		informationTable.setVisible(false);
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
