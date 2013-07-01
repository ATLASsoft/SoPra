package de.atlassoft.application;

import java.io.IOException;
import java.util.Calendar;

import de.atlassoft.io.persistence.PersistenceService;
import de.atlassoft.io.persistence.PersistenceServiceImpl;
import de.atlassoft.model.ModelService;
import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;
import de.atlassoft.ui.MainWindow;
import de.atlassoft.util.ImageHelper;

public class ApplicationServiceImpl implements ApplicationService {

	private ModelService model;
	private MainWindow window;
	private PersistenceService persistence;
	
	
	protected ApplicationServiceImpl() {
		initialize();
	}
	
	
	//TODO: im Finalen Programm löschen
	// Nur für Testzwecke, hier kann jeder seine Dummy Daten einspeisen,
	// falls er welche benötigt
	private void createDummy(){
		TrainType testType = new TrainType("Testzug", 150, 10);
		model.addTrainType(testType);
		
		RailwaySystem testRailSys = new RailwaySystem("1");
		model.setActiveRailwaySys(testRailSys);
		model.addRailwaySystemID("1");
		
		Node node1 = new Node("Node 1", 15, 23, 15, 15);
		model.getActiveRailwaySys().addNode(node1);
		Node node2 = new Node("Node 2", 555, 200, 15, 15);
		model.getActiveRailwaySys().addNode(node2);
		Node node3 = new Node("Node 3", 400, 300, 15, 15);
		model.getActiveRailwaySys().addNode(node3);
		Node node4 = new Node("Node 4", 356, 400, 15, 15);
		model.getActiveRailwaySys().addNode(node4);
		Node node5 = new Node("Node 5", 100, 53, 15, 15);
		model.getActiveRailwaySys().addNode(node5);
		
		Path path = new Path(node1, node5, 0);
		model.getActiveRailwaySys().addPath(path);
		path = new Path(node5, node3, 0);
		model.getActiveRailwaySys().addPath(path);
		path = new Path(node4, node3, 0);
		model.getActiveRailwaySys().addPath(path);
		path = new Path(node3, node2, 0);
		model.getActiveRailwaySys().addPath(path);
	}
	
	@Override
	public void initialize() {
		model = new ModelServiceImpl();
		persistence = new PersistenceServiceImpl();
		// TODO: unvollständig
		
		createDummy();
		// load images
		
		ImageHelper.loadImage("greenCheck", "img/greencheck.png");
		ImageHelper.loadImage("questionMark", "img/fragezeichen.jpg");
		ImageHelper.loadImage("loadButton", "img/loadButton.png");
		ImageHelper.loadImage("trainIcon", "img/trainIcon.png");
		ImageHelper.loadImage("cancelIcon", "img/redX.png");
		ImageHelper.loadImage("scheduleIcon", "img/scheduleIcon.png");
		ImageHelper.loadImage("railwaySysIcon", "img/railSysIcon.png");
		ImageHelper.loadImage("playIcon", "img/playButton.png");
		ImageHelper.loadImage("questionMarkIcon", "img/question_mark.png");
		ImageHelper.loadImage("trashIcon", "img/trashIcon.png");
		ImageHelper.loadImage("ATLASsoftLogo", "img/ATLASsoftLogo.gif");
		ImageHelper.loadImage("settingsIcon", "img/settingsIcon.png");
		ImageHelper.loadImage("standardTrainIcon", "img/train.gif");
		ImageHelper.loadImage("questionMarkSmall", "img/questionMarkSmall.png");
		ImageHelper.loadImage("crosshair", "img/crosshair.png");
		
		//TODO: Wieder aktivieren
//		SplashScreen.showSplashScreen(3);		
		window = new MainWindow(this);
	}

	@Override
	public void shutDown() {
		// TODO: unvollständig
		window.close();
		ImageHelper.disposeImages();
	}

	@Override
	public ModelService getModel() {
		return model;
	}

	@Override
	public void startSimulation(Calendar time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseSimulation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void continueSimulation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quitSimulation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addScheduleScheme(ScheduleScheme scheduleScheme) {
		String activeRailSysID = model.getActiveRailwaySys().getID();
		try {
			persistence.saveSchedule(scheduleScheme, activeRailSysID); //TODO: railsysid ist attribut von schedulescheme
			model.addActiveScheduleScheme(scheduleScheme);
		} catch (IOException e) {
			// TODO: Fehlerbehebung
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteScheduleScheme(ScheduleScheme scheduleScheme) {
		String activeRailSysID = model.getActiveRailwaySys().getID();
//		try { // TODO: Methode deleteSchedules anpassen
//			persistence.deleteSchedule(scheduleScheme, activeRailSysID);
//			model.removeActiveScheduleScheme(scheduleScheme);
//			model.removePassiveScheduleScheme(scheduleScheme);
//		} catch (IOException e) {
//			// TODO: Fehlerbehebung
//			e.printStackTrace();
//		}
	}

	@Override
	public void saveRailwaySystem(RailwaySystem railSys) {
		try {
			persistence.saveRailwaySystem(railSys);
			model.setActiveRailwaySys(railSys); //TODO: railway id adden
		} catch (IOException e) {
			// TODO: Fehlerbehebung
			e.printStackTrace();
		}
	}

	@Override
	public void deleteRailwaySystem(String railSysID) {
		String activeRailSysID = model.getActiveRailwaySys().getID();
		try {
			persistence.deleteRailwaySystem(activeRailSysID);
			if (activeRailSysID.equals(railSysID)) {
				model.setActiveRailwaySys(null);
			}
		} catch (IOException e) {
			// TODO: Fehlerbehandlung
			e.printStackTrace();
		}
	}

	@Override
	public void setActiveRailwaySystem(String railsSysID) {
		try {
			RailwaySystem railSys = persistence.loadRailwaySystem(railsSysID);
			model.setActiveRailwaySys(railSys);
		} catch (IOException e) {
			// TODO: Fehlerbehandlung
			e.printStackTrace();
		}
	}

	@Override
	public void addTrainType(TrainType trainType) {
		try {
			persistence.saveTrainType(trainType);
			model.addTrainType(trainType);
		} catch (IOException e) {
			// TODO: Fehlerbehandlung
			e.printStackTrace();
		}
	}

	@Override
	public void deleteTrainType(TrainType trainType) {
		try {
			persistence.deleteTrainType(trainType);
			model.deleteTrainType(trainType);
		} catch (IOException e) {
			// TODO Fehlerbehandlung
			e.printStackTrace();
		}
	}

	@Override
	public void showStatisticDoc(SimulationStatistic statistic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showScheduleDoc(ScheduleScheme scheduleScheme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDepartureBoardDoc(Node station) {
		// TODO Auto-generated method stub
		
	}

}
