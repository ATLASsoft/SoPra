package de.atlassoft.ui;

import java.util.Iterator;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
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
	private Text xCoord, yCoord;
	private NodeMap map;
	private Canvas c;
	
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
//				map.paintNodeMap(c);
				for (Iterator<Node> i = applicationService.getModel().getActiveRailwaySys().getNodes().iterator(); i.hasNext();) {
					if (i.next().getNodeFigure().getBounds().contains(new Point(e.x, e.y)) == true) {
						System.out.println("yippiayea");
					}
				}
			}	    	
	    });

		map = applicationService.getModel().getActiveRailwaySys().getNodeMap();
		map.paintNodeMap(c);
		currentRailSysComposite.layout();
		
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
		
		xCoord = new Text(buttonComposite, SWT.BORDER);
		xCoord.setText("X Koordinate");
		
		yCoord = new Text(buttonComposite, SWT.BORDER);
		yCoord.setText("Y Koordinate");
		
		Button createNode = new Button(buttonComposite, SWT.PUSH);
		createNode.setText("Punkt hinzufügen");
		createNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Node node = new Node("Node 1", Integer.parseInt(xCoord.getText()), Integer.parseInt(yCoord.getText()), 30, 30);
				applicationService.getModel().getActiveRailwaySys().addNode(node);
				map.paintNodeMap(c);
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
