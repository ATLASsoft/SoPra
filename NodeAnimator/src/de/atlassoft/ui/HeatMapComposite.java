package de.atlassoft.ui;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.application.ApplicationService;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;
import de.atlassoft.util.ColorHelper;
import de.hohenheim.view.map.NodeMap;
import de.hohenheim.view.node.NodeFigure;
import de.hohenheim.view.path.CenterAnchor;
import de.hohenheim.view.path.HeatPathFigure;
import de.hohenheim.view.path.PathFigure;

/**
 * This class shows a heat map of the last
 * simulation.
 *
 * @author Silvan Haeussermann
 */
public class HeatMapComposite {

	private Composite heatMapComposite;
	private ApplicationService applicationService;
	private NodeMap map;
	private int highestWorkload;
	private SimulationStatistic simulationStatistic;
	
	/**
	 * Constructor for the class HeatMapComposite
	 * 
	 * @param applicationService
	 * 		The applicationService of the application.
	 */
	public HeatMapComposite(TabFolder parent, SimulationStatistic simulationStatistic) {
		heatMapComposite = new Composite(parent, SWT.BORDER);
		this.simulationStatistic = simulationStatistic;
//		this.applicationService = applicationService;
		map = new NodeMap();
		highestWorkload = 0;
		
		//Get the highest workload of the map
		for (Node temp : simulationStatistic.getInvolvedNodes()) {
			int tempWorkload = 0;
			for (Entry<TrainType, Integer> entry : temp.getTrainTypeWorkloadMap().entrySet()) {
				tempWorkload = tempWorkload + entry.getValue();
			}
			if (tempWorkload > highestWorkload) {
				highestWorkload = tempWorkload;
			}
		}
		
		initUI();
	}
	
	/**
	 * Initiates the UI of the class.
	 */
	private void initUI() {
		
		heatMapComposite.setLayout(new GridLayout(2, false));
		
		Canvas c = new Canvas(heatMapComposite, SWT.FILL);
		c.setBackground(ColorConstants.white);
		c.setBounds(0, 0, 600, 500);
		GridData canvasGridData = new GridData();
		canvasGridData.widthHint = 600;
		canvasGridData.heightHint = 500;
		c.setLayoutData(canvasGridData);
		
		//Adds all the nodes to the map
		for (Node temp : simulationStatistic.getRailwaySystem().getNodes()) {
			addNode(temp.getNodeFigure());
		}
		
		//Adds all the paths to the map
		for (Path temp : simulationStatistic.getRailwaySystem().getPaths()) {
			addPath(temp.getStart().getNodeFigure(), temp.getEnd().getNodeFigure(), calculateWorkload(temp));
		}
		
		map.paintNodeMap(c);
		
		//Contains all the information on the right side
		Composite informationComposite = new Composite(heatMapComposite, SWT.BORDER);
		informationComposite.setLayout(new GridLayout());
		
		/*
		 * Container for the legend
		 */
		Composite legendComposite = new Composite(informationComposite, SWT.BORDER);
		legendComposite.setLayout(new GridLayout(3, false));
		
		Label relativeWorkloadLabel = new Label(legendComposite, SWT.NONE);
		relativeWorkloadLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		relativeWorkloadLabel.setText("Legende:");
		
			//first row
		Label yellow = new Label(legendComposite, SWT.NONE);
		yellow.setBackground(ColorHelper.getColor("yellow"));
		
		Label dot1 = new Label(legendComposite, SWT.NONE);
		dot1.setText(":");
		
		Label yellowWorkload = new Label(legendComposite, SWT.NONE);
		yellowWorkload.setText("0 - 20 % Auslastung");
		
			//second row
		Label orange = new Label(legendComposite, SWT.NONE);
		orange.setBackground(ColorHelper.getColor("orange"));
		
		Label dot2 = new Label(legendComposite, SWT.NONE);
		dot2.setText(":");
		
		Label orangeWorkload = new Label(legendComposite, SWT.NONE);
		orangeWorkload.setText("20 - 40 % Auslastung");
		
			//third row
		Label darkOrange = new Label(legendComposite, SWT.NONE);
		darkOrange.setBackground(ColorHelper.getColor("darkOrange"));
		
		Label dot3 = new Label(legendComposite, SWT.NONE);
		dot3.setText(":");
		
		Label darkOrangeWorkload = new Label(legendComposite, SWT.NONE);
		darkOrangeWorkload.setText("40 - 60 % Auslastung");
		
			//fourth row
		Label red = new Label(legendComposite, SWT.NONE);
		red.setBackground(ColorHelper.getColor("red"));
		
		Label dot4 = new Label(legendComposite, SWT.NONE);
		dot4.setText(":");
		
		Label redWorkload = new Label(legendComposite, SWT.NONE);
		redWorkload.setText("60 - 80 % Auslastung");
		
			//fifth row
		Label darkRed = new Label(legendComposite, SWT.NONE);
		darkRed.setBackground(ColorHelper.getColor("darkRed"));
		
		Label dot5 = new Label(legendComposite, SWT.NONE);
		dot5.setText(":");
		
		Label darkRedWorkload = new Label (legendComposite, SWT.NONE);
		darkRedWorkload.setText("80 - 100 % Auslastung");
		
		
		heatMapComposite.layout();
	}
	
	/**
	 * Calculates the relative workload of the path.
	 * 
	 * @return
	 * 		The relative workload of the path.
	 */
	private float calculateWorkload(Path path) {
		
		int pathWorkload = 0;
		for (Entry<TrainType, Integer> entry : path.getTrainTypeWorkloadMap().entrySet()) {
			pathWorkload = pathWorkload + entry.getValue();
		}
		
		return pathWorkload/highestWorkload;
	}
	
	/**
	 * Adds a node to the NodeMap.
	 */
	private void addNode(NodeFigure nodeFigure) {
		
		map.getNodeLayer().add(nodeFigure);
		map.getNodes().put(nodeFigure.getName(), nodeFigure);
		map.getPaths().put(nodeFigure, new ArrayList<PathFigure>());
	}
	
	/**
	 * Adds a path to the NodeMap.
	 * 
	 * @param start
	 * 		The start node.
	 * @param end
	 * 		The end node.
	 */
	private void addPath(NodeFigure start, NodeFigure end, float relativeWorkload) {
		
		HeatPathFigure pathFigure = new HeatPathFigure(relativeWorkload);
		pathFigure.setSourceAnchor(new CenterAnchor(start));
		pathFigure.setTargetAnchor(new CenterAnchor(end));
		
		map.getNodeLayer().add(pathFigure);
		map.getPaths().get(start).add(pathFigure);
		map.getPaths().get(end).add(pathFigure);
		map.getNodeLayer().remove(start);
		map.getNodeLayer().add(start);
		map.getNodeLayer().remove(end);
		map.getNodeLayer().add(end);
	}
	
	/**
	 * Returns the composite of this class.
	 * 
	 * @return
	 * 		The composite.
	 */
	public Composite getComposite() {
		return heatMapComposite;
	}
}
