package de.atlassoft.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.atlassoft.ai.AIService;
import de.atlassoft.ai.AIServiceImpl;
import de.atlassoft.io.persistence.PersistenceService;
import de.atlassoft.io.persistence.PersistenceServiceImpl;
import de.atlassoft.model.ModelService;
import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.model.Node;
import de.atlassoft.model.Path;
import de.atlassoft.model.RailwaySystem;
import de.atlassoft.model.ScheduleScheme;
import de.atlassoft.model.ScheduleType;
import de.atlassoft.model.SimulationStatistic;
import de.atlassoft.model.TrainType;
import de.atlassoft.ui.MainWindow;
import de.atlassoft.util.ImageHelper;

public class ApplicationServiceImpl implements ApplicationService {

	private ModelService model;
	private MainWindow window;
	private PersistenceService persistence;
	private AIService ai;
	
	
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
		
		ScheduleType testScheduleType = ScheduleType.SINGLE_RIDE;
		java.util.List<Integer> testDays = new ArrayList<Integer>();
		testDays.add(2);
		testDays.add(3);
		Calendar firstRide = new GregorianCalendar();
		firstRide.set(0, 0, 0, 15, 20);
		ScheduleScheme testScheduleScheme = new ScheduleScheme (testScheduleType, testType, testDays, firstRide, "1", "Testfahrplan");
		testScheduleScheme.addStop(node1, 0, 5);
		testScheduleScheme.addStop(node4, 10, 5);
		model.addActiveScheduleScheme(testScheduleScheme);
	}
	
	@Override
	public void initialize() {
		// initialize service intances
		model = new ModelServiceImpl();
		persistence = new PersistenceServiceImpl();
		ai = new AIServiceImpl();
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
		RailwaySystem railSys = model.getActiveRailwaySys();
		List<ScheduleScheme> schemes = model.getActiveScheduleSchemes();
		if (!ai.isRunning() && railSys != null) {
			ai.startSimulation(time, railSys, schemes);
		}
		
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
		try {
			persistence.saveSchedule(scheduleScheme);
			model.addActiveScheduleScheme(scheduleScheme);
		} catch (IOException e) {
//			showErrorMessage("Speichern fehlgeschlagen!"); //TODO: Fehlerbehandlung
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteScheduleScheme(ScheduleScheme scheduleScheme) {
//		try {
//			persistence.deleteSingleSchedule(scheduleScheme);
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
			model.addRailwaySystemID(railSys.getID());
			setActiveRailwaySystem(railSys.getID());
		} catch (IOException e) {
			// TODO: Fehlerbehebung
			e.printStackTrace();
		}
	}

	@Override
	public void deleteRailwaySystem(String railSysID) {
		String activeRailSysID = model.getActiveRailwaySys().getID();
		try {
			persistence.deleteRailwaySystem(railSysID);
			persistence.deleteSchedules(railSysID);
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
			List<ScheduleScheme> schemes = persistence.loadSchedules(railsSysID);
			for (ScheduleScheme s : schemes) {
				model.addActiveScheduleScheme(s);
			}
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
			model.deleteTrainType(trainType); //TODO: Fahrpläne löschen
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
