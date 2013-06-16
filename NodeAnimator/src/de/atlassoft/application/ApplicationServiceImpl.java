package de.atlassoft.application;

import java.util.Calendar;

import de.atlassoft.io.persistence.PersistenceService;
import de.atlassoft.io.persistence.PersistenceServiceXMLImpl;
import de.atlassoft.model.ModelService;
import de.atlassoft.model.ModelServiceImpl;
import de.atlassoft.model.Node;
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
		
	}
	
	@Override
	public void initialize() {
		model = new ModelServiceImpl();
		//persistence = new PersistenceServiceXMLImpl();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteScheduleScheme(ScheduleScheme scheduleScheme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveRailwaySystem(RailwaySystem railSys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRailwaySystem(String railSysID) {
		String activeRailSysID = model.getActiveRailwaySys().getID();
		if (activeRailSysID.equals(railSysID)) {
			model.setActiveRailwaySys(null);
		}
		//persistence.deleteRailwaySystem(activeRailSysID); // TODO: im finalen programm aktivieren
	}

	@Override
	public void setActiveRailwaySystem(String railsSysID) {
//		RailwaySystem railSys = persistence.loadRailwaySystem(railsSysID); // TODO: im finalen programm aktivieren
//		model.setActiveRailwaySys(railSys);
	}

	@Override
	public void addTrainType(TrainType trainType) {
		model.addTrainType(trainType);
		//persistence.saveTrainType(trainType); // TODO: im finalen programm aktivieren
	}

	@Override
	public void deleteTrainType(TrainType trainType) {
		model.deleteTrainType(trainType);
		//persistence.deleteTrainType(trainType); // TODO: im finalen programm aktivieren
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
