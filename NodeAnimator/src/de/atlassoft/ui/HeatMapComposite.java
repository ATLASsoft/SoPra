package de.atlassoft.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;

import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;
import de.atlassoft.util.ColorHelper;
import de.atlassoft.util.I18NService;
import de.atlassoft.util.I18NSingleton;
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
	
	private Composite heatMapComposite, informationComposite, nodeInformationComposite;
	private NodeMap map;
	private I18NService I18N;
	private int highestWorkload;
	private SimulationStatistic simulationStatistic;
	private HashMap<NodeFigure, NodeFigure> nodeFigures;
	private Combo scheduleCombo, trainTypeCombo;
	private ScheduleScheme activeSchedule;
	private TrainType activeTrainType;
	private Font captionFont;
	private DecimalFormat df;
	
	/**
	 * Constructor for the class HeatMapComposite
	 * 
	 * @param applicationService
	 * 		The applicationService of the application.
	 */
	public HeatMapComposite(TabFolder parent, SimulationStatistic simulationStatistic) {
		heatMapComposite = new Composite(parent, SWT.NONE);
		this.simulationStatistic = simulationStatistic;
		I18N = I18NSingleton.getInstance();
		map = new NodeMap();
		highestWorkload = 0;
		nodeFigures = new HashMap<NodeFigure, NodeFigure>();
		captionFont = new Font(Display.getCurrent(), "Arial", 9, SWT.BOLD);
		
		//format the counter
		df = new DecimalFormat("########0.00");
		
		calculateHighestOverallWorkload();
		
		initUI();
	}
	
	/**
	 * Initiates the UI of the class.
	 */
	private void initUI() {
		
		heatMapComposite.setLayout(new GridLayout(2, false));
		
		//Creates the map
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
			addPath(temp.getStart().getNodeFigure(), temp.getEnd().getNodeFigure(), calculateOverallWorkload(temp));
		}
		c.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (Node temp : simulationStatistic.getRailwaySystem().getNodes()) {
					if (temp.getNodeFigure().getBounds().contains(new Point(e.x, e.y))) {
						disposeInfoComposite();
						createNodeInformation(nodeInformationComposite, temp);
						return;
					}
				}
				for (Path temp : simulationStatistic.getRailwaySystem().getPaths()) {
					if (temp.getPathFigure().containsPoint(new Point(e.x, e.y))) {
						disposeInfoComposite();
						createPathInformation(nodeInformationComposite, temp);
						return;
					}
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		map.paintNodeMap(c);
		
		//Contains all the information on the right side
		informationComposite = new Composite(heatMapComposite, SWT.NONE);
		informationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout informationCompositeLayout = new GridLayout();
		informationCompositeLayout.verticalSpacing = 10;
		informationComposite.setLayout(informationCompositeLayout);
		
		/*
		 * Container for the selection
		 */
		Composite selectionComposite = new Composite(informationComposite, SWT.BORDER);
		selectionComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		selectionComposite.setLayout(new GridLayout(2, false));
		
		Label selectionCompositeTitle = new Label(selectionComposite, SWT.NONE);
		GridData selectionCompositeTitleData = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		selectionCompositeTitleData.heightHint = 30;
		selectionCompositeTitle.setLayoutData(selectionCompositeTitleData);
		selectionCompositeTitle.setFont(captionFont);
		selectionCompositeTitle.setText(I18N.getMessage("HeatMapComposite.ShowHeatmap"));
		
		Label scheduleLabel = new Label(selectionComposite, SWT.NONE);
		scheduleLabel.setText(I18N.getMessage("HeatMapComposite.Schedule"));
		
		scheduleCombo = new Combo(selectionComposite, SWT.READ_ONLY);
		scheduleCombo.add(I18N.getMessage("HeatMapComposite.AllSchedules"));
		for (ScheduleScheme temp : simulationStatistic.getInvolvedScheduleSchemes()) {
			scheduleCombo.add(temp.getID());
		}
		scheduleCombo.select(0);
		scheduleCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				clearMap();
				trainTypeCombo.select(0);
				for (ScheduleScheme temp : simulationStatistic.getInvolvedScheduleSchemes()) {
					if (temp.getID().equals(scheduleCombo.getItem(scheduleCombo.getSelectionIndex()))) {
						activeSchedule = temp;
						activeTrainType = null;
						disposeInfoComposite();
						createNoInformatoin(nodeInformationComposite);
						
						calculateHighestScheduleWorkload(temp);
						paintMap(temp);
						return;
					}
				}
				activeTrainType = null;
				activeSchedule = null;
				disposeInfoComposite();
				createNoInformatoin(nodeInformationComposite);
				
				calculateHighestOverallWorkload();
				paintMap();
			}
		});
		
		Label trainTypeLabel = new Label(selectionComposite, SWT.NONE);
		trainTypeLabel.setText(I18N.getMessage("HeatMapComposite.TrainType"));
		
		trainTypeCombo = new Combo(selectionComposite, SWT.READ_ONLY);
		trainTypeCombo.add(I18N.getMessage("HeatMapComposite.AllTrainTypes"));
		for (TrainType temp : simulationStatistic.getInvolvedTrainTypes()) {
			trainTypeCombo.add(temp.getName());
		}
		trainTypeCombo.select(0);
		trainTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				clearMap();
				scheduleCombo.select(0);
				for (TrainType temp : simulationStatistic.getInvolvedTrainTypes()) {
					if (temp.getName().equals(trainTypeCombo.getItem(trainTypeCombo.getSelectionIndex()))) {
						activeTrainType = temp;
						activeSchedule = null;
						disposeInfoComposite();
						createNoInformatoin(nodeInformationComposite);
						
						calculateHighestTrainTypeWorkload(temp);
						paintMap(temp);
						return;
					}
				}
				activeTrainType = null;
				activeSchedule = null;
				disposeInfoComposite();
				createNoInformatoin(nodeInformationComposite);
				
				calculateHighestOverallWorkload();
				paintMap();
			}
		});
		
		/*
		 * Container for the node and train information
		 */
		nodeInformationComposite = new Composite(informationComposite, SWT.BORDER);
		nodeInformationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		nodeInformationComposite.setLayout(new GridLayout(2, false));
		
		Label nodeInformationLabel = new Label(nodeInformationComposite, SWT.NONE);
		GridData nodeInformationLabelData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		nodeInformationLabelData.heightHint = 25;
		nodeInformationLabel.setLayoutData(nodeInformationLabelData);
		nodeInformationLabel.setFont(captionFont);

		nodeInformationLabel.setText(I18N.getMessage("HeatMapComposite.Information"));
		
		Label noInformation = new Label(nodeInformationComposite, SWT.NONE);
		noInformation.setText(I18N.getMessage("HeatMapComposite.NoElementSelected"));
		
		/*
		 * Container for the legend
		 */
		Composite legendComposite = new Composite(informationComposite, SWT.BORDER);
		legendComposite.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
		legendComposite.setLayout(new GridLayout(3, false));
		
		Label relativeWorkloadLabel = new Label(legendComposite, SWT.NONE);
		GridData relativeWorkloadLabelData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		relativeWorkloadLabelData.heightHint = 25;
		relativeWorkloadLabel.setLayoutData(relativeWorkloadLabelData);
		relativeWorkloadLabel.setFont(captionFont);
		relativeWorkloadLabel.setText(I18N.getMessage("HeatMapComposite.Legend"));
		
			//first row
		Label yellow = new Label(legendComposite, SWT.NONE);
		yellow.setBackground(ColorHelper.getColor("yellow"));
		yellow.setLayoutData(new GridData(20, 2));
		
		Label dot1 = new Label(legendComposite, SWT.NONE);
		dot1.setText(":");
		
		Label yellowWorkload = new Label(legendComposite, SWT.NONE);
		yellowWorkload.setText(I18N.getMessage("HeatMapComposite.Legend1"));
		
			//second row
		Label orange = new Label(legendComposite, SWT.NONE);
		orange.setBackground(ColorHelper.getColor("orange"));
		orange.setLayoutData(new GridData(20, 4));
		
		Label dot2 = new Label(legendComposite, SWT.NONE);
		dot2.setText(":");
		
		Label orangeWorkload = new Label(legendComposite, SWT.NONE);
		orangeWorkload.setText(I18N.getMessage("HeatMapComposite.Legend2"));
		
			//third row
		Label darkOrange = new Label(legendComposite, SWT.NONE);
		darkOrange.setBackground(ColorHelper.getColor("darkOrange"));
		darkOrange.setLayoutData(new GridData(20, 6));
		
		Label dot3 = new Label(legendComposite, SWT.NONE);
		dot3.setText(":");
		
		Label darkOrangeWorkload = new Label(legendComposite, SWT.NONE);
		darkOrangeWorkload.setText(I18N.getMessage("HeatMapComposite.Legend3"));
		
			//fourth row
		Label red = new Label(legendComposite, SWT.NONE);
		red.setBackground(ColorHelper.getColor("red"));
		red.setLayoutData(new GridData(20, 8));
		
		Label dot4 = new Label(legendComposite, SWT.NONE);
		dot4.setText(":");
		
		Label redWorkload = new Label(legendComposite, SWT.NONE);
		redWorkload.setText(I18N.getMessage("HeatMapComposite.Legend4"));
		
			//fifth row
		Label darkRed = new Label(legendComposite, SWT.NONE);
		darkRed.setBackground(ColorHelper.getColor("darkRed"));
		darkRed.setLayoutData(new GridData(20, 10));
		
		Label dot5 = new Label(legendComposite, SWT.NONE);
		dot5.setText(":");
		
		Label darkRedWorkload = new Label (legendComposite, SWT.NONE);
		darkRedWorkload.setText(I18N.getMessage("HeatMapComposite.Legend5"));
		
		
		heatMapComposite.layout();
	}
	
	/**
	 * Calculates the highest workload of all schedules and traintypes.
	 */
	private void calculateHighestOverallWorkload() {
		highestWorkload = 0;
		for (Node temp : simulationStatistic.getInvolvedNodes()) {
			int tempWorkload = 0;
			for (Entry<TrainType, Integer> entry : temp.getTrainTypeWorkloadMap().entrySet()) {
				tempWorkload = tempWorkload + entry.getValue();
			}
			if (tempWorkload > highestWorkload) {
				highestWorkload = tempWorkload;
			}
		}
	}
	
	
	/**
	 * Calculates the highest workload of one schedule.
	 */
	private void calculateHighestScheduleWorkload(ScheduleScheme schedule) {
		highestWorkload = 0;
		for (Node temp : simulationStatistic.getInvolvedNodes()) {
			int tempWorkload = 0;
			for (Entry<ScheduleScheme, Integer> entry : temp.getScheduleSchemeWorkloadMap().entrySet()) {
				if (entry.getKey().getID().equals(schedule.getID())) {
					tempWorkload = tempWorkload + entry.getValue();
				}
			}
			if (tempWorkload > highestWorkload) {
				highestWorkload = tempWorkload;
			}
		}
	}
	
	/**
	 * Calculates the highest workload of one train type.
	 */
	private void calculateHighestTrainTypeWorkload(TrainType trainType) {
		highestWorkload = 0;
		for (Node temp : simulationStatistic.getInvolvedNodes()) {
			int tempWorkload = 0;
			for (Entry<TrainType, Integer> entry : temp.getTrainTypeWorkloadMap().entrySet()) {
				if (entry.getKey().getName().equals(trainType.getName())) {
					tempWorkload = tempWorkload + entry.getValue();
				}
			}
			if (tempWorkload > highestWorkload) {
				highestWorkload = tempWorkload;
			}
		}
	}
	
	
	/**
	 * Calculates the relative workload of the path.
	 * 
	 * @return
	 * 		The relative workload of the path.
	 */
	private float calculateOverallWorkload(Path path) {
		
		int pathWorkload = 0;
		for (Entry<ScheduleScheme, Integer> entry : path.getScheduleSchemeWorkloadMap().entrySet()) {
			pathWorkload = pathWorkload + entry.getValue();
		}
		float wl = (float) pathWorkload/(float)highestWorkload;
		return wl;
	}
	
	/**
	 * Calculates the workload for a given schedule.
	 * 
	 * @param path
	 * 		The path
	 * @param schedule
	 * 		The schedule which should be calculated
	 * @return
	 * 		The workload
	 */
	private float calculateScheduleWorkload(Path path, ScheduleScheme schedule) {
		
		int pathWorkload = 0;
		for (Entry<ScheduleScheme, Integer> entry : path.getScheduleSchemeWorkloadMap().entrySet()) {
			if (entry.getKey().getID().equals(schedule.getID())) {
				pathWorkload = pathWorkload + entry.getValue();
			}
		}
		float wl = (float) pathWorkload / (float) highestWorkload;
		return wl;
	}
	
	/**
	 * Calculates the workload for a given train type.
	 * 
	 * @param path
	 * 		The path.
	 * @param trainType
	 * 		The train type which should be calculated
	 * @return
	 * 		The workload
	 */
	private float calculateTrainTypeWorkload(Path path, TrainType trainType) {
		
		int pathWorkload = 0;
		for (Entry<TrainType, Integer> entry : path.getTrainTypeWorkloadMap().entrySet()) {
			if (entry.getKey().getName().equals(trainType.getName())) {
				pathWorkload = pathWorkload + entry.getValue();
			}
		}
		float wl = (float) pathWorkload / (float) highestWorkload;
		return wl;
	}
	
	
	/**
	 * Adds a node to the NodeMap.
	 */
	private void addNode(NodeFigure nodeFigure) {
		
		NodeFigure figure = new NodeFigure(null);
		figure.setName(nodeFigure.getName());
		figure.setBounds(nodeFigure.getBounds());
		
		map.getNodeLayer().add(figure);
		map.getNodes().put(figure.getName(), figure);
		map.getPaths().put(figure, new ArrayList<PathFigure>());
		nodeFigures.put(nodeFigure, figure);
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
		
		NodeFigure newStart = nodeFigures.get(start);
		NodeFigure newEnd = nodeFigures.get(end);
		
		HeatPathFigure pathFigure = new HeatPathFigure(relativeWorkload);
		pathFigure.setSourceAnchor(new CenterAnchor(newStart));
		pathFigure.setTargetAnchor(new CenterAnchor(newEnd));
		
		map.getNodeLayer().add(pathFigure);
		map.getPaths().get(newStart).add(pathFigure);
		map.getPaths().get(newEnd).add(pathFigure);
		map.getNodeLayer().remove(newStart);
		map.getNodeLayer().add(newStart);
		map.getNodeLayer().remove(newEnd);
		map.getNodeLayer().add(newEnd);
	}
	
	/**
	 * Deletes all the elements from the map.
	 */
	private void clearMap() {
		map.getNodeLayer().removeAll();
		map.getPaths().clear();
		map.getNodes().clear();
	}
	
	/**
	 * Paints the map with the present highest workload.
	 */
	private void paintMap() {
		//Adds all the nodes to the map
		for (Node NodeTemp : simulationStatistic.getRailwaySystem().getNodes()) {
			addNode(NodeTemp.getNodeFigure());
		}
		
		//Adds all paths to the map with the new workload
		for (Path PathTemp : simulationStatistic.getRailwaySystem().getPaths()) {
			addPath(PathTemp.getStart().getNodeFigure(), PathTemp.getEnd().getNodeFigure(), calculateOverallWorkload(PathTemp));
		}
	}
	
	/**
	 * Paints the map with the present highest workload.
	 */
	private void paintMap(ScheduleScheme schedule) {
		//Adds all the nodes to the map
		for (Node NodeTemp : simulationStatistic.getRailwaySystem().getNodes()) {
			addNode(NodeTemp.getNodeFigure());
		}
		
		//Adds all paths to the map with the new workload
		for (Path PathTemp : simulationStatistic.getRailwaySystem().getPaths()) {
			addPath(PathTemp.getStart().getNodeFigure(), PathTemp.getEnd().getNodeFigure(), calculateScheduleWorkload(PathTemp, schedule));
		}
	}
	
	/**
	 * Paints the map with the present highest workload.
	 */
	private void paintMap(TrainType trainType) {
		//Adds all the nodes to the map
		for (Node NodeTemp : simulationStatistic.getRailwaySystem().getNodes()) {
			addNode(NodeTemp.getNodeFigure());
		}
		
		//Adds all paths to the map with the new workload
		for (Path PathTemp : simulationStatistic.getRailwaySystem().getPaths()) {
			addPath(PathTemp.getStart().getNodeFigure(), PathTemp.getEnd().getNodeFigure(), calculateTrainTypeWorkload(PathTemp, trainType));
		}
	}
	
	/**
	 * Creates the information of the selected Node.
	 * 
	 * @param composite
	 * 		The nodeInformationComposite
	 * @param node
	 * 		The selected node
	 */
	private void createNodeInformation(Composite composite, Node node) {
		Label title = new Label(composite, SWT.NONE);
		GridData titleData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		titleData.heightHint = 25;
		title.setLayoutData(titleData);
		title.setFont(captionFont);
		title.setText(I18N.getMessage("HeatMapComposite.StationInformation"));
		
		Label nameTitle = new Label(composite, SWT.NONE);
		nameTitle.setText(I18N.getMessage("HeatMapComposite.StationName"));
		
		Label name = new Label(composite, SWT.NONE);
		name.setText(node.getName());
		
		Label counterTitle = new Label(composite, SWT.NONE);
		counterTitle.setText(I18N.getMessage("HeatMapComposite.CountRides"));
		
		int counterC = 0;
		double meanDelayM = 0;
		//if a special train type is selected
		if (activeSchedule == null && activeTrainType != null) {
			for (Entry<TrainType, Integer> entry : node.getTrainTypeWorkloadMap().entrySet()) {
				if (entry.getKey().getName().equals(activeTrainType.getName())) {
					counterC = counterC + entry.getValue();
				}
			}
			meanDelayM = simulationStatistic.getMeanDelay(activeTrainType, node);
		}
		//if a special schedule is selected
		else if (activeSchedule != null && activeTrainType == null){
			for (Entry<ScheduleScheme, Integer> entry : node.getScheduleSchemeWorkloadMap().entrySet()) {
				if (entry.getKey().getID().equals(activeSchedule.getID())) {
					counterC = counterC + entry.getValue();
				}
			}
			meanDelayM = simulationStatistic.getMeanDelay(activeSchedule, node);
		}
		else {
			for (Entry<ScheduleScheme, Integer> entry : node.getScheduleSchemeWorkloadMap().entrySet()) {
				counterC = counterC + entry.getValue();
			}
			meanDelayM = simulationStatistic.getMeanDelay(node);
		}

		Label counter = new Label(composite, SWT.NONE);
		counter.setText(String.valueOf(counterC));
		
		Label meanDelayTitle = new Label(composite, SWT.NONE);
		meanDelayTitle.setText(I18N.getMessage("HeatMapComposite.MeanDelay"));
		
		Label meanDelay = new Label(composite, SWT.NONE);
		meanDelay.setText(String.valueOf(df.format(meanDelayM/60) + " m"));
		
		composite.layout();
		informationComposite.layout();
	}
	
	/**
	 * Creates the information about the selected path.
	 * 
	 * @param composite
	 * 		The nodeInformationComposite
	 * @param path
	 * 		The selected path
	 */
	private void createPathInformation(Composite composite, Path path) {
		Label title = new Label(composite, SWT.NONE);
		GridData titleData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		titleData.heightHint = 25;
		title.setLayoutData(titleData);
		title.setFont(captionFont);
		title.setText(I18N.getMessage("HeatMapComposite.PathInformation"));
		
		Label counterTitle = new Label(composite, SWT.NONE);
		counterTitle.setText(I18N.getMessage("HeatMapComposite.CountRides"));
		
		int counterC = 0;
		//if a special train type is selected
		if (activeSchedule == null && activeTrainType != null) {
			for (Entry<TrainType, Integer> entry : path.getTrainTypeWorkloadMap().entrySet()) {
				if (entry.getKey().getName().equals(activeTrainType.getName())) {
					counterC = counterC + entry.getValue();
				}
			}
		}
		//if a special schedule is selected
		else if (activeSchedule != null && activeTrainType == null){
			for (Entry<ScheduleScheme, Integer> entry : path.getScheduleSchemeWorkloadMap().entrySet()) {
				if (entry.getKey().getID().equals(activeSchedule.getID())) {
					counterC = counterC + entry.getValue();
				}
			}
		}
		else {
			for (Entry<ScheduleScheme, Integer> entry : path.getScheduleSchemeWorkloadMap().entrySet()) {
				counterC = counterC + entry.getValue();
			}
		}
		
		Label counter = new Label(composite, SWT.NONE);
		counter.setText(String.valueOf(counterC));
		
		Label topSpeedTitle = new Label(composite, SWT.NONE);
		topSpeedTitle.setText(I18N.getMessage("HeatMapComposite.TopSpeed"));
		
		Label topSpeed = new Label(composite, SWT.NONE);
		topSpeed.setText(String.valueOf(path.getTopSpeed()));
		
		composite.layout();
		informationComposite.layout();
	}
	
	/**
	 * Creates the information if nothing is selected
	 * 
	 * @param composite
	 * 		The nodeInformationComposite
	 */
	private void createNoInformatoin(Composite composite) {
		
		Label nodeInformationLabel = new Label(composite, SWT.NONE);
		GridData nodeInformationLabelData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		nodeInformationLabelData.heightHint = 25;
		nodeInformationLabel.setLayoutData(nodeInformationLabelData);
		nodeInformationLabel.setFont(captionFont);
		nodeInformationLabel.setText(I18N.getMessage("HeatMapComposite.Information"));
		
		Label noInformation = new Label(composite, SWT.NONE);
		noInformation.setText(I18N.getMessage("HeatMapComposite.NoElementSelected"));
		
		composite.layout();
		informationComposite.layout();
	}
	
	/**
	 * Disposes all the children of the nodeInformationComposite
	 */
	private void disposeInfoComposite() {
		for (Control kid : nodeInformationComposite.getChildren()) {
			kid.dispose();
		}
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
