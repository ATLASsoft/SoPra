package de.atlassoft.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

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
	
	//TODO: internationalisieren
	private Composite currentRailSysComposite;
	private ApplicationService applicationService;
	private I18NService I18N;
	private Text xCoord, yCoord;
	private NodeMap map;
	private Canvas c;
	private List nodeInformation;
	private Combo combo1, combo2;
	
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
		GridLayout railSysLayout = new GridLayout();
		railSysLayout.numColumns = 2;
		currentRailSysComposite.setLayout(railSysLayout);
		
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
			public void mouseDoubleClick(MouseEvent arg0) {
				
			}

			@Override
			public void mouseDown(MouseEvent arg0) {

			}

			@Override
			public void mouseUp(MouseEvent e) {
//				Node node = new Node("Node", e.x - 7, e.y - 7, 15, 15);
//				applicationService.getModel().getActiveRailwaySys().addNode(node);
				for (Node temp : applicationService.getModel().getActiveRailwaySys().getNodes()) {
					if (temp.getNodeFigure().getBounds().contains(new Point(e.x, e.y))) {
						nodeInformation.removeAll();
						nodeInformation.add(temp.getName());
						nodeInformation.add(temp.getNodeFigure().getBounds().toString());
					}
				}
			}	    	
	    });

		map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
		map.paintNodeMap(c);
		currentRailSysComposite.layout();
		
		// the composite with the controls
		Composite buttonComposite = new Composite(currentRailSysComposite, SWT.BORDER);
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.numColumns = 3;
		buttonComposite.setLayout(buttonLayout);
		GridData buttonGridData = new GridData();
		buttonGridData.grabExcessHorizontalSpace = true;
		buttonGridData.grabExcessVerticalSpace = true;
		buttonGridData.horizontalAlignment = SWT.FILL;
		buttonGridData.verticalAlignment = SWT.FILL;
		buttonComposite.setLayoutData(buttonGridData);
		
		// create node row
		xCoord = new Text(buttonComposite, SWT.BORDER);
		xCoord.setText("X Koordinate");
		xCoord.selectAll();
		
		yCoord = new Text(buttonComposite, SWT.BORDER);
		yCoord.setText("Y Koordinate");
		yCoord.selectAll();
		
		Button createNode = new Button(buttonComposite, SWT.PUSH);
		createNode.setText("Punkt hinzufügen");
		createNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Node node = new Node("Node 1", Integer.parseInt(xCoord.getText()), Integer.parseInt(yCoord.getText()), 30, 30);
				applicationService.getModel().getActiveRailwaySys().addNode(node);
			}
		});
		
		// add path row
		combo1 = new Combo(buttonComposite, SWT.NULL);
		combo2 = new Combo(buttonComposite, SWT.NULL);
		for (Node temp : applicationService.getModel().getActiveRailwaySys().getNodes()) {
			combo1.add(temp.getName());
			combo2.add(temp.getName());
		}
		combo1.select(0);
		combo2.select(1);
		
		Button createPath = new Button(buttonComposite, SWT.PUSH);
		createPath.setText("Create Path");
		createPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Node node1 = null, node2;
				for (Node temp : applicationService.getModel().getActiveRailwaySys().getNodes()) {
					if (temp.getName().equals(combo1.getItem(combo1.getSelectionIndex()))) {
						node1 = temp;
					}
					if (temp.getName().equals(combo2.getItem(combo2.getSelectionIndex()))) {
						node2 = temp;
						applicationService.getModel().getActiveRailwaySys().addPath(new Path(node1, node2, 0));
					}
				}
			}
		});
		
		//node information list
		nodeInformation = new List(buttonComposite, SWT.BORDER);
		nodeInformation.setEnabled(false);
		nodeInformation.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		GridData nodeInformationData = new GridData();
		nodeInformationData.horizontalSpan = 3;
		nodeInformationData.verticalAlignment = SWT.FILL;
		nodeInformationData.horizontalAlignment = SWT.FILL;
		nodeInformationData.grabExcessHorizontalSpace = true;
		nodeInformationData.grabExcessVerticalSpace = true;
		nodeInformation.setLayoutData(nodeInformationData);
		nodeInformation.add("Auf einen Knoten klicken");
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
